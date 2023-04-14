package classes;

import interfaces.ApplicationNodeAdressI;

public class ApplicationNodeAdress extends FacadeNodeAdress implements ApplicationNodeAdressI{

	public ApplicationNodeAdress(String uriPrefix, String uriCM ,String  uriNM ,String uriFCM) {
		super(uriPrefix, uriNM);
		this.uriCM = uriCM;
		this.uriFCM = uriFCM;
	}

	private String uriCM;
	private String uriFCM;
	@Override
	public String getContentManagementURI() throws Exception {
		// TODO Auto-generated method stub
		return uriCM;
	}
	
	@Override
	public Boolean ispeer() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}	
	@Override
	public Boolean isfacade() throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getFacadeCMURI() throws Exception {
		// TODO Auto-generated method stub
		return uriFCM;
	}

	
}
