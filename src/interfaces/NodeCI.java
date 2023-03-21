package interfaces;

import java.util.Set;

public interface NodeCI extends ProbingCI{
	public void	connecte (ContentNodeAddressI p  ) throws Exception ;
	
	public void disconnecte (ContentNodeAddressI p ) throws Exception ;
	
	public void acceptNeighbours(Set<ContentNodeAddressI> neighbours)throws Exception  ;
	
	public void acceptConnected (ContentNodeAddressI p ) throws Exception ;
}
