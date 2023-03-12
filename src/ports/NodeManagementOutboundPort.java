package ports;

import java.util.Set;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ContentNodeAddressI;
import interfaces.NodeManagementCI;
import interfaces.PeerNodeAddressI;

/**
 * @author lyna & shuhan 
 *
 */
public class NodeManagementOutboundPort extends AbstractOutboundPort implements  NodeManagementCI{

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
	public NodeManagementOutboundPort(String uri, ComponentI owner)
			throws Exception {
		super(uri,   NodeManagementCI.class, owner);
		assert	uri != null && owner != null ;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/** 
	* @see interfaces.NodeAdresseI#getNodeidentifier()  
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
	public Set<ContentNodeAddressI> join(ContentNodeAddressI p) throws Exception {
		return ((NodeManagementCI)this.getConnector()).join(p);
	}
	@Override
	public void leave(ContentNodeAddressI p) throws Exception {
		//System.out.println("-----------leave-------------------");
		//System.out.println(this.getPortURI() + " " + this.getConnector());
		((NodeManagementCI)this.getConnector()).leave(p);
		
	}

	

}
