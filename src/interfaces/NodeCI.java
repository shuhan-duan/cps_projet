package interfaces;

import java.util.Set;

public interface NodeCI extends ProbingCI{
	public void	connect (ContentNodeAddressI p  ) throws Exception ;
	
	public void disconnect (ContentNodeAddressI p ) throws Exception ;
	
	public void acceptNeighbours(Set<ContentNodeAddressI> neighbours)throws Exception  ;
	
	public void acceptConnected (ContentNodeAddressI p ) throws Exception ;
	
	// function used in management of the number of neighbors
	public int getNeighborCount()throws Exception;
}

