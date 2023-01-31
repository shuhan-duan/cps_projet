package Tme_1.interfaces;

public interface ContentDescriptorI extends ContentTemplateI {
 
	ContentNodeAddressI getContentNodeAdress();
	long getsize();
	boolean equals (ContentDescriptorI cd ) ;
}
