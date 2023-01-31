package Tme_1.ports;

import java.util.Set;

import Tme_1.classes.Pair;
import Tme_1.interfaces.NodeManagementCI;
import Tme_1.interfaces.PeerNodeAddressI;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.examples.basic_cs.components.URIProvider;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;

public class NodeManagementInBoundPort extends AbstractInboundPort implements NodeManagementCI {

	public NodeManagementInBoundPort(String uri , ComponentI owner)
			throws Exception {
		super(uri , (Class<? extends OfferedCI>) NodeManagementCI.class, owner);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 
	@Override
	public Set<PeerNodeAddressI> Join(PeerNodeAddressI p) throws Exception {
	
		return this.getOwner().handleRequest(new AbstractComponent.AbstractService<Set<PeerNodeAddressI>>() {
					@Override 
					public Set<PeerNodeAddressI> call() throws Exception {
						return ((Pair)this.getServiceOwner()).join(p) ;
					}
				}) ;
	}

	@Override
	public void leave(PeerNodeAddressI p) throws Exception {
	         ( (Pair)this.getOwner()).leave(p);
			
	}

}
