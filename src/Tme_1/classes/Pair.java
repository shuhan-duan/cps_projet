package Tme_1.classes;

import java.util.Set;

import Tme_1.interfaces.PeerNodeAddressI;
import fr.sorbonne_u.components.AbstractComponent;

/**
 * @author lyna & shuhan 
 *
 */
public class Pair  extends AbstractComponent{
	
	/**   
	* @Function: Pair.java
	* @Description: 
	*
	* @param: NodeUri
	* @param: nbThreads
	* @param: nbSchedulableThreads
	* @version: 
	* @author: lyna & shuhan 
	* @date: 30 janv. 2023 20:34:23 
	*/
	protected Pair(String NodeUri, int nbThreads, int nbSchedulableThreads) {
		super(NodeUri, nbThreads, nbSchedulableThreads);
	}

	protected String NodeUri;
	
	
	/**   
	* @Function: Pair.java
	* @Description: 
	*
	* @param: PeerNodeAddressI p
	* @return：Set<PeerNodeAddressI>
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 30 janv. 2023 20:34:57 
	*
	* 
	*/
	public Set<PeerNodeAddressI>  join   (PeerNodeAddressI p) 
	throws Exception{
	   System.out.print("c'est ok join ");
	   return  null; 
	}
	
	/**   
	* @Function: Pair.java
	* @Description: 
	*
	* @param: PeerNodeAddressI p
	* @return：void
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 30 janv. 2023 20:35:22 
	*
	* 
	*/
	public void leave (PeerNodeAddressI p)
	throws Exception{
	  System.out.print("c'est ok leave  ");
	}

}
