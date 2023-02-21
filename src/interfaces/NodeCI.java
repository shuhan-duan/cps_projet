package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

/**
 * @author lyna & shuhan 
 *
 */
public interface NodeCI extends OfferedCI, RequiredCI {
	/**   
	* @Function: NodeCI.java
	* @Description: 
	*
	* @param:PeerNodeAddressI p
	* @return：PeerNodeAddressI
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 31 janv. 2023 21:21:29 
	*
	* 
	*/
	
	
	public PeerNodeAddressI	connecte (PeerNodeAddressI p  ) throws Exception ;
    /**   
    * @Function: NodeCI.java
    * @Description: 
    *
    * @param:PeerNodeAddressI p
    * @return：void
    * @throws：Exception
    *
    * @version: v1.0.0
    * @author: lyna & shuhan 
    * @date: 31 janv. 2023 21:21:32 
    *
    * 
    */
    public void disconnecte (PeerNodeAddressI p ) throws Exception ;
}
