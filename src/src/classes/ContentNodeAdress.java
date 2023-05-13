package classes;

import interfaces.ContentNodeAddressI;

/**
* @author:
* @create time：12 févr. 2023 23:04:47
* @desc:
*/
public class ContentNodeAdress extends PeerNodeAddress  implements ContentNodeAddressI {

	public ContentNodeAdress(String uriPrefix, String uriCM , String uriNodeC) {
		super(uriPrefix, uriNodeC);
		this.uriCM = uriCM;
	}

	private String uriCM;

	@Override
	public String getContentManagementURI() throws Exception {
		// TODO Auto-generated method stub
		return uriCM;
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
