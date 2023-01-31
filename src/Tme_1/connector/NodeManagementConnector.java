package Tme_1.connector;

import java.util.Set;

import Tme_1.interfaces.NodeManagementCI;
import Tme_1.interfaces.PeerNodeAddressI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

/**
 * @author lyna & shuhan 
 *
 */
public class NodeManagementConnector  extends  AbstractConnector implements NodeManagementCI{

	/** 
	* @see Tme_1.interfaces.NodeManagementCI#Join(Tme_1.interfaces.PeerNodeAddressI)  
	* @Function: NodeManagementConnector.java
	* @Description: 
	*
	* @param:PeerNodeAddressI p
	* @return：Set<PeerNodeAddressI>
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: shuhan
	* @date: 31 janv. 2023 21:55:17 
	*
	*
	*/
	@Override
	public Set<PeerNodeAddressI> Join(PeerNodeAddressI p) throws Exception {
		System.out.print("From connector join ") ;
		return null;
	}

	/** 
	* @see Tme_1.interfaces.NodeManagementCI#leave(Tme_1.interfaces.PeerNodeAddressI)  
	* @Function: NodeManagementConnector.java
	* @Description: 
	*
	* @param:PeerNodeAddressI p
	* @return：void
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: shuhan
	* @date: 31 janv. 2023 21:55:21 
	*
	*/
	@Override
	public void leave(PeerNodeAddressI p) throws Exception {
		System.out.print("From connector leave") ;
	}

}
