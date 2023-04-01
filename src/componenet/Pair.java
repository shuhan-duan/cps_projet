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
import classes.ContentDescriptor;
import classes.ContentNodeAdress;
import connector.ContentManagementConector;
import connector.FacadeContentManagementConector;
import connector.NodeManagementConnector;
import connector.NodeConnector;
import fr.sorbonne_u.components.AbstractComponent;
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
@RequiredInterfaces(required={ClocksServerCI.class}) 
public class Pair  extends AbstractComponent implements MyCMI {
	protected ClocksServerOutboundPort csop; 
	public static int cpt = 0;
	public final int NB_PROB = 4;

	/**	the outbound port used to call the service.							*/
	protected NodeManagementOutboundPort	NMportOut ;
	protected String NMPortIn_facade;
	protected NodeCIntboundPort	NodePortIn ;
	protected ContentManagementCIIntbound CMportIn;
	/**	counting service invocations.										*/
	protected static int counter ;
	protected ContentNodeAdress adress;
	private ArrayList<ContentDescriptorI> contents;
	protected Set<ContentNodeAddressI> voisinsAddressI; //neighbours 


	//stock the pairs connected with this pair and the outportNodeC of me
	//adresse conecte avec le pair courant 
	private ConcurrentHashMap<ContentNodeAddressI,NodeCOutboundPort> outPortsNodeC;  //stock les voiins 
	//stock the neighber pairs connected with this pair and the outportCMpair of me
	//cntentMangement 
	private ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound> outPortsCM;
	
	


	
	protected Pair( String NMoutportUri ,String NMPortIn_facade, int DescriptorID)throws Exception {
		super(NMoutportUri, 10, 1);
		
		cpt++;
		this.adress = new ContentNodeAdress("Pair" + cpt,"CMuriIn"+ cpt, "NodeCuriIn"+ cpt);
		
		this.NMportOut =new NodeManagementOutboundPort(NMoutportUri, this) ;
		NMportOut.publishPort() ;
		this.NodePortIn = new NodeCIntboundPort(this ,adress.getNodeUri());
		NodePortIn.publishPort();
		this.CMportIn = new ContentManagementCIIntbound(this, adress.getContentManagementURI());
		CMportIn.publishPort();
		
		this.counter = DescriptorID ;
		
		
		this.NMPortIn_facade = NMPortIn_facade;
		
		this.outPortsNodeC = new ConcurrentHashMap<ContentNodeAddressI,NodeCOutboundPort>();
		this.outPortsCM = new ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound>();
		this.voisinsAddressI = new HashSet<ContentNodeAddressI>();
		this.contents = new ArrayList<ContentDescriptorI>();
		//add descriptor
		addDescriptor(DescriptorID);
		//Create Clock
		this.csop = new ClocksServerOutboundPort(this);
		this.csop.publishPort();
		
	}

