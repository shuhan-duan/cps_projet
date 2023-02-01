package Tme_1.componenet;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.examples.basic_cs.interfaces.URIConsumerCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import Tme_1.interfaces.*;
/**
 * @author lyna & shuhan 
 *
 */
public class Facade  extends AbstractComponent {

	/**   
	* @Function: Facade.java
	* @Description: 
	*
	* @param: reflectionInboundPortURI
	* @param: nbThreads
	* @param: nbSchedulableThreads
	* 
	* @version: 
	* @author: lyna & shuhan
	* @date: 30 janv. 2023 20:29:55 
	*/
	protected Facade(String reflectionInboundPortURI, int nbThreads, int nbSchedulableThreads) {
		super(reflectionInboundPortURI, nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
	}

}
