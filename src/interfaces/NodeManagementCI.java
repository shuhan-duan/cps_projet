package interfaces;

import java.util.Set;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * @author lyna & shuhan 
 *
 */
public interface NodeManagementCI extends OfferedCI, RequiredCI {
	/**   
	* @Function: NodeManagementCI.java
	* @Description: 
	*
	* @param: PeerNodeAddressI p
	* @return：Set <PeerNodeAddressI>
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 31 janv. 2023 21:22:24 
	*
	* 
	*/
	public Set <ContentNodeAddressI> join( ContentNodeAddressI p   ) throws Exception ;
	/**   
	* @Function: NodeManagementCI.java
	* @Description: 
	*
	* @param: PeerNodeAddressI p
	* @return：void
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 31 janv. 2023 21:22:26 
	*
	* 
	*/
	public void leave ( ContentNodeAddressI p  )  throws Exception;
}
