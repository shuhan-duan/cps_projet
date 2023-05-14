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
			public void connect(ContentNodeAddressI p) throws Exception {
				((NodeCI)this.getConnector()).connect(p);
			}

			@Override
			public void disconnect(ContentNodeAddressI p) throws Exception {
				((NodeCI)this.getConnector()).disconnect(p);
			}

			@Override
			public void acceptNeighbours(Set<ContentNodeAddressI> neighbours) throws Exception {
				((NodeCI)this.getConnector()).acceptNeighbours(neighbours);
			}

			@Override
			public void acceptConnected(ContentNodeAddressI p) throws Exception {
				((NodeCI)this.getConnector()).acceptConnected(p);
				
			}

			@Override
			public int getNeighborCount() throws Exception {
				
				return ((NodeCI)this.getConnector()).getNeighborCount();
			}

			@Override
			public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI,
					ContentNodeAddressI leastNeighbor, int leastNeighborCount) throws Exception {
				((NodeCI)this.getConnector()).probe(facade,  remainghops,  requestURI,
						 leastNeighbor, leastNeighborCount);
							
			}


	





	

}
