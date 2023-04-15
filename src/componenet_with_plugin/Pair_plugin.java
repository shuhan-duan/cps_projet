package componenet_with_plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import classes.ContentDescriptor;
import classes.ContentNodeAdress;
import connector.ContentManagementConector;
import connector.FacadeContentManagementConector;
import connector.NodeConnector;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.cps.p2Pcm.dataread.ContentDataManager;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;
import interfaces.ContentNodeAddressI;
import interfaces.ContentTemplateI;
import interfaces.MyCMI;
import ports.ContentManagementCIOutbound;
import ports.FacadeContentManagementCOutbound;
import ports.NodeCIntboundPort;
import ports.NodeCOutboundPort;
import ports.NodeManagementOutboundPort;
import ports_with_plugin.ContentManagementCIIntbound_plugin;

public class Pair_plugin  extends AbstractPlugin implements MyCMI{
	protected ClocksServerOutboundPort csop; 
	public static int cpt = 0;

	/**	the outbound port used to call the service.							*/
	protected NodeManagementOutboundPort	NMportOut ;
	protected String NMPortIn_facade;
	protected NodeCIntboundPort	NodePortIn ;
	protected ContentManagementCIIntbound_plugin CMportIn;
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
	
	protected Pair_plugin( String NMoutportUri ,String NMPortIn_facade, int DescriptorID)throws Exception {
		 super();
		    setPluginURI(AbstractPort.generatePortURI());		
		cpt++;
		this.adress = new ContentNodeAdress("Pair" + cpt,"CMuriIn"+ cpt, "NodeCuriIn"+ cpt);
		
		this.NMportOut =new NodeManagementOutboundPort(NMoutportUri, this.getOwner()) ;
		NMportOut.publishPort() ;
		this.NodePortIn = new NodeCIntboundPort(this.getOwner() ,adress.getNodeUri());
		NodePortIn.publishPort();
		this.CMportIn = new ContentManagementCIIntbound_plugin(this.getOwner(), adress.getContentManagementURI());
		CMportIn.publishPort();
		
		this.counter = DescriptorID ;
		this.NMPortIn_facade = NMPortIn_facade;
		this.outPortsNodeC = new ConcurrentHashMap<ContentNodeAddressI,NodeCOutboundPort>();
		this.outPortsCM = new ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound>();
		this.voisins = new HashSet<ContentNodeAddressI>();
		this.contents = new ArrayList<ContentDescriptorI>();
		//add descriptor
		addDescriptor(DescriptorID);
		//Create Clock
		this.csop = new ClocksServerOutboundPort(this.getOwner());
		this.csop.publishPort();	
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
	public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) throws Exception {
		System.out.println(requestURI+ " demande do prob in "+ this.adress.getNodeidentifier() + " hops "+ remainghops);
		if (remainghops <= 0) {
			//il retourne sa propre adresse à la façade ayant initié le sondage 
			this.NMportOut.acceptProbed(adress, requestURI);
		}else {
			//à un de ses voisins choisi aléatoirement
			ContentNodeAddressI[] array = voisins .toArray(new ContentNodeAddressI[0]);
			//s'il a pas de voisin encore ,il retourne sa propre adress
			if (voisins.size() == 0 ) {
				this.NMportOut.acceptProbed(adress, requestURI);
			}else {
				Random rand = new Random();
				int randomIndex = rand.nextInt(voisins.size());
				ContentNodeAddressI neighbor = array[randomIndex];
				NodeCOutboundPort nodeCout = outPortsNodeC.get(neighbor);
				nodeCout.probe(facade, remainghops-1, requestURI);	
			}					
		}
		
	}

public void acceptNeighbours(Set<ContentNodeAddressI> neighbours) throws Exception {
	//this.neighbors.addAll(neighbours);
	System.out.println("\nacceptNeighbours: when return liste to : "+this.adress.getNodeidentifier()+"  size:  " + neighbours.size());
	if(neighbours.size() == 0) { // liste vide le 1er pair n'a pas de voisins 
		System.out.println("\n"+adress.getNodeidentifier() +" says : I don't have neigber yet!");
	}else{
		for (ContentNodeAddressI p : neighbours) {
			System.out.println("je suis " + this.adress.getNodeidentifier()+ " j'ai voisin: " + p.getNodeidentifier());
			//do connection entre pair et pair en NodeCI
				//System.out.println("\nI am "+ adress.getNodeidentifier()+", I will connect with my neighber : "+p.getNodeidentifier());

				cpt++;
				String outportN = "myOutPortNodeCIpair"+ cpt;
				NodeCOutboundPort NportOut = new NodeCOutboundPort(outportN, this.getOwner());
				NportOut.publishPort();
				outPortsNodeC.put(p, NportOut);
				this.getOwner().doPortConnection(NportOut.getPortURI(),	p.getNodeUri(),NodeConnector.class.getCanonicalName());
				//System.out.println("\nc'est ok " + p.getNodeidentifier() +" connecte avec "+ this.adress.getNodeidentifier() +" en "+ outPortsNodeC.get(p).getPortURI()+" en NodeCI" );
				
				String outportCM = "myOutportCMpair" + cpt ;
				ContentManagementCIOutbound CMportOut = new ContentManagementCIOutbound(outportCM,this.getOwner());
				CMportOut.publishPort();
				outPortsCM.put(p, CMportOut);
				this.getOwner().doPortConnection(outportCM,	p.getContentManagementURI(),ContentManagementConector.class.getCanonicalName());
				System.out.println("\nc'est ok "+ p.getNodeidentifier() +" connect  avec " +this.adress.getNodeidentifier() +" en "+ CMportOut.getPortURI() +" en ContentManagement");
				NportOut.connecte(this.adress);		
		}
	}
	
}

public void acceptConnected(ContentNodeAddressI p) throws Exception {
	//si p est connecte ,ajoute p dans neighbors
	
	voisins.add(p);
	//System.out.println("acceptConnected : les voisins de " + this.adress.getNodeidentifier()+"  est ajoute "+p.getNodeidentifier()+"  size: "+voisins.size());
	
}

@Override
public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
	requestURI = this.adress.getNodeidentifier();
	System.out.println("\nwill do find in "+requestURI);
	Boolean flag = false;
	
