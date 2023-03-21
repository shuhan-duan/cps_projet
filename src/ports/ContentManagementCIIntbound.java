package ports;

import java.util.Set;

import componenet.Facade;
import componenet.Pair;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentTemplateI;
import interfaces.MyCMI;
import interfaces.NodeAdresseI;
import interfaces.NodeCI;

public class ContentManagementCIIntbound  extends AbstractInboundPort  implements  ContentManagementCI {

	public ContentManagementCIIntbound (ComponentI owner , String pluginURI) throws Exception {
		super(ContentManagementCI.class, owner ,pluginURI ,null);
		assert	uri != null && owner instanceof ContentManagementCI ;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void find(ContentTemplateI cd, int hops, NodeAdresseI requester, String requestURI) throws Exception {
		this.getOwner().runTask(
				owner -> {
					try {
						((MyCMI) owner).find(cd ,hops ,requester,requestURI);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}

	@Override
	public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, NodeAdresseI requester,
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
