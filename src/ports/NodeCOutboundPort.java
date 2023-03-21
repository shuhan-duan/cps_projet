package ports;


import java.util.Set;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentManagementCI;
import interfaces.ContentNodeAddressI;
import interfaces.NodeCI;


public class NodeCOutboundPort   extends AbstractOutboundPort implements  NodeCI{

			private static final long serialVersionUID = 1L;

			// -------------------------------------------------------------------------
			// Constructors
			// -------------------------------------------------------------------------

			public				NodeCOutboundPort(ComponentI owner)
			throws Exception
			{
				super(NodeCI.class, owner);
			}

			public				NodeCOutboundPort(
				String uri,
				ComponentI owner
				) throws Exception
			{
				super(uri, NodeCI.class, owner);
			}

			// -------------------------------------------------------------------------
			// Methods
			// -------------------------------------------------------------------------		
			

			@Override
			public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) throws Exception {
				((NodeCI)this.getConnector()).probe(facade ,remainghops ,requestURI);
				
			}

			@Override
			public void connecte(ContentNodeAddressI p) throws Exception {
				((NodeCI)this.getConnector()).connecte(p);
			}

			@Override
			public void disconnecte(ContentNodeAddressI p) throws Exception {
				((NodeCI)this.getConnector()).disconnecte(p);
			}

			@Override
			public void acceptNeighbours(Set<ContentNodeAddressI> neighbours) throws Exception {
				((NodeCI)this.getConnector()).acceptNeighbours(neighbours);
			}

			@Override
			public void acceptConnected(ContentNodeAddressI p) throws Exception {
				((NodeCI)this.getConnector()).acceptConnected(p);
				
			}


	





	

}
