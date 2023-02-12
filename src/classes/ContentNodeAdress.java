package classes;

import interfaces.ContentNodeAddressI;

/**
* @author:
* @create time：12 févr. 2023 23:04:47
* @desc:
*/
public class ContentNodeAdress implements ContentNodeAddressI{
	private String nodeURI;
	private String nodeIdentifier;
	private String contentManagementURI;
	private boolean isFacade;
	private boolean isPeer;
	

	@Override
	public String getNodeUri() throws Exception {
		// TODO Auto-generated method stub
		return nodeURI;
	}

	@Override
	public String getNodeidentifier() throws Exception {
		// TODO Auto-generated method stub
		return nodeIdentifier;
	}

	@Override
	public Boolean isfacade() throws Exception {
		// TODO Auto-generated method stub
		return isFacade;
	}

	@Override
	public Boolean ispeer() throws Exception {
		// TODO Auto-generated method stub
		return isPeer;
	}

	@Override
	public String getContentManagementURI() {
		// TODO Auto-generated method stub
		return contentManagementURI;
	}

}
