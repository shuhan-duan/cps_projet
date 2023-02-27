package ports;
import java.util.Set;

import componenet.*;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ContentNodeAddressI;
import interfaces.FacadeNodeAdressI;
import interfaces.NodeManagementCI;
import interfaces.PeerNodeAddressI;

/**
 * @author lyna & shuhan 
 *
 */
public class NodeManagementInBoundPort extends AbstractInboundPort  implements NodeManagementCI  {

	private static final long serialVersionUID = 1L;

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
	public NodeManagementInBoundPort(String uri , ComponentI owner)
			throws Exception {
		super(uri ,  NodeManagementCI.class, owner);
		assert	uri != null && owner instanceof NodeManagementCI ;

	}

 
	/** 
	* @see interfaces.NodeManagementCI#join(interfaces.PeerNodeAddressI)
	* @Function: NodeManagementInBoundPort.java
	* @Description: 
	*
	* @param: PeerNodeAddressI p
	* @return：Set<PeerNodeAddressI>
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan
	* @date: 30 janv. 2023 21:05:34 
	*          
	*/
	
	public Set<PeerNodeAddressI> join(PeerNodeAddressI p) throws Exception {
	
		return this.getOwner().handleRequest(
				owner ->(((Facade) owner).joinPair(p))
					);
	}
	
	/**   
	* @Function: ManagementInBoundPort.java
	* @Description: 
	*
	* @param:PeerNodeAddressI p
	* @return：void
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 6 févr. 2023 14:04:42 
	*
	* 
	*/
	public void leave(PeerNodeAddressI p) throws Exception {
		this.getOwner().runTask(
				owner -> {
					try {
						((Facade) owner).leavePair(p);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	}

}
