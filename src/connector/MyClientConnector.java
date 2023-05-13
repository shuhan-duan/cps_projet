package connector;

import java.util.Set;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ContentDescriptorI;
import interfaces.MyClientI;

public class MyClientConnector  extends  AbstractConnector implements  MyClientI{

	@Override
	public void foundRes(ContentDescriptorI found, String requsetURI) throws Exception {
		
		((MyClientI)this.offering).foundRes(found, requsetURI);
	}

	@Override
	public void acceptMatched(Set<ContentDescriptorI> matched, String requsetURI) throws Exception {
		((MyClientI)this.offering).acceptMatched(matched, requsetURI);
		
	}

}
