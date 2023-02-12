package componenet;



import java.util.Set;

import classes.ContentNodeAdress;
import connector.ManagementConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.PeerNodeAddressI;
import ports.ManagementOutboundPort;
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
	protected NodeCOutboundPort	port_sortant ;
	/**	counting service invocations.										*/
	protected int						counter ;
	protected String NodeUri;
	protected NodeCOutboundPort	 uriNodeCI;
	private ContentNodeAdress contentNodeAddress;
	private Set<PeerNodeAddressI> liste;
     
	/**
	 * @param uri				URI of the component
	 * @param OutboundPort	URI of the URI getter outbound port.
	 * @throws Exception		<i>todo.</i>
	 */
	protected Pair(String NodeUri, String outboundPortURI)throws Exception {
		super(NodeUri, 0, 1);
		this.uriGetterPort =new ManagementOutboundPort(outboundPortURI, this) ;
		this.uriGetterPort.publishPort() ;
		this.counter = 0 ;
		this.uriNodeCI = new NodeCOutboundPort(NodeUri,this);

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
		this.port_sortant= new NodeCOutboundPort("Sortant-uri",this);
		this.port_sortant.publishPort();
		this.doPortConnection(port_sortant.getPortURI(),p.getNodeUri(),ManagementConnector.class.getCanonicalName());
		
		for(PeerNodeAddressI peer: this.liste) {
			this.port_sortant= new NodeCOutboundPort("Sortant-uri",this);
			this.port_sortant.publishPort();
			this.doPortConnection(port_sortant.getPortURI(),p.getNodeUri(),ManagementConnector.class.getCanonicalName());
			this.port_sortant.connect(peer);
			// creer equals pour les string => getURI()
		}
		
		System.out.print("c'est ok connecte  ");
		return this.contentNodeAddress;
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
	public void disconect (PeerNodeAddressI p ) throws Exception
	{
		System.out.print("c'est ok disconnect  ");
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
		this.logMessage("starting consumer component.") ;
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
		this.uriGetterPort.unpublishPort() ;

		// This called at the end to make the component internal
		// state move to the finalised state.
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
		this.runTask(
			new AbstractComponent.AbstractTask() {
				@Override
				public void run() {
					try { 
						((Pair)this.getTaskOwner()).uriGetterPort.Join((Pair)this.getTaskOwner());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}) ;
	}






	@Override
	public String getNodeidentifier() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public Boolean isfacade() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public Boolean ispeer() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}






	@Override
	public String getNodeUri() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	

}
