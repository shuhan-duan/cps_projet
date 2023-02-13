package connector;


import java.util.Set;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.NodeManagementCI;
import interfaces.PeerNodeAddressI;


/**
 * @author lyna & shuhan 
 *
 */
public class ManagementConnector  extends  AbstractConnector implements NodeManagementCI{


	


	@Override
	public Set<PeerNodeAddressI> Join(PeerNodeAddressI p) throws Exception {
		
		return ((NodeManagementCI)this.offering).Join(p);
	}

	@Override
	public void leave(PeerNodeAddressI p) throws Exception {
		 ((NodeManagementCI)this.offering).leave(p);		
	}
	

	

}