	if (hops <= 0) {
		System.out.println("c'est find in pair qui termine");
	}else {
		// Cherche parmi ses propres contenus
		for (ContentDescriptorI content : this.contents) {
			if (content.match(cd)) {  //pour march le patron avec le contenue 
				System.out.println("found in " + this.adress.getNodeidentifier());
				FacadeContentManagementCOutbound outCM = new FacadeContentManagementCOutbound("myPairFCMout"+cpt, this.getOwner());
				outCM.publishPort();
				
				this.getOwner().doPortConnection(outCM.getPortURI(), requester.getFacadeCMURI(), FacadeContentManagementConector.class.getCanonicalName());
				
				outCM.acceptFound(content, requestURI);
				flag = true;
			}
		}
 		
		if (flag == false) {
			//sinon il cherche dans les voisin 
			System.out.println(this.adress.getNodeidentifier() + " n'a pas touve");
			// Si le contenu n'est pas dans le nœud courant, demande à un autre pair
			if (voisins.size() == 0) {
					System.out.println("\npas de neighbor : "+ this.adress.getNodeidentifier());
						
			}else {
				
						ContentNodeAddressI[] array = voisins .toArray(new ContentNodeAddressI[0]);
						Random rand = new Random();
						int randomIndex = rand.nextInt(voisins.size());
						ContentNodeAddressI neighbor = array[randomIndex];
						ContentManagementCIOutbound outportCM = outPortsCM.get(neighbor);
						outportCM.find(cd, hops-1, requester, requestURI);
						//System.out.println("\nwill do find in :" + neighbor.getNodeidentifier()+" "+ outportCM.getPortURI());
						//System.out.println(outportCM.getPortURI()+ " is connected? "+outportCM.connected());					
			}
		}
		
	}
	
}

