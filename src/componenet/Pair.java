package componenet;



import java.util.Set;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.NodeCI;
import interfaces.PeerNodeAddressI;
import ports.ManagementOutboundPort;

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
	/**	counting service invocations.										*/
	protected int						counter ;
	protected String NodeUri;
     
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
						Set<PeerNodeAddressI> liste =	 ((Pair)this.getTaskOwner()).uriGetterPort.Join((Pair)this.getTaskOwner());
						
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


	public PeerNodeAddressI connect(PeerNodeAddressI p) throws Exception {
		System.out.println(" conexion ");
		return p; 
}


	public void disconnect(PeerNodeAddressI p) throws Exception {
		System.out.println(" disconect ");
	}



	@Override
	public String getNodeUri() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	

}