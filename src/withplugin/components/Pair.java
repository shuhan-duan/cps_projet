package withplugin.components;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import classes.ContentDescriptor;
import connector.NodeManagementConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.p2Pcm.dataread.ContentDataManager;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;
import interfaces.ContentDescriptorI;
import interfaces.NodeManagementCI;
import ports.NodeManagementOutboundPort;
import withplugin.plugins.PairPlugin;
import fr.sorbonne_u.utils.aclocks.AcceleratedClock;
import fr.sorbonne_u.utils.aclocks.ClocksServer;
import fr.sorbonne_u.utils.aclocks.ClocksServerCI;
import fr.sorbonne_u.utils.aclocks.ClocksServerConnector;
import withplugin.CVM;


@RequiredInterfaces(required={ClocksServerCI.class}) 
public class Pair extends AbstractComponent {
	// -------------------------------------------------------------------------
	// Component variables and constants
	// -------------------------------------------------------------------------
	
	protected ClocksServerOutboundPort csop;
	private PairPlugin plugin;
	
	/**	the outbound port used to call the service.							*/
	public NodeManagementOutboundPort	NMportOut ;
	
	protected String NMPortIn_facade;
	
	private int counter;
	
	
	
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	
	protected Pair( String NMoutportUri ,String NMPortIn_facade, int DescriptorID)throws Exception {
		super(NMoutportUri, 10, 1);
		
		this.counter = DescriptorID ;
		this.NMPortIn_facade = NMPortIn_facade;
		
		this.NMportOut =new NodeManagementOutboundPort(NMoutportUri, this) ;
		NMportOut.publishPort() ;
		
		plugin = new PairPlugin(NMoutportUri, DescriptorID ,NMPortIn_facade);
		plugin.setPluginURI("pair-pluginUri"+counter);
		
		this.installPlugin(plugin);
		//Create Clock
		this.csop = new ClocksServerOutboundPort(this);
		this.csop.publishPort();

	}

	//-------------------------------------------------------------------------
	// Component life-cycle
	//-------------------------------------------------------------------------
	
	@Override
	public void start() throws ComponentStartException {
		super.start();
		try {
			doPortConnection( NMportOut.getPortURI(),NMPortIn_facade, NodeManagementConnector.class.getCanonicalName());
			//System.out.println(NMportOut.connected());
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
		long delayInNanos =clock.nanoDelayUntilAcceleratedInstant(startInstant.plusSeconds(10+counter*10));
		//long delayInNanos =clock.nanoDelayUntilAcceleratedInstant(startInstant.plusSeconds(10));
		
		//do join et connect 
				this.scheduleTask(
						o -> {
							try {
								((Pair)o).actionJoin();
							} catch (Exception e) {
								e.printStackTrace();
							}
						},
						delayInNanos,
						TimeUnit.NANOSECONDS);
				
	
			
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
	
	public void	 actionJoin() throws Exception
	{
		
		this.NMportOut.join(plugin.adress);
	}
	
	public void  actionLeave() throws Exception {
		
	}
	
	
}
