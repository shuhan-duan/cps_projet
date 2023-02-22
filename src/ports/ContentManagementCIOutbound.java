package ports;

import java.util.Set;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentTemplateI;
import interfaces.NodeManagementCI;
import componenet.Facade;
import componenet.Pair;

public class ContentManagementCIOutbound   extends AbstractOutboundPort  implements    ContentManagementCI{
  ///a revoir le constructor 
	

	public ContentManagementCIOutbound(String uri,  ComponentI owner)
			throws Exception {
		super(uri, ContentManagementCI.class, owner);
		//TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ContentDescriptorI find(ContentTemplateI cd, int hops) throws Exception {
		return  ((ContentManagementCI)this.getConnector()).find(cd , hops );

	}

	@Override
	public Set<ContentDescriptorI> match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops)
			throws Exception {
		  return ((ContentManagementCI)this.getConnector()).match(cd , matched , hops );
	}

}
