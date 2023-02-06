package interfaces;


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

	
}
