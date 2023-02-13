package ports;
import java.util.Set;

import componenet.*;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.FacadeNodeAdressI;
import interfaces.NodeManagementCI;
import interfaces.PeerNodeAddressI;

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
		super(uri ,  FacadeNodeAdressI.class, owner);
		assert	uri != null && owner instanceof NodeManagementCI ;

	}
	/**   
	* @Function: ManagementInBoundPort.java
	* @Description: 
	*
	* @param:ComponentI owner
	* @version: 
	* @author: lyna & shuhan
	* @date: 6 févr. 2023 14:03:54 
	*/
	public ManagementInBoundPort(ComponentI owner) throws Exception{
		super( FacadeNodeAdressI.class, owner); 
		assert	owner instanceof NodeManagementCI ;

	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 
	/** 
	* @see interfaces.NodeManagementCI#Join(interfaces.PeerNodeAddressI)  
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
						return ((Facade)this.getServiceOwner()).Join(p) ;
					}
				}) ;
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
		this.getOwner().handleRequest(new AbstractComponent.AbstractService<Void>() {
			@Override 
			public Void call() throws Exception {
				((Facade)this.getServiceOwner()).leave(p) ;
				return null;
			}
		}) ;
	}

	
	
}