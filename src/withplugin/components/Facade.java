package withplugin.components;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import classes.ApplicationNodeAdress;
import connector.ContentManagementConector;
import connector.NodeConnector;
import connector.NodeManagementConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentNodeAddressI;
import interfaces.MyThreadServiceI;
import ports.ContentManagementCIOutbound;
import ports.NodeCOutboundPort;
import ports.NodeManagementInBoundPort;
import ports.NodeManagementOutboundPort;
import withplugin.plugins.FacadePlugin;




public class Facade  extends AbstractComponent implements MyThreadServiceI{
	
	// -------------------------------------------------------------------------
	// Component variables and constants
	// -------------------------------------------------------------------------
	private static final int MAX_ROOTS = 2; 
	private static final int MAX_PROBES = 2;
	private static final int nbFacades = 2;
	
	/** URI of the facade inbound component (convenience).						*/
	protected static final String	NMInboundPortURI = "inportNMfacade"; // 
	protected static final String	FCMInPortFacadeURI = "inportFCMfacade";
	
	//to count the times that facade has received the result of probed
	private ConcurrentHashMap<String, Integer> cptAcceptProbed = new ConcurrentHashMap<>();
	
    private ConcurrentHashMap<String, Set<ContentNodeAddressI>> resProbed = new ConcurrentHashMap<>();
    
    //stock "NodeCOutboundPort" connected with each pair to call probe on root and acceptNeighbours on pair
    private ConcurrentHashMap<String, NodeCOutboundPort> outPortsNodeC = new ConcurrentHashMap<>();
    //stock "NodeManagementOutboundPort" connected with neighbors facade to call probe on neighbor facade  
    private ConcurrentHashMap<Integer, NodeManagementOutboundPort> outPortsNM = new ConcurrentHashMap<>();
    
    private NodeManagementInBoundPort inPortNM;
    
    private ApplicationNodeAdress adress;
    
    private FacadePlugin facade_plugin;
 
	private int id ;
	
	private Random random = new Random();
	
	private static final int NB_OF_THREADS = 2;

	private static final String NM_THREAD_SERVICE_URI = "facade_nm_pool";
	private static final String FCM_THREAD_SERVICE_URI = "facade_fcm_pool";
	
	

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	protected	Facade(String FacadeURI ,String FCMInPortClientURI) throws Exception
	{
		// the reflection inbound port URI is the URI of the component
		super(FacadeURI, NB_OF_THREADS, 0) ;
		
		String[] parts = FacadeURI.split("Facade");
		this.id = Integer.parseInt(parts[1]);
		
		//public ApplicationNodeAdress(String uriPrefix, String uriFCM ,String  uriNM)
		this.adress = new ApplicationNodeAdress(FacadeURI ,FCMInPortFacadeURI +id ,NMInboundPortURI +id) ;
		
		this.inPortNM = new NodeManagementInBoundPort(this, adress.getNodeManagementUri());
		this.inPortNM.publishPort();
		
		
		int nbThreadsNM = 1;
		int nbThreadsFCM = NB_OF_THREADS - nbThreadsNM;
		
		this.createNewExecutorService(NM_THREAD_SERVICE_URI+id, nbThreadsNM, false);
		this.createNewExecutorService(FCM_THREAD_SERVICE_URI+id, nbThreadsFCM, false);
		
		//FCMInPortFacadeURI is used for plugin
		this.facade_plugin = new FacadePlugin(adress,FCMInPortClientURI);
		this.installPlugin(facade_plugin);
		System.out.println("facade"+id+"[label=\"Facade "+ id +"\"];");
		
		
	}
	//-------------------------------------------------------------------------
	// Component life-cycle
	//-------------------------------------------------------------------------
	
