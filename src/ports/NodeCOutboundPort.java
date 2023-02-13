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


	public NodeCOutboundPort(Class<? extends RequiredCI> implementedInterface, ComponentI owner) throws Exception {
		super(implementedInterface, owner);
	}

	

	public NodeCOutboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri,(Class<? extends RequiredCI>) NodeCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;


	@Override
	public PeerNodeAddressI connecte(PeerNodeAddressI p) throws Exception {
		return ((Pair)this.getOwner()).connecte(p); 

	}

	@Override
	public void disconnecte(PeerNodeAddressI p) throws Exception {

		((NodeCI) getConnector()).disconnecte(p);

		
	}
	





	

}
