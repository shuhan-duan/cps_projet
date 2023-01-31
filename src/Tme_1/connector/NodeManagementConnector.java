package Tme_1.connector;

import java.util.Set;

import Tme_1.interfaces.NodeManagementCI;
import Tme_1.interfaces.PeerNodeAddressI;
import fr.sorbonne_u.components.connectors.AbstractConnector;

public class NodeManagementConnector  extends  AbstractConnector implements NodeManagementCI{

	@Override
	public Set<PeerNodeAddressI> Join(PeerNodeAddressI p) throws Exception {
		System.out.print("From connector ") ;
		return null;
	}

	@Override
	public void leave(PeerNodeAddressI p) throws Exception {
		System.out.print("From connector ") ;
	}

}
