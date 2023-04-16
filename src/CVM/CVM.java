package CVM;


import java.time.Instant;
import java.util.concurrent.TimeUnit;

import componenet.Client;
import componenet.Facade;
import componenet.Pair;
import connector.NodeManagementConnector;
import connector.NodeConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.helpers.CVMDebugModes;
import fr.sorbonne_u.utils.aclocks.ClocksServer;


/**
 * @author lyna & shuhan 
 *
 */
public class CVM extends AbstractCVM{
	/** URI of the facade component (convenience).						*/
	protected static final String	ContentManagementInboudPort = "inportCMfacade";

	protected static final String	ContentManagementOutboudPort = "outportCMclient";

	/** URI of the provider outbound port (simplifies the connection).		*/
	protected static final String	NodeManagementOutboundPort = "outportNMpair";

	/** URI of the consumer inbound port (simplifies the connection).		*/
	protected static final String	NodeManagemenInboundPort = "inportNMfacade";
	
	protected static final String	FacadeCMInPortClient = "inportFCMclient";
	
	protected static final String	FacadeCMInPortFacade = "inportFCMfacade";
	
	protected final int NB_PEER = 10;  
	protected final int NB_FACADE = 1;
	
	protected static final long		DELAY_TO_START_IN_NANOS =	TimeUnit.SECONDS.toNanos(5);
   public static final String		CLOCK_URI = "my-clock";

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
  
	@Override
	public void			deploy() throws Exception
	{
		assert	!this.deploymentDone() ;
		long unixEpochStartTimeInNanos =	TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis())	+ CVM.DELAY_TO_START_IN_NANOS;
		Instant	startInstant = Instant.parse("2023-02-07T08:00:00Z");
		double accelerationFactor = 60.0;
		AbstractComponent.createComponent(ClocksServer.class.getCanonicalName(),
				new Object[]{CVM.CLOCK_URI, unixEpochStartTimeInNanos,
							 startInstant, accelerationFactor});
		// ---------------------------------------------------------------------
		// Creation phase
		// ---------------------------------------------------------------------

		// create the component client
				AbstractComponent.createComponent(
						Client.class.getCanonicalName(),
						new Object[]{ContentManagementInboudPort,
								ContentManagementOutboudPort,FacadeCMInPortClient});
		System.out.println("\nCreate Composant client OK ");
		
		// create the component facade
		for(int i = 0 ; i < NB_FACADE ; i++ ) {
			AbstractComponent.createComponent(
					Facade.class.getCanonicalName(),
					new Object[]{ContentManagementInboudPort,
							NodeManagemenInboundPort ,FacadeCMInPortClient,FacadeCMInPortFacade});
		}				
				System.out.println("\nCreate Composant Facade OK ");
				
		// create the component pairs
		for (int i = 0; i < NB_PEER  ; i++) {
			AbstractComponent.createComponent(
							Pair.class.getCanonicalName(),
							new Object[]{
									NodeManagementOutboundPort+i,
									NodeManagemenInboundPort, i });
		}
		System.out.println("\nCreate Composant pairs OK ");

		

		

		super.deploy();
		assert	this.deploymentDone();
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
			a.startStandardLifeCycle(40000L);

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
