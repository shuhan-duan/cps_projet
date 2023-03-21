package interfaces;


import java.util.Set;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ContentManagementCI extends OfferedCI ,RequiredCI {
	
	public void find(ContentTemplateI cd  ,int hops ,NodeAdresseI requester , String requestURI )throws Exception;
	
	public void match(ContentTemplateI cd , Set<ContentDescriptorI> matched  , int hops ,NodeAdresseI requester ,String requestURI)throws Exception;

}
