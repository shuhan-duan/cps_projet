package componenet;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import classes.ContentDescriptor;
import classes.ContentNodeAdress;
import connector.ContentManagementCIConector;
import connector.ManagementConnector;
import connector.NodeC_conector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.components.ports.InboundPortI;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentNodeAddressI;
import interfaces.ContentTemplateI;
import interfaces.NodeCI;
import interfaces.PeerNodeAddressI;
import ports.ContentManagementCIIntbound;
import ports.ContentManagementCIOutbound;
import ports.ManagementOutboundPort;
import ports.NodeCIntboundPort;
import ports.NodeCOutboundPort;

/**
 * @author lyna & shuhan 
 *
 */
@RequiredInterfaces(required = {PeerNodeAddressI.class})

public class Pair  extends AbstractComponent implements PeerNodeAddressI {
	

	protected final static int	N = 2 ;
	public static int cpt = 0;

	/**	the outbound port used to call the service.							*/
	protected ManagementOutboundPort	NMportOut ;
	protected NodeCIntboundPort	NodePortIn ;
	/**	counting service invocations.										*/
	protected int						counter ;
	protected String adress;
	
	//stock the pairs connected with this pair and the outportNodeC of me 
	private ConcurrentHashMap<PeerNodeAddressI,NodeCOutboundPort> outPortsNodeC; 
	//stock the neighber pairs connected with this pair and the outportCMpair of me
	private ConcurrentHashMap<PeerNodeAddressI,ContentManagementCIOutbound> outPortsCM; 
	private ConcurrentHashMap<ContentTemplateI, ContentDescriptorI> contents;
	protected ContentManagementCIIntbound CMportIn;
     
