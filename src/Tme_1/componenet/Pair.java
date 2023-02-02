package Tme_1.componenet;
import  Tme_1.interfaces.*;

import java.util.HashSet;
import java.util.Set;

import Tme_1.interfaces.PeerNodeAddressI;
import Tme_1.ports.ManagementOutboundPort;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.examples.basic_cs.interfaces.URIConsumerCI;
import fr.sorbonne_u.components.examples.basic_cs.ports.URIConsumerOutboundPort;

/**
 * @author lyna & shuhan 
 *
 */
@RequiredInterfaces(required = {PeerNodeAddressI.class})

public class Pair  extends AbstractComponent {
	
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
		this.uriGetterPort.localPublishPort() ;
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

	
	
	/**   
	* @Function: Pair.java
	* @Description: 
	*
	* @param: PeerNodeAddressI p
	* @return：Set<PeerNodeAddressI>
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 30 janv. 2023 20:34:57 
	*
	* 
	*/
	//Ilfaut coder la fonction Join 
	public Set<PeerNodeAddressI>  Join   (PeerNodeAddressI p) 
	throws Exception{
	   System.out.print("c'est ok join ");
	   return  new HashSet<PeerNodeAddressI>(); 
	}
	
	/**   
	* @Function: Pair.java
	* @Description: 
	*
	* @param: PeerNodeAddressI p
	* @return：void
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 30 janv. 2023 20:35:22 
	*
	* 
	*/
	public void leave (PeerNodeAddressI p)
	throws Exception{
	  System.out.print("c'est ok leave  ");
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
						((Pair)this.getTaskOwner()).Join(uriGetterPort  );
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}) ;
	}



	

}
