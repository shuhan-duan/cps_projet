package Tme_1.connector;

import java.util.Set;

import Tme_1.interfaces.NodeManagementCI;
import Tme_1.interfaces.PeerNodeAddressI;
import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.examples.basic_cs.interfaces.URIProviderCI;

/**
 * @author lyna & shuhan 
 *
 */
public class ManagementConnector  extends  AbstractConnector implements PeerNodeAddressI{

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
	public String getNodeidentifier() throws Exception {
		return ((PeerNodeAddressI)this.offering).getNodeidentifier() ;
	}

	@Override
	public Boolean isfacade() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean ispeer() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNodeUri() throws Exception {
		return ((PeerNodeAddressI)this.offering).getNodeUri();
	}

}
