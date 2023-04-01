package ports;

import java.util.Set;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;

import interfaces.ContentTemplateI;
import interfaces.FacadeContentManagementCI;
import interfaces.MyCMI;
import interfaces.NodeAdresseI;

public class FacadeContentManagementCInbound extends AbstractInboundPort implements  FacadeContentManagementCI {
	public FacadeContentManagementCInbound(ComponentI owner ,String uri)
			throws Exception {
				super(uri, FacadeContentManagementCI.class, owner);
		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
		this.getOwner().runTask(
				owner -> {
					try {
						((MyCMI) owner).find(cd ,hops ,requester,requestURI);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}
	@Override
	public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester,
			String requestURI) throws Exception {
		this.getOwner().runTask(
				owner -> {
					try {
						((MyCMI) owner).match(cd ,matched ,hops ,requester,requestURI);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
	}
	@Override
	public void acceptFound(ContentDescriptorI found, String requsetURI) throws Exception {
		this.getOwner().runTask(
				owner -> {
					try {
						((FacadeContentManagementCI)owner).acceptFound(found, requsetURI);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}
	@Override
	public void acceptMatched(Set<ContentDescriptorI> matched, String requsetURI) throws Exception {
		this.getOwner().runTask(
				owner -> {
					try {
						((FacadeContentManagementCI)owner).acceptMatched(matched, requsetURI);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});
		
	}

}
