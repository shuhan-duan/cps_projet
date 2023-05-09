package withplugin.plugins;


import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import classes.ApplicationNodeAdress;
import connector.FacadeContentManagementConector;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.ComponentI;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentNodeAddressI;
import interfaces.ContentTemplateI;
import interfaces.MyCMI;
import interfaces.MyFCMI;
import interfaces.NodeManagementCI;
import ports.ContentManagementCIOutbound;
import ports.FacadeContentManagementCOutbound;
import withplugin.ports.ContentManagementCIIntboundPlugin;
import withplugin.ports.FacadeContentManagementCInboundPlugin;



public class FacadePlugin extends AbstractPlugin implements MyCMI ,MyFCMI{
	
	private static final long serialVersionUID = 1L;

	// -------------------------------------------------------------------------
	// Plug-in variables and constants
	// -------------------------------------------------------------------------
	
	
	public static int cpt_facade = 0;
	
    protected ApplicationNodeAdress		adress;
    
    //stock the outports of facade and the racine pair connected with it
  	//On Stock "ContentManagementCIOutbound" pour chaque pair pour faire appel a find et match sur pair 
  	public ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound> outPortsCM;
  	

  	
  	protected ContentManagementCIIntboundPlugin CMportIn;
  	protected FacadeContentManagementCInboundPlugin fCMportIn; //connect avec racine
  	protected FacadeContentManagementCOutbound fCMportOutbound; //connect avec client
  	
  	protected String fCMInbountPortClient ;
  	
  	// -------------------------------------------------------------------------
 	// Life cycle
 	// -------------------------------------------------------------------------
 	
  	public FacadePlugin(String ContentManagementInboudPort,	String 	NodeManagemenInboundPort ,
  			String FCMInbountPortClient, String FacadeCMInPortFacade)throws Exception {
		    super();
		    
		    setPluginURI(AbstractPort.generatePortURI());
		    			
			this.outPortsCM =  new ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound>();
		
			
			
			
		  }
  	
  	@Override
	  public void initialise() throws Exception {
		     super.initialise();
		     
		
		fCMportIn = new FacadeContentManagementCInboundPlugin(this.getPluginURI(),this.getOwner());
		fCMportIn.publishPort();
		
		CMportIn = new ContentManagementCIIntboundPlugin(this.getOwner(),this.getPluginURI());
		CMportIn.publishPort();
		
		fCMportOutbound = new FacadeContentManagementCOutbound("myFCMfacade", this.getOwner());
		fCMportOutbound.publishPort();
		
		this.adress = new ApplicationNodeAdress("facade-plugin", null, CMportIn.getPortURI(), fCMportIn.getPortURI());
		
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
			
			fCMportIn.unpublishPort();
			
			CMportIn.unpublishPort();
			
			fCMportOutbound.unpublishPort();	
			
		}
		

		// -------------------------------------------------------------------------
		// Plug-in services implementation
		// -------------------------------------------------------------------------
		

				public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) throws Exception{}

				@Override
				public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
					Set<ContentNodeAddressI> racineSet = outPortsCM.keySet();
					requester =this.adress;
					for (ContentNodeAddressI root : racineSet) {
						requestURI = root.getNodeidentifier();
						outPortsCM.get(root).find(cd, hops, requester, requestURI);
					}
					
				}
				@Override
				public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester,
						String requestURI) throws Exception {
					Set<ContentNodeAddressI> racineSet = outPortsCM.keySet();
					requester =this.adress;
					for (ContentNodeAddressI root : racineSet) {
						requestURI = root.getNodeidentifier();
						outPortsCM.get(root).match(cd, matched, hops, requester, requestURI);
					}
					
				}
				
				
				
				
				
				public void acceptFound(ContentDescriptorI found, String requsetURI) throws Exception {
					
					this.getOwner().doPortConnection(fCMportOutbound.getPortURI(), fCMInbountPortClient, FacadeContentManagementConector.class.getCanonicalName());
					
					if (found != null) {
						fCMportOutbound.acceptFound(found, requsetURI);
					}		
				}
				
				public void acceptMatched(Set<ContentDescriptorI> matched ,String requsetURI) throws Exception {
					this.getOwner().doPortConnection(fCMportOutbound.getPortURI(), fCMInbountPortClient, FacadeContentManagementConector.class.getCanonicalName());
					if (matched != null) {
						fCMportOutbound.acceptMatched(matched, requsetURI);
					}
				}

}
