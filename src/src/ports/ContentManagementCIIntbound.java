package ports;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentTemplateI;
import interfaces.MyCMI;
import interfaces.NodeAdresseI;
import interfaces.NodeCI;

public class ContentManagementCIIntbound  extends AbstractInboundPort  implements  ContentManagementCI {

	public ContentManagementCIIntbound(ComponentI owner ,String uri)
			throws Exception {
				super(uri, ContentManagementCI.class, owner);
		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester
			, String requestURI) throws Exception {
		this.getOwner().runTask(
				owner -> {
					try {
						((MyCMI) owner).find(cd ,hops ,requester,requestURI );
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}

	@Override
	public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester,
			String requestURI) throws Exception {
		this.getOwner().runTask(
				owner -> {
					try {
						((MyCMI) owner).match(cd ,matched ,hops ,requester,requestURI);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}

	

}
