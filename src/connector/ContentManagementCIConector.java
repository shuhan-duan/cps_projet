package connector;

import java.util.Set;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentTemplateI;

public class ContentManagementCIConector   extends  AbstractConnector implements  ContentManagementCI {

	@Override
	public ContentDescriptorI find(ContentTemplateI cd, int hops) throws Exception {
		return ((ContentManagementCI)this.offering).find(cd,hops);
	}

	@Override
	public Set<ContentDescriptorI> match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops)
			throws Exception {
		return ((ContentManagementCI)this.offering).match(cd,matched,hops);
	}

}
