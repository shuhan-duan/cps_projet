package classes;

import interfaces.FacadeNodeAdressI;



public class FacadeNodeAdress  extends   NodeAdresse  implements FacadeNodeAdressI {

	public FacadeNodeAdress(String uriPrefix, String uriNM) {
		super(uriPrefix);
		this.uriNM =  uriNM;
	}

	private String  uriNM ;


	@Override
	public String getNodeManagementUri() throws Exception {
		return uriNM;
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
