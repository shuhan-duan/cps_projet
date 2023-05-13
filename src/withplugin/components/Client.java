package withplugin.components;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.utils.aclocks.AcceleratedClock;
import fr.sorbonne_u.utils.aclocks.ClocksServer;
import fr.sorbonne_u.utils.aclocks.ClocksServerCI;
import fr.sorbonne_u.utils.aclocks.ClocksServerConnector;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;
import classes.ContentTemplate;
import connector.FacadeContentManagementConector;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.p2Pcm.dataread.ContentDataManager;
import interfaces.ContentDescriptorI;
import interfaces.ContentTemplateI;
import interfaces.MyClientI;
import ports.FacadeContentManagementCOutbound;
import ports.MyClientCInbound;
import withplugin.CVM;
@RequiredInterfaces(required={ClocksServerCI.class}) 

/**
 * @author azerouk Shuhan
 *
 */

public class Client extends AbstractComponent{
		
		protected MyClientCInbound inportFCM_client;
	    private FacadeContentManagementCOutbound outportFCM_client;
		protected ClocksServerOutboundPort csop; 
	
	    protected String inportCM_facadeURI;
	    protected final int ID_TEMP = 0;
	    
    
    
    // -------------------------------------------------------------------------
 	// Constructors
 	// -------------------------------------------------------------------------
    
    protected Client(String FacadeCMInPortClientURI ,String inportFCMfacadeURI) throws Exception {
        super(2,1);
        inportFCM_client = new MyClientCInbound(FacadeCMInPortClientURI, this);
        inportFCM_client.publishPort();
        
        outportFCM_client = new FacadeContentManagementCOutbound(this);
        outportFCM_client.publishPort();
        
        this.inportCM_facadeURI = inportFCMfacadeURI;
		//Create Clock
		this.csop = new ClocksServerOutboundPort(this);  
		this.csop.publishPort();
    }
    
    
	
	//-------------------------------------------------------------------------
	// Component life-cycle
	//-------------------------------------------------------------------------
    
    @Override
    public void start() throws ComponentStartException {
      try {
        super.start();
        //connect with facade in FCM       
        this.doPortConnection(outportFCM_client.getPortURI(), inportCM_facadeURI, FacadeContentManagementConector.class.getCanonicalName());	
        
      } catch (Exception e) {
        throw new ComponentStartException(e);
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
		
		long delayInNanos =clock.nanoDelayUntilAcceleratedInstant(startInstant.plusSeconds(200));
		
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
	}
	
	public void acceptMatched(Set<ContentDescriptorI> matched ,String requsetURI) throws Exception {
		System.out.println("\nIn " + requsetURI +"  we have matched: ");
		for (ContentDescriptorI contentDescriptorI : matched) {
			System.out.println(contentDescriptorI.toString());
		}
	}
	
    
	private void action() throws Exception
	{
    	 //choose template
        ContentTemplateI temp = createTemplate(ID_TEMP);
        //find
        doFind(temp);
        //match
        //doMatch(temp);
	}
    
    private ContentTemplate createTemplate(int numbre) throws ClassNotFoundException, IOException{
        //ContentDataManager.DATA_DIR_NAME = "src/testsDataCPSAvril";
        ContentDataManager.DATA_DIR_NAME = "src/data";
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
    	this.outportFCM_client.find(temp, 5, null, null);
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
        
 
	}
    
    
    

}