package connector;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ContentNodeAddressI;
import interfaces.NodeCI;

public class NodeConnector  extends  AbstractConnector implements NodeCI{

	@Override
	public ContentNodeAddressI connecte(ContentNodeAddressI p) throws Exception {
    		return ((NodeCI)this.offering).connecte(p);
	}

	@Override
	public void disconnecte(ContentNodeAddressI p) throws Exception {
		 ((NodeCI)this.offering).disconnecte(p);	
	}

}
