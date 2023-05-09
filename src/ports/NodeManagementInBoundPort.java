package ports;




import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentNodeAddressI;
import interfaces.NodeManagementCI;
import withplugin.components.Facade;

public class NodeManagementInBoundPort extends AbstractInboundPort implements NodeManagementCI{

	/**   
	* @Function: NodeManagementInBoundPort.java
	* @Description: 
	*
	* @param:uri
	* @param:owner
	* @version: 
	* @author: lyna & shuhan
	* @date: 30 janv. 2023 21:04:56 
	*/
	public NodeManagementInBoundPort( ComponentI owner,String uri )
			throws Exception {
		super(uri ,  NodeManagementCI.class, owner);
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
