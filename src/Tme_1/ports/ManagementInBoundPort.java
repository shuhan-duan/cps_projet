package Tme_1.ports;
import Tme_1.componenet.*;

import java.util.Set;

import Tme_1.componenet.Pair;
import Tme_1.interfaces.FacadeNodeAdressI;
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
public class ManagementInBoundPort extends AbstractInboundPort  implements NodeManagementCI  {

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
	public ManagementInBoundPort(String uri , ComponentI owner)
			throws Exception {
		super(uri , (Class<? extends OfferedCI>) NodeManagementCI.class, owner);
	}
	public ManagementInBoundPort(ComponentI owner) throws Exception{
		super((Class<? extends OfferedCI>) NodeManagementCI.class, owner); 
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
	
	public Set<PeerNodeAddressI> Join(PeerNodeAddressI p) throws Exception {
	
		return this.getOwner().handleRequest(new AbstractComponent.AbstractService<Set<PeerNodeAddressI>>() {
					@Override 
					public Set<PeerNodeAddressI> call() throws Exception {
						return ((Pair)this.getServiceOwner()).Join(p) ;
					}
				}) ;
	}
	@Override
	public void leave(PeerNodeAddressI p) throws Exception {
		// TODO Auto-generated method stub
		
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
	
	
}
