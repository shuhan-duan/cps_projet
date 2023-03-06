package tests;


import componenet.Client;
import componenet.Facade;
import componenet.Pair;
import connector.NodeManagementConnector;
import connector.NodeConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.helpers.CVMDebugModes;


/**
 * @author lyna & shuhan 
 *
 */
public class CVM extends AbstractCVM{
	/** URI of the facade component (convenience).						*/
	protected static final String	ContentManagementInboudPort = "inportCMfacade";

	protected static final String	ContentManagementOutboudPort = "outportCMclient";

	/** URI of the provider outbound port (simplifies the connection).		*/
	protected static final String	NodeManagementOutboundPort = "outportNMpair";

	/** URI of the consumer inbound port (simplifies the connection).		*/
	protected static final String	NodeManagemenInboundPort = "inportNMfacade";
	protected final int NB_PEER = 10;

	/**   
	* @Function: CVM.java
	* @Description: 
	*
	* @param:
	* @version: 
	* @author: lyna & shuhan
	* @date: 31 janv. 2023 21:31:56 
	*/
	public CVM() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void			deploy() throws Exception
	{
		assert	!this.deploymentDone() ;

		// ---------------------------------------------------------------------
		// Creation phase
		// ---------------------------------------------------------------------

		

		
		// create the component pairs
		for (int i = 0; i < NB_PEER  ; i++) {
			AbstractComponent.createComponent(
							Pair.class.getCanonicalName(),
							new Object[]{
									NodeManagementOutboundPort+i,
									NodeManagemenInboundPort, i });
		}
		System.out.println("\nCreate Composant pairs OK ");

		// create the component facade
			AbstractComponent.createComponent(
					Facade.class.getCanonicalName(),
					new Object[]{ContentManagementInboudPort,
							NodeManagemenInboundPort});
		System.out.println("\nCreate Composant Facade OK ");

		// create the component client
		AbstractComponent.createComponent(
				Client.class.getCanonicalName(),
				new Object[]{ContentManagementInboudPort,
						ContentManagementOutboudPort});
		System.out.println("\nCreate Composant client OK ");


		
		

		super.deploy();
		assert	this.deploymentDone();
	}


	/**   
	* @Function: CVM.java
	* @Description: 
	*
	* @param: String[] args
	* @return：void
	* @throws：
	*
	* @version: v1.0.0
	* @author: lyna & shuhan
	* @date: 2 févr. 2023 21:55:34 
	*
	* 
	*/
	public static void		main(String[] args)
	{
		try {
			// Create an instance of the defined component virtual machine.
			CVM a = new CVM();
			// Execute the application.
			a.startStandardLifeCycle(40000L);

			// Give some time to see the traces (convenience).
			Thread.sleep(1000L);

			// Simplifies the termination (termination has yet to be treated
			// properly in BCM).
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
