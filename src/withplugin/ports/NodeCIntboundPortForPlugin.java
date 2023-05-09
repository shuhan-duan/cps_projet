package withplugin.ports;

import java.util.Set;


import withplugin.components.Pair;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentNodeAddressI;
import interfaces.NodeCI;
import withplugin.plugins.PairPlugin;

public class NodeCIntboundPortForPlugin extends AbstractInboundPort  implements NodeCI {
private static final long serialVersionUID = 1L;
	
	public NodeCIntboundPortForPlugin( ComponentI owner ,String plugin)
			throws Exception {
	    super(NodeCI.class, owner, plugin, null);
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
	public void connecte(ContentNodeAddressI p) throws Exception {
		 this.getOwner().runTask(
			      new AbstractComponent.AbstractTask(this.getPluginURI()) {
			        @Override
			        public void run() {
			          try {
			            ((PairPlugin) this.getTaskProviderReference()).connecte(p);
			          } catch (Exception e) {
			            throw new RuntimeException(e);
			          }
			        }
			      });
		
	}

	@Override
	public void disconnecte(ContentNodeAddressI p) throws Exception {
		 this.getOwner().runTask(
			        new AbstractComponent.AbstractTask(this.getPluginURI()) {
			          @Override
			          public void run() {
			            try {
			              ((PairPlugin) this.getTaskProviderReference()).disconnecte(p);
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

}
