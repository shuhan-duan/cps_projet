package connector;


import java.util.Set;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ContentNodeAddressI;
import interfaces.NodeManagementCI;



/**
 * @author lyna & shuhan 
 *
 */
public class NodeManagementConnector  extends  AbstractConnector implements NodeManagementCI{

	@Override
	public Set<ContentNodeAddressI> join(ContentNodeAddressI p) throws Exception {
		//System.out.println("ici dans ManagementConnector-------------");
		//System.out.println(this.offeringPortURI);
		return ((NodeManagementCI)this.offering).join(p);
	}

	@Override
	public void leave(ContentNodeAddressI p) throws Exception {
		 ((NodeManagementCI)this.offering).leave(p);			
	}
	

	

}
