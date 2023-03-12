package componenet;



import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.Soundbank;

import classes.ContentDescriptor;
import classes.ContentNodeAdress;
import connector.ContentManagementConector;
import connector.NodeManagementConnector;
import connector.NodeConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentNodeAddressI;
import interfaces.ContentTemplateI;
import interfaces.MyCMI;
import interfaces.NodeManagementCI;
import ports.*;
import tests.CVM;
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
@RequiredInterfaces(required={ClocksServerCI.class}) 
public class Pair  extends AbstractComponent implements MyCMI {
	protected ClocksServerOutboundPort csop; 
	public static int cpt = 0;

	/**	the outbound port used to call the service.							*/
	protected NodeManagementOutboundPort	NMportOut ;
	protected String NMPortIn_facade;
	protected NodeCIntboundPort	NodePortIn ;
	protected ContentManagementCIIntbound CMportIn;
	/**	counting service invocations.										*/
	protected static int counter ;
	protected ContentNodeAdress adress;
	Set<ContentNodeAddressI> liste;


	//stock the pairs connected with this pair and the outportNodeC of me
	private ConcurrentHashMap<ContentNodeAddressI,NodeCOutboundPort> outPortsNodeC;
	//stock the neighber pairs connected with this pair and the outportCMpair of me
	private ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound> outPortsCM;
	private ArrayList<ContentDescriptorI> contents;


	/**
	 * @param NMoutportUri	URI of the outbound port NM.
	 * @throws Exception		<i>todo.</i>
	 */
	protected Pair( String NMoutportUri ,String NMPortIn_facade, int DescriptorID)throws Exception {
		super(NMoutportUri, 10, 1);
		
		cpt++;
		this.adress = new ContentNodeAdress("Pair" + cpt,"CMuriIn"+ cpt, "NodeCuriIn"+ cpt);
		this.NMportOut =new NodeManagementOutboundPort(NMoutportUri, this) ;
		NMportOut.publishPort() ;
		this.NodePortIn = new NodeCIntboundPort(adress.getNodeUri() ,this);
		NodePortIn.publishPort();
		this.CMportIn = new ContentManagementCIIntbound(adress.getContentManagementURI(), this);
		CMportIn.publishPort();
		this.counter = 0 ;
		this.NMPortIn_facade = NMPortIn_facade;
		this.outPortsNodeC = new ConcurrentHashMap<ContentNodeAddressI,NodeCOutboundPort>();
		this.outPortsCM = new ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound>();
		this.contents = new ArrayList<ContentDescriptorI>();
		//add descriptor
		addDescriptor(DescriptorID);
		//Create Clock
		this.csop = new ClocksServerOutboundPort(this);
		this.csop.publishPort();
		
	}

	/**
	 * @Function: Pair.java
	 * @Description:
	 *
	 * @param:
	 * @return：
	 * @throws：
	 *
	 * @version: v1.0.0
	 * @author: lyna & shuhan
	 * @throws Exception
	 * @date: 6 févr. 2023 17:44:32
	 *
	 *
	 */
	public ContentNodeAddressI connecte (ContentNodeAddressI p ) throws Exception
	{  
		if (p == null) {
			throw new Exception("Connection failed , pair is null");
		} else {
			//do connect entre pair et pair en NodeCI
			//System.out.println("\nIn connect: me "+ adress.getNodeUri()+ " neiber: "+ p.getNodeUri());
			counter++;
			String outportN = "myOutPortNodeCIpair"+ counter;
			NodeCOutboundPort NportOut = new NodeCOutboundPort(outportN, this);
			NportOut.publishPort();
			outPortsNodeC.put(p, NportOut);
			doPortConnection(outPortsNodeC.get(p).getPortURI(), p.getNodeUri(), NodeConnector.class.getCanonicalName());
			//System.out.println("\nreverse:c'est ok " + p.getNodeidentifier() +" connecte avec "+ this.adress.getNodeidentifier() +" en "+ outPortsNodeC.get(p).getPortURI()+" en NodeCI" );

			//do connect entre pair et pair en ContentManagementCI
			String outportCM = "myOutportCMpair" + ++cpt ;
			ContentManagementCIOutbound CMportOut = new ContentManagementCIOutbound(outportCM,this );
			CMportOut.publishPort();
			outPortsCM.put(p, CMportOut);
			doPortConnection(outPortsCM.get(p).getPortURI(), p.getContentManagementURI(), ContentManagementConector.class.getCanonicalName());
			//System.out.println("\nreverse:c'est ok " + p.getNodeidentifier() +" connecte avec "+ this.adress.getNodeidentifier()+" en "+ outPortsCM.get(p).getPortURI() +" en ContentManagementCI" );
		}
		return p;
	}

