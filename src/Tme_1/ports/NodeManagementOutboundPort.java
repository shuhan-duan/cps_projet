package Tme_1.ports;

import java.util.Set;

import Tme_1.interfaces.NodeManagementCI;
import Tme_1.interfaces.PeerNodeAddressI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.examples.basic_cs.interfaces.URIConsumerCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

public class NodeManagementOutboundPort extends AbstractOutboundPort implements NodeManagementCI {

	public NodeManagementOutboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri, (Class<? extends RequiredCI>) NodeManagementCI.class, owner);
		assert	uri != null && owner != null ;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Set<PeerNodeAddressI> Join(PeerNodeAddressI p) throws Exception {

		return ((NodeManagementCI)this.getConnector()).Join(p); 
	}

	@Override
	public void leave(PeerNodeAddressI p) throws Exception {
		 ((NodeManagementCI)this.getConnector()).leave(p); 	
   
	}

}
