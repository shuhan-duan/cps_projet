package Tme_1.interfaces;

public interface NodeCI {
	PeerNodeAddressI	connect (PeerNodeAddressI p  ) throws Exception ;
    void disconnect (PeerNodeAddressI p ) throws Exception ;
}
