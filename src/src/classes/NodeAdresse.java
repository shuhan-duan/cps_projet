package classes;


import interfaces.NodeAdresseI;


public abstract class NodeAdresse implements NodeAdresseI {

	String uriPrefix ; 
	
	
	
	public NodeAdresse(String uriPrefix) {
		this.uriPrefix = uriPrefix;
	}

	@Override
	public String getNodeidentifier() throws Exception {
		return uriPrefix;
	}

	public abstract Boolean isfacade() throws Exception;
    
    public abstract Boolean ispeer() throws Exception;

	

}
