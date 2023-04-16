package componenet;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import classes.ApplicationNodeAdress;
import classes.FacadeNodeAdress;
import connector.ContentManagementConector;
import connector.FacadeContentManagementConector;
import connector.NodeConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.*;
import ports.ContentManagementCIIntbound;
import ports.ContentManagementCIOutbound;
import ports.FacadeContentManagementCInbound;
import ports.FacadeContentManagementCOutbound;
import ports.NodeCOutboundPort;
import ports.NodeManagementInBoundPort;
import Plugin.*;
/**
 * @author lyna & shuhan 
 *
 */

public class Facade  extends AbstractComponent{
	public static int cpt = 0;//current racine nb
	public static int cpt_facade = 0;
	protected final int NB_RACINE = 3;
	protected final int NB_PROBE = 3;
	

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
	Facade_plugin facade_plugin ;
 
	protected	Facade(	String ContentManagementInboudPort,	String 	NodeManagemenInboundPort ,String FCMInbountPortClient, String FacadeCMInPortFacade) throws Exception
		{
			// the reflection inbound port URI is the URI of the component
			super(NodeManagemenInboundPort, 1, 0) ;
			Facade_plugin facade_plugin = new Facade_plugin(ContentManagementInboudPort, NodeManagemenInboundPort,FCMInbountPortClient,FacadeCMInPortFacade);
			this.installPlugin(facade_plugin);
			
		}

	
	
	
	

}