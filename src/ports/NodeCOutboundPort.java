package ports;

import java.util.Set;

import componenet.Pair;
import connector.NodeC_conector;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.NodeCI;
import interfaces.NodeManagementCI;
import interfaces.PeerNodeAddressI;

public class NodeCOutboundPort   extends AbstractOutboundPort implements  NodeCI{

	public NodeCOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, NodeCI.class, owner);
	}


	@Override
	public PeerNodeAddressI connecte(PeerNodeAddressI p) throws Exception {
		return ((NodeCI) getConnector()).connecte(p);
	}

	@Override
	public void disconnecte(PeerNodeAddressI p) throws Exception {
		((NodeCI) this.getConnector()).disconnecte(p);

		
	}
	





	

}
