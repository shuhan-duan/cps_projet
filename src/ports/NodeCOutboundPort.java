package ports;


import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ContentNodeAddressI;
import interfaces.NodeCI;


public class NodeCOutboundPort   extends AbstractOutboundPort implements  NodeCI{

	private static final long serialVersionUID = 1L;

	public NodeCOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, NodeCI.class, owner);
	}


	@Override
	public ContentNodeAddressI connecte(ContentNodeAddressI p) throws Exception {
		return ((NodeCI) getConnector()).connecte(p);
	}

	@Override
	public void disconnecte(ContentNodeAddressI p) throws Exception {
		((NodeCI) this.getConnector()).disconnecte(p);

		
	}
	





	

}
