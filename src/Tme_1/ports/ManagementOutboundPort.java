package Tme_1.ports;

import java.util.Set;

import Tme_1.interfaces.NodeManagementCI;
import Tme_1.interfaces.PeerNodeAddressI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.examples.basic_cs.interfaces.URIConsumerCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * @author lyna & shuhan 
 *
 */
public class ManagementOutboundPort extends AbstractOutboundPort implements  NodeManagementCI{

	/**   
	* @Function: NodeManagementOutboundPort.java
	* @Description: 
	*
	* @param: uri
	* @param: owner
	* @version: 
	* @author: lyna & shuhan
	* @date: 30 janv. 2023 21:08:00 
	*/
	public ManagementOutboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri, (Class<? extends RequiredCI>) NodeManagementCI.class, owner);
		assert	uri != null && owner != null ;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 
	* @see Tme_1.interfaces.NodeManagementCI#Join(Tme_1.interfaces.PeerNodeAddressI)  
	* @Function: NodeManagementOutboundPort.java
	* @Function: NodeManagementInBoundPort.java
	* @Description: 
	*
	* @param: PeerNodeAddressI p
	* @return：Set<PeerNodeAddressI>
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan
	* @date: 30 janv. 2023 21:08:57 
	*/
	
	public Set<PeerNodeAddressI> Join(PeerNodeAddressI p) throws Exception {

		return ((NodeManagementCI)this.connector).Join(p); 
	}

	/** 
	* @see Tme_1.interfaces.NodeManagementCI#leave(Tme_1.interfaces.PeerNodeAddressI)  
	* @Function: NodeManagementOutboundPort.java
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
	
	public void leave(PeerNodeAddressI p) throws Exception {
		 ((NodeManagementCI)this.getConnector()).leave(p); 	
   
	}

	

}
