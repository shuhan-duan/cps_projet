package withplugin;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.utils.aclocks.ClocksServer;
import withplugin.components.Client;
import withplugin.components.Facade;
import withplugin.components.Pair;

public class DistributedCVM extends AbstractDistributedCVM {
	protected static final String PAIR_URI = "Pair";
	protected static final String FACADE_URI = "Facade";
	protected static final String FCMInPortClientURI = "inPortFCMClient";

	protected final int NB_PEER = 10;  
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 * @author: lyna & shuhan
	 * @date: 14 Mai . 2023 21:31:56 
	 */
	public DistributedCVM(String[] args) throws Exception {
		super(args);
	}
	
	/**
	 * 
	 * @throws Exception
	 *  @author: lyna & shuhan
	 * @date: 14 Mai 2023 21:31:56 
	 */
	@Override
	public void instantiateAndPublish() throws Exception {
		String jvmURI = AbstractCVM.getThisJVMURI();
		String[] parts = jvmURI.split("Facade");
		String uri = parts[0];
		
		if (uri.equals(FACADE_URI)) {
			// create the component facade
			AbstractComponent.createComponent(
					Facade.class.getCanonicalName(),
					new Object[]{jvmURI,
							FCMInPortClientURI}); // pass the FCMInPortClientURI to facade
			
			// create the component pairs
			for (int i = 0; i < NB_PEER  ; i++) {
				AbstractComponent.createComponent(
								Pair.class.getCanonicalName(),
								new Object[]{PAIR_URI+i,
										jvmURI
										});
			}			
		}else {
			System.out.println("Unknown JVM URI: "+
									AbstractCVM.getThisJVMURI());
		}
		
		// create the only component client ADN the only ClocksServer
		// client connects to facade1 by default
		if (jvmURI.equals("Facade1")) {
			AbstractComponent.createComponent(
					Client.class.getCanonicalName(),
					new Object[]{"client",
							jvmURI}); 
			long unixEpochStartTimeInNanos =	TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis())	+ CVM.DELAY_TO_START_IN_NANOS;
			Instant	startInstant = Instant.parse("2023-02-07T08:00:00Z");
			double accelerationFactor = 60.0;
			AbstractComponent.createComponent(ClocksServer.class.getCanonicalName(),
					new Object[]{CVM.CLOCK_URI, unixEpochStartTimeInNanos,
								 startInstant, accelerationFactor});
		}
		
		super.instantiateAndPublish();
	}
	
	// the interconnection is done in component facade
	/**
	 * 
	 * @throws Exception
	 *  @author: lyna & shuhan
	 * @date: 14 Mai 2023 21:31:56 
	 */
	public static void main(String[] args) {		
		try {
			DistributedCVM dCvm = new DistributedCVM(args);
			
			dCvm.startStandardLifeCycle(50000L);
			
			Thread.sleep(10000L);
			
			System.exit(0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
