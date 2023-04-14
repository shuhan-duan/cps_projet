package ports;

import java.util.Set;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;

import interfaces.ContentTemplateI;
import interfaces.FacadeContentManagementCI;
import interfaces.MyCMI;
import interfaces.MyFCMI;
import interfaces.NodeAdresseI;

public class FacadeContentManagementCOutbound extends AbstractOutboundPort implements  FacadeContentManagementCI{
	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
			// Constructors
			// -------------------------------------------------------------------------



			public				FacadeContentManagementCOutbound(
				String uri,
				ComponentI owner
				) throws Exception
			{
				super(uri, FacadeContentManagementCI.class, owner);
			}

	@Override
	public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
		// TODO Auto-generated method stub
		((MyCMI)this.getConnector()).find(cd ,hops ,requester,requestURI);
	}

	@Override
	public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester,
			String requestURI) throws Exception {
		((MyCMI)this.getConnector()).match(cd ,matched ,hops ,requester,requestURI);
		
	}

	@Override
	public void acceptFound(ContentDescriptorI found, String requsetURI) throws Exception {
		
		((MyFCMI)this.getConnector()).acceptFound(found, requsetURI);
		
	}
	@Override
	public void acceptMatched(Set<ContentDescriptorI> matched, String requsetURI) throws Exception {
		((MyFCMI)this.getConnector()).acceptMatched(matched, requsetURI);
		
	}

}
