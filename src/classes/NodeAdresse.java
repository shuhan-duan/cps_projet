package classes;

import interfaces.FacadeNodeAdressI;
import interfaces.NodeAdresseI;
import interfaces.PeerNodeAddressI;

public class NodeAdresse implements NodeAdresseI {

	String uriPrefix ; 
	
	
	
	public NodeAdresse(String uriPrefix) {
		this.uriPrefix = uriPrefix;
	}

	@Override
	public String getNodeidentifier() throws Exception {
		return uriPrefix;
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
