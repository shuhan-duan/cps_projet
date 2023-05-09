package ports;

import java.util.Set;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentTemplateI;
import interfaces.MyCMI;
import interfaces.NodeAdresseI;
import interfaces.NodeManagementCI;


public class ContentManagementCIOutbound   extends AbstractOutboundPort  implements    ContentManagementCI{
  
	

	// -------------------------------------------------------------------------
		// Constructors
		// -------------------------------------------------------------------------



		public				ContentManagementCIOutbound(
			String uri,
			ComponentI owner
			) throws Exception
		{
			super(uri, ContentManagementCI.class, owner);
		}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
		((MyCMI)this.getConnector()).find(cd ,hops ,requester,requestURI);
		
	}

	@Override
	public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester,
			String requestURI) throws Exception {
		((MyCMI)this.getConnector()).match(cd ,matched ,hops ,requester,requestURI);
		
	}

	

}
