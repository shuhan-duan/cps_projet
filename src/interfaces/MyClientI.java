package interfaces;

import java.util.Set;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface MyClientI extends OfferedCI ,RequiredCI{
	public void foundRes(ContentDescriptorI found, String requsetURI) throws Exception;
	
	public void acceptMatched(Set<ContentDescriptorI> matched ,String requsetURI) throws Exception;
}