	/*
	public ContentNodeAddressI connecte (ContentNodeAddressI p ) throws Exception
	{  
		//connect pair et pair en NodeCI et CM
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
			System.out.println("\nc'est ok " + p.getNodeidentifier() +" connecte avec "+ this.adress.getNodeidentifier());
		}
		return p;
	}

	
	public void disconnectePair (ContentNodeAddressI p ) throws Exception
	{   	
		
		    
			
		
	}
	
	@Override
	public void find(ContentTemplateI ct, int hops, NodeAdresseI requester, String requestURI)throws Exception{
		//System.out.println("\nc'est find in pair "+this.adress.getNodeidentifier() + " " +  hops);
		if (hops == 0) {
			//System.out.println("c'est find in pair qui termine");
			
		}
		// Cherche parmi ses propres contenus
		for (ContentDescriptorI content : this.contents) {
			if (content.match(ct)) {  //pour march le patron avec le contenue 
				System.out.println("found in " + this.adress.getNodeidentifier());
				
			}
		}
		//sinon il cherche dans les voisin 
		System.out.println(this.adress.getNodeidentifier() + " n'a pas touve");
		// Si le contenu n'est pas dans le nœud courant, demande à un autre pair
		Set<ContentNodeAddressI> neighbors = outPortsCM.keySet();
		if (neighbors == null) {
			System.out.println("\npas de neighbor : "+ this.adress.getNodeidentifier());
			
		}else {
			ContentNodeAddressI[] array = neighbors .toArray(new ContentNodeAddressI[0]);
			Random rand = new Random();
			int randomIndex = rand.nextInt(neighbors.size());
			ContentNodeAddressI neighbor = array[randomIndex];
			ContentManagementCIOutbound outportCM = outPortsCM.get(neighbor);
			//System.out.println("\nwill do find in :" + neighbor.getNodeidentifier()+" "+ outportCM.getPortURI());
			//System.out.println(outportCM.getPortURI()+ " is connected? "+outportCM.connected());
			ContentDescriptorI content = ((ContentManagementCI)outportCM).find(ct, hops - 1);
			if (content != null) {
				
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
			
		}
		

	}
	
	@Override
	public Set<ContentDescriptorI> match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops)
			throws Exception {
		//System.out.println("\nc'est  match in pair " + this.adress.getNodeidentifier()+ " " +  hops);

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
			//System.out.println("\nwill do match in :" + neighbor.getNodeidentifier()+" "+ outportCM.getPortURI());
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
			
		}
		return matched;
	}
	
	*/
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
			//conexion avec la facade 
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
				
