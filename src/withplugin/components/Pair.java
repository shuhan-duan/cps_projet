package withplugin.components;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.thaiopensource.validate.Flag;

import classes.ContentDescriptor;
import classes.ContentNodeAdress;
import classes.NodeAdresse;
import connector.FacadeContentManagementConector;
import connector.NodeManagementConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionCI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import fr.sorbonne_u.cps.p2Pcm.dataread.ContentDataManager;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;
import interfaces.ContentDescriptorI;
import interfaces.FacadeContentManagementCI;
import interfaces.MyThreadServiceI;
import interfaces.NodeManagementCI;
import ports.NodeManagementOutboundPort;
import withplugin.plugins.PairPlugin;
import fr.sorbonne_u.utils.aclocks.AcceleratedClock;
import fr.sorbonne_u.utils.aclocks.ClocksServer;
import fr.sorbonne_u.utils.aclocks.ClocksServerCI;
import fr.sorbonne_u.utils.aclocks.ClocksServerConnector;
import withplugin.CVM;


@RequiredInterfaces(required={ClocksServerCI.class ,NodeManagementCI.class}) 
public class Pair extends AbstractComponent implements MyThreadServiceI{
	// -------------------------------------------------------------------------
	// Component variables and constants
	// -------------------------------------------------------------------------

	/** URI of the pair inbound port (simplifies the connection).		*/
	protected static final String	NodeCInPortPairURI = "inportNodeCpair";
	protected static final String	CMInPortPairURI = "inportCMpair";
	
	protected ClocksServerOutboundPort csop;
	private PairPlugin plugin;
	
	/**	the outbound port used to call the service.							*/
	private NodeManagementOutboundPort	outPortNM ; // for calling join and leave , connected with facade

	private ContentNodeAdress adress;
	private ArrayList<ContentDescriptorI> contents; 
	
	private int id;
	protected String facadeNMURI; // for connect a facade in NM
	
	private static final int NB_OF_THREADS = 10;

	private static final String NM_THREAD_SERVICE_URI = "pair_nm_pool";
	private static final String CM_THREAD_SERVICE_URI = "pair_cm_pool";
	
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	protected Pair(String PAIR_URI ,String FACADE_URI ,String path)throws Exception {
		super(PAIR_URI , NB_OF_THREADS, 10);
		String[] parts = PAIR_URI.split("Pair");
		this.id = Integer.parseInt(parts[1]);
		
		this.outPortNM =new NodeManagementOutboundPort(this);
		outPortNM.publishPort() ;
		this.facadeNMURI = FACADE_URI;
		
		this.adress = new ContentNodeAdress(PAIR_URI, CMInPortPairURI+id, NodeCInPortPairURI+id);
		this.contents = new ArrayList<ContentDescriptorI>();
		
		//add descriptors to ArrayList contents
		addDescriptor(id ,path);
		
		
		//Create Clock
		this.csop = new ClocksServerOutboundPort(this);
		this.csop.publishPort();

		int nbThreadsNM = 2;
		int nbThreadsCM = NB_OF_THREADS - nbThreadsNM;
		
		this.createNewExecutorService(NM_THREAD_SERVICE_URI+id, nbThreadsNM, true);
		this.createNewExecutorService(CM_THREAD_SERVICE_URI+id, nbThreadsCM, false);
		
		//pass the outPortNM in plugin to call acceptprobed
		plugin = new PairPlugin(adress ,contents);
		plugin.setPluginURI("pair-pluginUri"+id);
		this.installPlugin(plugin);
	}

	//-------------------------------------------------------------------------
	// Component life-cycle
	//-------------------------------------------------------------------------
	
