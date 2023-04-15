package ports_with_plugin;



import componenet.Facade;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentNodeAddressI;
import interfaces.NodeManagementCI;

public class NodeManagementInBoundPort_plugin extends AbstractInboundPort implements NodeManagementCI{

	/**   
	* @Function: NodeManagementInBoundPort.java
	* @Description: 
	*
	* @param:uri
	* @param:owner
	* @version: 
	* @author: lyna & shuhan
	* @date: 30 janv. 2023 21:04:56 
	*/
	public NodeManagementInBoundPort_plugin( ComponentI owner,String plugin )
			throws Exception {
	    super(NodeManagementCI.class, owner, plugin, null);
	//	assert	uri != null && owner instanceof NodeManagementCI ;

	}

	private static final long serialVersionUID = 1L;

	@Override
	public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) throws Exception{
		 this.getOwner().runTask(
			        new AbstractComponent.AbstractTask(this.getPluginURI()) {
			          @Override
			          public void run() {
			            try {
			              ((Facade) this.getTaskProviderReference()).probe(facade, remainghops,requestURI);
			            } catch (Exception e) {
			              e.printStackTrace();
			            }
			          }
			        });
	}

	@Override
	public void join(ContentNodeAddressI p) throws Exception {
		
		 this.getOwner().runTask(
			      new AbstractComponent.AbstractTask(this.getPluginURI()) {
			        @Override
			        public void run() {
			          try {
			            ((Facade) this.getTaskProviderReference()).joinPair(p);
			          } catch (Exception e) {
			            e.printStackTrace();
			          }
			        }
			      });
	}

	@Override
	public void leave(ContentNodeAddressI p) throws Exception {
		this.getOwner().runTask(
		        new AbstractComponent.AbstractTask(this.getPluginURI()) {
		          @Override
		          public void run() {
		            try {
		              ((Facade) this.getTaskProviderReference()).leavePair(p);
		            } catch (Exception e) {
		              e.printStackTrace();
		            }
		          }
		        });
		
	}

	@Override
	public void acceptProbed(ContentNodeAddressI p, String requsetURI) throws Exception {
		 this.getOwner().runTask(
			        new AbstractComponent.AbstractTask(this.getPluginURI()) {
			          @Override
			          public void run() {
			            try {
			              ((Facade) this.getTaskProviderReference()).acceptProbed(p, requsetURI);
			            } catch (Exception e) {
			              e.printStackTrace();
			            }
			          }
			        });
		
	}

}
