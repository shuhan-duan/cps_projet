package ports;

import java.util.Set;


import componenet.Facade;
import componenet.Pair;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ContentNodeAddressI;
import interfaces.FacadeNodeAdressI;
import interfaces.NodeCI;
import interfaces.NodeManagementCI;
import interfaces.PeerNodeAddressI;

public class NodeCIntboundPort  extends AbstractInboundPort  implements NodeCI  {


	private static final long serialVersionUID = 1L;

	public NodeCIntboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri ,  NodeCI.class, owner);
	}

	@Override
	public ContentNodeAddressI connecte(ContentNodeAddressI a) throws Exception {
		return ((Pair) this.getOwner()).connecte(a) ;
	}

	@Override
	public void disconnecte(ContentNodeAddressI p) throws Exception {
		
				((Pair)this.getOwner()).disconnectePair(p) ;
		
	}
	
}

