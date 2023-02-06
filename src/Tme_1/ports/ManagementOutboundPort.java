package Tme_1.ports;

import java.util.Set;


import Tme_1.interfaces.PeerNodeAddressI;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;

/**
 * @author lyna & shuhan 
 *
 */
public class ManagementOutboundPort extends AbstractOutboundPort implements  PeerNodeAddressI{

	/**   
	* @Function: NodeManagementOutboundPort.java
	* @Description: 
	*
	* @param: uri
	* @param: owner
	* @version: 
	* @author: lyna & shuhan
	* @date: 30 janv. 2023 21:08:00 
	*/
	public ManagementOutboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri,   PeerNodeAddressI.class, owner);
		assert	uri != null && owner != null ;
	}
	public ManagementOutboundPort(ComponentI owner) throws Exception{
		super( PeerNodeAddressI.class, owner);
		assert	owner != null ;

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 
	* @see Tme_1.interfaces.NodeManagementCI#Join(Tme_1.interfaces.PeerNodeAddressI)  
	* @Function: NodeManagementOutboundPort.java
	* @Function: NodeManagementInBoundPort.java
	* @Description: 
	*
	* @param: PeerNodeAddressI p
	* @return：Set<PeerNodeAddressI>
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan
	* @date: 30 janv. 2023 21:08:57 
	*/
	
	public Set<PeerNodeAddressI> Join(PeerNodeAddressI p) throws Exception {

		return ((PeerNodeAddressI)this.getConnector()).Join(p); 
	}

	/** 
	* @see Tme_1.interfaces.NodeManagementCI#leave(Tme_1.interfaces.PeerNodeAddressI)  
	* @Function: NodeManagementOutboundPort.java
	* @Description: 
	*
	* @param:PeerNodeAddressI p
	* @return：void
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan
	* @date: 30 janv. 2023 21:06:53 
	* 
	*/
	
	/** 
	* @see Tme_1.interfaces.PeerNodeAddressI#leave(Tme_1.interfaces.PeerNodeAddressI)  
	* @Function: ManagementOutboundPort.java
	* @Description: 
	*
	* @param:PeerNodeAddressI p
	* @return：void
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan
	* @date: 6 févr. 2023 14:09:23 
	*
	* 
	*/
	public void leave(PeerNodeAddressI p) throws Exception {
		 ((PeerNodeAddressI)this.getConnector()).leave(p); 	
   
	}
	/** 
	* @see Tme_1.interfaces.NodeAdresseI#getNodeidentifier()  
	* @Function: ManagementOutboundPort.java
	* @Description: 
	*
	* @param:
	* @return：String
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan
	* @date: 6 févr. 2023 14:09:26 
	*
	* 
	*/
	@Override
	public String getNodeidentifier() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	/** 
	* @see Tme_1.interfaces.NodeAdresseI#isfacade()  
	* @Function: ManagementOutboundPort.java
	* @Description: 
	*
	* @param:
	* @return：Boolean
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan
	* @date: 6 févr. 2023 14:09:32 
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
	* @Function: ManagementOutboundPort.java
	* @Description: 
	*
	* @param:
	* @return：Boolean
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan
	* @date: 6 févr. 2023 14:09:35 
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
	* @Function: ManagementOutboundPort.java
	* @Description: 
	*
	* @param:
	* @return：String
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan
	* @date: 6 févr. 2023 14:09:38 
	*
	* 
	*/
	@Override
	public String getNodeUri() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	

}
