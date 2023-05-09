package withplugin.components;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import classes.ApplicationNodeAdress;
import connector.ContentManagementConector;
import connector.NodeConnector;
import fr.sorbonne_u.components.AbstractComponent;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentNodeAddressI;
import ports.ContentManagementCIOutbound;
import ports.NodeCOutboundPort;
import ports.NodeManagementInBoundPort;
import withplugin.plugins.FacadePlugin;



public class Facade  extends AbstractComponent{
	// -------------------------------------------------------------------------
	// Component variables and constants
	// -------------------------------------------------------------------------
	private final int MAX_ROOTS = 3;
	private final int MAX_PROBES = 3;
		
	private final ConcurrentHashMap<String, Integer> cptAcceptProbed = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<ContentNodeAddressI>> liste = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, NodeCOutboundPort> outPortsNodeC = new ConcurrentHashMap<>();
    private final NodeManagementInBoundPort NMportIn;
    private final ApplicationNodeAdress adress;
    private final FacadePlugin facade_plugin;

	
    private int rootCount = 0; //current racine nb
	
	
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	
	protected	Facade(	String ContentManagementInboudPort,	String 	NodeManagemenInboundPort ,String FCMInbountPortClient, String FacadeCMInPortFacade) throws Exception
	{
		// the reflection inbound port URI is the URI of the component
		super(NodeManagemenInboundPort, 2, 0) ;
		
		this.adress = new ApplicationNodeAdress("Facade",ContentManagementInboudPort,NodeManagemenInboundPort,FacadeCMInPortFacade) ;
		
		this.NMportIn = new NodeManagementInBoundPort( this,NodeManagemenInboundPort);
		this.NMportIn.publishPort();
		
		this.facade_plugin = new FacadePlugin(ContentManagementInboudPort, NodeManagemenInboundPort,FCMInbountPortClient,FacadeCMInPortFacade);
		this.installPlugin(facade_plugin);
		
	}
	
	// -------------------------------------------------------------------------
	// Services implementation
	// -------------------------------------------------------------------------
	
	
	//facade recoit le result de probe , p est le result , requestURI est le demander
	public void acceptProbed(ContentNodeAddressI p, String requsetURI) throws Exception {
		liste.get(requsetURI).add(p);
		int cpt_acceptProbed = cptAcceptProbed.get(requsetURI);
		cpt_acceptProbed++;
		cptAcceptProbed.put(requsetURI, cpt_acceptProbed);
		if (cpt_acceptProbed < MAX_PROBES) {
			System.out.println(cptAcceptProbed.get(requsetURI)+ "  "+ requsetURI+"  "+ p.getNodeidentifier());
		}else {
			NodeCOutboundPort nodeCportOut = outPortsNodeC.get(requsetURI);
			System.out.println(cptAcceptProbed.get(requsetURI)+ "  "+ requsetURI+"  "+ p.getNodeidentifier());
			nodeCportOut.acceptNeighbours(liste.get(requsetURI));
			System.out.println("\nfini acceptProbed : "+ requsetURI);
		
		}			
		
	}
	
	public void joinPair(ContentNodeAddressI p) throws Exception {
		if (rootCount < MAX_ROOTS - 1) {
	        connectToContentManagementCI(p);
	        connectToNodeCI(p);
	        rootCount++;
	    } else {
	        connectToNodeCI(p);
	        probeRoots(p);
	    }
	}
	
	private void connectToContentManagementCI(ContentNodeAddressI address) throws Exception {
	    String outportCM_Facade = "myOutportCMfacade" + rootCount;
	    ContentManagementCIOutbound CMportOut = new ContentManagementCIOutbound(outportCM_Facade, this);
	    CMportOut.publishPort();
	    facade_plugin.outPortsCM.put(address, CMportOut);
	    String inportCM_Pair = address.getContentManagementURI();

	    this.doPortConnection(outportCM_Facade, inportCM_Pair, ContentManagementConector.class.getCanonicalName());

	    System.out.println(address.getNodeidentifier() + " is connected to " + outportCM_Facade + " as root in FacadeContentManagementCI");
	}
	
	private void connectToNodeCI(ContentNodeAddressI address) throws Exception {
	    String outportNodeC_Facade = "myOutportNodeCfacade" + rootCount;
	    NodeCOutboundPort nodeCportOut = new NodeCOutboundPort(outportNodeC_Facade, this);
	    nodeCportOut.publishPort();
	    outPortsNodeC.put(address.getNodeidentifier(), nodeCportOut);
	    String inportNodeC_pair = address.getNodeUri();

	    this.doPortConnection(outportNodeC_Facade, inportNodeC_pair, NodeConnector.class.getCanonicalName());

	    System.out.println(address.getNodeidentifier() + " is connected to " + outportNodeC_Facade + " in NodeCI");
	}
	
	private void probeRoots(ContentNodeAddressI address) throws Exception {
		int cpt_acceptProbed = 0; 
		cptAcceptProbed.put(address.getNodeidentifier(),cpt_acceptProbed);
		Set<ContentNodeAddressI> roots = facade_plugin.outPortsCM.keySet();
		ContentNodeAddressI[] array = roots .toArray(new ContentNodeAddressI[0]);
		Random random = new Random();
		for(int i = 0 ; i < MAX_PROBES ;i++) {
			int randomIndex = random.nextInt(array.length);
			ContentNodeAddressI root = array[randomIndex]; 
			NodeCOutboundPort nodeCOutboundPort = outPortsNodeC.get(root.getNodeidentifier());
			nodeCOutboundPort.probe(adress, MAX_PROBES, address.getNodeidentifier());
			Set<ContentNodeAddressI> set = new HashSet<>();
			liste.put(address.getNodeidentifier(), set);
		}			
	}
	
	public void leavePair(ContentNodeAddressI address) throws Exception {
	    if (facade_plugin.outPortsCM.containsKey(address)) {
	        this.doPortDisconnection(facade_plugin.outPortsCM.get(address).getPortURI());
	        this.doPortDisconnection(outPortsNodeC.get(address.getNodeidentifier()).getPortURI());
	        System.out.println("Removed root pair " + address.getNodeidentifier() + " with CM and NodeCI");
	        facade_plugin.outPortsCM.remove(address);
	    } else {
	    	System.out.println("No root pair connected with facade");
	    }
	}


	public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) {
		// TODO Auto-generated method stub
		
	}
}
