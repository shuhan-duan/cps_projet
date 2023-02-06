package classes;

import java.util.Set;

import interfaces.ContentDescriptorI;
import interfaces.ContentNodeAddressI;

public class ContentDescriptor extends ContentTemplate implements  ContentDescriptorI {

	public ContentDescriptor(String title, String albumtitre, Set<String> interpreter, Set<String> comopsers, long size,
			ContentNodeAddressI ca) {
		super(title, albumtitre, interpreter, comopsers);
		this.size = size;
		this.ca = ca;
	}

	long size ; 
	ContentNodeAddressI ca ;
	

	@Override
	public ContentNodeAddressI getContentNodeAdress() throws Exception {
		// TODO Auto-generated method stub
		return ca;
	}

	@Override
	public long getsize() throws Exception {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public boolean equals(ContentDescriptorI cd) throws Exception {
		return  cd.equals(ca);
	}

	

}
