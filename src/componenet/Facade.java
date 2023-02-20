package componenet;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import classes.FacadeNodeAdress;
import connector.ContentManagementCIConector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.exceptions.PostconditionException;
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
	//stock the outports of facade and the pair connecter with it
	private ConcurrentHashMap<PeerNodeAddressI,ContentManagementCIOutbound> outPortsCM; 

	private ConcurrentHashMap<ContentTemplateI, ContentDescriptorI> contents;
	protected ManagementInBoundPort  NMportIn;
	protected ContentManagementCIIntbound CMportIn;

	protected	Facade(	String ContentManagementInboudPort,	String 	NodeManagemenInboundPort) throws Exception
		{
			// the reflection inbound port URI is the URI of the component
			super(NodeManagemenInboundPort, 1, 0) ;

			assert	ContentManagementInboudPort != null :
						new PreconditionException("inportCM can't be null!");
			assert	NodeManagemenInboundPort != null :
						new PreconditionException("inportNM can't be null!");

			this.adress = new FacadeNodeAdress(ContentManagementInboudPort,NodeManagemenInboundPort) ;
			this.peerNodeList = new HashSet<PeerNodeAddressI>();
			this.outPortsCM =  new ConcurrentHashMap<PeerNodeAddressI,ContentManagementCIOutbound>();
			this.contents = new ConcurrentHashMap<ContentTemplateI, ContentDescriptorI>();
			// if the offered interface is not declared in an annotation on
			// the component class, it can be added manually with the
			// following instruction:
			//this.addOfferedInterface(URIProviderI.class) ;

			// create the port that exposes the offered interface with the
			// given URI to ease the connection from client components.
			NMportIn = new ManagementInBoundPort(NodeManagemenInboundPort, this);
			// publish the port
			NMportIn.publishPort();
			CMportIn = new ContentManagementCIIntbound(ContentManagementInboudPort, this);
			CMportIn.publishPort();

			if (AbstractCVM.isDistributed) {
				this.getLogger().setDirectory(System.getProperty("user.dir"));
			} else {
				this.getLogger().setDirectory(System.getProperty("user.home"));
			}
			this.getTracer().setTitle("provider");
			this.getTracer().setRelativePosition(1, 0);

			Facade.checkInvariant(this) ;
			AbstractComponent.checkImplementationInvariant(this);
			AbstractComponent.checkInvariant(this);
			assert	this.isPortExisting(NodeManagemenInboundPort) :
						new PostconditionException("The component must have a "
								+ "port with URI " + NodeManagemenInboundPort);
			assert	this.findPortFromURI(NodeManagemenInboundPort).
						getImplementedInterface().equals(FacadeNodeAdressI.class) :
						new PostconditionException("The component must have a "
								+ "port with implemented interface URIProviderI");
			assert	this.findPortFromURI(NodeManagemenInboundPort).isPublished() :
						new PostconditionException("The component must have a "
								+ "port published with URI " + NodeManagemenInboundPort);
			assert	this.isPortExisting(ContentManagementInboudPort) :
						new PostconditionException("The component must have a "
								+ "port with URI " + ContentManagementInboudPort);
			assert	this.findPortFromURI(ContentManagementInboudPort).
						getImplementedInterface().equals(FacadeNodeAdressI.class) :
						new PostconditionException("The component must have a "
								+ "port with implemented interface URIProviderI");
			assert	this.findPortFromURI(ContentManagementInboudPort).isPublished() :
						new PostconditionException("The component must have a "
								+ "port published with URI " + ContentManagementInboudPort);
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
	public Set<PeerNodeAddressI>  Join   (PeerNodeAddressI p) 
	throws Exception{
		peerNodeList.add(p);
		System.out.println("\n"+ p.getNodeUri()+ " est join avec facade\n");

		//do connect entre facade et pair en ContentManagementCI
		String outportCM_Facade = "myOutportCMfacade" + UUID.randomUUID();
		ContentManagementCIOutbound CMportOut= new ContentManagementCIOutbound(outportCM_Facade,this);
		outPortsCM.put(p, CMportOut);
		CMportOut.publishPort();
		String inportCM_Pair = ((Pair)p).CMportIn.getPortURI(); 
		//System.out.println("ici "+inportCM_Pair);
		System.out.println( "\n"+p.getNodeUri() +" demande de connecte avec "+ this.adress +" en ContentManagementCI\n");
		doPortConnection(outportCM_Facade, inportCM_Pair, ContentManagementCIConector.class.getCanonicalName());
		System.out.println("\nc'est ok " + p.getNodeUri() +" connecte avec "+ this.adress +" en ContentManagementCI\n" );	
		

		return this.peerNodeList;
			
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
		System.out.println("\n"+ p.getNodeUri()+" demande de quitter facade\n");

		if(!peerNodeList.isEmpty()) {
			peerNodeList.remove(p);
			System.out.println("\nsupprime l'adress pair " + p.getNodeUri()+" avec " + this.adress+"\n");
		}
		else {	   
			System.out.println(" il y a pas de pair connecte avec facade ");	
		}
	}

	public ContentDescriptorI find (ContentTemplateI  ct , int hops ) throws Exception{
		System.out.println("\nc'est  find in facade \n");

		if (hops == 0) {
			return null;
		}
		// Cherche parmi ses propres contenus
		for (ContentDescriptorI content : this.contents.values()) {
			if (content.match(ct)) {
				return content;
			}
		}
		// Si le contenu n'est pas dans le nœud courant, demande à un autre pair
		Set<PeerNodeAddressI> neighbors = outPortsCM.keySet();
		if (neighbors == null) {
			return null;
		}
		System.out.println(outPortsCM);
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