package withplugin.plugins;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import classes.ApplicationNodeAdress;
import connector.ContentManagementConector;
import connector.FacadeContentManagementConector;
import connector.NodeConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionCI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentNodeAddressI;
import interfaces.ContentTemplateI;
import interfaces.MyCMI;
import interfaces.MyFCMI;
import interfaces.NodeManagementCI;
import ports.ContentManagementCIOutbound;
import ports.FacadeContentManagementCOutbound;
import ports.NodeCOutboundPort;
import withplugin.ports.ContentManagementCIIntboundPlugin;
import withplugin.ports.FacadeContentManagementCInboundPlugin;
import withplugin.ports.NodeManagementInBoundPortPlugin;



public class FacadePlugin extends AbstractPlugin implements MyCMI ,MyFCMI{
	
	// -------------------------------------------------------------------------
	// Plug-in variables and constants
	// -------------------------------------------------------------------------
	
	public static int cpt = 0;//current racine nb
	public static int cpt_facade = 0;
	protected final int NB_RACINE = 3;
	protected final int NB_PROBE = 3;
    protected ApplicationNodeAdress		adress;
    
    //stock the outports of facade and the racine pair connected with it
  	//On Stock "ContentManagementCIOutbound" pour chaque pair pour faire appel a find et match sur pair 
  	private ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound> outPortsCM;
  	
  	
  	private ConcurrentHashMap<String,NodeCOutboundPort> outPortsNodeC;
  	
  	private  ConcurrentHashMap<String,Integer> cptAcceptProbed;
  	protected ConcurrentHashMap<String,Set<ContentNodeAddressI>>  liste ; // pour stocker les results de prob
  	
  	protected NodeManagementInBoundPortPlugin  NMportIn;
  	protected ContentManagementCIIntboundPlugin CMportIn;
  	protected FacadeContentManagementCInboundPlugin fCMportIn; //connect avec racine
  	protected FacadeContentManagementCOutbound fCMportOutbound; //connect avec client
  	
  	protected String fCMInbountPortClient ;
  	
  	// -------------------------------------------------------------------------
 	// Life cycle
 	// -------------------------------------------------------------------------
 	
  	public FacadePlugin(String ContentManagementInboudPort,	String 	NodeManagemenInboundPort ,
  			String FCMInbountPortClient, String FacadeCMInPortFacade)throws Exception {
		    super();
		    
		    setPluginURI(AbstractPort.generatePortURI());
		    
		    this.adress = new ApplicationNodeAdress("Facade",ContentManagementInboudPort,NodeManagemenInboundPort,FacadeCMInPortFacade) ;
			this.liste = new ConcurrentHashMap<String,Set<ContentNodeAddressI> >();
			this.outPortsCM =  new ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound>();
			this.outPortsNodeC = new ConcurrentHashMap<String, NodeCOutboundPort>();
			this.cptAcceptProbed = new ConcurrentHashMap<String, Integer>();
			
			
		  }
  	
  	@Override
	  public void initialise() throws Exception {
		     super.initialise();
		     
		NMportIn = new NodeManagementInBoundPortPlugin( this.getOwner(),this.getPluginURI());
		NMportIn.publishPort();
		fCMportIn = new FacadeContentManagementCInboundPlugin(this.getPluginURI(),this.getOwner());
		fCMportIn.publishPort();
		
		CMportIn = new ContentManagementCIIntboundPlugin(this.getOwner(),this.getPluginURI());
		CMportIn.publishPort();
		System.out.println("------  "+this.getPluginURI()+ "  "+adress.getContentManagementURI()+ "  "+ CMportIn.getPortURI());
		fCMportOutbound = new FacadeContentManagementCOutbound("myFCMfacade", this.getOwner());
		fCMportOutbound.publishPort();
		
	  }
  	

	  	@Override
	  	public void installOn(ComponentI owner) throws Exception {
		    super.installOn(owner);
		    this.addOfferedInterface(NodeManagementCI.class);
		    this.addRequiredInterface(ContentManagementCI.class);
		    this.addOfferedInterface(ContentManagementCI.class);
	  	}
	  
