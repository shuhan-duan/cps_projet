package tests;


import componenet.Facade;
import componenet.Pair;
import connector.ManagementConnector;
import connector.NodeC_conector;
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
	protected static final String	PAIR_COMPONENT_URI1 = "my-URI-pair1";
	/** URI of the pair component (convenience).						*/
	protected static final String	PAIR_COMPONENT_URI2 = "my-URI-pair2";
	/** URI of the provider outbound port (simplifies the connection).		*/
	protected static final String	NodeManagementOutboundPort1 = "outportNMpair1";
	protected static final String	NodeManagementOutboundPort2 = "outportNMpair2";

	/** URI of the consumer inbound port (simplifies the connection).		*/
	protected static final String	NodeManagemenInboundPort = "inportNMfacade";

	/**   
	* @Function: CVM.java
	* @Description: 
	*
	* @param:
	* @version: 
	* @author: lyna & shuhan
	* @date: 31 janv. 2023 21:31:56 
	*/
	public CVM() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	protected String	uriFacadeURI;
	protected String	uriPair1URI;
	protected String	uriPair2URI;

	@Override
	public void			deploy() throws Exception
	{
		assert	!this.deploymentDone() ;

		// ---------------------------------------------------------------------
		// Configuration phase
		// ---------------------------------------------------------------------

		// debugging mode configuration; comment and uncomment the line to see
		// the difference
		/* 
		 * AbstractCVM.DEBUG_MODE.add(CVMDebugModes.LIFE_CYCLE);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.INTERFACES);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.PORTS);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CONNECTING);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.CALLING);
		AbstractCVM.DEBUG_MODE.add(CVMDebugModes.EXECUTOR_SERVICES);
		*/
		

		// ---------------------------------------------------------------------
		// Creation phase
		// ---------------------------------------------------------------------

		// create the component facade
		this.uriFacadeURI =
			AbstractComponent.createComponent(
					Facade.class.getCanonicalName(),
					new Object[]{FACADE_COMPONENT_URI,
							NodeManagemenInboundPort});
		assert	this.isDeployedComponent(this.uriFacadeURI);
		// make it trace its operations; comment and uncomment the line to see
		// the difference
		//this.toggleTracing(this.uriFacadeURI);
		//this.toggleLogging(this.uriFacadeURI);
		System.out.println("\nCreate Composant Facade OK \n");

		
		// create the component pair1 
		this.uriPair1URI =
			AbstractComponent.createComponent(
					Pair.class.getCanonicalName(),
					new Object[]{PAIR_COMPONENT_URI1,
							NodeManagementOutboundPort1});  
		assert	this.isDeployedComponent(this.uriPair1URI);
		//this.toggleTracing(this.uriPair1URI);
		//this.toggleLogging(this.uriPair1URI);
		
		// create the component pair2
		this.uriPair2URI =
			AbstractComponent.createComponent(
					Pair.class.getCanonicalName(),
					new Object[]{PAIR_COMPONENT_URI2,
							NodeManagementOutboundPort2});
		assert	this.isDeployedComponent(this.uriPair2URI);
		//this.toggleTracing(this.uriPair2URI);
		//this.toggleLogging(this.uriPair2URI);

		// ---------------------------------------------------------------------
		// Connection phase
		// ---------------------------------------------------------------------

		// do the connection du composant pair1 avec facade 
		this.doPortConnection(
				this.uriPair1URI,
				NodeManagementOutboundPort1,
				NodeManagemenInboundPort,
				ManagementConnector.class.getCanonicalName()) ;
				//System.out.println("++++++++++++++++++++++++++ 1 " );
		// do the connection du composant pair2 avec facade 
		this.doPortConnection(
				this.uriPair2URI,
				NodeManagementOutboundPort2,
				NodeManagemenInboundPort,
				ManagementConnector.class.getCanonicalName()) ;
				//System.out.println("++++++++++++++++++++++++++ 2");

		

		// ---------------------------------------------------------------------
		// Deployment done
		// ---------------------------------------------------------------------
		System.out.println("\nCreate Composant Connector  pair1 et pair2 OK \n");
		//Connection des deuc composant pair grace a nodeCi
		/*
		System.out.println("//////////////////");
		System.out.println(this.uriFacadeURI);
		System.out.println(this.uriPair1URI);
		System.out.println(this.uriPair2URI);
		 */
		

		super.deploy();
		assert	this.deploymentDone();
	}

	/**   
	* @Function: CVM.java
	* @Description: 
	*
	* @param:
	* @return：void
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: shuhan
	* @date: 31 janv. 2023 21:35:23 
	*
	* 
	*/
	@Override
	public void	finalise() throws Exception
	{
		// Port disconnections can be done here for static architectures
		// otherwise, they can be done in the finalise methods of components.
		this.doPortDisconnection(this.uriPair1URI,
				NodeManagementOutboundPort1);
		this.doPortDisconnection(this.uriPair2URI,
				NodeManagementOutboundPort2);
		super.finalise();	
	}
	
	
	@Override
	public void	 shutdown() throws Exception
	{
		assert	this.allFinalised();
		// any disconnection not done yet can be performed here
		
		super.shutdown();
	}
	/**   
	* @Function: CVM.java
	* @Description: 
	*
	* @param: String[] args
	* @return：void
	* @throws：
	*
	* @version: v1.0.0
	* @author: lyna & shuhan
	* @date: 2 févr. 2023 21:55:34 
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
			Thread.sleep(1000L);

			// Simplifies the termination (termination has yet to be treated
			// properly in BCM).
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
