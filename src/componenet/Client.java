package componenet;


import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import CVM.CVM;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.utils.aclocks.AcceleratedClock;
import fr.sorbonne_u.utils.aclocks.ClocksServer;
import fr.sorbonne_u.utils.aclocks.ClocksServerCI;
import fr.sorbonne_u.utils.aclocks.ClocksServerConnector;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;
import classes.ContentTemplate;
import connector.ContentManagementConector;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.p2Pcm.dataread.ContentDataManager;
import interfaces.ContentDescriptorI;
import interfaces.ContentTemplateI;
import interfaces.MyFCMI;
import ports.ContentManagementCIOutbound;
import ports.FacadeContentManagementCInbound;
@RequiredInterfaces(required={ClocksServerCI.class}) 

/**
 * @author azerouk Shuhan
 *
 */

public class Client extends AbstractComponent implements MyFCMI{
    protected ContentManagementCIOutbound outportCM_client;
    protected FacadeContentManagementCInbound inportFCM_client;
	protected ClocksServerOutboundPort csop; 

    protected String inportCM_facade;
    protected final int ID_TEMP = 0;

    protected Client(String ContentManagementInboudPort, String ContentManagementOutboudPort,String FCMInbountPortClient) throws Exception {
        super(2,1);
        outportCM_client = new ContentManagementCIOutbound(ContentManagementOutboudPort,this);
        outportCM_client.publishPort();
        inportFCM_client = new FacadeContentManagementCInbound(FCMInbountPortClient, this);
        inportFCM_client.publishPort();
        inportCM_facade = ContentManagementInboudPort; 
        
      //Create Clock
      this.csop = new ClocksServerOutboundPort(this);  
      this.csop.publishPort();
    }
    
    public ContentTemplate createTemplate(int numbre) throws ClassNotFoundException, IOException{
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

    
    public void doFind(ContentTemplateI temp) throws Exception {
    	//find
        System.out.println("\nplease find the template:\n "+ temp.toString()+"\n");
       this.outportCM_client.find(temp, 15, null, inportCM_facade); 
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

    public void doMatch(ContentTemplateI temp) throws Exception {
    	//match
        System.out.println("\nplease match the template:\n"+ temp.toString());
        
        Set<ContentDescriptorI> matched = new HashSet<>();
        this.outportCM_client.match(temp,matched, 5, null, inportCM_facade); 
 
	}
    
    public void acceptFound(ContentDescriptorI found, String requsetURI) throws Exception {
		if (found != null) {
			System.out.println("dans "+requsetURI +" on a trouve : "+ found.toString());
		}		
	}
	
	public void acceptMatched(Set<ContentDescriptorI> matched ,String requsetURI) throws Exception {
		System.out.println("\nIn " + requsetURI +"  we have matched: ");
		for (ContentDescriptorI contentDescriptorI : matched) {
			System.out.println(contentDescriptorI.toString());
		}
	}

    
    @Override
    public void start() throws ComponentStartException {
      try {
        super.start();
        //conexionc avec facade 
        
        String str = outportCM_client.getPortURI();
        this.doPortConnection(str, inportCM_facade, ContentManagementConector.class.getCanonicalName());		
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
		
		long delayInNanos =clock.nanoDelayUntilAcceleratedInstant(startInstant.plusSeconds(150));
		
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
    
    
    
    public void		action() throws Exception
	{
    	 //choose template
        ContentTemplateI temp = createTemplate(ID_TEMP);
        //find
        //doFind(temp);
        //match
        //doMatch(temp);
	}
    
    
    
    
    

}