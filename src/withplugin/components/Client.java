package withplugin.components;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.utils.aclocks.AcceleratedClock;
import fr.sorbonne_u.utils.aclocks.ClocksServer;
import fr.sorbonne_u.utils.aclocks.ClocksServerCI;
import fr.sorbonne_u.utils.aclocks.ClocksServerConnector;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;
import classes.ContentTemplate;
import connector.FacadeContentManagementConector;
import connector.NodeManagementConnector;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionCI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import fr.sorbonne_u.cps.p2Pcm.dataread.ContentDataManager;
import interfaces.ContentDescriptorI;
import interfaces.ContentTemplateI;
import interfaces.FacadeContentManagementCI;
import interfaces.MyClientI;
import interfaces.NodeManagementCI;
import ports.FacadeContentManagementCOutbound;
import ports.MyClientCInbound;
import withplugin.CVM;


/**
 * @author azerouk Shuhan
 *
 */
@RequiredInterfaces(required={ClocksServerCI.class, FacadeContentManagementCI.class}) 
@OfferedInterfaces(offered ={MyClientI.class}) 
public class Client extends AbstractComponent{
	
		/** URI of the consumer inbound port (simplifies the connection).		*/
		protected static final String	FCMInPortClientURI = "inPortFCMClient";
		
		protected MyClientCInbound inportFCM_client;
	    private FacadeContentManagementCOutbound outportFCM_client;
		protected ClocksServerOutboundPort csop; 
	
	    protected String facadeFCMURI; // for connect the facade in FCM
	    protected final int ID_TEMP = 0;
	    private String path;
	    private Instant startTime;
    
    // -------------------------------------------------------------------------
 	// Constructors
 	// -------------------------------------------------------------------------
    
    protected Client(String clientURI , String facadeFCMURI ,String path) throws Exception {
        super(clientURI,2,1);
        
        inportFCM_client = new MyClientCInbound(FCMInPortClientURI, this);
        inportFCM_client.publishPort();
        
        outportFCM_client = new FacadeContentManagementCOutbound(this);
        outportFCM_client.publishPort();
        
        this.facadeFCMURI = facadeFCMURI; 
        this.path = path;
		//Create Clock
		this.csop = new ClocksServerOutboundPort(this);  
		this.csop.publishPort();
    }
    
    
	
	//-------------------------------------------------------------------------
	// Component life-cycle
	//-------------------------------------------------------------------------
    @Override
	public void start() throws ComponentStartException {
		super.start();
		try {
			//System.out.println("in pair "+ id +",  : " +);
			doPortConnection( outportFCM_client.getPortURI(), facadeFCMURI , FacadeContentManagementConector.class.getCanonicalName());
			//System.out.println("pair"+id+" ->"++"[color=red];");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    @Override
    public void execute() throws Exception {
      super.execute();
      this.doPortConnection(
				this.csop.getPortURI(),
				ClocksServer.STANDARD_INBOUNDPORT_URI,
				ClocksServerConnector.class.getCanonicalName());
		AcceleratedClock clock = this.csop.getClock(CVM.CLOCK_URI);
		Instant startInstant = clock.getStartInstant();
		clock.waitUntilStart();
		
		long delayInNanos =clock.nanoDelayUntilAcceleratedInstant(startInstant.plusSeconds(60));
		
		startTime = Instant.now();
		
		this.scheduleTask(
				o -> {
					try {
						((Client)o).action();
					} catch (Exception e) {
						e.printStackTrace();
					} 
				},
				delayInNanos,
				TimeUnit.NANOSECONDS);  
				

    }
    
    
    // -------------------------------------------------------------------------
 	// Services implementation
 	// -------------------------------------------------------------------------
    
    public void foundRes(ContentDescriptorI found, String requsetURI) throws Exception {
		if (found != null) {
			System.out.println("has returned the result to client,"
					+ " in "+requsetURI +" we found : "+ found.toString());
		}
		
		Instant endTime = Instant.now();
		Duration duration = Duration.between(startTime, endTime);
		long responseTimeInMillis = duration.toMillis();
		System.out.println("responseTimeInMillis of find : "+ responseTimeInMillis+ "\n");

	}
	
	public void acceptMatched(Set<ContentDescriptorI> matched ,String requsetURI) throws Exception {
		System.out.println("\nIn " + requsetURI +"  we have matched: ");
		for (ContentDescriptorI contentDescriptorI : matched) {
			System.out.println(contentDescriptorI.toString());
		}
	}
	
	private void doConnectFacade() throws Exception {
		//connect with facade in FCM
        this.addRequiredInterface(ReflectionCI.class);
        ReflectionOutboundPort rop = new ReflectionOutboundPort(this);
		rop.publishPort();
		
		this.doPortConnection(rop.getPortURI(), facadeFCMURI, ReflectionConnector.class.getCanonicalName());

		String[] otherInboundPortUI = rop.findInboundPortURIsFromInterface(FacadeContentManagementCI.class);
		if (otherInboundPortUI.length == 0 || otherInboundPortUI == null)
			System.out.println("cannot connet facade in FCM");
		else {
			this.doPortConnection(outportFCM_client.getPortURI(), otherInboundPortUI[0],
					FacadeContentManagementConector.class.getCanonicalName());
		}	
		
		this.doPortDisconnection(rop.getPortURI());
		rop.unpublishPort();
		rop.destroyPort();
		this.removeRequiredInterface(ReflectionCI.class);	
		System.out.println("-------------------");
	}
    
	private void action() throws Exception
	{
    	 //choose template
        ContentTemplateI temp = createTemplate(ID_TEMP ,path);
        //doConnectFacade();
        //find
        doFind(temp);
        //match
        //doMatch(temp);
	}
    
    private ContentTemplate createTemplate(int numbre ,String path) throws ClassNotFoundException, IOException{
        //ContentDataManager.DATA_DIR_NAME = "testsDataCPSAvril";
        ContentDataManager.DATA_DIR_NAME = path;
        ArrayList<HashMap<String, Object>> result = ContentDataManager.readTemplates(numbre);
        Random rn = new Random ();
        int randomindex = rn.nextInt(result.size());
        HashMap<String, Object> res = result.get(randomindex);
        ContentTemplate temp = new ContentTemplate(res);
        return temp;
    }

    /**   
	* @Function: Client.java
	* @Description: 
	*
	* @param: ContentTemplateI temp
	* @return：
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 06 Fev. 2023 20:34:57 
	*
	* 
	*/

    
    private void doFind(ContentTemplateI temp) throws Exception {
    	//call find in the facade connected
    	this.outportFCM_client.find(temp, 4, null, null);
        System.out.println("\nplease find the template:\n "+ temp.toString()+"\n");
       
	}
    
    /**   
 	* @Function: Client.java
 	* @Description: 
 	*
 	* @param: ContentTemplateI temp
 	* @return：
 	* @throws：Exception
 	*
 	* @version: v1.0.0
 	* @author: lyna & shuhan 
 	* @date: 06 Fev. 2023 20:34:57 
 	*
 	* 
 	*/

    private void doMatch(ContentTemplateI temp) throws Exception {
        System.out.println("\nplease match the template:\n"+ temp.toString());
        
        Set<ContentDescriptorI> matched = new HashSet<>();
        this.outportFCM_client.match(temp, matched, 5, null, null);
	}
    
    
    

}