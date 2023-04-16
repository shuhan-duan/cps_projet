package ports;

import java.util.Set;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentNodeAddressI;
import interfaces.NodeManagementCI;


/**
 * @author lyna & shuhan 
 *
 */
public class NodeManagementOutboundPort extends AbstractOutboundPort implements  NodeManagementCI{

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------
	
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
	// -------------------------------------------------------------------------
		// Methods
		// -------------------------------------------------------------------------	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void probe(ApplicationNodeAdressI facade, int remainghops, String requestURI) throws Exception {
		((NodeManagementCI)this.getConnector()).probe(facade ,remainghops ,requestURI);
		
	}

	@Override
	public void join(ContentNodeAddressI p) throws Exception {
		System.out.println("Join OutPort");
		((NodeManagementCI)this.getConnector()).join(p);
		
	}

	@Override
	public void leave(ContentNodeAddressI p) throws Exception {
		((NodeManagementCI)this.getConnector()).leave(p);
		
	}

	@Override
	public void acceptProbed(ContentNodeAddressI p, String requsetURI) throws Exception {
		((NodeManagementCI)this.getConnector()).acceptProbed(p, requsetURI);
		
	}

	
	
	

}
