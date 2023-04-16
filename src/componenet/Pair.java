package componenet;



import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.Soundbank;

import CVM.CVM;
import Plugin.Pair_plugin;
import classes.ContentDescriptor;
import classes.ContentNodeAdress;
import connector.ContentManagementConector;
import connector.FacadeContentManagementConector;
import connector.NodeManagementConnector;
import connector.NodeConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentNodeAddressI;
import interfaces.ContentTemplateI;
import interfaces.FacadeContentManagementCI;
import interfaces.MyCMI;
import interfaces.NodeAdresseI;
import interfaces.NodeCI;
import interfaces.NodeManagementCI;
import ports.*;
import fr.sorbonne_u.cps.p2Pcm.dataread.ContentDataManager;
import fr.sorbonne_u.utils.aclocks.AcceleratedClock;
import fr.sorbonne_u.utils.aclocks.ClocksServer;
import fr.sorbonne_u.utils.aclocks.ClocksServerCI;
import fr.sorbonne_u.utils.aclocks.ClocksServerConnector;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;
/**
 * @author lyna & shuhan 
 *
 */
@RequiredInterfaces(required = { NodeManagementCI.class, NodeCI.class ,ClocksServerCI.class })
@OfferedInterfaces(offered = { NodeCI.class })
public class Pair  extends AbstractComponent  {
	protected ClocksServerOutboundPort csop; 
	public static int cpt = 0;

	/**	the outbound port used to call the service.							*/
	protected NodeManagementOutboundPort	NMportOut ;
	protected String NMPortIn_facade;
	protected NodeCIntboundPort	NodePortIn ;
	protected ContentManagementCIIntbound CMportIn;
	/**	counting service invocations.										*/
	protected int counter ;
	protected ContentNodeAdress adress;
	private ArrayList<ContentDescriptorI> contents;
	protected Set<ContentNodeAddressI> voisins; //neighbours 


	//stock the pairs connected with this pair and the outportNodeC of me
	//adresse conecte avec le pair courant 
	private ConcurrentHashMap<ContentNodeAddressI,NodeCOutboundPort> outPortsNodeC;  //stock les voiins 
	//stock the neighber pairs connected with this pair and the outportCMpair of me
	//cntentMangement 
	private ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound> outPortsCM;
	
	Pair_plugin plugin;


	
	protected Pair(String reflectionInboundPortURI, String NMoutportUri ,String NMPortIn_facade, int DescriptorID)throws Exception {
		super(reflectionInboundPortURI, 1, 1);
		 this.adress = new ContentNodeAdress("Pair" + cpt,"CMuriIn"+ cpt, "NodeCuriIn"+ cpt);

		plugin = new Pair_plugin(NMoutportUri, NMPortIn_facade, DescriptorID ,adress);
		this.installPlugin(plugin);
		this.csop = new ClocksServerOutboundPort(this);
		this.csop.publishPort();
		

	}

	//-------------------------------------------------------------------------
	// Component life-cycle
	//-------------------------------------------------------------------------
	@Override
	public void start() throws ComponentStartException {
		super.start();
		
	}

	@Override
	public void			execute() throws Exception
	{ 	
		this.doPortConnection(this.csop.getPortURI(),ClocksServer.STANDARD_INBOUNDPORT_URI,ClocksServerConnector.class.getCanonicalName());
		AcceleratedClock clock = this.csop.getClock(CVM.CLOCK_URI);
		Instant startInstant = clock.getStartInstant();
		clock.waitUntilStart();
		//long delayInNanos =clock.nanoDelayUntilAcceleratedInstant(startInstant.plusSeconds(10+counter*10));
		long delayInNanos =clock.nanoDelayUntilAcceleratedInstant(startInstant.plusSeconds(10));

		//do join et connect 
				this.scheduleTask(
						o -> {
							try {
								((Pair)o).action1();
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
		//this.doPortDisconnection(this.csop.getPortURI());

	//	this.doPortDisconnection(NMportOut.getPortURI());
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

	public void		action1() throws Exception
	{
		plugin.NMportOut.join(this.adress);
		
	}
	
	public void action2() throws Exception 
	{
		for (ContentNodeAddressI p: voisins ) {
			NodeCOutboundPort NportOut = outPortsNodeC.get(p);
			NportOut.disconnecte(this.adress);
		}
		
	}

	//leave  
	public void action3() throws Exception 
	{
		this.NMportOut.leave(adress);
		
	}
	



	
	


	
	

	

	


}
	

