package withplugin.plugins;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import classes.ApplicationNodeAdress;
import connector.FacadeContentManagementConector;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentNodeAddressI;
import interfaces.ContentTemplateI;
import interfaces.MyCMI;
import interfaces.MyFCMI;
import interfaces.MyThreadServiceI;
import interfaces.NodeManagementCI;
import ports.ContentManagementCIOutbound;
import ports.FacadeContentManagementCOutbound;
import ports.NodeCOutboundPort;
import ports.NodeManagementOutboundPort;
import withplugin.ports.ContentManagementCIIntboundPlugin;
import withplugin.ports.FacadeContentManagementCInboundPlugin;



public class FacadePlugin extends AbstractPlugin implements MyCMI ,MyFCMI{
	
		private static final long serialVersionUID = 1L;
	
		// -------------------------------------------------------------------------
		// Plug-in variables and constants
		// -------------------------------------------------------------------------	
		private ApplicationNodeAdress		adress;
	    
	  	//On Stock "ContentManagementCIOutbound" pour chaque racine pour faire appel a find et match sur pair 
	  	private ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound> rootsOutPortsCM;
	  	
	  	
	  	private FacadeContentManagementCInboundPlugin inPortFCM; //connect with client(find)and roots(acceptFound)
	  	private FacadeContentManagementCOutbound outPortFCM; //call acceptFound and acceptMatched in client
	  	
	  	private String inPortFCMclientURI ;
	  	
	  	private boolean flag;
	  	private final Object lock = new Object(); //lock for flag
  	
	  	// -------------------------------------------------------------------------
	 	// Life cycle
	 	// -------------------------------------------------------------------------
	 	
	  	public FacadePlugin(ApplicationNodeAdress adress)throws Exception {
		    super();
		    
		    setPluginURI(AbstractPort.generatePortURI());
		    			
			this.rootsOutPortsCM =  new ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound>();
			this.adress = adress;
			this.flag = false;
			
			
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
		     outPortFCM = new FacadeContentManagementCOutbound(this.getOwner());
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

		@Override
		public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
			Set<ContentNodeAddressI> racineSet = rootsOutPortsCM.keySet();
			
			//set the requester initial
			if (requester == null) {
			    requester = this.adress;
			}
			for (ContentNodeAddressI root : racineSet) {
				requestURI = root.getNodeidentifier();
				rootsOutPortsCM.get(root).find(cd, hops, requester, requestURI);
			}
			
		}
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
				
				
		public void acceptFound(ContentDescriptorI found, String requsetURI) throws Exception {
			//Use locks to ensure that only one result of find is returned
		    synchronized (lock) {
		        if (!flag) {
		            this.getOwner().doPortConnection(outPortFCM.getPortURI(), inPortFCMclientURI, FacadeContentManagementConector.class.getCanonicalName());

		            if (found != null) {
		                outPortFCM.acceptFound(found, requsetURI);
		                flag = true;
		            }
		        }
		    }
		}
		
		public void acceptMatched(Set<ContentDescriptorI> matched ,String requsetURI) throws Exception {
//			this.getOwner().doPortConnection(outPortFCM.getPortURI(), inPortFCMclientURI, FacadeContentManagementConector.class.getCanonicalName());
//			if (matched != null) {
//				outPortFCM.acceptMatched(matched, requsetURI);
//			}
		}
		
		// -------------------------------------------------------------------------
		// Auxiliary Functions to use in Facade
		// -------------------------------------------------------------------------
		public void addRootOutPortsCM(ContentNodeAddressI address, ContentManagementCIOutbound outPortCM ) {
			this.rootsOutPortsCM.put(address, outPortCM);
		}
		
		public ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound> getRootOutPortsCM() {
			return this.rootsOutPortsCM;
		}
		
		public void removeRootOutPortsCM(ContentNodeAddressI address) {
			this.rootsOutPortsCM.remove(address);
		}
}
