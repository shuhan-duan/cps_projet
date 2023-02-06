package Tme_1.interfaces;

/**
 * @author lyna & shuhan 
 *
 */
public interface NodeCI {
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
	public PeerNodeAddressI	connect (PeerNodeAddressI p  ) throws Exception ;
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
    public void disconnect (PeerNodeAddressI p ) throws Exception ;
}
