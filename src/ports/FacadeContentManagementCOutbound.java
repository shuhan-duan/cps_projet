package ports;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;

import interfaces.ContentTemplateI;
import interfaces.FacadeContentManagementCI;
import interfaces.MyCMI;

public class FacadeContentManagementCOutbound extends AbstractOutboundPort implements  FacadeContentManagementCI{
	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------



	public	FacadeContentManagementCOutbound(
		ComponentI owner
		) throws Exception
	{
		super(generatePortURI(), FacadeContentManagementCI.class, owner);
		assert  owner != null ;
	}

	@Override
	public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester,
			String requestURI) throws Exception {
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
		
		((FacadeContentManagementCI)this.getConnector()).acceptFound(found, requsetURI);
		
	}
	@Override
	public void acceptMatched(Set<ContentDescriptorI> matched, String requsetURI) throws Exception {
		((FacadeContentManagementCI)this.getConnector()).acceptMatched(matched, requsetURI);
		
	}

}
