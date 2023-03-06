package classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import interfaces.ContentTemplateI;

public class ContentTemplate implements   ContentTemplateI{
	private String title , albumtitre;
	private Set<String> interpreters , composers;
	
	@Override
	public String getTitre() throws Exception {
		return title ;
	}
	@Override
	public String getALbumTitre() throws Exception {
		return albumtitre;
	}
	@Override
	public Set<String> getInterpreters() throws Exception {
		return interpreters;
	}
	@Override
	public Set<String> getComposers() throws Exception {
		return composers;
	}
	public ContentTemplate(String title, String albumtitre, Set<String> interpreter, Set<String> comopsers) {
		super();
		this.title = title;
		this.albumtitre = albumtitre;
		this.interpreters = interpreter;
		this.composers = comopsers;
	}

	public ContentTemplate(HashMap<String, Object> toLoad) {
        this.title = (String) toLoad.get("title");
        this.albumtitre = (String) toLoad.get("album-title");
        this.composers = new HashSet<String>();
        this.interpreters = new HashSet<String>();

        ArrayList<?> composersBeforeCast = (ArrayList<?>) toLoad.get("composers");
        if (composersBeforeCast != null) {
        	for (Object object : composersBeforeCast)
                this.composers.add((String) object);
		}

        ArrayList<?> intepretersBeforeCast = (ArrayList<?>) toLoad.get("interpreters");
        if(intepretersBeforeCast!=null){
            for (Object object : intepretersBeforeCast)
            this.interpreters.add((String) object);
        }
    }
	
	@Override 
	public String toString() {
		String intersString  ="";
		for (String string : interpreters) {
			intersString += string+", ";
		}
		String compString ="";
		for (String string : composers) {
			compString += string +", ";
		}
		return "\ntitle: "+ title +" \nalbumtitre: " + albumtitre +" \ninterpreters: "+intersString+ " \ncomposers :"+compString;
	}
}
