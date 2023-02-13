package componenet;



import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import classes.ContentNodeAdress;
import connector.ManagementConnector;
import connector.NodeC_conector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.components.ports.InboundPortI;
import interfaces.NodeCI;
import interfaces.PeerNodeAddressI;
import ports.ManagementOutboundPort;
import ports.NodeCIntboundPort;
import ports.NodeCOutboundPort;

/**
 * @author lyna & shuhan 
 *
 */
@RequiredInterfaces(required = {PeerNodeAddressI.class})

public class Pair  extends AbstractComponent implements PeerNodeAddressI{
	
	/**   
	* @Function: Pair.java
	* @Description: 
	*
	* @param: NodeUri
	* @param: nbThreads
	* @version: 
	* @author: lyna & shuhan 
	* @date: 30 janv. 2023 20:34:23 
	*/
	protected final static int	N = 2 ;


	/**	the outbound port used to call the service.							*/
	protected ManagementOutboundPort	uriGetterPort ;
	protected NodeCIntboundPort	port_entrant ;
	/**	counting service invocations.										*/
	protected int						counter ;
	protected String NodeUri;
	private ContentNodeAdress contentNodeAddress;

	private ConcurrentHashMap<PeerNodeAddressI,NodeCOutboundPort> outPorts; 
     
	/**
	 * @param uri				URI of the component
	 * @param OutboundPort	URI of the URI getter outbound port.
	 * @throws Exception		<i>todo.</i>
	 */
	protected Pair(String NodeUri, String outboundPortURI)throws Exception {
		super(NodeUri, 0, 1);
	
		//System.out.println(NodeUri + " " + outboundPortURI);
		this.NodeUri = NodeUri ;	
		this.uriGetterPort =new ManagementOutboundPort(outboundPortURI, this) ;
		this.uriGetterPort.publishPort() ;
		this.port_entrant = new NodeCIntboundPort(NodeUri ,this);
		port_entrant.publishPort();
		this.counter = 0 ;
		this.outPorts = new ConcurrentHashMap<PeerNodeAddressI,NodeCOutboundPort>();

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
		String inport = p.getNodeidentifier();
		String outport = AbstractOutboundPort.generatePortURI();
		NodeCOutboundPort port_sortant = new NodeCOutboundPort(outport,this );
		port_sortant.publishPort();
		outPorts.put(p, port_sortant);
		this.doPortConnection(outport, inport, NodeC_conector.class.getCanonicalName());
		System.out.println("c'est ok " + this.NodeUri  +" connecte avec "+ p.getNodeUri());
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
	public void disconnecte (PeerNodeAddressI p ) throws Exception
	{
		NodeCOutboundPort port_sortant = outPorts.get(p);
		port_sortant.unpublishPort();
		outPorts.remove(p);
		this.doPortDisconnection(port_sortant.getPortURI());
		System.out.println("c'est ok "+  this.getNodeUri() +" disconnect  avec " + port_sortant.getPortURI());
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
		System.out.println("*************");
		for (PeerNodeAddressI pAddressI:  outPorts.keySet()) {
			if(pAddressI != this){  
				this.disconnecte(pAddressI);			
			}
		} 
		this.uriGetterPort.unpublishPort() ;
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
		//System.out.println(this.uriGetterPort.getPortURI() + " " + this.uriGetterPort.connected());
		Set<PeerNodeAddressI> liste = this.uriGetterPort.Join(this);
		//connect
		for (PeerNodeAddressI pAddressI: liste ) {
			if(pAddressI != this){
				String inport = pAddressI.getNodeidentifier();
				String outport = AbstractOutboundPort.generatePortURI();
				NodeCOutboundPort port_sortant = new NodeCOutboundPort(outport,this );
				port_sortant.publishPort();
				outPorts.put(pAddressI, port_sortant);
				System.out.println("-------------");
				System.out.println(port_sortant.getPortURI());
				this.doPortConnection(port_sortant.getPortURI(), inport, NodeC_conector.class.getCanonicalName());
				port_sortant.connecte(pAddressI);
			}
		}
		//Thread.sleep(1000);
		//leave
		this.uriGetterPort.leave(this);
		
	}


	@Override
	public String getNodeidentifier() throws Exception {
		return port_entrant.getPortURI();
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
		return NodeUri;
	}
}
