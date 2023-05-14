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
	/**
	 * 
	 * @param uri
	 * @param owner
	 * @param plugin
	 * @param executorServiceURI
	 * @throws Exception
	 * @author lyna & shuhan
	 */
	public NodeCIntboundPortForPlugin(String uri, ComponentI owner ,String plugin , String executorServiceURI)
			throws Exception {
	    super(uri,NodeCI.class, owner, plugin, executorServiceURI);
	}
	/**
	 * 
	 * @param facade
	 * @param remainghops
	 * @param requestURI
	 * @throws Exception
	 * @author lyna & shuhan
	 */
	@Override
	public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) throws Exception {
		 this.getOwner().runTask(
				 this.getExecutorServiceURI(),
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
	/**
	 * 
	 * @param p
	 * @throws Exception
	 * @author lyna & shuhan
	 */
	@Override
	public void connect(ContentNodeAddressI p) throws Exception {
		 this.getOwner().runTask(
				 this.getExecutorServiceURI(),
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
	/**
	 * 
	 * @param p
	 * @throws Exception
	 * @author lyna & shuhan
	 */
	@Override
	public void disconnect(ContentNodeAddressI p) throws Exception {
		 this.getOwner().runTask(
				 this.getExecutorServiceURI(),
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
	/**
	 * @author lyna & shuhan
	 * @param neighbours
	 * @throws Exception
	 */
	@Override
	public void acceptNeighbours(Set<ContentNodeAddressI> neighbours) throws Exception {
		 this.getOwner().runTask(
				 this.getExecutorServiceURI(),
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
	/**
	 * 
	 * @param p
	 * @throws Exception
	 * @author shuhan lyna 
	 */
	@Override
	public void acceptConnected(ContentNodeAddressI p) throws Exception {
		this.getOwner().runTask(
				this.getExecutorServiceURI(),
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
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getNeighborCount() throws Exception {
		return this.getOwner().handleRequest(
				this.getExecutorServiceURI(),
				new AbstractComponent.AbstractService<Integer>(this.getPluginURI()) {
				@Override
				public Integer call() throws Exception {					
				return ((PairPlugin) this.getServiceProviderReference()).getNeighborCount(); }
				});
	}
	/**
	 * 
	 * @param facade
	 * @param remainghops
	 * @param requestURI
	 * @param leastNeighbor
	 * @param leastNeighborCount
	 * @throws Exception
	 * @author lyna & shuhan
	 */
	@Override
	public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI,
			ContentNodeAddressI leastNeighbor, int leastNeighborCount) throws Exception {
		this.getOwner().runTask(
				this.getExecutorServiceURI(),
		        new AbstractComponent.AbstractTask(this.getPluginURI()) {
		          @Override
		          public void run() {
		            try {
		              ((PairPlugin) this.getTaskProviderReference()).probe(facade,  remainghops,  requestURI,
		     				 leastNeighbor, leastNeighborCount);
		            } catch (Exception e) {
		              throw new RuntimeException(e);
		            }
		          }
		        });
		
	}

}
