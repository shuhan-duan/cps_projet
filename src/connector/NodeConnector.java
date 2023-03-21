package connector;

import java.util.Set;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentNodeAddressI;
import interfaces.NodeCI;

public class NodeConnector  extends  AbstractConnector implements NodeCI{

	

	@Override
	public void disconnecte(ContentNodeAddressI p) throws Exception {
		 ((NodeCI)this.offering).disconnecte(p);	
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
	public void connecte(ContentNodeAddressI p) throws Exception {
		((NodeCI)this.offering).connecte(p);
		
	}

}
