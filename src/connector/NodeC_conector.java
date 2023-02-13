package connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.NodeCI;
import interfaces.PeerNodeAddressI;

public class NodeC_conector  extends  AbstractConnector implements NodeCI{

	@Override
	public PeerNodeAddressI connecte(PeerNodeAddressI p) throws Exception {
    		return ((NodeCI)this.offering).connecte(p);
	}

	@Override
	public void disconnecte(PeerNodeAddressI p) throws Exception {
		 ((NodeCI)this.offering).disconnecte(p);	
	}

}
