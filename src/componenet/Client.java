package componenet;

import java.util.HashSet;

import classes.ContentDescriptor;
import classes.ContentTemplate;
import connector.ContentManagementCIConector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentTemplateI;
import ports.ContentManagementCIOutbound;

@RequiredInterfaces(required = { ContentManagementCI.class })
public class Client extends AbstractComponent {
    protected ContentManagementCIOutbound outportCM_client ;
    protected ContentDescriptorI patron;
    protected String inportCM_facade;
    protected ContentTemplateI c_t;
    

    protected Client(String ContentManagementInboudPort, String ContentManagementOutboudPort) throws Exception {
        super(1,0);
        outportCM_client = new ContentManagementCIOutbound("test_port_client",this);
        outportCM_client.publishPort();
        inportCM_facade = ContentManagementInboudPort;
        c_t= new ContentTemplate (" ", " ", null, null );
    }

    public synchronized void start() throws ComponentStartException {
		try {	super.start();
            doPortConnection(this.outportCM_client.getPortURI(),this.inportCM_facade,ContentManagementCIConector.class.getCanonicalName());

		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
	
	}
	public void execute() throws Exception{
        //System.out.println("je suis la  debut find ");
        Thread.sleep(2000);
 	  ContentDescriptorI c_d=this.outportCM_client.find(c_t,10);	
       if (c_d==null)
      {
        System.out.println("Descriptor null");
      }else {
        System.out.println(c_d.getALbum()) ;

      }
    }
    
	
    public synchronized void shutdown() throws ComponentShutdownException {
		try {
			this.outportCM_client.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}
	public synchronized void finalise() throws Exception{
		super.finalise();
	}


}