package classes;

import interfaces.FacadeNodeAdressI;
import interfaces.NodeAdresseI;
import interfaces.PeerNodeAddressI;

public class NodeAdresse implements NodeAdresseI {
String idetifier ; 
	
	
	@Override
	public String getNodeidentifier() throws Exception {
		// TODO Auto-generated method stub
		return idetifier;
	}

	@Override
	public Boolean isfacade() throws Exception {
		// TODO Auto-generated method stub
		return   this instanceof  FacadeNodeAdressI;
	}

	@Override
	public Boolean ispeer() throws Exception {
		// TODO Auto-generated method stub
		 return this instanceof  PeerNodeAddressI;
	}

	public NodeAdresse(String idetifier) {
		super();
		this.idetifier = idetifier;
	}

}
