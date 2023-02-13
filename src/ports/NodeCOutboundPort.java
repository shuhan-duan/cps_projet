package ports;

import java.util.Set;

<<<<<<< HEAD
=======
import connector.NodeConnector;
>>>>>>> d0c35ec572c270934f1c4a1384469c304cef547b
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.NodeCI;
<<<<<<< HEAD
import interfaces.NodeManagementCI;
=======
>>>>>>> d0c35ec572c270934f1c4a1384469c304cef547b
import interfaces.PeerNodeAddressI;

public class NodeCOutboundPort   extends AbstractOutboundPort implements  NodeCI{

<<<<<<< HEAD
	public NodeCOutboundPort(Class<? extends RequiredCI> implementedInterface, ComponentI owner) throws Exception {
		super(implementedInterface, owner);
	}

=======
	

	public NodeCOutboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri,(Class<? extends RequiredCI>) NodeCI.class, owner);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

>>>>>>> d0c35ec572c270934f1c4a1384469c304cef547b
	@Override
	public PeerNodeAddressI connect(PeerNodeAddressI p) throws Exception {
		return ((NodeCI)this.getConnector()).connect(p); 

	}

	@Override
	public void disconnect(PeerNodeAddressI p) throws Exception {
<<<<<<< HEAD
		// TODO Auto-generated method stub
=======
		((NodeConnector) getConnector()).disconnect(p);
>>>>>>> d0c35ec572c270934f1c4a1384469c304cef547b
		
	}
	





	

}
