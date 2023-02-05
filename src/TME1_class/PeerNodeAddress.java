package TME1_class;

import java.util.Set;

import Tme_1.interfaces.PeerNodeAddressI;

public class PeerNodeAddress extends NodeAdresse  implements PeerNodeAddressI {

	public PeerNodeAddress(String idetifier) {
		super(idetifier);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getNodeUri() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void leave(PeerNodeAddressI p) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<PeerNodeAddressI> Join(PeerNodeAddressI p) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
