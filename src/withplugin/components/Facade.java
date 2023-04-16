package withplugin.components;

import fr.sorbonne_u.components.AbstractComponent;
import withplugin.plugins.FacadePlugin;

public class Facade  extends AbstractComponent{
	protected	Facade(	String ContentManagementInboudPort,	String 	NodeManagemenInboundPort ,String FCMInbountPortClient, String FacadeCMInPortFacade) throws Exception
	{
		// the reflection inbound port URI is the URI of the component
		super(NodeManagemenInboundPort, 2, 0) ;
		FacadePlugin facade_plugin = new FacadePlugin(ContentManagementInboudPort, NodeManagemenInboundPort,FCMInbountPortClient,FacadeCMInPortFacade);
		this.installPlugin(facade_plugin);
		
	}


}
