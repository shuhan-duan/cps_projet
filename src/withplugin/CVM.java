package withplugin;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;



import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

import fr.sorbonne_u.utils.aclocks.ClocksServer;
import withplugin.components.Client;
import withplugin.components.Facade;
import withplugin.components.Pair;


/**
 * @author lyna & shuhan 
 *
 */
public class CVM extends AbstractCVM{
	/** URI of the facade inbound component (convenience).						*/
	protected static final String	NMInboundPortURI = "inportNMfacade"; // 
	protected static final String	FacadeCMInPortFacadeURI = "inportFCMfacade";
	
	/** URI of the consumer inbound port (simplifies the connection).		*/
	protected static final String	FacadeCMInPortClientURI = "inportFCMclient";
	
	/** URI of the pair inbound port (simplifies the connection).		*/
	protected static final String	NodeCInPortPairURI = "inportNodeCpair";
	protected static final String	CMInPortPairURI = "inportCMpair";
	
	protected final int NB_PEER = 10;  
	protected final int NB_FACADE = 2 ;
	
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

		
		
		// create the component facade
		for(int i = 0 ; i < NB_FACADE ; i++ ) {
			AbstractComponent.createComponent(
					Facade.class.getCanonicalName(),
					new Object[]{i,
							NMInboundPortURI,
							FacadeCMInPortFacadeURI,
							NB_FACADE,// for the interconnection of facades
							FacadeCMInPortClientURI // for call accept in client
					});
		}				
				System.out.println("\nCreate Composant Facade OK ");
				
		// create the component pairs
		// for connect pair with facade , should pass the inportNM of facade to pair
		for (int i = 0; i < NB_PEER  ; i++) {
			AbstractComponent.createComponent(
							Pair.class.getCanonicalName(),
							new Object[]{i,
									NodeCInPortPairURI,
									CMInPortPairURI,
									NMInboundPortURI+ i%NB_FACADE // choose a facade
									});
		}
		System.out.println("\nCreate Composant pairs OK ");

		
		// create the component client
		AbstractComponent.createComponent(
				Client.class.getCanonicalName(),
				new Object[]{FacadeCMInPortClientURI,FacadeCMInPortFacadeURI+selectRandomFacadeId()}); // not sure client will connect with only one facade
		System.out.println("\nCreate Composant client OK ");
		

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
			a.startStandardLifeCycle(50000L);

			// Give some time to see the traces (convenience).
			Thread.sleep(10000L);

			// Simplifies the termination (termination has yet to be treated
			// properly in BCM).
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int selectRandomFacadeId() {
	    Random random = new Random();
	    int randomId = random.nextInt(NB_FACADE);
	    return randomId;
	}
}
