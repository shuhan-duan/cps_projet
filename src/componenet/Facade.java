package componenet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import classes.ApplicationNodeAdress;
import classes.FacadeNodeAdress;
import connector.ContentManagementConector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.*;
import ports.ContentManagementCIIntbound;
import ports.ContentManagementCIOutbound;
import ports.NodeManagementInBoundPort;

/**
 * @author lyna & shuhan 
 *
 */

public class Facade  extends AbstractComponent implements MyCMI {
	public static int cpt = 0;//current racine nb
	public static int cpt_facade = 0;
	protected final int NB_RACINE = 3;

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
	protected ApplicationNodeAdress		adress;
	protected Set<ContentNodeAddressI>   peerNodeList ;
	//stock the outports of facade and the racine pair connected with it
	private ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound> outPortsCM;
	protected NodeManagementInBoundPort  NMportIn;
	protected ContentManagementCIIntbound CMportIn;



	protected	Facade(	String ContentManagementInboudPort,	String 	NodeManagemenInboundPort) throws Exception
		{
			// the reflection inbound port URI is the URI of the component
			super(NodeManagemenInboundPort, 2, 0) ;
			this.adress = new ApplicationNodeAdress("Facade",ContentManagementInboudPort,NodeManagemenInboundPort) ;
			this.peerNodeList = new HashSet<ContentNodeAddressI>();
			this.outPortsCM =  new ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound>();

			// create the port that exposes the offered interface with the
			// given URI to ease the connection from client components.
			NMportIn = new NodeManagementInBoundPort(this.adress.getNodeManagementUri(), this);
			NMportIn.publishPort();
			CMportIn = new ContentManagementCIIntbound(this.adress.getContentManagementURI(), this);
			CMportIn.publishPort();

		}

	/**   
	* @Function: Pair.java
	* @Description: 
	*
	* @param: ContentNodeAddressI p
	* @return：Set<ContentNodeAddressI>
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 30 janv. 2023 20:34:57 
	*
	* 
	*/
	//Il faut coder la fonction Join 
	public synchronized Set<ContentNodeAddressI> joinPair(ContentNodeAddressI p)
	throws Exception{
		peerNodeList.add(p);
		 
		Set<ContentNodeAddressI> result = new HashSet<>(peerNodeList);
    	result.remove(p);
    	
    	//build connection entre facade and pair in ContentManagementCI
    	if (peerNodeList.size() < NB_RACINE +1 ){ 
			cpt++; 
			//do connect entre facade et racine en ContentManagementCI 
			String outportCM_Facade = "myOutportCMfacade" + cpt; 
			ContentManagementCIOutbound CMportOut = new ContentManagementCIOutbound(outportCM_Facade,this);
			CMportOut.publishPort(); 
			outPortsCM.put(p, CMportOut); 
			String inportCM_Pair = p.getContentManagementURI();
			doPortConnection(outportCM_Facade,
					  			inportCM_Pair, 
					  			ContentManagementConector.class.getCanonicalName());
			System.out.println("\nc'est ok " + p.getNodeidentifier() +" connecte avec "+outportCM_Facade +" en ContentManagementCI" ); 
			outPortsCM.put(p,CMportOut); 
		}
		
    	return result;
	}
	
	/**   
	* @Function: Pair.java
	* @Description: 
	*
	* @param: ContentNodeAddressI p
	* @return：void
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 30 janv. 2023 20:35:22 
	*
	* 
	*/
	public void leavePair (ContentNodeAddressI p)
	throws Exception{
		// leave pair from facade with CM
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
	@Override
	public ContentDescriptorI find (ContentTemplateI  ct , int hops ) throws Exception{
		//System.out.println("\nc'est find in facade");

		if (hops == 0) {
			return null;
		}
		Set<ContentNodeAddressI> neighbors = outPortsCM.keySet();
		
		if (neighbors == null) {
			System.out.println("\n	on a pas de neighbors");
			return null;
		}else {
			ContentNodeAddressI[] array = neighbors .toArray(new ContentNodeAddressI[0]);
			Random rand = new Random();
			int randomIndex = rand.nextInt(neighbors.size());
			ContentNodeAddressI neighbor = array[randomIndex];
			ContentManagementCIOutbound outportCM = outPortsCM.get(neighbor);
			//System.out.println("\nwill do find in :" + neighbor.getNodeidentifier()+" "+ outportCM.getPortURI());
			//System.out.println(outportCM.getPortURI()+ " is connected? "+outportCM.connected());
			ContentDescriptorI content = ((ContentManagementCI)outportCM).find(ct, hops - 1);
			if (content != null) {
				return content;
			}
		
		}
		return null;
	}
	@Override
	public Set<ContentDescriptorI> match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops) throws Exception{
		//System.out.println("\nc'est  match in facade");

		if (hops == 0) {
			return matched;
		}
		
		Set<ContentNodeAddressI> neighbors = outPortsCM.keySet();
		if (neighbors == null) {
			System.out.println("\n	on a pas de neighbors");
			return matched;
		}else {
			ContentNodeAddressI[] array = neighbors .toArray(new ContentNodeAddressI[0]);
			Random rand = new Random();
			int randomIndex = rand.nextInt(neighbors.size());
			ContentNodeAddressI neighbor = array[randomIndex];
			ContentManagementCIOutbound outportCM = outPortsCM.get(neighbor);
			Set<ContentDescriptorI> reSet =((ContentManagementCI)outportCM).match(cd, matched ,hops -1	);
			matched.addAll(reSet);
			return matched;
		
		}
	}

}