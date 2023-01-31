package Tme_1.interfaces;

/**
 * @author lyna & shuhan 
 *
 */

public interface ContentDescriptorI extends ContentTemplateI {
 
	/**   
	* @Function: ContentDescriptorI.java
	* @Description: 
	*
	* @param: void
	* @return：ContentNodeAddressI
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: shuhan &lyna
	* @date: 31 janv. 2023 21:11:01 
	*
	* 
	*/
	public ContentNodeAddressI getContentNodeAdress()throws Exception;
	
	/**   
	* @Function: ContentDescriptorI.java
	* @Description: 
	*
	* @param:void
	* @return：long
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: shuhan &lyna
	* @date: 31 janv. 2023 21:12:10 
	*
	* 
	*/
	public long getsize()throws Exception;
	
	
	/**   
	* @Function: ContentDescriptorI.java
	* @Description: 
	*
	* @param: ContentDescriptorI cd
	* @return：boolean
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: shuhan &lyna
	* @date: 31 janv. 2023 21:12:48 
	*
	* 
	*/
	public boolean equals (ContentDescriptorI cd ) throws Exception;
}
