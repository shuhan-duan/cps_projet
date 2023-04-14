package connector;

import java.util.Set;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;
import interfaces.ContentTemplateI;
import interfaces.FacadeContentManagementCI;
import interfaces.NodeAdresseI;
import javassist.expr.Instanceof;

public class FacadeContentManagementConector  extends  AbstractConnector implements  FacadeContentManagementCI{

	@Override
	public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
		((FacadeContentManagementCI)this.offering).find(cd,hops, requester, requestURI);
		
	}

	@Override
	public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester,
			String requestURI) throws Exception {
		((FacadeContentManagementCI)this.offering).match(cd,matched, hops, requester, requestURI);
		
	}

	@Override
	public void acceptFound(ContentDescriptorI found, String requsetURI) throws Exception {
		
		((FacadeContentManagementCI)this.offering).acceptFound(found, requsetURI);
	}

	@Override
	public void acceptMatched(Set<ContentDescriptorI> matched, String requsetURI) throws Exception {
		((FacadeContentManagementCI)this.offering).acceptMatched(matched, requsetURI);
		
	}

}
