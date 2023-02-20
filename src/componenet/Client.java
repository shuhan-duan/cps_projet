package componenet;

import java.util.HashSet;

import classes.ContentDescriptor;
import classes.ContentNodeAdress;
import fr.sorbonne_u.components.AbstractComponent;
import interfaces.ContentDescriptorI;
import interfaces.ContentNodeAddressI;
import ports.ContentManagementCIOutbound;

public class Client extends AbstractComponent {
    protected ContentManagementCIOutbound outportCM_client;
    protected ContentDescriptorI patron;

    protected Client(String outport, int nbThreads, int nbSchedulableThreads) throws Exception {
        super(outport, nbThreads, nbSchedulableThreads);
        outportCM_client = new ContentManagementCIOutbound(outport, this);
        outportCM_client.publishPort();
        // Create a ContentDescriptorI object
		ContentNodeAddressI ca = new ContentNodeAdress();
	    ContentDescriptor patron = new ContentDescriptor("my title", "my album title",new HashSet<String>(), new HashSet<String>(), 1000L, ca);

    }
    
}
