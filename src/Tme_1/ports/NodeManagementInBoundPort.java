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

/**
 * @author lyna & shuhan 
 *
 */
public class NodeManagementInBoundPort extends AbstractInboundPort implements NodeManagementCI {

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
		super(uri , (Class<? extends OfferedCI>) NodeManagementCI.class, owner);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 
	/** 
	* @see Tme_1.interfaces.NodeManagementCI#Join(Tme_1.interfaces.PeerNodeAddressI)  
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
	@Override
	public Set<PeerNodeAddressI> Join(PeerNodeAddressI p) throws Exception {
	
		return this.getOwner().handleRequest(new AbstractComponent.AbstractService<Set<PeerNodeAddressI>>() {
					@Override 
					public Set<PeerNodeAddressI> call() throws Exception {
						return ((Pair)this.getServiceOwner()).join(p) ;
					}
				}) ;
	}

	/** 
	* @see Tme_1.interfaces.NodeManagementCI#leave(Tme_1.interfaces.PeerNodeAddressI)  
	* @Function: NodeManagementInBoundPort.java
	* @Description: 
	*
	* @param:PeerNodeAddressI p
	* @return：void
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan
	* @date: 30 janv. 2023 21:06:53 
	*
	*/
	@Override
	public void leave(PeerNodeAddressI p) throws Exception {
	         ( (Pair)this.getOwner()).leave(p);
			
	}

}
