package classes;


import interfaces.FacadeNodeAdressI;


public class FacadeNodeAdress extends   NodeAdresse  implements FacadeNodeAdressI {

	public FacadeNodeAdress(String uriCM, String uriNM) {
		super(uriCM);
		this.uriNM = uriNM;
	}

	String  uriNM ;


	@Override
	public String getNodeManagementUri() throws Exception {
		return uriNM;
	}


}
