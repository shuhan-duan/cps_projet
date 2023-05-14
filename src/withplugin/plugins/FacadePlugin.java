package withplugin.plugins;



import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import classes.ApplicationNodeAdress;
import connector.MyClientConnector;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.ComponentI;

import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentNodeAddressI;
import interfaces.ContentTemplateI;
import interfaces.FacadeContentManagementCI;
import interfaces.MyCMI;
import interfaces.MyThreadServiceI;
import interfaces.NodeManagementCI;
import ports.ContentManagementCIOutbound;
import ports.MyClientCOutbound;
import withplugin.ports.FacadeContentManagementCInboundPlugin;



public class FacadePlugin extends AbstractPlugin implements MyCMI,FacadeContentManagementCI{
	
		private static final long serialVersionUID = 1L;
	
		// -------------------------------------------------------------------------
		// Plug-in variables and constants
		// -------------------------------------------------------------------------	
		private ApplicationNodeAdress		adress;
	    
	  	//On Stock "ContentManagementCIOutbound" pour chaque racine pour faire appel a find et match sur pair 
	  	private ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound> rootsOutPortsCM;
	  	
	  	
	  	private FacadeContentManagementCInboundPlugin inPortFCM; //connect with client(find)and roots(acceptFound)
	  	private MyClientCOutbound outPortFCM; //call acceptFound and acceptMatched in client
	  	
	  	private String inPortFCMclientURI ;
	  	private static AtomicBoolean resultReturned = new AtomicBoolean(false);
	  	
  	
	  	// -------------------------------------------------------------------------
	 	// Life cycle
	 	// -------------------------------------------------------------------------
	 	/**
		 * 
		 * @param adress
		 * @param inPortFCMclientURI
		 * @throws Exception
		 * @author shuna & lyna 
		 */
	  	public FacadePlugin(ApplicationNodeAdress adress ,String inPortFCMclientURI)throws Exception {
		    super();
		    
		    setPluginURI(AbstractPort.generatePortURI());
		    			
			this.rootsOutPortsCM =  new ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound>();
			this.adress = adress;
			
			this.inPortFCMclientURI = inPortFCMclientURI;
			
		  }
  	
		@Override
		public void initialise() throws Exception {
		     super.initialise();     
		     // Create the inbound port FCM 
		     inPortFCM = new FacadeContentManagementCInboundPlugin(adress.getContentManagementURI(),
		    		 this.getOwner(), 
		    		 this.getPluginURI(),
		    		 ((MyThreadServiceI)this.getOwner()).
		    		 get_CM_THREAD_POOL_URI());
		     inPortFCM.publishPort();

		     // the outport FCM
		     outPortFCM = new MyClientCOutbound(this.getOwner());
		     outPortFCM.publishPort();
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

			super.finalise();
		}
	
		/**
		 * @see fr.sorbonne_u.components.AbstractPlugin#uninstall()
		 */
		@Override
		public void			uninstall() throws Exception
		{
			
			inPortFCM.unpublishPort();
			
		}
		

		// -------------------------------------------------------------------------
		// Plug-in services implementation
		// -------------------------------------------------------------------------
		
		/**
		 * 
		 * @param cd
		 * @param hops
		 * @param requester
		 * @param requestURI
		 * @throws Exception
		 * @author shuhan & lyna 
		 */
		@Override
		public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI ) throws Exception {
			Set<ContentNodeAddressI> racineSet = rootsOutPortsCM.keySet();			
			//set the requester initial
			if (requester == null) {
			    requester = this.adress;
			}
			for (ContentNodeAddressI root : racineSet) {
				requestURI = root.getNodeidentifier();
				
				rootsOutPortsCM.get(root).find(cd, hops, requester, requestURI );
			}
			
		}
		/**
		 * 
		 * @param cd
		 * @param matched
		 * @param hops
		 * @param requester
		 * @param requestURI
		 * @throws Exception
		 * @author shuan & lyna
		 */
		@Override
		public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester,
				String requestURI) throws Exception {
			Set<ContentNodeAddressI> racineSet = rootsOutPortsCM.keySet();
			//set the requester initial
			if (requester == null) {
			    requester = this.adress;
			}
			for (ContentNodeAddressI root : racineSet) {
				requestURI = root.getNodeidentifier();
				rootsOutPortsCM.get(root).match(cd, matched, hops, requester, requestURI);
			}
			
		}
				
		/**
		 * 
		 * @param found
		 * @param requsetURI
		 * @throws Exception
		 * @author shuhan
		 */
		public void acceptFound(ContentDescriptorI found, String requsetURI) throws Exception {
	        if (resultReturned.compareAndSet(false, true)) {
	            System.out.println("has returned the result to "+ adress.getNodeidentifier());

	            this.getOwner().doPortConnection(outPortFCM.getPortURI(),
	                    inPortFCMclientURI,
	                    MyClientConnector.class.getCanonicalName());

	            if (found != null) {
	                outPortFCM.foundRes(found, requsetURI);
	            }
	        } else {
	            return;
	        }
	    }
	/**
	 * 
	 * @param matched
	 * @param requsetURI
	 * @throws Exception
	 * @author shuhan
	 */
		public void acceptMatched(Set<ContentDescriptorI> matched ,String requsetURI) throws Exception {
			this.getOwner().doPortConnection(outPortFCM.getPortURI(),
                    inPortFCMclientURI,
                    MyClientConnector.class.getCanonicalName());
			
			if (matched != null) {
				outPortFCM.acceptMatched(matched, requsetURI);
			}
		}
		
		// -------------------------------------------------------------------------
		// Auxiliary Functions to use in Facade
		// -------------------------------------------------------------------------
		/**
		 * 
		 * @param address
		 * @param outPortCM
		 * @author shuhan 
		 */
		public void addRootOutPortsCM(ContentNodeAddressI address, ContentManagementCIOutbound outPortCM ) {
			this.rootsOutPortsCM.put(address, outPortCM);
		}
		
		public ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound> getRootOutPortsCM() {
			return this.rootsOutPortsCM;
		}
		/**
		 * 
		 * @param address
		 * @author shuhan
		 */
		public void removeRootOutPortsCM(ContentNodeAddressI address) {
			this.rootsOutPortsCM.remove(address);
		}
}
