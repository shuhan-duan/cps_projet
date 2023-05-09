package withplugin.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import classes.ContentDescriptor;
import classes.ContentNodeAdress;
import connector.ContentManagementConector;
import connector.FacadeContentManagementConector;
import connector.NodeConnector;
import connector.NodeManagementConnector;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionCI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import fr.sorbonne_u.cps.p2Pcm.dataread.ContentDataManager;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;
import interfaces.ContentNodeAddressI;
import interfaces.ContentTemplateI;
import interfaces.MyCMI;
import interfaces.NodeCI;
import interfaces.NodeManagementCI;
import ports.ContentManagementCIOutbound;
import ports.FacadeContentManagementCOutbound;
import ports.NodeCOutboundPort;
import ports.NodeManagementOutboundPort;
import withplugin.ports.ContentManagementCIIntboundPlugin;
import withplugin.ports.NodeCIntboundPortForPlugin;
import withplugin.CVM;

public class PairPlugin extends AbstractPlugin implements MyCMI {
	
		// -------------------------------------------------------------------------
		// Plug-in variables and constants
		// -------------------------------------------------------------------------
		protected NodeCIntboundPortForPlugin	NodePortIn ;
		protected Set<ContentNodeAddressI> voisins; //neighbours 
		//stock the pairs connected with this pair and the outportNodeC of me
		//adresse conecte avec le pair courant 
		private ConcurrentHashMap<ContentNodeAddressI,NodeCOutboundPort> outPortsNodeC;  //stock les vosins 
		private static int cpt = 0;
		public ContentNodeAdress adress;
		
		private int counter;
		
		protected ContentManagementCIIntboundPlugin CMportIn;
		private ArrayList<ContentDescriptorI> contents;
		
		/**	the outbound port used to call the service.							*/
		private String NMoutportUri ;
		public NodeManagementOutboundPort	NMportOut ;
		private String NMPortIn_facade ; 
		
		
		//stock the neighber pairs connected with this pair and the outportCMpair of me
		//ContentMangement 
		public ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound> outPortsCM;
		
		private static boolean flag ; // pour indiquer si on a trouve ou pas 
		
		// -------------------------------------------------------------------------
		// Life cycle
		// -------------------------------------------------------------------------
		
		/**
		 * create a pair plug-in.
		 * 
		 * <p><strong>Contract</strong></p>
		 * 
		 * <pre>
		 * pre	{@code executorServiceURI != null}
		 * post	true			// no postcondition.
		 * </pre>
		 * @throws Exception 
		 *
		 */
		public	PairPlugin( String NMoutportUri , int DescriptorID ,String NMPortIn_facade) throws Exception
		{
			super();
					
			cpt++;
			
			this.NMoutportUri = NMoutportUri+"plugin";
			this.counter = DescriptorID ;
			this.outPortsNodeC = new ConcurrentHashMap<ContentNodeAddressI,NodeCOutboundPort>();
			this.outPortsCM = new ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound>();
			this.voisins = new HashSet<ContentNodeAddressI>();
			this.contents = new ArrayList<ContentDescriptorI>();
			this.NMPortIn_facade = NMPortIn_facade;
			
			
			this.flag = false;
			
			
			
		}
		
		
		@Override
		public void installOn(ComponentI owner) throws Exception {
		    super.installOn(owner);
		    
		    // Add the interface
		    this.addOfferedInterface(NodeCI.class);
		    this.addRequiredInterface(NodeCI.class);
		    this.addRequiredInterface(NodeManagementCI.class);
		
		  }
		
		@Override
		public void initialise() throws Exception {
			 super.initialise();
			 
			// Create the inbound port
			 this.NodePortIn = new NodeCIntboundPortForPlugin(this.getOwner() ,getPluginURI());
			 NodePortIn.publishPort();
			 this.CMportIn = new ContentManagementCIIntboundPlugin(this.getOwner(),getPluginURI());
			 CMportIn.publishPort();
			 
			 // Create the outbound port
			 this.NMportOut = new NodeManagementOutboundPort(NMoutportUri, this.getOwner());
			NMportOut.publishPort();
			
			// connect with facade 
			this.getOwner().doPortConnection(NMoutportUri, NMPortIn_facade, NodeManagementConnector.class.getCanonicalName());
			 
			 this.adress = new ContentNodeAdress("Pair-plugin" + cpt, CMportIn.getPortURI(), NodePortIn.getPortURI());
			 
			//add descriptor
			addDescriptor(counter);
			
		}
		
