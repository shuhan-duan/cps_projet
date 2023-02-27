package ports;

import java.util.Set;

import classes.PeerNodeAddress;
import componenet.Facade;
import componenet.Pair;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentTemplateI;
import interfaces.FacadeNodeAdressI;

public class ContentManagementCIIntbound  extends AbstractInboundPort  implements     ContentManagementCI {

	public ContentManagementCIIntbound(ComponentI owner) throws Exception {
		super(ContentManagementCI.class, owner);
	}

	public ContentManagementCIIntbound(String uri,ComponentI owner)
			throws Exception {
		super(uri, ContentManagementCI.class, owner);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ContentDescriptorI find(ContentTemplateI cd, int hops) throws Exception {
		System.out.println("je suis dans find CM_in");
		return this.getOwner().handleRequest(new AbstractComponent.AbstractService<ContentDescriptorI>() {
			@Override 
			public ContentDescriptorI call() throws Exception {
				return ((ContentManagementCI) getOwner()).find(cd,hops) ;
			}
		}) ;
			
	}

	@Override
	public Set<ContentDescriptorI> match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops)
			throws Exception {
		return null;
	}

}
