package Tme_1.interfaces;

import java.util.Set;

import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * @author lyna & shuhan 
 *
 */
public interface PeerNodeAddressI extends NodeAdresseI , RequiredCI{
	
	/**   
	* @Function: PeerNodeAddressI.java
	* @Description: 
	*
	* @param:null
	* @return：String
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 31 janv. 2023 21:23:06 
	*
	* 
	*/
	public String getNodeUri() throws Exception;

	/**   
	* @Function: PeerNodeAddressI.java
	* @Description: 
	*
	* @param: PeerNodeAddressI p
	* @return：void
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 6 févr. 2023 14:00:40 
	*
	* 
	*/
	public void leave(PeerNodeAddressI p) throws Exception;

	/**   
	* @Function: PeerNodeAddressI.java
	* @Description: 
	*
	* @param:PeerNodeAddressI p
	* @return：Set<PeerNodeAddressI>
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 6 févr. 2023 14:01:06 
	*
	* 
	*/
	public Set<PeerNodeAddressI> Join(PeerNodeAddressI p) throws Exception;
}
