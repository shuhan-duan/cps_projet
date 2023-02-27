package componenet;


import connector.ContentManagementConector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ContentDescriptorI;
import ports.ContentManagementCIOutbound;

public class Client extends AbstractComponent {
    protected ContentManagementCIOutbound outportCM_client;
    protected ContentDescriptorI patron;
    protected String inportCM_facade;

    protected Client(String ContentManagementInboudPort, String ContentManagementOutboudPort) throws Exception {
        super(1,0);
        outportCM_client = new ContentManagementCIOutbound(ContentManagementOutboudPort,this);
        outportCM_client.publishPort();
        inportCM_facade = ContentManagementInboudPort;
        
    }
    
    @Override
    public void start() throws ComponentStartException {
      try {
        super.start();
        this.doPortConnection(outportCM_client.getPortURI(), inportCM_facade, ContentManagementConector.class.getCanonicalName());
      } catch (Exception e) {
        throw new ComponentStartException(e);
      }
    }

}