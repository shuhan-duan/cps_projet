package componenet;

import java.util.HashSet;

import classes.ContentDescriptor;
import classes.ContentNodeAdress;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentNodeAddressI;
import ports.ContentManagementCIOutbound;

@RequiredInterfaces(required = { ContentManagementCI.class })
public class Client extends AbstractComponent {
    protected ContentManagementCIOutbound outportCM_client;
    protected ContentDescriptorI patron;
    protected String inportCM_facade;

    protected Client(String ContentManagementInboudPort, String ContentManagementOutboudPort) throws Exception {
        super(1,0);
        outportCM_client = new ContentManagementCIOutbound(ContentManagementOutboudPort,this);
        inportCM_facade = ContentManagementInboudPort;
    }

}