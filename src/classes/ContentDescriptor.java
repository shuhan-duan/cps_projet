package classes;

import java.util.HashMap;
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

	public ContentDescriptor(HashMap<String, Object> toLoad) {
        super(toLoad);
        this.size = (Long) toLoad.get("size");
    }
	
	private long size ; 
	private ContentNodeAddressI ca ;
	

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
		boolean res = false;
        if(t.getTitre()!=null) res = isTitleEquals(t);
        if(t.getALbumTitre()!=null) res = isAlbumTitleEquals(t);
        if(t.getInterpreters().size()!=0) res = isIntrepretersContains(t);
        if(t.getComposers().size()!=0) res =isComposersContains(t);
        return res;
	}

	private boolean isTitleEquals(ContentTemplateI t) throws Exception {
        return (t.getTitre()).equals(this.getTitre());
    }

    private boolean isAlbumTitleEquals(ContentTemplateI t) throws Exception {
        return t.getALbumTitre().equals(this.getALbumTitre());
    }
	
	/**
     * > If the interpreters of the t are contained in the interpreters of the
     * content template, then
     * return true
     * 
     * @param t The t to be checked.
     * @return A boolean value.
	 * @throws Exception
     */
    private boolean isIntrepretersContains(ContentTemplateI t) throws Exception {
        return this.getInterpreters().containsAll(t.getInterpreters());
    }

    /**
     * If the composers of this template contain all the composers of the t,
     * then return true.
     * 
     * @param t The t to be fulfilled.
     * @return A boolean value.
     * @throws Exception
     */
    private boolean isComposersContains(ContentTemplateI t) throws Exception {
        return this.getComposers().containsAll(t.getComposers());
    }
    
    @Override
	public String toString() {
		return super.toString()+ " \nsize :" +size;
	}
}
