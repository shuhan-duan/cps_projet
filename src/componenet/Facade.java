package componenet;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.processing.SupportedOptions;

import classes.ContentDescriptor;
import connector.ContentManagementCIConector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.components.ports.PortI;
import fr.sorbonne_u.exceptions.PostconditionException;
import fr.sorbonne_u.exceptions.PreconditionException;
import interfaces.*;
import ports.ContentManagementCIOutbound;
import ports.ManagementInBoundPort;
import ports.ManagementOutboundPort;
/**
 * @author lyna & shuhan 
 *
 */
@OfferedInterfaces(offered = {NodeCI.class,ContentManagementCI.class,NodeManagementCI.class})
@RequiredInterfaces(required = {ContentManagementCI.class})

public class Facade  extends AbstractComponent  implements FacadeNodeAdressI{

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
	protected String		adress;
	protected Set<PeerNodeAddressI>   peerNodeList ;
	//stock the outports of facade and the pair connecter with it
	private ConcurrentHashMap<PeerNodeAddressI,ContentManagementCIOutbound> outPortsCM; 
	private ConcurrentHashMap<ContentTemplateI, ContentDescriptorI> contents;

	protected	Facade(	String adress,	String inportNM	 ) throws Exception
		{
			// the reflection inbound port URI is the URI of the component
			super(adress, 1, 0) ;

			assert	adress != null :
						new PreconditionException("adress can't be null!");
			assert	inportNM != null :
						new PreconditionException("inportNM can't be null!");

			this.adress = adress ;
			this.peerNodeList = new HashSet<PeerNodeAddressI>();
			this.outPortsCM =  new ConcurrentHashMap<PeerNodeAddressI,ContentManagementCIOutbound>();
			this.contents = new ConcurrentHashMap<ContentTemplateI, ContentDescriptorI>();
			// if the offered interface is not declared in an annotation on
			// the component class, it can be added manually with the
			// following instruction:
			//this.addOfferedInterface(URIProviderI.class) ;

			// create the port that exposes the offered interface with the
			// given URI to ease the connection from client components.
			ManagementInBoundPort  NMportIn = new ManagementInBoundPort(inportNM, this);
			// publish the port
			NMportIn.publishPort();

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
			assert	this.adress.equals(adress) :
						new PostconditionException("The URI prefix has not "
													+ "been initialised!");
			assert	this.isPortExisting(inportNM) :
						new PostconditionException("The component must have a "
								+ "port with URI " + inportNM);
			assert	this.findPortFromURI(inportNM).
						getImplementedInterface().equals(FacadeNodeAdressI.class) :
						new PostconditionException("The component must have a "
								+ "port with implemented interface URIProviderI");
			assert	this.findPortFromURI(inportNM).isPublished() :
						new PostconditionException("The component must have a "
								+ "port published with URI " + inportNM);
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
		for (PeerNodeAddressI pair : neighbors) {
			try {
				ContentDescriptorI content = ((Pair)pair).find(ct, hops - 1);
				if (content != null) {
					return content;
				}
			} catch (Exception e) {
				System.err.println("Failed to contact neighbor pair: " + pair.getNodeUri());
			}
		}
	
		return null;

	}

	@Override
	public String getNodeidentifier() throws Exception {
		return adress; 
	}

	@Override
	public Boolean isfacade() throws Exception {
		return true;
	}

	@Override
	public Boolean ispeer() throws Exception {
		return false;
	}

	@Override
	public String getNodeManagementUri() throws Exception {
		return adress;
	}
	
}