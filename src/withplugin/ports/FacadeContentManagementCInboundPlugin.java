package withplugin.ports;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;

import interfaces.ContentTemplateI;
import interfaces.FacadeContentManagementCI;
import interfaces.MyCMI;

public class FacadeContentManagementCInboundPlugin extends AbstractInboundPort implements  FacadeContentManagementCI {
	/**
	 * 
	 * @param uri
	 * @param owner
	 * @param pluginURI
	 * @param executorServiceURI
	 * @throws Exception
	 * @author shuhan & lyna
	 */
	public FacadeContentManagementCInboundPlugin(String uri, ComponentI owner ,String pluginURI ,String executorServiceURI)
			throws Exception {
		super(uri,FacadeContentManagementCI.class, owner, pluginURI, executorServiceURI);
		
	}
	
	
	private static final long serialVersionUID = 1L;
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
	public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester
			, String requestURI ) throws Exception {
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
	/**
	 * 
	 * @param cd
	 * @param matched
	 * @param hops
	 * @param requester
	 * @param requestURI
	 * @throws Exception
	 * @author lyna & shuhan
	 */
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
	/**
	 * @author lyna & shuhan
	 * @param found
	 * @param requsetURI
	 * @throws Exception
	 */
	@Override
	public void acceptFound(ContentDescriptorI found, String requsetURI) throws Exception {
		
		this.getOwner().runTask(
				new AbstractComponent.AbstractTask(this.getPluginURI()) {
                    @Override
                    public void run() {
                        try {
                            ((FacadeContentManagementCI) this.getTaskProviderReference()).acceptFound(found, requsetURI);;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
		
	}
	/**
	 * 
	 * @param matched
	 * @param requsetURI
	 * @throws Exception
	 * @author lyna & shuhan
	 */
	@Override
	public void acceptMatched(Set<ContentDescriptorI> matched, String requsetURI) throws Exception {
		this.getOwner().runTask(
				new AbstractComponent.AbstractTask(this.getPluginURI()) {
                    @Override
                    public void run() {
                        try {
                            ((FacadeContentManagementCI) this.getTaskProviderReference()).acceptMatched(matched, requsetURI);;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
		
	}

}
