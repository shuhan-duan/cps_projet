package Tme_1.connector;


import java.util.Set;


import Tme_1.interfaces.PeerNodeAddressI;
import fr.sorbonne_u.components.connectors.AbstractConnector;


/**
 * @author lyna & shuhan 
 *
 */
public class ManagementConnector  extends  AbstractConnector implements PeerNodeAddressI{


	/** 
	* @see Tme_1.interfaces.NodeAdresseI#getNodeidentifier()  
	* @Function: ManagementConnector.java
	* @Description: 
	*
	* @param:null
	* @return：String
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: shuhan
	* @date: 3 févr. 2023 21:56:54 
	*
	* 
	*/
	@Override
	public String getNodeidentifier() throws Exception {
		return ((PeerNodeAddressI)this.offering).getNodeidentifier() ;
	}

	/** 
	* @see Tme_1.interfaces.NodeAdresseI#isfacade()  
	* @Function: ManagementConnector.java
	* @Description: 
	*
	* @param: null
	* @return：Boolean
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: shuhan
	* @date: 3 févr. 2023 21:57:41 
	*
	* 
	*/
	@Override
	public Boolean isfacade() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	* @see Tme_1.interfaces.NodeAdresseI#ispeer()  
	* @Function: ManagementConnector.java
	* @Description: 
	*
	* @param: null
	* @return：Boolean
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: shuhan
	* @date: 3 févr. 2023 21:57:44 
	*
	* 
	*/
	@Override
	public Boolean ispeer() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	* @see Tme_1.interfaces.PeerNodeAddressI#getNodeUri()  
	* @Function: ManagementConnector.java
	* @Description: 
	*
	* @param:
	* @return：String
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: shuhan
	* @date: 3 févr. 2023 21:57:47 
	*
	* 
	*/
	@Override
	public String getNodeUri() throws Exception {
		return ((PeerNodeAddressI)this.offering).getNodeUri();
	}

	/** 
	* @see Tme_1.interfaces.PeerNodeAddressI#leave(Tme_1.interfaces.PeerNodeAddressI)  
	* @Function: ManagementConnector.java
	* @Description: 
	*
	* @param:PeerNodeAddressI p
	* @return：void
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: shuhan
	* @date: 3 févr. 2023 21:57:50 
	*
	* 
	*/
	@Override
	public void leave(PeerNodeAddressI p) throws Exception {
		 ((PeerNodeAddressI)this.offering).leave(p);
		
	}

	/** 
	* @see Tme_1.interfaces.PeerNodeAddressI#Join(Tme_1.interfaces.PeerNodeAddressI)  
	* @Function: ManagementConnector.java
	* @Description: 
	*
	* @param:PeerNodeAddressI p
	* @return：Set<PeerNodeAddressI>
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: shuhan
	* @date: 3 févr. 2023 21:57:53 
	*
	* 
	*/
	@Override
	public Set<PeerNodeAddressI> Join(PeerNodeAddressI p) throws Exception {
		return ((PeerNodeAddressI)this.offering).Join(p);
	}

}
