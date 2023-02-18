package componenet;



import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import classes.ContentDescriptor;
import connector.ContentManagementCIConector;
import connector.ManagementConnector;
import connector.NodeC_conector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.components.ports.InboundPortI;
import interfaces.ContentDescriptorI;
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


	/**	the outbound port used to call the service.							*/
	protected ManagementOutboundPort	NMportOut ;
	protected NodeCIntboundPort	NoPortIn ;
	/**	counting service invocations.										*/
	protected int						counter ;
	protected String adress;
	//private ContentNodeadress contentNodeAddress;
	private ConcurrentHashMap<PeerNodeAddressI,NodeCOutboundPort> outPortsNodeC; 
	protected ContentDescriptorI contentDescriptor;
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
		this.NoPortIn = new NodeCIntboundPort(adress ,this);
		NoPortIn.publishPort();
		this.counter = 0 ;
		this.outPortsNodeC = new ConcurrentHashMap<PeerNodeAddressI,NodeCOutboundPort>();
		this.contentDescriptor = null ;
		String inportCM = "inportCM";
		this.CMportIn = new ContentManagementCIIntbound(inportCM, this);

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
		String inportN = this.NoPortIn.getPortURI();
		String outportN_voisin = AbstractOutboundPort.generatePortURI();
		NodeCOutboundPort NportOut_voisin = new NodeCOutboundPort(outportN_voisin,(Pair)p );
		NportOut_voisin.publishPort();
		System.out.println( p.getNodeUri() +" demande de connecte avec "+ this.adress +" en NodeCI");
		outPortsNodeC.put(p, NportOut_voisin);
		this.doPortConnection(outportN_voisin, inportN, NodeC_conector.class.getCanonicalName());
		System.out.println("c'est ok " + p.getNodeUri() +" connecte avec "+ this.adress +" en NodeCI" );
		
		String inportCM = this.CMportIn.getPortURI();
		String outportCM_voisin = AbstractOutboundPort.generatePortURI();
		ContentManagementCIOutbound CMportOut_voisin = new ContentManagementCIOutbound(outportCM_voisin,(Pair)p );
		System.out.println( p.getNodeUri() +" demande de connecte avec "+ this.adress +" en ContentManagementCI");
		CMportOut_voisin.publishPort();
		//System.out.println("inportCM : "+ inportCM +" of "+ this.adress);
		//this.doPortConnection(outport,inport, ContentManagementCIConector.class.getCanonicalName());
		doPortConnection(outportCM_voisin,inportCM, ContentManagementCIConector.class.getCanonicalName());
		System.out.println("c'est ok " + p.getNodeUri() +" connecte avec "+ this.adress +" en ContentManagementCI" );
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
		NodeCOutboundPort port_sortant = outPortsNodeC.get(p);
		port_sortant.unpublishPort();
		outPortsNodeC.remove(p);
		this.doPortDisconnection(port_sortant.getPortURI());
		System.out.println("c'est ok "+  this.getNodeUri() +" disconnect  avec " + port_sortant.getPortURI());
	}
	
	public ContentDescriptorI find(ContentTemplateI cd  ,int hops )throws Exception{
		hops -- ; 
		System.out.println("find in pair "+this.adress);
		if (hops != 0)
		{
			 for (PeerNodeAddressI p : outPortsNodeC.keySet()) {
				
				return ((Pair) p).find(cd , hops );
			 }
		}else 
		{ 
			return contentDescriptor;
		}
		return null;
	}
	

	//-------------------------------------------------------------------------
	// Component life-cycle
	//-------------------------------------------------------------------------

	/**
	 * a component is always started by calling this method, so intercept the
	 * call and make sure the task of the component is executed.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true				// no more preconditions.
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
	 * @see fr.sorbonne_u.components.AbstractComponent#start()
	 */
	@Override
	public void			start() throws ComponentStartException
	{
		super.start() ;
		// initialisation code can be put here; do not however call any
		// services of this component or of another component as they will
		// not have started yet, hence not able to execute any incoming calls.
	}

	

	/**
	 * @see fr.sorbonne_u.components.AbstractComponent#finalise()
	 */
	@Override
	public void			finalise() throws Exception
	{
		this.logMessage("stopping consumer component.") ;
		this.printExecutionLogOnFile("consumer");
		// This is the place where to clean up resources, such as
		// disconnecting ports and unpublishing outbound ports that
		// will be destroyed when shutting down.
		// In static architectures like in this example, ports can also
		// be disconnected by the finalise method of the component
		// virtual machine.
		
		
		//disconnect
		/* 
		System.out.println("*************");
		for (PeerNodeAddressI pAddressI:  outPortsNodeC.keySet()) {
			if(pAddressI != this){  
				this.disconnecte(pAddressI);			
			}
		} */
		this.NMportOut.unpublishPort() ;
		super.finalise();
	}
	
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
		//System.out.println(this.NMportOut.getPortURI() + " " + this.NMportOut.connected());
		Set<PeerNodeAddressI> liste = this.NMportOut.Join(this);

		//connect
		for (PeerNodeAddressI pAddressI: liste ) {
			if(pAddressI != this){
				String inport = pAddressI.getNodeidentifier();
				String outport = AbstractOutboundPort.generatePortURI();
				NodeCOutboundPort port_sortant = new NodeCOutboundPort(outport,this );
				port_sortant.publishPort();
				outPortsNodeC.put(pAddressI, port_sortant);
				System.out.println("-------------");
				System.out.println(port_sortant.getPortURI());
				this.doPortConnection(port_sortant.getPortURI(), inport, NodeC_conector.class.getCanonicalName());
				port_sortant.connecte(pAddressI);
			}
		}
		//Thread.sleep(1000);

		//leave
		//this.NMportOut.leave(this);

		//find
		this.find(contentDescriptor, 2);
		
	}


	@Override
	public String getNodeidentifier() throws Exception {
		return adress;
	}


	@Override
	public Boolean isfacade() throws Exception {
		return null;
	}


 	@Override
	public Boolean ispeer() throws Exception {
		return null;
	}


	
	@Override
	public String getNodeUri() throws Exception {
		return adress;
	}
}
