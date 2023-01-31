package Tme_1.tests;


import Tme_1.classes.Facade;
import Tme_1.classes.Pair;
import Tme_1.connector.NodeManagementConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.helpers.CVMDebugModes;

/**
 * @author lyna & shuhan 
 *
 */
public class CVM extends AbstractCVM{
	/** URI of the facade component (convenience).						*/
	protected static final String	FACADE_COMPONENT_URI = "my-URI-facade";
	/** URI of the pair component (convenience).						*/
	protected static final String	PAIR_COMPONENT_URI = "my-URI-pair";
	/** URI of the provider outbound port (simplifies the connection).		*/
	protected static final String	NodeManagementOutboundPort = "oport";
	/** URI of the consumer inbound port (simplifies the connection).		*/
	protected static final String	NodeManagemenInboundPort = "iport";
	/**   
	* @Function: CVM.java
	* @Description: 
	*
	* @param:
	* @version: 
	* @author: shuhan
	* @date: 31 janv. 2023 21:31:56 
	*/
	public CVM() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	protected String	uriFacadeURI;
	protected String	uriPairURI;
	@Override
	public void			deploy() throws Exception
	{
		assert	!this.deploymentDone() ;

		// ---------------------------------------------------------------------
		// Configuration phase
		// ---------------------------------------------------------------------

		// debugging mode configuration; comment and uncomment the line to see
		// the difference
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.LIFE_CYCLE);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.INTERFACES);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PORTS);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CALLING);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.EXECUTOR_SERVICES);

		// ---------------------------------------------------------------------
		// Creation phase
		// ---------------------------------------------------------------------

		// create the component pair
		this.uriPairURI =
			AbstractComponent.createComponent(
					Facade.class.getCanonicalName(),
					new Object[]{PAIR_COMPONENT_URI,
							NodeManagemenInboundPort});
		assert	this.isDeployedComponent(this.uriPairURI);
		// make it trace its operations; comment and uncomment the line to see
		// the difference
		this.toggleTracing(this.uriPairURI);
		this.toggleLogging(this.uriPairURI);

		// create the component facade
		this.uriFacadeURI =
			AbstractComponent.createComponent(
					Pair.class.getCanonicalName(),
					new Object[]{FACADE_COMPONENT_URI,
							NodeManagementOutboundPort});
		assert	this.isDeployedComponent(this.uriFacadeURI);
		// make it trace its operations; comment and uncomment the line to see
		// the difference
		this.toggleTracing(this.uriFacadeURI);
		this.toggleLogging(this.uriFacadeURI);
		
		// ---------------------------------------------------------------------
		// Connection phase
		// ---------------------------------------------------------------------

		// do the connection
		this.doPortConnection(
				this.uriPairURI,
				NodeManagementOutboundPort,
				NodeManagemenInboundPort,
				NodeManagementConnector.class.getCanonicalName()) ;

		// ---------------------------------------------------------------------
		// Deployment done
		// ---------------------------------------------------------------------

		super.deploy();
		assert	this.deploymentDone();
	}

	/**   
	* @Function: CVM.java
	* @Description: 
	*
	* @param:
	* @return：
	* @throws：
	*
	* @version: v1.0.0
	* @author: shuhan
	* @date: 31 janv. 2023 21:35:23 
	*
	* 
	*/
	public static void		main(String[] args)
	{
		try {
			// Create an instance of the defined component virtual machine.
			CVM a = new CVM();
			// Execute the application.
			a.startStandardLifeCycle(20000L);
			// Give some time to see the traces (convenience).
			Thread.sleep(5000L);
			// Simplifies the termination (termination has yet to be treated
			// properly in BCM).
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
