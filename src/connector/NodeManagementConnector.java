package connector;


import java.util.Set;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentNodeAddressI;
import interfaces.NodeManagementCI;



/**
 * @author lyna & shuhan 
 *
 */
public class NodeManagementConnector  extends  AbstractConnector implements NodeManagementCI{

	@Override
	public void join(ContentNodeAddressI p) throws Exception {
	//	System.out.println("ici dans ManagementConnector-------------");
		//System.out.println(this.offeringPortURI);
		((NodeManagementCI)this.offering).join(p);
	}

	@Override
	public void leave(ContentNodeAddressI p) throws Exception {
		 ((NodeManagementCI)this.offering).leave(p);			
	}

	@Override
	public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) throws Exception {
		((NodeManagementCI)this.offering).probe(facade, remainghops, requestURI);
		
	}

	@Override
	public void acceptProbed(ContentNodeAddressI p, String requsetURI) throws Exception {
		((NodeManagementCI)this.offering).acceptProbed(p, requsetURI);
		
	}
	

	

}
