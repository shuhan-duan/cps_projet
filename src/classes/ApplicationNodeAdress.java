package classes;

import interfaces.ApplicationNodeAdressI;

public class ApplicationNodeAdress  implements ApplicationNodeAdressI{

	private String nodeManagementURI;
	private String nodeIdentifier;
	private String contentManagementURI;
	private boolean isFacade;
	private boolean isPeer;
	
	public ApplicationNodeAdress(String managementURI, String nodeIdentifier, String contentManagementURI) {
		super();
		this.nodeManagementURI = managementURI;
		this.nodeIdentifier = nodeIdentifier;
		this.contentManagementURI = contentManagementURI;
	}

	@Override
	public String getNodeManagementUri() throws Exception {
			return nodeManagementURI;
	}

	@Override
	public String getNodeidentifier() throws Exception {
		return nodeIdentifier;
	}

	@Override
	public Boolean isfacade() throws Exception {
		throw new UnsupportedOperationException("Unimplemented method 'isfacade'");
	}

	@Override
	public Boolean ispeer() throws Exception {
		throw new UnsupportedOperationException("Unimplemented method 'ispeer'");
	}



	@Override
	public  String getContentManagementURI() throws Exception {
		return contentManagementURI;
	}
	
	@Override
	public Boolean ispeer() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}	
	@Override
	public Boolean isfacade() throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

}