	/**
	 * @Function: Pair.java
	 * @Description:
	 *
	 * @param:
	 * @return：
	 * @throws：
	 *
	 * @version: v1.0.0
	 * @author: lyna & shuhan
	 * @date: 6 févr. 2023 17:45:00
	 *
	 *
	 */
	public void disconnectePair (ContentNodeAddressI p ) throws Exception
	{   	//get the outportNodeCI of this , which is connected with p
		    NodeCOutboundPort voisin= outPortsNodeC.get(p);
			voisin.unpublishPort();
			this.doPortDisconnection(voisin.getPortURI());
			outPortsNodeC.remove(p);
			System.out.println("\nc'est ok "+ p.getNodeidentifier() +" disconnect  avec " + this.adress.getNodeidentifier()+" en NodeCI");
			//get the outportContentManagementCI of this , which is connected with p
			ContentManagementCIOutbound voisin2= outPortsCM.get(p);
			voisin2.unpublishPort();
			this.doPortDisconnection(voisin2.getPortURI());
			outPortsCM.remove(p);
			System.out.println("\nc'est ok "+ p.getNodeidentifier() +" disconnect  avec " + this.adress.getNodeidentifier()+" en ContentManagementCI");
			
	}
	
	@Override
	public ContentDescriptorI find(ContentTemplateI ct  ,int hops )throws Exception{
		System.out.println("\nc'est find in pair "+this.adress.getNodeidentifier() + " " +  hops);
		if (hops == 0) {
			System.out.println("c'est find in pair qui termine");
			return null;
		}
		// Cherche parmi ses propres contenus
		for (ContentDescriptorI content : this.contents) {
			if (content.match(ct)) {
				return content;
			}
		}
		System.out.println(this.adress.getNodeidentifier() + " n'a pas touve");
		// Si le contenu n'est pas dans le nœud courant, demande à un autre pair
		Set<ContentNodeAddressI> neighbors = outPortsCM.keySet();
		if (neighbors == null) {
			System.out.println("\npas de neighbor : "+ this.adress.getNodeidentifier());
			return null;
		}else {
			ContentNodeAddressI[] array = neighbors .toArray(new ContentNodeAddressI[0]);
			Random rand = new Random();
			int randomIndex = rand.nextInt(neighbors.size());
			ContentNodeAddressI neighbor = array[randomIndex];
			ContentManagementCIOutbound outportCM = outPortsCM.get(neighbor);
			System.out.println("\nwill do find in :" + neighbor.getNodeidentifier()+" "+ outportCM.getPortURI());
			//System.out.println(outportCM.getPortURI()+ " is connected? "+outportCM.connected());
			ContentDescriptorI content = ((ContentManagementCI)outportCM).find(ct, hops - 1);
			if (content != null) {
				return content;
			}	
			/*
			for ( ContentNodeAddressI neighbor: neighbors) {
				ContentManagementCIOutbound outportCM = outPortsCM.get(neighbor);
				System.out.println("\nwill do find in :" + neighbor.getNodeidentifier()+" "+ outportCM.getPortURI());
				System.out.println(outportCM.getPortURI()+ " is connected? "+outportCM.connected());
				//mais ici il entre pas dans find 
				ContentDescriptorI content = ((ContentManagementCI)outportCM).find(ct, hops - 1);
				if (content != null) {
					return content;
				}			
			}
			*/
		}
		return null;

	}
	
