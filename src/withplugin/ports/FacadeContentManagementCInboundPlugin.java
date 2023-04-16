package withplugin.ports;

import java.util.Set;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;

import interfaces.ContentTemplateI;
import interfaces.FacadeContentManagementCI;
import interfaces.MyCMI;
import interfaces.MyFCMI;

public class FacadeContentManagementCInboundPlugin extends AbstractInboundPort implements  FacadeContentManagementCI {
	
	public FacadeContentManagementCInboundPlugin(String pluginURI ,ComponentI owner)
			throws Exception {
		super(FacadeContentManagementCI.class, owner, pluginURI, null);
		
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
	@Override
	public void acceptFound(ContentDescriptorI found, String requsetURI) throws Exception {
		
		this.getOwner().runTask(
				owner -> {
					try {
						((MyFCMI)owner).acceptFound(found, requsetURI);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}
	@Override
	public void acceptMatched(Set<ContentDescriptorI> matched, String requsetURI) throws Exception {
		this.getOwner().runTask(
				owner -> {
					try {
						((MyFCMI)owner).acceptMatched(matched, requsetURI);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}

}
