package connector;

import java.util.Set;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentNodeAddressI;
import interfaces.NodeCI;

public class NodeConnector  extends  AbstractConnector implements NodeCI{

	

	@Override
	public void disconnect(ContentNodeAddressI p) throws Exception {
		 ((NodeCI)this.offering).disconnect(p);	
	}

	@Override
	public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) throws Exception {
		((NodeCI)this.offering).probe(facade, remainghops, requestURI);
		
	}

	@Override
	public void acceptNeighbours(Set<ContentNodeAddressI> neighbours) throws Exception {
		((NodeCI)this.offering).acceptNeighbours(neighbours);
		
	}

	@Override
	public void acceptConnected(ContentNodeAddressI p) throws Exception {
		((NodeCI)this.offering).acceptConnected(p);
		
	}

	@Override
	public void connect(ContentNodeAddressI p) throws Exception {
		((NodeCI)this.offering).connect(p);
		
	}

	@Override
	public int getNeighborCount() throws Exception {
		return ((NodeCI)this.offering).getNeighborCount();
	}

}