				/*
		long delayInNanos2 = clock.nanoDelayUntilAcceleratedInstant(startInstant.plusSeconds(100));
		//System.out.println("pair disconntc -----" + delayInNanos2 );
		// disconnect  
		this.scheduleTask(o ->{
			try {
				((Pair)o).action2();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, delayInNanos2,
				TimeUnit.NANOSECONDS);
		
		long delayInNanos3 = clock.nanoDelayUntilAcceleratedInstant(startInstant.plusSeconds(100));
		//leave
		this.scheduleTask(o ->{
			try {
				((Pair)o).action3();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, delayInNanos3,
				TimeUnit.NANOSECONDS);
				*/
}

	@Override
	public void	finalise() throws Exception
	{
		this.doPortDisconnection(this.csop.getPortURI());

		this.doPortDisconnection(NMportOut.getPortURI());
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
		this.NMportOut.join(this.adress);
			/*
			 */

		
	}
	

	//connect  
	public void action2() throws Exception {
		
}
	/*
	public void action3() throws Exception {
	//Disconecte
		for (ContentNodeAddressI p: liste ) {
			NodeCOutboundPort NportOut = outPortsNodeC.get(p);
			NportOut.disconnecte(this.adress);
		}
		//leave 
		this.NMportOut.leave(adress);
	}
*/
	
	public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) throws Exception {
		System.out.println(requestURI+ " demande do prob in "+ this.adress.getNodeidentifier() + " hops "+ remainghops);
		if (remainghops <= 0) {
			//il retourne sa propre adresse à la façade ayant initié le sondage 
			this.NMportOut.acceptProbed(adress, requestURI);
		}else {
			//à un de ses voisins choisi aléatoirement
			ContentNodeAddressI[] array = voisinsAddressI .toArray(new ContentNodeAddressI[0]);
			//s'il a pas de voisin encore ,il retourne sa propre adress
			if (voisinsAddressI.size() == 0 ) {
				this.NMportOut.acceptProbed(adress, requestURI);
			}else {
				Random rand = new Random();
				int randomIndex = rand.nextInt(voisinsAddressI.size());
				ContentNodeAddressI neighbor = array[randomIndex];
				NodeCOutboundPort nodeCout = outPortsNodeC.get(neighbor);
				nodeCout.probe(facade, remainghops-1, requestURI);	
			}					
		}
		
	}

	public void acceptNeighbours(Set<ContentNodeAddressI> neighbours) throws Exception {
		//this.neighbors.addAll(neighbours);
		System.out.println("\nwhen return liste to : "+this.adress.getNodeidentifier()+"  size:  " + neighbours.size());
		if(neighbours.size() == 0) { // liste vide le 1er pair n'a pas de voisins 
			System.out.println("\n"+adress.getNodeidentifier() +" says : I don't have neigber yet!");
		}else{
			for (ContentNodeAddressI p : neighbours) {
				System.out.println("je suis " + this.adress.getNodeidentifier()+ " j'ai voisin: " + p.getNodeidentifier());
				//do connection entre pair et pair en NodeCI
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
					System.out.println("\nc'est ok "+ p.getNodeidentifier() +" connect  avec " +this.adress.getNodeidentifier() +" en "+ CMportOut.getPortURI() +" en ContentManagement");
					NportOut.connecte(this.adress);		
			}		
		}
		
	}

	public void acceptConnected(ContentNodeAddressI p) {
		//si p est connecte ,ajoute p dans neighbors
		voisinsAddressI.add(p);
		
	}

	@Override
	public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
		requestURI = this.adress.getNodeidentifier();
		if (hops <= 0) {
			System.out.println("c'est find in pair qui termine");
		}
		// Cherche parmi ses propres contenus
		for (ContentDescriptorI content : this.contents) {
			if (content.match(cd)) {  //pour march le patron avec le contenue 
				System.out.println("found in " + this.adress.getNodeidentifier());
				FacadeContentManagementCOutbound outCM = new FacadeContentManagementCOutbound("myPairFCMout", this);
				doPortConnection(outCM.getPortURI(), requester.getContentManagementURI(), FacadeContentManagementConector.class.getCanonicalName());
				outCM.acceptFound(content, requestURI);
			}
		}
 				
		//sinon il cherche dans les voisin 
		System.out.println(this.adress.getNodeidentifier() + " n'a pas touve");
		// Si le contenu n'est pas dans le nœud courant, demande à un autre pair
		if (voisinsAddressI == null) {
				System.out.println("\npas de neighbor : "+ this.adress.getNodeidentifier());
					
		}else {
				ContentNodeAddressI[] array = voisinsAddressI .toArray(new ContentNodeAddressI[0]);
					Random rand = new Random();
					int randomIndex = rand.nextInt(voisinsAddressI.size());
					ContentNodeAddressI neighbor = array[randomIndex];
					ContentManagementCIOutbound outportCM = outPortsCM.get(neighbor);
					outportCM.find(cd, hops-1, requester, requestURI);
					//System.out.println("\nwill do find in :" + neighbor.getNodeidentifier()+" "+ outportCM.getPortURI());
					//System.out.println(outportCM.getPortURI()+ " is connected? "+outportCM.connected());					
		}
	}

	@Override
	public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester,
			String requestURI) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void connecte(ContentNodeAddressI p) throws Exception {
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
		System.out.println("\nc'est ok "+ p.getNodeidentifier() +" connect  avec " +this.adress.getNodeidentifier() +" en "+ CMportOut.getPortURI() +" en ContentManagement");	
		
		NportOut.acceptConnected(p);
	}

	public void disconnectePair(ContentNodeAddressI p) throws Exception {
		//disconnect pair et pair en NodeC et CM
		//get the outportNodeCI of this , which is connected with p
	    NodeCOutboundPort outportN= outPortsNodeC.get(p);
	    if (outportN!=null) { 
	    	//System.out.println(voisin.connected()+"  1 "+ voisin.getConnector());
			this.doPortDisconnection(outportN.getPortURI());
			outportN.unpublishPort();
			outPortsNodeC.remove(p);
			//System.out.println("\nc'est ok "+ p.getNodeidentifier() +" disconnect  avec " + this.adress.getNodeidentifier()+" en NodeCI");
			//get the outportContentManagementCI of this , which is connected with p
			
			ContentManagementCIOutbound outportCM= outPortsCM.get(p);
			this.doPortDisconnection(outportCM.getPortURI());
			outportCM.unpublishPort();
			outPortsCM.remove(p);
			System.out.println("\nc'est ok "+ p.getNodeidentifier() +" disconnect  avec " + this.adress.getNodeidentifier());
			voisinsAddressI.remove(p);
	    }		
	}

	


}
	

