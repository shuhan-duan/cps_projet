package connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.NodeCI;
import interfaces.PeerNodeAddressI;
import ports.NodeCIntboundPort;

/**
* @author:
* @create time：12 févr. 2023 22:51:05
* @desc:
*/
public class NodeConnector extends AbstractConnector implements NodeCI {

	@Override
	public PeerNodeAddressI connect(PeerNodeAddressI p) throws Exception {
		return ((NodeCIntboundPort)this.offering).connect(p);
	}

	@Override
	public void disconnect(PeerNodeAddressI p) throws Exception {
		((NodeCIntboundPort)this.offering).disconnect(p);
	}

}