	@Override
	public void start() throws ComponentStartException {
		super.start();
		try {
			//System.out.println("in pair "+ id +",  : " +);
			doPortConnection( outPortNM.getPortURI(), facadeNMURI , NodeManagementConnector.class.getCanonicalName());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void			execute() throws Exception
	{ 
		
		this.doPortConnection(this.csop.getPortURI(),ClocksServer.STANDARD_INBOUNDPORT_URI,ClocksServerConnector.class.getCanonicalName());
		AcceleratedClock clock = this.csop.getClock(CVM.CLOCK_URI);
		Instant startInstant = clock.getStartInstant();
		clock.waitUntilStart();
		
		long delayInNanos1 =clock.nanoDelayUntilAcceleratedInstant(startInstant.plusSeconds(10));
		actionJoin(delayInNanos1);
		
		
		long delayInNanos2 =clock.nanoDelayUntilAcceleratedInstant(startInstant.plusSeconds(300));
		actionLeave(delayInNanos2);
		
		long delayInNanos3 =clock.nanoDelayUntilAcceleratedInstant(startInstant.plusSeconds(350));
		actionDisconnect(delayInNanos3);
				
			
	}	

	@Override
	public void	finalise() throws Exception
	{
		this.doPortDisconnection(this.csop.getPortURI());

		super.finalise();
	}
	
	@Override
	public synchronized void	shutdown() throws ComponentShutdownException
	{
		try {
			this.csop.unpublishPort();
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}

	// -------------------------------------------------------------------------
	// Services implementation
	// -------------------------------------------------------------------------
	
	private void addDescriptor(int number ,String path)
			throws Exception{
		ContentDataManager.DATA_DIR_NAME = path ;
		//ContentDataManager.DATA_DIR_NAME = "testsDataCPSAvril"; // in folder /deployment
		ArrayList<HashMap<String, Object>> result = ContentDataManager.readDescriptors(number);
		for (HashMap<String, Object> hashMap : result) {
			ContentDescriptorI descriptor = new ContentDescriptor(hashMap);
			contents.add(descriptor);
		}  
		System.out.println("pair"+id+"[label=\"Pair "+ id +"\"];");
	}
	
	private void	actionConnectFacade(long delayInNanos) throws Exception
	{
		this.scheduleTask(
				o -> {
					try {
						doConnectFacade();
					} catch (Exception e) {
						e.printStackTrace();
					}
				},
				delayInNanos,
				TimeUnit.NANOSECONDS);
		
	}
	
	private void  actionJoin(long delayInNanos) throws Exception
	{
		this.scheduleTask(
				o -> {
					try {
						this.outPortNM.join(this.adress);
					} catch (Exception e) {
						e.printStackTrace();
					}
				},
				delayInNanos,
				TimeUnit.NANOSECONDS);
		
	}
	
	private void  actionLeave(long delayInNanos) throws Exception {
		this.scheduleTask(
				o -> {
					try {
						this.outPortNM.leave(this.adress);
					} catch (Exception e) {
						e.printStackTrace();
					}
				},
				delayInNanos,
				TimeUnit.NANOSECONDS);
		
	}
	
	private void actionDisconnect(long delayInNanos) {
		this.scheduleTask(
				o -> {
					try {
						this.plugin.doDisconnect();
					} catch (Exception e) {
						e.printStackTrace();
					}
				},
				delayInNanos,
				TimeUnit.NANOSECONDS);
		
	}
	
	private void doConnectFacade() throws Exception {
		//connect with facade in NM
        this.addRequiredInterface(ReflectionCI.class);
        ReflectionOutboundPort rop = new ReflectionOutboundPort(this);
		rop.publishPort();
		
		this.doPortConnection(rop.getPortURI(), facadeNMURI, ReflectionConnector.class.getCanonicalName());
		
		String[] otherInboundPortUI = rop.findInboundPortURIsFromInterface(NodeManagementCI.class);
		if (otherInboundPortUI.length == 0 || otherInboundPortUI == null)
			System.out.println("cannot connet facade in NM");
		else {
			this.doPortConnection(outPortNM.getPortURI(), otherInboundPortUI[0],
					NodeManagementConnector.class.getCanonicalName());
		}
		
		this.doPortDisconnection(rop.getPortURI());
		rop.unpublishPort();
		rop.destroyPort();
		this.removeRequiredInterface(ReflectionCI.class);
		
		//System.out.println("pair"+id+" ->"++"[color=red];");
		
	}
	
	@Override
	public String get_THREAD_POOL_URI() {
		return NM_THREAD_SERVICE_URI+id;
	}
	@Override
	public String get_CM_THREAD_POOL_URI() {
		return CM_THREAD_SERVICE_URI+id;
	}
	
}
