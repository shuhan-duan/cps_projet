package classes;



import interfaces.PeerNodeAddressI;

public class PeerNodeAddress extends NodeAdresse  implements PeerNodeAddressI {


	public PeerNodeAddress(String uriPrefix, String uriNodeC) {
		super(uriPrefix);
		this.uriNodeC = uriNodeC;
	}

	String uriNodeC ;

	@Override
	public String getNodeUri() throws Exception {
		return uriNodeC;
	}

	@Override
	public Boolean ispeer() throws Exception {
		// TODO Auto-generated method stub
		return true;
	}	
	@Override
	public Boolean isfacade() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
