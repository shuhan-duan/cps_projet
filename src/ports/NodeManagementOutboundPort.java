package ports;

import java.util.Set;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentNodeAddressI;
import interfaces.NodeCI;
import interfaces.NodeManagementCI;
import interfaces.PeerNodeAddressI;

/**
 * @author lyna & shuhan 
 *
 */
public class NodeManagementOutboundPort extends AbstractOutboundPort implements  NodeManagementCI{

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public				NodeManagementOutboundPort(ComponentI owner)
	throws Exception
	{
		super(NodeManagementCI.class, owner);
	}

	public				NodeManagementOutboundPort(
		String uri,
		ComponentI owner
		) throws Exception
	{
		super(uri, NodeManagementCI.class, owner);
	}
	// -------------------------------------------------------------------------
		// Methods
		// -------------------------------------------------------------------------	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) throws Exception {
		((NodeManagementCI)this.getConnector()).probe(facade ,remainghops ,requestURI);
		
	}

	@Override
	public void join(ContentNodeAddressI p) throws Exception {
		((NodeManagementCI)this.getConnector()).join(p);
		
	}

	@Override
	public void leave(ContentNodeAddressI p) throws Exception {
		((NodeManagementCI)this.getConnector()).leave(p);
		
	}

	@Override
	public void acceptProbed(ContentNodeAddressI p, String requsetURI) throws Exception {
		((NodeManagementCI)this.getConnector()).acceptProbed(p, requsetURI);
		
	}

	
	
	

}
