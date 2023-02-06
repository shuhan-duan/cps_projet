package Tme_1.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;

/**
 * @author lyna & shuhan 
 *
 */
public interface FacadeNodeAdressI extends NodeAdresseI , OfferedCI {
	
	/**   
	* @Function: FacadeNodeAdressI.java
	* @Description: 
	*
	* @param:null
	* @return：String
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 6 févr. 2023 14:02:28 
	*
	* 
	*/
	public String getNodeManagementUri() throws Exception;

}
