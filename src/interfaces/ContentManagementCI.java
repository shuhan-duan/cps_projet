package interfaces;

import java.util.Set;

/**
 * @author lyna & shuhan 
 *
 */
public interface ContentManagementCI {
	/**   
	* @Function: ContentManagementCI.java
	* @Description: 
	*
	* @param:ContentTemplateI cd
	* @param:hops int
	* @return：ContentDescriptorI
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan
	* @date: 31 janv. 2023 21:13:43 
	*
	* 
	*/
	public ContentDescriptorI find(ContentTemplateI cd  ,int hops )throws Exception;
	
	 /**   
	* @Function: ContentManagementCI.java
	* @Description: 
	*
	* @param:ContentTemplateI cd
	* @param:Set<ContentDescriptorI> matched
	* @param: int hops
	* @return：Set<ContentDescriptorI>
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan
	* @date: 31 janv. 2023 21:14:26 
	*
	* 
	*/
	public Set<ContentDescriptorI> match(ContentTemplateI cd , Set<ContentDescriptorI> matched  , int hops)throws Exception;
}
