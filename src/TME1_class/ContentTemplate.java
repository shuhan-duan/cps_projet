package TME1_class;

import java.util.Set;

import Tme_1.interfaces.ContentTemplateI;

public class ContentTemplate implements   ContentTemplateI{
	private String title , albumtitre;
	Set<String> interpreter , comopsers;
	
	@Override
	public String getTitre() throws Exception {
		// TODO Auto-generated method stub
		return title ;
	}
	@Override
	public String getALbum() throws Exception {
		// TODO Auto-generated method stub
		return albumtitre;
	}
	@Override
	public Set<String> getInterpreters() throws Exception {
		// TODO Auto-generated method stub
		return interpreter;
	}
	@Override
	public Set<String> getComposers() throws Exception {
		// TODO Auto-generated method stub
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
