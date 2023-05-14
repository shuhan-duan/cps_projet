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
	protected static final String 	PAIR_URI = "Pair";
	protected static final String 	FACADE_URI = "Facade";
	
	protected static final String 	FCMInPortClientURI = "inPortFCMClient";
	protected static final String	NMInboundFacadePortURI = "inportNMfacade";
	protected static final String	FacadeCMInPortFacadeURI = "inportFCMfacade";	
	
	protected static final long DELAY_TO_START_IN_NANOS = TimeUnit.SECONDS.toNanos(1);

	protected final int NB_PEER = 10;  
	protected final int NB_Facade = 5;  
	
	public DistributedCVM(String[] args) throws Exception {
		super(args);
	}
	
	@Override
	public void instantiateAndPublish() throws Exception {
		String jvmURI = AbstractCVM.getThisJVMURI();
		String[] parts = jvmURI.split("(?<=\\D)(?=\\d)");
		String uri = parts[0];
		int idFacade = Integer.parseInt(parts[1]);
		System.out.println("jvmURI : "+ jvmURI );
		if (uri.equals(FACADE_URI)) {
			// create the component facade
			AbstractComponent.createComponent(
					Facade.class.getCanonicalName(),
					new Object[]{jvmURI,
							FCMInPortClientURI,// pass the FCMInPortClientURI to facade
							NB_Facade}); 
			
			// create the component pairs
			for (int i = 0; i < NB_PEER  ; i++) {
				int idPair = i+10*idFacade;
				AbstractComponent.createComponent(
								Pair.class.getCanonicalName(),
								new Object[]{PAIR_URI+idPair,
										// choose a facade and pass the NMInboundFacadePortURI to pair
										NMInboundFacadePortURI+ idFacade ,
										"testsDataCPSAvril"});
			}			
		}else {
			System.out.println("Unknown JVM URI: "+
									AbstractCVM.getThisJVMURI()+ "  uri  :" +uri);
		}
		
		// create the only component client ADN the only ClocksServer
		// client connects to facade0 by default
		if (jvmURI.equals("Facade0")) {
			AbstractComponent.createComponent(
					Client.class.getCanonicalName(),
					new Object[]{"client",
							FacadeCMInPortFacadeURI+ idFacade,
							"testsDataCPSAvril"}); 
			
			long unixEpochStartTimeInNanos =	TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis())	+ DELAY_TO_START_IN_NANOS;
			Instant	startInstant = Instant.parse("2023-02-07T08:00:00Z");
			double accelerationFactor = 60.0;
			
			AbstractComponent.createComponent(ClocksServer.class.getCanonicalName(),
					new Object[]{CVM.CLOCK_URI, unixEpochStartTimeInNanos,
								 startInstant, accelerationFactor});
		}
		
		super.instantiateAndPublish();
	}
	
	// the interconnection is done in component facade
	
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
