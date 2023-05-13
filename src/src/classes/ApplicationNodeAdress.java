package classes;

import interfaces.ApplicationNodeAdressI;

public class ApplicationNodeAdress extends FacadeNodeAdress implements ApplicationNodeAdressI{

	public ApplicationNodeAdress(String uriPrefix, String uriFCM ,String  uriNM) {
		super(uriPrefix, uriNM);
		this.uriFCM = uriFCM;
	}

	private String uriFCM;
	
	@Override
	public String getContentManagementURI() throws Exception {
		// TODO Auto-generated method stub
		return uriFCM;
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


	
}