@Override
public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester,
		String requestURI) throws Exception {
	requestURI = this.adress.getNodeidentifier();
	System.out.println("\nwill do match in "+requestURI);
	Boolean flag = false;
	if (hops <= 0) {
		System.out.println("c'est match in pair qui termine");
	}else {
		// Cherche parmi ses propres contenus
		for (ContentDescriptorI content : this.contents) {
			if (content.match(cd)) {  //pour march le patron avec le contenue 
				System.out.println("matched in " + this.adress.getNodeidentifier());
				FacadeContentManagementCOutbound outCM = new FacadeContentManagementCOutbound("myPairFCMout"+cpt, this.getOwner());
				outCM.publishPort();
				
				this.getOwner().doPortConnection(outCM.getPortURI(), requester.getFacadeCMURI(), FacadeContentManagementConector.class.getCanonicalName());
				matched.add(content);
				outCM.acceptMatched(matched, requestURI);
				flag =true ;
			}
		}
 		if (flag) {
				
		}else {
				//sinon il cherche dans les voisin 
				System.out.println(this.adress.getNodeidentifier() + " cannot match");
				// Si le contenu n'est pas dans le nœud courant, demande à un autre pair
				if (voisins.size() == 0) {
						System.out.println("\npas de neighbor : "+ this.adress.getNodeidentifier());
							
				}else {
							ContentNodeAddressI[] array = voisins .toArray(new ContentNodeAddressI[0]);
							Random rand = new Random();
							int randomIndex = rand.nextInt(voisins.size());
							ContentNodeAddressI neighbor = array[randomIndex];
							ContentManagementCIOutbound outportCM = outPortsCM.get(neighbor);
							outportCM.match(cd, matched, hops -1, requester, requestURI);
							//System.out.println("\nwill do find in :" + neighbor.getNodeidentifier()+" "+ outportCM.getPortURI());
							//System.out.println(outportCM.getPortURI()+ " is connected? "+outportCM.connected());					
				}
			}				
		}
}

public void connecte(ContentNodeAddressI p) throws Exception {
	cpt++;
	String outportN = "myOutPortNodeCIpair"+ cpt;
	NodeCOutboundPort NportOut = new NodeCOutboundPort(outportN, this.getOwner());
	NportOut.publishPort();
	outPortsNodeC.put(p, NportOut);
	this.getOwner().doPortConnection(NportOut.getPortURI(),	p.getNodeUri(),NodeConnector.class.getCanonicalName());
	//System.out.println("\nc'est ok " + p.getNodeidentifier() +" connecte avec "+ this.adress.getNodeidentifier() +" en "+ outPortsNodeC.get(p).getPortURI()+" en NodeCI" );
	
	String outportCM = "myOutportCMpair" + cpt ;
	ContentManagementCIOutbound CMportOut = new ContentManagementCIOutbound(outportCM,this.getOwner() );
	CMportOut.publishPort();
	outPortsCM.put(p, CMportOut);
	this.getOwner().doPortConnection(outportCM,	p.getContentManagementURI(),ContentManagementConector.class.getCanonicalName());
	System.out.println("\nc'est ok "+ p.getNodeidentifier() +" connect  avec " +this.adress.getNodeidentifier() +" en "+ CMportOut.getPortURI() +" en ContentManagement");	
	voisins.add(p);
	//System.out.println(p.getNodeidentifier()+"  est ajooute dans les voisins de " +adress.getNodeidentifier()+ "  size :  "+ voisins.size());
	NportOut.acceptConnected(this.adress);
}

public void disconnectePair(ContentNodeAddressI p) throws Exception {
	//disconnect pair et pair en NodeC et CM
	//get the outportNodeCI of this , which is connected with p
    NodeCOutboundPort outportN= outPortsNodeC.get(p);
    if (outportN!=null) { 
    	this.getOwner().doPortDisconnection(outportN.getPortURI());
		outportN.unpublishPort();
		outPortsNodeC.remove(p);
		//System.out.println("\nc'est ok "+ p.getNodeidentifier() +" disconnect  avec " + this.adress.getNodeidentifier()+" en NodeCI");
		
		//get the outportContentManagementCI of this , which is connected with p			
		ContentManagementCIOutbound outportCM= outPortsCM.get(p);
		this.getOwner().doPortDisconnection(outportCM.getPortURI());
		outportCM.unpublishPort();
		outPortsCM.remove(p);
		System.out.println("\nc'est ok "+ p.getNodeidentifier() +" disconnect  avec " + this.adress.getNodeidentifier());
		voisins.remove(p);
    }		
}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