		/**
		  * @see fr.sorbonne_u.components.PluginI#finalise()
		*/
		@Override
	  	public void			finalise() throws Exception
		{
			this.liste.clear();
			
			super.finalise();
		}
	
		/**
		 * @see fr.sorbonne_u.components.AbstractPlugin#uninstall()
		 */
		@Override
		public void			uninstall() throws Exception
		{
			
			NMportIn.unpublishPort();
			
			fCMportIn.unpublishPort();
			
			CMportIn.unpublishPort();
			
			fCMportOutbound.unpublishPort();	
			
		}
		

		// -------------------------------------------------------------------------
		// Plug-in services implementation
		// -------------------------------------------------------------------------
		//facade recoit le result de probe , p est le result , requestURI est le demander
				public void acceptProbed(ContentNodeAddressI p, String requsetURI) throws Exception {
					liste.get(requsetURI).add(p);
					int cpt_acceptProbed = cptAcceptProbed.get(requsetURI);
					cpt_acceptProbed++;
					cptAcceptProbed.put(requsetURI, cpt_acceptProbed);
					if (cpt_acceptProbed < NB_PROBE) {
						System.out.println(cptAcceptProbed.get(requsetURI)+ "  "+ requsetURI+"  "+ p.getNodeidentifier());
					}else {
						NodeCOutboundPort nodeCportOut = outPortsNodeC.get(requsetURI);
						System.out.println(cptAcceptProbed.get(requsetURI)+ "  "+ requsetURI+"  "+ p.getNodeidentifier());
						nodeCportOut.acceptNeighbours(liste.get(requsetURI));
						System.out.println("\nfini acceptProbed : "+ requsetURI);
					
					}			
					
				}

