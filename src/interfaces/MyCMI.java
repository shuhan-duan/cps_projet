package interfaces;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public interface MyCMI {
	
	public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester
			, String requestURI) throws Exception;

	public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester,
			String requestURI) throws Exception;

	
}

