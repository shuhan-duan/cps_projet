package Tme_1.interfaces;

import java.util.Set;

public interface NodeManagementCI {
Set <PeerNodeAddressI> Join( PeerNodeAddressI p   ) throws Exception ;
  void leave ( PeerNodeAddressI p  )  throws Exception;
}
