package classes;


import interfaces.FacadeNodeAdressI;


public class FacadeNodeAdress extends   NodeAdresse  implements FacadeNodeAdressI {
String managememnt ;


	public FacadeNodeAdress(String idetifier, String managememnt) {
	super(idetifier);
	this.managememnt = managememnt;
}


	@Override
	public String getNodeManagementUri() throws Exception {
		// TODO Auto-generated method stub
		return managememnt;
	}


}