	@Override
	public void start() throws ComponentStartException {
		super.start();
		try {
			//the façade components will be interconnected via the NodeManagementCI interface
			for (int i = 0; i < nbFacades; i++) {
				if (i == id) {
					// not connect with itself
				}else {
					NodeManagementOutboundPort outPortNM = new NodeManagementOutboundPort(this);
					outPortNM.publishPort();
					outPortsNM.put(i, outPortNM);
					doPortConnection(outPortNM.getPortURI(), NMInboundPortURI+i, NodeManagementConnector.class.getCanonicalName());
					System.out.println("facade"+id +" -> facade"+ i);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	// -------------------------------------------------------------------------
	// Services implementation
	// -------------------------------------------------------------------------
	
	
	//facade receives the result from probe , p is the result , requestURI is the request
	public void acceptProbed(ContentNodeAddressI p, String requsetURI) throws Exception {
		resProbed.get(requsetURI).add(p);
		int cpt_acceptProbed = cptAcceptProbed.get(requsetURI);
		cpt_acceptProbed++;
		cptAcceptProbed.put(requsetURI, cpt_acceptProbed);
		//System.out.println(requsetURI + " recieved the result of probe : result "+ cptAcceptProbed.get(requsetURI)+" : "+ p.getNodeidentifier());
		if (cptAcceptProbed.get(requsetURI) == MAX_PROBES) {
			NodeCOutboundPort outPortNodeC = outPortsNodeC.get(requsetURI);
			outPortNodeC.acceptNeighbours(resProbed.get(requsetURI));
			//System.out.println("\nfini acceptProbed : "+ requsetURI);
		}					
	}
	
	public void join(ContentNodeAddressI p) throws Exception {
		if ( facade_plugin.getRootOutPortsCM().size()< MAX_ROOTS) {
	        connectToContentManagementCI(p);
	        connectToNodeCI(p);
	    } else {
	        connectToNodeCI(p);
	        //probe(this.adress, MAX_PROBES, p.getNodeidentifier());
	        doProbe(p);
	    }
	}
	
	
	public void leave(ContentNodeAddressI address) throws Exception {
	    if (facade_plugin.getRootOutPortsCM().containsKey(address)) {
	        this.doPortDisconnection(facade_plugin.getRootOutPortsCM().get(address).getPortURI());
	        this.doPortDisconnection(outPortsNodeC.get(address.getNodeidentifier()).getPortURI());
	        System.out.println("Removed root :" + address.getNodeidentifier());
	        facade_plugin.removeRootOutPortsCM(address);
	    } else {
	    	System.out.println(address.getNodeidentifier()+" is not connected with facade as root");
	    }
	}


	public void probe(ApplicationNodeAdressI facadeInitial, int remainghops, String requestURI) throws Exception {
		//randomly choose to probe by roots or facades
	    boolean doRoots = random.nextBoolean();
	    if (doRoots) {
	        probeRoots(requestURI, facadeInitial ,remainghops);
	    } else {
	        probeFacade(requestURI, facadeInitial ,remainghops);
	    }	    
//		probeRoots(requestURI, facadeInitial ,remainghops);
//		probeFacade(requestURI, facadeInitial ,remainghops);
	}
	
	// -------------------------------------------------------------------------
	// Auxiliary Functions to use in Facade
	// -------------------------------------------------------------------------
	private void connectToContentManagementCI(ContentNodeAddressI address) throws Exception {
	    String outportCM_Facade = AbstractOutboundPort.generatePortURI();
	    ContentManagementCIOutbound outPortCM = new ContentManagementCIOutbound(outportCM_Facade, this);
	    outPortCM.publishPort();
	    facade_plugin.addRootOutPortsCM(address,outPortCM);
	    
	    String inportCM_Pair = address.getContentManagementURI();
	    this.doPortConnection(outportCM_Facade, inportCM_Pair, ContentManagementConector.class.getCanonicalName());

	    System.out.println(address.getNodeidentifier() + " -> " + this.adress.getNodeidentifier()+"[style=dashed]");
	}
	
	private void connectToNodeCI(ContentNodeAddressI address) throws Exception {
	    String outportNodeC_Facade = AbstractOutboundPort.generatePortURI();
	    NodeCOutboundPort outPortNodeC = new NodeCOutboundPort(outportNodeC_Facade, this);
	    outPortNodeC.publishPort();
	    outPortsNodeC.put(address.getNodeidentifier(), outPortNodeC);
	    
	    String inportNodeC_pair = address.getNodeUri();
	    this.doPortConnection(outportNodeC_Facade, inportNodeC_pair, NodeConnector.class.getCanonicalName());

	}
	
	// Initialization return results for probe 
	private void doProbe(ContentNodeAddressI address) throws Exception {
		probeFacade(address.getNodeidentifier(), adress, MAX_PROBES);
		probeRoots(address.getNodeidentifier(), adress, MAX_PROBES);
		
		//Initialization return results
	    int cpt_acceptProbed = 0; 
	    cptAcceptProbed.put(address.getNodeidentifier(), cpt_acceptProbed);
	    Set<ContentNodeAddressI> set = new HashSet<>();
	    resProbed.put(address.getNodeidentifier(), set);
	}

	
	private void probeRoots(String requestURI, ApplicationNodeAdressI adressnInitiale, int hops) throws Exception {
	    Set<ContentNodeAddressI> roots = facade_plugin.getRootOutPortsCM().keySet();
	    // call probe for each root
	    for (ContentNodeAddressI root : roots) {
	        NodeCOutboundPort outPortNodeC = outPortsNodeC.get(root.getNodeidentifier());
	        //outPortNodeC.probe(adressnInitiale, hops, requestURI);
	        outPortNodeC.probe(adressnInitiale,  hops,  requestURI,
					 null, 0);
	    }
	    if (adressnInitiale == adress) {
			
		}
	}

	
	private void probeFacade(String requestURI, ApplicationNodeAdressI adressnInitiale, int hops) throws Exception {
	    // randomly call probe in a neighbor facade 		
	    int randomIndex = random.nextInt(outPortsNM.size());
	    Iterator<Integer> iterator = outPortsNM.keySet().iterator();
	    Integer facadeId = null ;
	    for (int i = 0; i <= randomIndex; i++) {
			facadeId = iterator.next();
		}
	    NodeManagementOutboundPort outPortNM = outPortsNM.get(facadeId);
	    outPortNM.probe(adressnInitiale, hops, requestURI);
	}

	@Override
	public String get_THREAD_POOL_URI() {
	
		return NM_THREAD_SERVICE_URI+id;
	}
	
	@Override
	public String get_CM_THREAD_POOL_URI() {	
		return FCM_THREAD_SERVICE_URI+id;
	}
}
