package componenet;

import java.util.HashSet;
import java.util.Set;
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
	private ConcurrentHashMap<PeerNodeAddressI,ContentManagementCIOutbound> outPortsCM; 

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

	


	public ContentDescriptorI find (ContentTemplateI  ct , int hops ) throws Exception{
		System.out.println("find in facade ");
		
		return null;
	}
	//--------------------------------------------------------------------------
	// Component life-cycle
	//--------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public void			start() throws ComponentStartException
	{
		this.logMessage("starting provider component.");
		super.start();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public void			finalise() throws Exception
	{
		this.logMessage("stopping provider component.");
		this.printExecutionLogOnFile("provider");
		super.finalise();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdown()
	 */
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
		try {
			PortI[] p = this.findPortsFromInterface(FacadeNodeAdressI.class);
			p[0].unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdown();
	}

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#shutdownNow()
	 */
	@Override
	public void			shutdownNow() throws ComponentShutdownException
	{
		try {
			PortI[] p = this.findPortsFromInterface(FacadeNodeAdressI.class);
			p[0].unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e);
		}
		super.shutdownNow();
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
		System.out.println("c'est ok join ");

			
		String outportCM = AbstractOutboundPort.generatePortURI();
		ContentManagementCIOutbound CMportOut= new ContentManagementCIOutbound(outportCM,this);
		outPortsCM.put(p, CMportOut);
		CMportOut.publishPort();
		String inportCM_Pair = ((Pair)p).CMportIn.getPortURI();
		System.out.println( p.getNodeUri() +" demande de connecte avec "+ this.adress +" en ContentManagementCI");
		doPortConnection(outportCM,inportCM_Pair , ContentManagementCIConector.class.getCanonicalName());
		System.out.println("c'est ok " + p.getNodeUri() +" connecte avec "+ this.adress +" en ContentManagementCI" );
			

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
		if(!peerNodeList.isEmpty()) {
			peerNodeList.remove(p);
			System.out.println("supprime l'adress pair " + p.getNodeUri()+" avec " + this.adress);
		}
		else {	   
			System.out.println(" il y a pas de pair en connectent  ");	
		}
	}

	@Override
	public String getNodeidentifier() throws Exception {
		// TODO Auto-generated method stub
		return adress; 
	}

	@Override
	public Boolean isfacade() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean ispeer() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNodeManagementUri() throws Exception {
		// TODO Auto-generated method stub
		return adress;
	}
	
}