	@Override
	public Set<ContentDescriptorI> match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops)
			throws Exception {
		System.out.println("\nc'est  match in pair " + this.adress.getNodeidentifier()+ " " +  hops);

		if (hops == 0) {
			return matched;
		}
		// Cherche parmi ses propres contenus
		for (ContentDescriptorI content : this.contents) {
			if (content.match(cd)) {
				matched.add(content);
			}
		}
		
		// Si le contenu n'est pas dans le nœud courant, demande à un autre pair
		Set<ContentNodeAddressI> neighbors = outPortsCM.keySet();
		if (neighbors == null) {
			return matched;
		}else {
			ContentNodeAddressI[] array = neighbors .toArray(new ContentNodeAddressI[0]);
			Random rand = new Random();
			int randomIndex = rand.nextInt(neighbors.size());
			ContentNodeAddressI neighbor = array[randomIndex];
			ContentManagementCIOutbound outportCM = outPortsCM.get(neighbor);
			System.out.println("\nwill do match in :" + neighbor.getNodeidentifier()+" "+ outportCM.getPortURI());
			//System.out.println(outportCM.getPortURI()+ " is connected? "+outportCM.connected());
			Set<ContentDescriptorI> reSet =((ContentManagementCI)outportCM).match(cd, matched ,hops -1	);
			matched.addAll(reSet);
			/*
			for ( ContentNodeAddressI neighbor: neighbors) {
				ContentManagementCIOutbound outportCM = outPortsCM.get(neighbor);
				System.out.println("\nwill do match in :" + neighbor.getNodeidentifier()+" "+ outportCM.getPortURI());
				Set<ContentDescriptorI> reSet =((ContentManagementCI)outportCM).match(cd, matched ,hops -1	);
				matched.addAll(reSet);
				return matched;
			}
			*/
		}
		return matched;
	}
	
	
	public void addDescriptor(int number)
			throws Exception{
		ContentDataManager.DATA_DIR_NAME = "src/data";
		ArrayList<HashMap<String, Object>> result = ContentDataManager.readDescriptors(number);
		for (HashMap<String, Object> hashMap : result) {
			ContentDescriptorI descriptor = new ContentDescriptor(hashMap);
			contents.add(descriptor);
		}  
		
		for (ContentDescriptorI c : this.contents) {
			System.out.println("\nI am "+ this.adress.getNodeidentifier()+"\n"+c.toString());
		}
	}

	//-------------------------------------------------------------------------
	// Component life-cycle
	//-------------------------------------------------------------------------
	@Override
	public void start() throws ComponentStartException {
		super.start();
		try {
			this.doPortConnection(NMportOut.getPortURI(), NMPortIn_facade,NodeManagementConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
	}

	@Override
	public void			execute() throws Exception
	{ 
		this.doPortConnection(
				this.csop.getPortURI(),
				ClocksServer.STANDARD_INBOUNDPORT_URI,
				ClocksServerConnector.class.getCanonicalName());
		AcceleratedClock clock = this.csop.getClock(CVM.CLOCK_URI);
		Instant startInstant = clock.getStartInstant();
		clock.waitUntilStart();
		long delayInNanos =clock.nanoDelayUntilAcceleratedInstant(startInstant.plusSeconds(150));
			//do join
				this.scheduleTask(
						o -> {
							try {
								((Pair)o).action();
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
		this.doPortDisconnection(NMportOut.getPortURI());
		//Disconecte
		for (ContentNodeAddressI p: liste ) {
					outPortsNodeC.get(p).disconnecte(this.adress);
				}
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
	
	public void		action() throws Exception
	{
		liste = this.NMportOut.join(this.adress);
		if(liste.size() == 0) {
			System.out.println("\n"+adress.getNodeidentifier() +" says : I don't have neigber yet!");
		}else{
			//do connection entre pair et pair en NodeCI
			for (ContentNodeAddressI p: liste ) {
				//System.out.println("\nI am "+ adress.getNodeidentifier()+", I will connect with my neighber : "+p.getNodeidentifier());

				cpt++;
				String outportN = "myOutPortNodeCIpair"+ cpt;
				NodeCOutboundPort NportOut = new NodeCOutboundPort(outportN, this);
				NportOut.publishPort();
				outPortsNodeC.put(p, NportOut);
				doPortConnection(NportOut.getPortURI(),	p.getNodeUri(),NodeConnector.class.getCanonicalName());
				//System.out.println("\nc'est ok " + p.getNodeidentifier() +" connecte avec "+ this.adress.getNodeidentifier() +" en "+ outPortsNodeC.get(p).getPortURI()+" en NodeCI" );
				
				String outportCM = "myOutportCMpair" + cpt ;
				ContentManagementCIOutbound CMportOut = new ContentManagementCIOutbound(outportCM,this );
				CMportOut.publishPort();
				outPortsCM.put(p, CMportOut);
				doPortConnection(outportCM,	p.getContentManagementURI(),ContentManagementConector.class.getCanonicalName());
				//System.out.println("\nc'est ok "+ p.getNodeidentifier() +" connect  avec " +this.adress.getNodeidentifier() +" en "+ CMportOut.getPortURI() +" en ContentManagement");
				NportOut.connecte(this.adress);		
			}  
	}
		
	}

}
	

