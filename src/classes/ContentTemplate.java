package classes;

import java.util.Set;

import interfaces.ContentTemplateI;

public class ContentTemplate implements   ContentTemplateI{
	private String title , albumtitre;
	private Set<String> interpreter , comopsers;
	
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
		return interpreter;
	}
	@Override
	public Set<String> getComposers() throws Exception {
		return comopsers;
	}
	public ContentTemplate(String title, String albumtitre, Set<String> interpreter, Set<String> comopsers) {
		super();
		this.title = title;
		this.albumtitre = albumtitre;
		this.interpreter = interpreter;
		this.comopsers = comopsers;
	}

}
