package classes;

import interfaces.ApplicationNodeAdressI;

public class ApplicationNodeAdress extends FacadeNodeAdress implements ApplicationNodeAdressI{

	public ApplicationNodeAdress(String uriPrefix, String uriCM ,String  uriNM) {
		super(uriPrefix, uriNM);
		this.uriCM = uriCM;
	}

	private String uriCM;
	@Override
	public String getContentManagementURI() throws Exception {
		// TODO Auto-generated method stub
		return uriCM;
	}

}
