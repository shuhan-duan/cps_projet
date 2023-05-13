package ports;

import java.util.Set;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ContentDescriptorI;
import interfaces.MyClientI;
import withplugin.components.Client;

public class MyClientCInbound extends AbstractInboundPort implements MyClientI{
	
	public MyClientCInbound(String uri,  ComponentI owner)
			throws Exception {
		super(uri, MyClientI.class, owner);
		// TODO Auto-generated constructor stub
	}
	private static final long serialVersionUID = 1L;
	@Override
	public void foundRes(ContentDescriptorI found, String requsetURI) throws Exception {		
		this.getOwner().runTask(
				owner -> {
					try {
						((Client)owner).foundRes(found, requsetURI);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}
	@Override
	public void acceptMatched(Set<ContentDescriptorI> matched, String requsetURI) throws Exception {
		
		this.getOwner().runTask(
				owner -> {
					try {						
						((Client)owner).acceptMatched(matched, requsetURI);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}

}
