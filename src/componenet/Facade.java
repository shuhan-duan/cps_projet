package componenet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.spi.DirStateFactory.Result;

import classes.FacadeNodeAdress;
import connector.ContentManagementCIConector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.*;
import ports.ContentManagementCIIntbound;
import ports.ContentManagementCIOutbound;
import ports.ManagementInBoundPort;

/**
 * @author lyna & shuhan 
 *
 */
@OfferedInterfaces(offered = {NodeCI.class,ContentManagementCI.class,NodeManagementCI.class})
@RequiredInterfaces(required = {ContentManagementCI.class})

public class Facade  extends AbstractComponent  {
	public static int cpt = 0;//current racine nb
	protected final int NB_RACINE = 2;

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
	protected FacadeNodeAdress		adress;
	protected Set<PeerNodeAddressI>   peerNodeList ;
	//stock the outports of facade and the racine pair connected with it
	private ConcurrentHashMap<PeerNodeAddressI,ContentManagementCIOutbound> outPortsCM;

	private ConcurrentHashMap<ContentTemplateI, ContentDescriptorI> contents;
	protected ManagementInBoundPort  NMportIn;
	protected ContentManagementCIIntbound CMportIn;
	protected ContentManagementCIOutbound CMportOut;

	//liste_racine 
	private HashMap<PeerNodeAddressI, ContentManagementCIOutbound> liste_racine;


	protected	Facade(	String ContentManagementInboudPort,	String 	NodeManagemenInboundPort) throws Exception
		{
			// the reflection inbound port URI is the URI of the component
			super(NodeManagemenInboundPort, 1, 0) ;
			this.adress = new FacadeNodeAdress(ContentManagementInboudPort,NodeManagemenInboundPort) ;
			this.peerNodeList = new HashSet<PeerNodeAddressI>();
			this.outPortsCM =  new ConcurrentHashMap<PeerNodeAddressI,ContentManagementCIOutbound>();
			this.contents = new ConcurrentHashMap<ContentTemplateI, ContentDescriptorI>();

			// create the port that exposes the offered interface with the
			// given URI to ease the connection from client components.
			NMportIn = new ManagementInBoundPort(this.adress.getNodeManagementUri(), this);
			NMportIn.publishPort();
			CMportIn = new ContentManagementCIIntbound(this.adress.getNodeidentifier(), this);
			CMportIn.publishPort();


		}

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
	//Il faut coder la fonction Join 
	public synchronized Set<PeerNodeAddressI> joinPair(PeerNodeAddressI p)
	throws Exception{
		peerNodeList.add(p);
		if (peerNodeList.size() < NB_RACINE+1 ){
			cpt++;
			//do connect entre facade et racine en ContentManagementCI
			String outportCM_Facade = "myOutportCMfacade" + cpt;
			ContentManagementCIOutbound CMportOut= new ContentManagementCIOutbound(outportCM_Facade,this);
			CMportOut.publishPort();
			outPortsCM.put(p, CMportOut);
			String inportCM_Pair =p.getNodeidentifier();
			doPortConnection(outportCM_Facade, inportCM_Pair, ContentManagementCIConector.class.getCanonicalName());
		}else {
			liste_racine.put(p ,this.CMportOut);
		}
		Set<PeerNodeAddressI> result = new HashSet<>(peerNodeList);
    	result.remove(p);
    	return result;
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
	* @date: 30 janv. 2023 20:35:22 fin
	*
	* 
	*/
	public void leavePair (PeerNodeAddressI p)
	throws Exception{
		if (outPortsCM.containsKey(p)){
			if(!outPortsCM.isEmpty()) {
				doPortDisconnection(outPortsCM.get(p).getPortURI());
				System.out.println("\nsupprime le inportCM racine pair :  " + p.getNodeidentifier()+" avec le outport facade: " + outPortsCM.get(p).getPortURI());
				outPortsCM.remove(p);
			}
			else {
				System.out.println(" il y a plus de pair racine connecte avec facade ");
			}
		}
	}

	public ContentDescriptorI find (ContentTemplateI  ct , int hops ) throws Exception{
		// Si le contenu n'est pas dans le nœud courant, demande à un autre pair
		Set<PeerNodeAddressI> neighbors = outPortsCM.keySet();
		if (neighbors == null) {
			return null;
		}
		for ( ContentManagementCIOutbound outportCMfacade: outPortsCM.values()) {
			try {
				ContentDescriptorI content = outportCMfacade.find(ct, hops); ;
				if (content != null) {
					System.out.println("-------------find-------");
					System.out.println("Value returned by find is " +content);
					return content;
				}
			} catch (Exception e) {
				System.err.println("Failed to contact neighbor pair: " + outportCMfacade);
			}
		}
		return null;
	}

	/*
	@Override
	public void			execute() throws Exception
	{
		// application execution code (similar to a main method in Java) is
		// put here.
		System.out.println("Entree dans l execute de la facade");

		this.logMessage("executing facade component.") ;

		// Run the first service method invocation; the code of the method run
		// below will be executed asynchronously as a separate task, hence this
		// method execute will be free to finish its execution and free the
		// thread that is executing it.

		
		
		
		

		while(outPortsCM.isEmpty()){
			Thread.sleep(1000L);
		 }
		  System.out.println("Fini");
		try { 
			this.find(cd, 3); System.out.println("ici");
		} catch (Exception e) {
			e.printStackTrace(); 
		  }
		} 
	 */
	

}