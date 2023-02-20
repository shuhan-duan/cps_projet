package classes;


import interfaces.PeerNodeAddressI;

public class PeerNodeAddress extends NodeAdresse  implements PeerNodeAddressI {


	public PeerNodeAddress(String uriCM, String uriNodeC) {
		super(uriCM);
		this.uriNodeC = uriNodeC;
	}


	String uriNodeC;

	@Override
	public String getNodeUri() throws Exception {
		return uriNodeC;
	}

	

}
