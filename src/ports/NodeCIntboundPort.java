package ports;

import java.util.Set;


import componenet.Facade;
import componenet.Pair;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.FacadeNodeAdressI;
import interfaces.NodeCI;
import interfaces.NodeManagementCI;
import interfaces.PeerNodeAddressI;

public class NodeCIntboundPort  extends AbstractInboundPort  implements NodeCI  {

	

	public NodeCIntboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri ,  NodeCI.class, owner);
	}

	@Override
	public PeerNodeAddressI connecte(PeerNodeAddressI p) throws Exception {
		return this.getOwner().handleRequest(new AbstractComponent.AbstractService<PeerNodeAddressI>() {
			@Override 
			public PeerNodeAddressI call() throws Exception {
				return ((Pair)this.getServiceOwner()).connectPair(p) ;
			}
		}) ;
	}

	@Override
	public void disconnecte(PeerNodeAddressI p) throws Exception {
		
		this.getOwner().handleRequest(new AbstractComponent.AbstractService<Void>() {
			@Override 
			public Void call() throws Exception {
				((Pair)this.getServiceOwner()).disconnectePair(p) ;
				return null;
			}
		}) ;
		
	}
	
}

