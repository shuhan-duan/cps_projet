package withplugin.ports;

import java.util.Set;



import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentNodeAddressI;
import interfaces.NodeCI;
import withplugin.plugins.PairPlugin;

public class NodeCIntboundPortForPlugin extends AbstractInboundPort  implements NodeCI {
private static final long serialVersionUID = 1L;
	
	public NodeCIntboundPortForPlugin(String uri, ComponentI owner ,String plugin , String executorServiceURI)
			throws Exception {
	    super(uri,NodeCI.class, owner, plugin, executorServiceURI);
	}

	@Override
	public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) throws Exception {
		 this.getOwner().runTask(
			        new AbstractComponent.AbstractTask(this.getPluginURI()) {
			          @Override
			          public void run() {
			            try {
			              ((PairPlugin) this.getTaskProviderReference()).probe( facade, remainghops, requestURI);
			            } catch (Exception e) {
			              throw new RuntimeException(e);
			            }
			          }
			        });
		
	}

	@Override
	public void connect(ContentNodeAddressI p) throws Exception {
		 this.getOwner().runTask(
			      new AbstractComponent.AbstractTask(this.getPluginURI()) {
			        @Override
			        public void run() {
			          try {
			            ((PairPlugin) this.getTaskProviderReference()).connect(p);
			          } catch (Exception e) {
			            throw new RuntimeException(e);
			          }
			        }
			      });
		
	}

	@Override
	public void disconnect(ContentNodeAddressI p) throws Exception {
		 this.getOwner().runTask(
			        new AbstractComponent.AbstractTask(this.getPluginURI()) {
			          @Override
			          public void run() {
			            try {
			              ((PairPlugin) this.getTaskProviderReference()).disconnect(p);
			            } catch (Exception e) {
			              throw new RuntimeException(e);
			            }
			          }
			        });
		
	}

	@Override
	public void acceptNeighbours(Set<ContentNodeAddressI> neighbours) throws Exception {
		 this.getOwner().runTask(
			        new AbstractComponent.AbstractTask(this.getPluginURI()) {
			          @Override
			          public void run() {
			            try {
			              ((PairPlugin) this.getTaskProviderReference()).acceptNeighbours(neighbours);
			            } catch (Exception e) {
			              throw new RuntimeException(e);
			            }
			          }
			        });
		
	}

	@Override
	public void acceptConnected(ContentNodeAddressI p) throws Exception {
		this.getOwner().runTask(
		        new AbstractComponent.AbstractTask(this.getPluginURI()) {
		          @Override
		          public void run() {
		            try {
		              ((PairPlugin) this.getTaskProviderReference()).acceptConnected(p);
		            } catch (Exception e) {
		              throw new RuntimeException(e);
		            }
		          }
		        });
		
	}

	@Override
	public int getNeighborCount() throws Exception {
		return this.getOwner().handleRequest(
				new AbstractComponent.AbstractService<Integer>(this.getPluginURI()) {
				@Override
				public Integer call() throws Exception {					
				return ((PairPlugin) this.getServiceProviderReference()).getNeighborCount(); }
				});
	}

}