	/**
	 * @param adress				adress of the component
	 * @param OutboundPort	URI of the URI getter outbound port.
	 * @throws Exception		<i>todo.</i>
	 */
	protected Pair(String adress, String NMoutboundPortURI)throws Exception {
		super(adress, 0, 1);
	
		//System.out.println(adress + " " + NMoutboundPortURI);
		this.adress = adress ;	
		this.NMportOut =new ManagementOutboundPort(NMoutboundPortURI, this) ;
		this.NMportOut.publishPort() ;
		this.NodePortIn = new NodeCIntboundPort(adress ,this);
		NodePortIn.publishPort();
		this.counter = 0 ;
		this.outPortsNodeC = new ConcurrentHashMap<PeerNodeAddressI,NodeCOutboundPort>();
		this.outPortsCM = new ConcurrentHashMap<PeerNodeAddressI,ContentManagementCIOutbound>();
		this.contents = new ConcurrentHashMap<ContentTemplateI, ContentDescriptorI>();
		String inportCM = "inportCM" + cpt++;
		this.CMportIn = new ContentManagementCIIntbound(inportCM, this);
		CMportIn.publishPort();

		if (AbstractCVM.isDistributed) {
			this.getLogger().setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.getLogger().setDirectory(System.getProperty("user.home")) ;
		}
		this.getTracer().setTitle("consumer") ;
		this.getTracer().setRelativePosition(1, 1) ;

		AbstractComponent.checkImplementationInvariant(this);
		AbstractComponent.checkInvariant(this);
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
	public PeerNodeAddressI connecte (PeerNodeAddressI p ) throws Exception
	{   
		if (p == null) {
			throw new Exception("Connection failed , pair is null");
		} else {
			System.out.println("\npair "+ p.getNodeUri() +" demande de connecter avec pair "+ this.getNodeUri()+ "\n");
			//do connect entre pair et pair en NodeCI
			counter++;
			String inportNode_voisin = ((Pair)p).NodePortIn.getPortURI();
			String outportN = "myOutPortNodeCIpair"+counter;
			NodeCOutboundPort NportOut = new NodeCOutboundPort(outportN, this);
			NportOut.publishPort();
			outPortsNodeC.put(p, NportOut);
			//System.out.println("\nthe uri of outport NodeCI  "+ p.getNodeUri() +" "+ NportOut.getPortURI());
			doPortConnection(NportOut.getPortURI(), inportNode_voisin, NodeC_conector.class.getCanonicalName());
			System.out.println("\nc'est ok " + p.getNodeUri() +" connecte avec "+ this.adress +" en NodeCI\n" );

			//do connect entre pair et pair en ContentManagementCI
			String inportCM_voisin = ((Pair)p).CMportIn.getPortURI();
			String outportCM = "myOutportCMpair" + UUID.randomUUID();
			ContentManagementCIOutbound CMportOut = new ContentManagementCIOutbound(outportCM,this );
			System.out.println( "\n"+ p.getNodeUri() +" demande de connecte avec "+ this.adress +" en ContentManagementCI\n");
			CMportOut.publishPort();
			outPortsCM.put(p, CMportOut);
			//System.out.println("inportCM : "+ inportCM +" of "+ this.adress);
			//this.doPortConnection(outport,inport, ContentManagementCIConector.class.getCanonicalName());
			doPortConnection(outportCM,inportCM_voisin, ContentManagementCIConector.class.getCanonicalName());
			System.out.println("\nc'est ok " + p.getNodeUri() +" connecte avec "+ this.adress +" en ContentManagementCI\n" );
		}
		 
		return this;
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
	public void disconnecte (PeerNodeAddressI p ) throws Exception
	{

		System.out.println("\n"+ p.getNodeUri()+" demande de disconnecter avec pair "+ this.getNodeUri()+ " en NodeCI\n");
		//get the outportNodeCI of this , which is connected with p
		NodeCOutboundPort NodeCportOut = outPortsNodeC.get(p);
		//System.out.println("ici");
		NodeCportOut.unpublishPort();
		outPortsNodeC.remove(p);
		this.doPortDisconnection(NodeCportOut.getPortURI());
		System.out.println("\nc'est ok "+ p.getNodeUri() +" disconnect  avec " + this.getNodeUri() +" en NodeCI\n");
		
		//do disconnection en CM
		System.out.println("\n"+ p.getNodeUri()+" demande de disconnecter avec pair "+ this.getNodeUri()+ " en ContentManagementCI\n");
		//get the outportCM of this , which is connected with p
		ContentManagementCIOutbound CMportOut = outPortsCM.get(p);
		//System.out.println("ici");
		CMportOut.unpublishPort();
		outPortsCM.remove(p);
		this.doPortDisconnection(CMportOut.getPortURI());
		System.out.println("\nc'est ok "+ p.getNodeUri() +" disconnect  avec " + this.getNodeUri() +" en ContentManagementCI\n");

		
	}
	

	public ContentDescriptorI find(ContentTemplateI ct  ,int hops )throws Exception{
		System.out.println("\nc'est dans find in pair "+this.adress);
		if (hops == 0) {
			return null;
		}
		// Cherche parmi ses propres contenus
		for (ContentDescriptorI content : this.contents.values()) {
			if (content.match(ct)) {
				return content;
			}
		}
		// Si le contenu n'est pas dans le nœud courant, demande à un autre pair
		Set<PeerNodeAddressI> neighbors = outPortsCM.keySet();
		if (neighbors == null) {
			return null;
		}
		for (PeerNodeAddressI pair : neighbors) {
			try {
				ContentDescriptorI content = ((Pair)pair).find(ct, hops - 1);
				if (content != null) {
					return content;
				}
			} catch (Exception e) {
				System.err.println("Failed to contact neighbor pair: " + pair.getNodeUri());
			}
		}
	
		return null;
		
	}
	

	//-------------------------------------------------------------------------
	// Component life-cycle
	//-------------------------------------------------------------------------

	
	
	@Override
	public void			execute() throws Exception
	{
		// application execution code (similar to a main method in Java) is
		// put here.

		this.logMessage("executing consumer component.") ;

		// Run the first service method invocation; the code of the method run
		// below will be executed asynchronously as a separate task, hence this
		// method execute will be free to finish its execution and free the
		// thread that is executing it.

		try {
			//do join
			Set<PeerNodeAddressI> liste = this.NMportOut.Join(this);
			if(liste.size() == 1) {
				System.out.println("the list of adress of pair in facade is empty!");
			}
			//do connection entre pair et pair en NodeCI
			for (PeerNodeAddressI p: liste ) {
				if(p != this){
					String inportNode_voisin = ((Pair)p).NodePortIn.getPortURI();
					String outportN = "myOutPortNodeCIpair"+counter;
					NodeCOutboundPort NportOut = new NodeCOutboundPort(outportN, this);
					NportOut.publishPort();
					outPortsNodeC.put(p, NportOut);
					//System.out.println("\nthe uri of outport NodeCI  "+ p.getNodeUri() +" "+ NportOut.getPortURI());
					doPortConnection(NportOut.getPortURI(), inportNode_voisin, NodeC_conector.class.getCanonicalName());
					System.out.println("\nc'est ok " + p.getNodeUri() +" connecte avec "+ this.adress +" en NodeCI\n" );

					String inportCM_voisin = ((Pair)p).CMportIn.getPortURI();
					String outportCM = "myOutportCMpair" + UUID.randomUUID();
					ContentManagementCIOutbound CMportOut = new ContentManagementCIOutbound(outportCM,this );
					System.out.println( "\n"+ p.getNodeUri() +" demande de connecte avec "+ this.adress +" en ContentManagementCI\n");
					CMportOut.publishPort();
					outPortsCM.put(p, CMportOut);
					//System.out.println("inportCM : "+ inportCM +" of "+ this.adress);
					//this.doPortConnection(outport,inport, ContentManagementCIConector.class.getCanonicalName());
					doPortConnection(outportCM,inportCM_voisin, ContentManagementCIConector.class.getCanonicalName());
					System.out.println("\nc'est ok " + p.getNodeUri() +" connecte avec "+ this.adress +" en ContentManagementCI\n" );


				}
			}

			// Create a ContentDescriptorI object
			ContentNodeAddressI ca = new ContentNodeAdress();
			ContentDescriptorI cd = new ContentDescriptor("my title", "my album title", 
											new HashSet<String>(), new HashSet<String>(), 1000L, ca);
			
			ContentDescriptorI result = ((ContentManagementCI) NMportOut).find(cd, 1);
			System.out.println("-------------find-------");
			System.out.println(result.toString());

			Thread.sleep(1000);

			//disconnect
			for (PeerNodeAddressI p:  outPortsNodeC.keySet()) {
				if(p != this){  
					NodeCOutboundPort NodeCportOut = outPortsNodeC.get(p);
					//System.out.println("ici");
					NodeCportOut.unpublishPort();
					outPortsNodeC.remove(p);
					this.doPortDisconnection(NodeCportOut.getPortURI());
					System.out.println("\nc'est ok "+ p.getNodeUri() +" disconnect  avec " + this.getNodeUri() +" en NodeCI  p\n");
					
					//do disconnection en CM
					//get the outportCM of this , which is connected with p
					ContentManagementCIOutbound CMportOut = outPortsCM.get(p);
					//System.out.println("ici");
					CMportOut.unpublishPort();
					outPortsCM.remove(p);
					this.doPortDisconnection(CMportOut.getPortURI());
					System.out.println("\nc'est ok "+ p.getNodeUri() +" disconnect  avec " + this.getNodeUri() +" en ContentManagementCI p\n");

				}
			} 
			while ( ! outPortsNodeC.isEmpty())
			{
				//wait
			}
			outPortsNodeC.clear();

			//leave
			NMportOut.leave(this);
			this.NMportOut.unpublishPort() ;
					

			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	

	@Override
	public String getNodeidentifier() throws Exception {
		return adress;
	}


	@Override
	public Boolean isfacade() throws Exception {
		return false;
	}


 	@Override
	public Boolean ispeer() throws Exception {
		return true;
	}


	
	@Override
	public String getNodeUri() throws Exception {
		return adress;
	}
}
