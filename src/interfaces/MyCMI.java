package interfaces;

import java.util.Set;

public interface MyCMI {
	
	public void find(ContentTemplateI cd, int hops, NodeAdresseI requester, String requestURI) throws Exception;

	public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, NodeAdresseI requester,
			String requestURI) throws Exception;

}

