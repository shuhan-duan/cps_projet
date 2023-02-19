package classes;

import java.util.Set;

import interfaces.ContentDescriptorI;
import interfaces.ContentNodeAddressI;
import interfaces.ContentTemplateI;

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

	@Override
	public boolean match(ContentTemplateI t) throws Exception {
		if (isTitleEquals(t))
			return true;
		if (isAlbumTitleEquals(t))
			return true;
		if (isIntrepretersContains(t))
			return true;
		if (isComposersContains(t))
			return true;
		return false;
	}

	private boolean isTitleEquals(ContentTemplateI request) throws Exception {
        return (request.getTitre()).equals(this.getTitre());
    }

    private boolean isAlbumTitleEquals(ContentTemplateI request) throws Exception {
        return request.getALbum().equals(getTitre());
    }
	
	/**
     * > If the interpreters of the request are contained in the interpreters of the
     * content template, then
     * return true
     * 
     * @param request The request to be checked.
     * @return A boolean value.
	 * @throws Exception
     */
    private boolean isIntrepretersContains(ContentTemplateI request) throws Exception {
        return getInterpreters().containsAll(request.getInterpreters());
    }

    /**
     * If the composers of this template contain all the composers of the request,
     * then return true.
     * 
     * @param request The request to be fulfilled.
     * @return A boolean value.
     * @throws Exception
     */
    private boolean isComposersContains(ContentTemplateI request) throws Exception {
        return getComposers().containsAll(request.getInterpreters());
    }
}
