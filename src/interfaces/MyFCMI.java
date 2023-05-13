package interfaces;

import java.util.Set;

public interface MyFCMI {
	public void acceptFound(ContentDescriptorI found, String requsetURI) throws Exception;
	
	public void acceptMatched(Set<ContentDescriptorI> matched ,String requsetURI) throws Exception;
}