				public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) throws Exception{}

				@Override
				public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
					Set<ContentNodeAddressI> racineSet = outPortsCM.keySet();
					requester =this.adress;
					for (ContentNodeAddressI root : racineSet) {
						requestURI = root.getNodeidentifier();
						outPortsCM.get(root).find(cd, hops, requester, requestURI);
					}
					
				}
				@Override
				public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester,
						String requestURI) throws Exception {
					Set<ContentNodeAddressI> racineSet = outPortsCM.keySet();
					requester =this.adress;
					for (ContentNodeAddressI root : racineSet) {
						requestURI = root.getNodeidentifier();
						outPortsCM.get(root).match(cd, matched, hops, requester, requestURI);
					}
					
				}
				
				
				public void joinPair(ContentNodeAddressI p) throws Exception {
					
					//quand on a encore besoin de racine
					if (cpt < NB_RACINE -1  ) {
						
						//do connect entre facade et racine en "ContentManagementCI" 
						String outportCM_Facade = "myOutportCMfacade" + cpt; 
						ContentManagementCIOutbound CMportOut = new ContentManagementCIOutbound(outportCM_Facade,this.getOwner());
						CMportOut.publishPort(); 
						outPortsCM.put(p, CMportOut); 
						String inportCM_Pair = p.getContentManagementURI();
						
						// Use the reflection approach to get the URI of the inbound port of the component.
						this.addRequiredInterface(ReflectionCI.class);
						ReflectionOutboundPort rop = new ReflectionOutboundPort(this.getOwner());
						rop.publishPort();
						System.out.println("------------ join "+ inportCM_Pair);
						this.getOwner().doPortConnection(
								rop.getPortURI(),
								inportCM_Pair,
								ReflectionConnector.class.getCanonicalName());
						
						
						String[] uris = rop.findPortURIsFromInterface(ContentManagementCI.class);
						assert	uris != null && uris.length == 1;
						
						System.out.println("------------ join + "+ uris[0]);
						
						this.getOwner().doPortDisconnection(rop.getPortURI());
						rop.unpublishPort();
						rop.destroyPort();
						this.removeRequiredInterface(ReflectionCI.class);
						
					
						this.getOwner().doPortConnection(outportCM_Facade,
											uris[0], 
								  			ContentManagementConector.class.getCanonicalName());
						System.out.println("------------ join ++ ");
						outPortsCM.put(p,CMportOut); 
						
						//do connect entre facade et racine en "NodeCI" 
						String outportNodeC_Facade = "myOutportNodeCfacade" + cpt; 
						NodeCOutboundPort NodeCportOut = new NodeCOutboundPort(outportNodeC_Facade, this.getOwner());
						NodeCportOut.publishPort(); 
						outPortsNodeC.put(p.getNodeidentifier(), NodeCportOut); 
						String inportNodeC_pair = p.getNodeUri();
						this.getOwner().doPortConnection(outportNodeC_Facade,
								inportNodeC_pair, 
								  			NodeConnector.class.getCanonicalName());
						System.out.println("\nc'est ok " + p.getNodeidentifier() +" est connecte avec "+outportCM_Facade +" comme racine en FacadeContentManagementCI et NodeCI" ); 
						cpt++;
					}else {
						//do connect entre facade et pair en "NodeCI" 
						String outportNodeC_Facade = "myOutportNodeCfacade" + cpt; 
						NodeCOutboundPort nodeCportOut = new NodeCOutboundPort(outportNodeC_Facade, this.getOwner());
						nodeCportOut.publishPort(); 
						outPortsNodeC.put(p.getNodeidentifier(), nodeCportOut); 
						String inportNodeC_pair = p.getNodeUri();
						this.getOwner().doPortConnection(outportNodeC_Facade,
								inportNodeC_pair, 
								  			NodeConnector.class.getCanonicalName());
						System.out.println("\nc'est ok " + p.getNodeidentifier() +" connecte avec "+outportNodeC_Facade +" en NodeCI" ); 
						
						//get the neighbors by doing probe with roots
						//appeler probe sur racines
						
						int cpt_acceptProbed = 0; 
						cptAcceptProbed.put(p.getNodeidentifier(),cpt_acceptProbed);
						Set<ContentNodeAddressI> roots = outPortsCM.keySet();
						ContentNodeAddressI[] array = roots .toArray(new ContentNodeAddressI[0]);
						Random random = new Random();
						for(int i = 0 ; i < NB_PROBE ;i++) {
							int randomIndex = random.nextInt(array.length);
							ContentNodeAddressI root = array[randomIndex];
							NodeCOutboundPort nodeCOutboundPort = outPortsNodeC.get(root.getNodeidentifier());
							nodeCOutboundPort.probe(adress, NB_PROBE, p.getNodeidentifier());
							Set<ContentNodeAddressI> set = new HashSet<>();
							liste.put(p.getNodeidentifier(), set);
						}			
										 
					}
					 
					
			}
				public void leavePair(ContentNodeAddressI p) throws Exception {
					// leave root pair from facade with CM et NodeCI
							if (outPortsCM.containsKey(p)){
								if(!outPortsCM.isEmpty()) {
									this.getOwner().doPortDisconnection(outPortsCM.get(p).getPortURI());
									this.getOwner().doPortDisconnection(outPortsNodeC.get(p.getNodeUri()).getPortURI());
									System.out.println("\nsupprime le inportCM racine pair :  " + p.getNodeidentifier()+" avec le outport facade: " + outPortsCM.get(p).getPortURI());
									outPortsCM.remove(p);
								}
								else {
									System.out.println(" il y a plus de pair racine connecte avec facade ");
								}
							}  
					
				}
				
				public void acceptFound(ContentDescriptorI found, String requsetURI) throws Exception {
					
					this.getOwner().doPortConnection(fCMportOutbound.getPortURI(), fCMInbountPortClient, FacadeContentManagementConector.class.getCanonicalName());
					
					if (found != null) {
						fCMportOutbound.acceptFound(found, requsetURI);
					}		
				}
				
				public void acceptMatched(Set<ContentDescriptorI> matched ,String requsetURI) throws Exception {
					this.getOwner().doPortConnection(fCMportOutbound.getPortURI(), fCMInbountPortClient, FacadeContentManagementConector.class.getCanonicalName());
					if (matched != null) {
						fCMportOutbound.acceptMatched(matched, requsetURI);
					}
				}

}
