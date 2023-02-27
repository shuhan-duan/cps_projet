package interfaces;

import java.util.Set;

/**
 * @author lyna & shuhan 
 *
 */
public interface ContentTemplateI {
	/**   
	* @Function: ContentTemplateI.java
	* @Description: 
	*
	* @param:
	* @return：String
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 31 janv. 2023 21:16:49 
	*
	* 
	*/
	public String getTitre ()throws Exception;
	
	/**   
	* @Function: ContentTemplateI.java
	* @Description: 
	*
	* @param:
	* @return：String
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 31 janv. 2023 21:16:52 
	*
	* 
	*/
	public String getALbumTitre ()throws Exception;
	/**   
	* @Function: ContentTemplateI.java
	* @Description: 
	*
	* @param:
	* @return：Set <String>
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 31 janv. 2023 21:16:55 
	*
	* 
	*/
	public Set <String>  getInterpreters ()throws Exception;
	/**   
	* @Function: ContentTemplateI.java
	* @Description: 
	*
	* @param:
	* @return：Set <String>
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 31 janv. 2023 21:16:58 
	*
	* 
	*/
	public Set <String>  getComposers ()throws Exception;
}
