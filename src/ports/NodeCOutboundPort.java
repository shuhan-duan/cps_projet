package ports;

import java.util.Set;

import connector.NodeConnector;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.NodeCI;
import interfaces.PeerNodeAddressI;

public class NodeCOutboundPort   extends AbstractOutboundPort implements  NodeCI{

	

	public NodeCOutboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri,(Class<? extends RequiredCI>) NodeCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	@Override
	public PeerNodeAddressI connect(PeerNodeAddressI p) throws Exception {
		return ((NodeCI)this.getConnector()).connect(p); 

	}

	@Override
	public void disconnect(PeerNodeAddressI p) throws Exception {
		((NodeConnector) getConnector()).disconnect(p);
		
	}
	





	

}
