package ports;

import java.util.Set;

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

	@Override
	public PeerNodeAddressI connect(PeerNodeAddressI p) throws Exception {
		return ((NodeCI)this.getConnector()).connect(p); 

	}

	@Override
	public void disconnect(PeerNodeAddressI p) throws Exception {
		// TODO Auto-generated method stub
		
	}
	





	

}
