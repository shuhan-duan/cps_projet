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
<<<<<<< HEAD
		 ((NodeManagementCI)this.offering).leave(p);		
=======
		
		((NodeManagementCI)this.offering).leave(p);
>>>>>>> d0c35ec572c270934f1c4a1384469c304cef547b
	}
	

	

}
