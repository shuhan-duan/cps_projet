package Tme_1.interfaces;

import java.util.Set;

public interface ContentManagementCI {
	public ContentDescriptorI find(ContentTemplateI cd  ,int hops );
	 Set<ContentDescriptorI> match(ContentTemplateI cd , Set<ContentDescriptorI> matched  , int hops);
}
