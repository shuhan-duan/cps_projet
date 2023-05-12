package withplugin.components;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import classes.ContentDescriptor;
import classes.ContentNodeAdress;
import classes.NodeAdresse;
import connector.NodeManagementConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
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
	private NodeManagementOutboundPort	outPortNM ; // for calling join and leave , connected with facade
	
	//the inbound port NMPort of facade
	private String inPortNMfacadeURI ;
	
	private ContentNodeAdress adress;
	private ArrayList<ContentDescriptorI> contents; 
	
	private int id;
	
	
	
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	protected Pair(int DescriptorID, String NodeCInPortPair ,String CMInPortPair,String inPortNMfacadeURI )throws Exception {
		super("pair"+DescriptorID , 10, 1);
		
		this.id = DescriptorID ;
		
		this.inPortNMfacadeURI = inPortNMfacadeURI;
		
		this.outPortNM =new NodeManagementOutboundPort(this);
		outPortNM.publishPort() ;
		
		this.adress = new ContentNodeAdress("pair"+id, CMInPortPair+id, NodeCInPortPair+id);
		this.contents = new ArrayList<ContentDescriptorI>();
		
		//add descriptors to ArrayList contents
		addDescriptor(id);
		
		//pass the outPortNM to call acceptprobed
		plugin = new PairPlugin(adress ,contents);
		plugin.setPluginURI("pair-pluginUri"+id);
		
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
			//System.out.println("in pair "+ id +", inPortNMfacadeURI : " +inPortNMfacadeURI);
			doPortConnection( outPortNM.getPortURI(),inPortNMfacadeURI, NodeManagementConnector.class.getCanonicalName());
			//System.out.println("pair"+id+" ->"+inPortNMfacadeURI+"[color=red];");
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
		long delayInNanos =clock.nanoDelayUntilAcceleratedInstant(startInstant.plusSeconds(10));
		
		
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
	
	private void addDescriptor(int number)
			throws Exception{
		//ContentDataManager.DATA_DIR_NAME = "src/data";
		ContentDataManager.DATA_DIR_NAME = "src/testsDataCPSAvril";
		ArrayList<HashMap<String, Object>> result = ContentDataManager.readDescriptors(number);
		for (HashMap<String, Object> hashMap : result) {
			ContentDescriptorI descriptor = new ContentDescriptor(hashMap);
			contents.add(descriptor);
		}  
		System.out.println("pair"+id+"[label=\"Pair "+ id +"\"];");
	}
	
	private void	 actionJoin() throws Exception
	{
		
		this.outPortNM.join(this.adress);
	}
	
	private void  actionLeave() throws Exception {
		this.outPortNM.leave(this.adress);
	}
	
	
}
