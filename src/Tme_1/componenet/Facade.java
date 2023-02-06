package Tme_1.componenet;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.PortI;
import fr.sorbonne_u.exceptions.PostconditionException;
import fr.sorbonne_u.exceptions.PreconditionException;
import Tme_1.interfaces.*;
import Tme_1.ports.ManagementInBoundPort;
/**
 * @author lyna & shuhan 
 *
 */
@OfferedInterfaces(offered = {FacadeNodeAdressI.class})

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
	protected String		uriPrefix;

	protected	Facade(	String uriPrefix,	String providerPortURI	) throws Exception
		{
			// the reflection inbound port URI is the URI of the component
			super(uriPrefix, 1, 0) ;

			assert	uriPrefix != null :
						new PreconditionException("uri can't be null!");
			assert	providerPortURI != null :
						new PreconditionException("providerPortURI can't be null!");

			this.uriPrefix = uriPrefix ;

			// if the offered interface is not declared in an annotation on
			// the component class, it can be added manually with the
			// following instruction:
			//this.addOfferedInterface(URIProviderI.class) ;

			// create the port that exposes the offered interface with the
			// given URI to ease the connection from client components.
			PortI p = new ManagementInBoundPort(providerPortURI, this);
			// publish the port
			p.publishPort();

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
			assert	this.uriPrefix.equals(uriPrefix) :
						new PostconditionException("The URI prefix has not "
													+ "been initialised!");
			assert	this.isPortExisting(providerPortURI) :
						new PostconditionException("The component must have a "
								+ "port with URI " + providerPortURI);
			assert	this.findPortFromURI(providerPortURI).
						getImplementedInterface().equals(FacadeNodeAdressI.class) :
						new PostconditionException("The component must have a "
								+ "port with implemented interface URIProviderI");
			assert	this.findPortFromURI(providerPortURI).isPublished() :
						new PostconditionException("The component must have a "
								+ "port published with URI " + providerPortURI);
		}
	//--------------------------------------------------------------------------
	// Component life-cycle
	//--------------------------------------------------------------------------

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
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


	
	public String getNodeidentifier() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Boolean isfacade() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Boolean ispeer() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getNodeManagementUri() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
