package ports;

import java.util.Set;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ContentDescriptorI;
import interfaces.MyClientI;

public class MyClientCOutbound extends AbstractOutboundPort implements  MyClientI{

	public MyClientCOutbound(ComponentI owner)
			throws Exception {
		super(generatePortURI(), MyClientI.class, owner);
		// TODO Auto-generated constructor stub
	}
	private static final long serialVersionUID = 1L;
	
	
	@Override
	public void foundRes(ContentDescriptorI found, String requsetURI) throws Exception {
		
		((MyClientI)this.getConnector()).foundRes(found, requsetURI);
		
	}
	@Override
	public void acceptMatched(Set<ContentDescriptorI> matched, String requsetURI) throws Exception {
		((MyClientI)this.getConnector()).acceptMatched(matched, requsetURI);
		
	}


}
