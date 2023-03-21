package ports;



import componenet.Facade;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentNodeAddressI;
import interfaces.NodeManagementCI;

public class NodeManagementInBoundPort extends AbstractInboundPort implements NodeManagementCI{

	public NodeManagementInBoundPort (ComponentI owner , String pluginURI) throws Exception {
		super(NodeManagementCI.class, owner ,pluginURI ,null);
		assert	uri != null && owner instanceof NodeManagementCI ;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) throws Exception{
		this.getOwner().runTask(
				owner -> {
					try {
						((Facade) owner).probe(facade , remainghops ,requestURI);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	}

	@Override
	public void join(ContentNodeAddressI p) throws Exception {
		this.getOwner().runTask(
				owner -> {
					try {
						((Facade) owner).joinPair(p);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}

	@Override
	public void leave(ContentNodeAddressI p) throws Exception {
		this.getOwner().runTask(
				owner -> {
					try {
						((Facade) owner).leavePair(p);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}

	@Override
	public void acceptProbed(ContentNodeAddressI p, String requsetURI) throws Exception {
		this.getOwner().runTask(
				owner -> {
					try {
						((Facade) owner).acceptProbed(p , requsetURI);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}

}
