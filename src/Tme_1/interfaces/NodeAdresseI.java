package Tme_1.interfaces;

/**
 * @author lyna & shuhan 
 *
 */
public interface NodeAdresseI {
	
	/**   
	* @Function: NodeAdresseI.java
	* @Description: 
	*
	* @param:
	* @return：String
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 31 janv. 2023 21:20:40 
	*
	* 
	*/
	public String getNodeidentifier()throws Exception;
	/**   
	* @Function: NodeAdresseI.java
	* @Description: 
	*
	* @param:
	* @return：Boolean
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 31 janv. 2023 21:20:42 
	*
	* 
	*/
	public Boolean isfacade()throws Exception; 
	/**   
	* @Function: NodeAdresseI.java
	* @Description: 
	*
	* @param:
	* @return：Boolean
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 31 janv. 2023 21:20:45 
	*
	* 
	*/
	public Boolean ispeer()throws Exception;

}
