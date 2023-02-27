package componenet;


import classes.ContentDescriptor;
import classes.ContentNodeAdress;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentNodeAddressI;
import interfaces.ContentTemplateI;
import ports.ContentManagementCIOutbound;

public class Client extends AbstractComponent {
    protected ContentManagementCIOutbound outportCM_client ;
    protected ContentDescriptorI patron;
    protected String inportCM_facade;
    protected ContentTemplateI c_t;
    

    protected Client(String ContentManagementInboudPort, String ContentManagementOutboudPort) throws Exception {
        super(1,0);
        outportCM_client = new ContentManagementCIOutbound(ContentManagementOutboudPort,this);
        inportCM_facade = ContentManagementInboudPort;
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