package Tme_1.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;

public interface FacadeNodeAdressI extends NodeAdresseI , OfferedCI {
	
	public String getNodeManagementUri() throws Exception;

}
