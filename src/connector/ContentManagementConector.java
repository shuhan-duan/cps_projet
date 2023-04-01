package connector;

import java.util.Set;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentTemplateI;
import interfaces.NodeAdresseI;

public class ContentManagementConector   extends  AbstractConnector implements  ContentManagementCI {

	@Override
	public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI)
			throws Exception {
		((ContentManagementCI)this.offering).find(cd,hops, requester, requestURI);
		
	}

	@Override
	public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester,
			String requestURI) throws Exception {
		((ContentManagementCI)this.offering).match(cd,matched, hops, requester, requestURI);
		
	}

}
