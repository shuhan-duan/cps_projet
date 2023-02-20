package classes;

import interfaces.FacadeNodeAdressI;
import interfaces.NodeAdresseI;
import interfaces.PeerNodeAddressI;

public class NodeAdresse implements NodeAdresseI {

	String uriCM ; 
	
	
	
	public NodeAdresse(String uriCM) {
		this.uriCM = uriCM;
	}

	@Override
	public String getNodeidentifier() throws Exception {
		return uriCM;
	}

	@Override
	public Boolean isfacade() throws Exception {
		return   this instanceof  FacadeNodeAdressI;
	}

	@Override
	public Boolean ispeer() throws Exception {
		
		 return this instanceof  PeerNodeAddressI;
	}

}
