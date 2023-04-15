package ports_with_plugin;

import java.util.Set;


import componenet.Pair;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentNodeAddressI;
import interfaces.NodeCI;

import Plugin.Pair_plugin;
public class NodeCIntboundPort_plugin  extends AbstractInboundPort  implements NodeCI  {


private static final long serialVersionUID = 1L;
	
	public NodeCIntboundPort_plugin( ComponentI owner ,String plugin)
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
			              ((Pair_plugin) this.getTaskProviderReference()).probe( facade, remainghops, requestURI);
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
			            ((Pair_plugin) this.getTaskProviderReference()).connecte(p);
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
			              ((Pair_plugin) this.getTaskProviderReference()).disconnectePair(p);
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
			              ((Pair_plugin) this.getTaskProviderReference()).acceptNeighbours(neighbours);
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
		              ((Pair_plugin) this.getTaskProviderReference()).acceptConnected(p);
		            } catch (Exception e) {
		              throw new RuntimeException(e);
		            }
		          }
		        });
		
	}
	
}

