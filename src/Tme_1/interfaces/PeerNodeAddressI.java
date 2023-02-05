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
	* @param:
	* @return：String
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: shuhan
	* @date: 31 janv. 2023 21:23:06 
	*
	* 
	*/
	public String getNodeUri() throws Exception;
      
	public void leave(PeerNodeAddressI p) throws Exception;

	public Set<PeerNodeAddressI> Join(PeerNodeAddressI p) throws Exception;
}
