package ports;

import java.util.Set;


import componenet.Facade;
import componenet.Pair;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentNodeAddressI;
import interfaces.FacadeNodeAdressI;
import interfaces.NodeCI;
import interfaces.NodeManagementCI;
import interfaces.PeerNodeAddressI;

public class NodeCIntboundPort  extends AbstractInboundPort  implements NodeCI  {


private static final long serialVersionUID = 1L;
	
	public NodeCIntboundPort (ComponentI owner , String pluginURI) throws Exception {
		super(NodeCI.class, owner ,pluginURI ,null);
		assert	uri != null && owner instanceof NodeCI ;
	}

	@Override
	public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) throws Exception {
		((Pair)this.getOwner()).runTask(
				owner -> {
					try {
						((Pair) owner).probe(facade ,remainghops ,requestURI);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}

	@Override
	public void connecte(ContentNodeAddressI p) throws Exception {
		((Pair)this.getOwner()).runTask(
				owner -> {
					try {
						((Pair) owner).connecte(p);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}

	@Override
	public void disconnecte(ContentNodeAddressI p) throws Exception {
		((Pair)this.getOwner()).runTask(
				owner -> {
					try {
						((Pair) owner).disconnectePair(p);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}

	@Override
	public void acceptNeighbours(Set<ContentNodeAddressI> neighbours) throws Exception {
		((Pair)this.getOwner()).runTask(
				owner -> {
					try {
						((Pair) owner).acceptNeighbours(neighbours);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}

	@Override
	public void acceptConnected(ContentNodeAddressI p) throws Exception {
		((Pair)this.getOwner()).runTask(
				owner -> {
					try {
						((Pair) owner).acceptConnected(p);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}
	
}

