package interfaces;



import fr.sorbonne_u.components.interfaces.OfferedCI;

public interface NodeManagementCI extends ProbingCI ,OfferedCI{
	
	public void join( ContentNodeAddressI p   ) throws Exception ;
	
	public void leave ( ContentNodeAddressI p  )  throws Exception;
	
	public void acceptProbed(ContentNodeAddressI p ,String requsetURI )throws Exception ;

}
