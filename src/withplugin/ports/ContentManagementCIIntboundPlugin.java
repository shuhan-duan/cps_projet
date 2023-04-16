package withplugin.ports;

import java.util.Set;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentTemplateI;
import interfaces.MyCMI;


public class ContentManagementCIIntboundPlugin  extends AbstractInboundPort  implements  ContentManagementCI {

	public ContentManagementCIIntboundPlugin(ComponentI owner ,String plugin)
			throws Exception {
		super(ContentManagementCI.class, owner, plugin, null);
		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
		 this.getOwner().runTask(
	                new AbstractComponent.AbstractTask(this.getPluginURI()) {
	                    @Override
	                    public void run() {
	                        try {
	                            ((MyCMI) this.getTaskProviderReference()).find(cd, hops, requester,requestURI);
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                    }
	                });
		
	}

	@Override
	public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester,
			String requestURI) throws Exception {
		  this.getOwner().runTask(
	                new AbstractComponent.AbstractTask(this.getPluginURI()) {
	                    @Override
	                    public void run() {
	                        try {
	                            ((MyCMI) this.getTaskProviderReference()).match(cd, matched, hops, requester,requestURI);
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                    }
	                });
		
	}

	

}