		@Override
		public void			finalise() throws Exception
		{
			this.contents.clear();
			this.contents = null;
	       //		to do
			super.finalise();
		}
		
		@Override
		public void			uninstall() throws Exception
		{
			
			this.NodePortIn.unpublishPort();
			this.CMportIn.unpublishPort();
			
			// remove the interface
		    this.removeOfferedInterface(NodeCI.class);
		    this.removeOfferedInterface(NodeCI.class);
		    this.removeOfferedInterface(NodeManagementCI.class);
		}
		
		
		// -------------------------------------------------------------------------
		// Plug-in services implementation
		// -------------------------------------------------------------------------
		
		
	
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
		
		
		public void acceptConnected(ContentNodeAddressI p) throws Exception {
			//si p est connecte ,ajoute p dans neighbors
			
			voisins.add(p);
			//System.out.println("acceptConnected : les voisins de " + this.adress.getNodeidentifier()+"  est ajoute "+p.getNodeidentifier()+"  size: "+voisins.size());
			
		}


		
		public void disconnecte(ContentNodeAddressI p) throws Exception {
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
		
		// -------------------------------------------------------------------------
		// Plug-in services implementation
		// -------------------------------------------------------------------------
	
		@Override
		public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
		    requestURI = this.adress.getNodeidentifier();
		    System.out.println("\nwill do find in "+requestURI);

		    boolean found = false;

		    // Cherche parmi ses propres contenus
		    for (ContentDescriptorI content : this.contents) {
		        if (content.match(cd)) {
		            System.out.println("found in " + this.adress.getNodeidentifier());
		            FacadeContentManagementCOutbound outCM = new FacadeContentManagementCOutbound("myPairFCMout"+cpt, this.getOwner());
		            outCM.publishPort();

		            this.getOwner().doPortConnection(outCM.getPortURI(), requester.getFacadeCMURI(), FacadeContentManagementConector.class.getCanonicalName());

		            outCM.acceptFound(content, requestURI);
		            found = true;
		            break;
		        }
		    }

		    if (!found && hops > 0) {
		        //sinon il cherche dans les voisins 
		        System.out.println(this.adress.getNodeidentifier() + " n'a pas trouvé");

		        if (outPortsCM.size() == 0) {
		            System.out.println("\npas de neighbor : "+ this.adress.getNodeidentifier());
		        } else {
		            for (ContentManagementCIOutbound outportCM : outPortsCM.values()) {
		                outportCM.find(cd, hops-1, requester, requestURI);
		                if (found) {
		                    break;
		                }
		            }
		        }
		    }
		}

		
		@Override
		public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
		    requestURI = this.adress.getNodeidentifier();
		    System.out.println("\nwill do match in " + requestURI);

		    boolean flag = false;
		    if (hops <= 0) {
		        System.out.println("c'est match in pair qui termine");
		    } else {
		        // Cherche parmi ses propres contenus
		        for (ContentDescriptorI content : this.contents) {
		            if (content.match(cd)) {
		                System.out.println("matched in " + this.adress.getNodeidentifier());
		                FacadeContentManagementCOutbound outCM = new FacadeContentManagementCOutbound("myPairFCMout" + cpt, this.getOwner());
		                outCM.publishPort();

		                this.getOwner().doPortConnection(outCM.getPortURI(), requester.getFacadeCMURI(), FacadeContentManagementConector.class.getCanonicalName());
		                matched.add(content);
		                outCM.acceptMatched(matched, requestURI);
		                flag = true;
		            }
		        }
		        if (!flag) {
		            //sinon il cherche dans les voisins 
		            System.out.println(this.adress.getNodeidentifier() + " cannot match");
		            // Si le contenu n'est pas dans le nœud courant, demande à un autre pair
		            if (outPortsCM.size() == 0) {
		                System.out.println("\npas de neighbor : " + this.adress.getNodeidentifier());

		            } else {
		                Iterator<ContentManagementCIOutbound> it = outPortsCM.values().iterator();
		                while (it.hasNext() && !flag) {
		                    ContentManagementCIOutbound outportCM = it.next();
		                    outportCM.match(cd, matched, hops - 1, requester, requestURI);
		                }
		            }
		        }
		    }
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
		

}
