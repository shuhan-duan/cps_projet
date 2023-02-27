package connector;


import java.util.Set;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ContentNodeAddressI;
import interfaces.NodeManagementCI;
import interfaces.PeerNodeAddressI;



/**
 * @author lyna & shuhan 
 *
 */
public class NodeManagementConnector  extends  AbstractConnector implements NodeManagementCI{

	@Override
	public Set<PeerNodeAddressI> join(PeerNodeAddressI p) throws Exception {
		//System.out.println("ici dans ManagementConnector-------------");
		//System.out.println(this.offeringPortURI);
		return ((NodeManagementCI)this.offering).join(p);
	}

	@Override
	public void leave(PeerNodeAddressI p) throws Exception {
		 ((NodeManagementCI)this.offering).leave(p);			
	}
	

	

}
