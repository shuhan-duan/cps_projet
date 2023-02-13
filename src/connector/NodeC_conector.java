package connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.NodeCI;
import interfaces.PeerNodeAddressI;

public class NodeC_conector  extends  AbstractConnector implements NodeCI{

	@Override
	public PeerNodeAddressI connect(PeerNodeAddressI p) throws Exception {
    		return ((NodeCI)this.offering).connect(p);
	}

	@Override
	public void disconnect(PeerNodeAddressI p) throws Exception {
		 ((NodeCI)this.offering).disconnect(p);	
	}

}
