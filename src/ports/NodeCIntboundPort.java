package ports;

import java.util.Set;

<<<<<<< HEAD
=======
import componenet.Facade;
>>>>>>> d0c35ec572c270934f1c4a1384469c304cef547b
import componenet.Pair;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.FacadeNodeAdressI;
import interfaces.NodeCI;
import interfaces.NodeManagementCI;
import interfaces.PeerNodeAddressI;

public class NodeCIntboundPort  extends AbstractInboundPort  implements NodeCI  {

	public NodeCIntboundPort(Class<? extends OfferedCI> implementedInterface, ComponentI owner) throws Exception {
		super(implementedInterface, owner);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PeerNodeAddressI connect(PeerNodeAddressI p) throws Exception {
		return this.getOwner().handleRequest(new AbstractComponent.AbstractService<PeerNodeAddressI>() {
			@Override 
			public PeerNodeAddressI call() throws Exception {
				return ((NodeCI)this.getServiceOwner()).connect(p) ;
			}
		}) ;
	}

	@Override
	public void disconnect(PeerNodeAddressI p) throws Exception {
<<<<<<< HEAD
		// TODO Auto-generated method stub
		
	}



	
}
=======
			this.getOwner().handleRequest(new AbstractComponent.AbstractService<Void>() {
				@Override 
				public Void call() throws Exception {
					((NodeCI)this.getServiceOwner()).disconnect(p) ;
					return null;
				}
			}) ;
	}
	
}
>>>>>>> d0c35ec572c270934f1c4a1384469c304cef547b
