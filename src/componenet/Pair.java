package componenet;



import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import classes.ContentNodeAdress;
import connector.ContentManagementConector;
import connector.NodeManagementConnector;
import connector.NodeConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.ContentDescriptorI;
import interfaces.ContentNodeAddressI;
import interfaces.ContentTemplateI;
import ports.*;

/**
 * @author lyna & shuhan 
 *
 */

public class Pair  extends AbstractComponent  {

	public static int cpt = 0;

	/**	the outbound port used to call the service.							*/
	protected NodeManagementOutboundPort	NMportOut ;
	protected String NMPortIn_facade;
	protected NodeCIntboundPort	NodePortIn ;
	protected ContentManagementCIIntbound CMportIn;
	/**	counting service invocations.										*/
	protected static int counter ;
	protected ContentNodeAdress adress;
	


	//stock the pairs connected with this pair and the outportNodeC of me
	private ConcurrentHashMap<ContentNodeAddressI,NodeCOutboundPort> outPortsNodeC;
	//stock the neighber pairs connected with this pair and the outportCMpair of me
	private ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound> outPortsCM;
	private ConcurrentHashMap<ContentTemplateI, ContentDescriptorI> contents;


	/**
	 * @param NMoutportUri	URI of the outbound port NM.
	 * @throws Exception		<i>todo.</i>
	 */
	protected Pair( String NMoutportUri ,String NMPortIn_facade)throws Exception {
		super(NMoutportUri, 0, 1);
		
		cpt++;
		this.adress = new ContentNodeAdress("Pair" + cpt,"CMuriIn"+ cpt, "NodeCuriIn"+ cpt);
		this.NMportOut =new NodeManagementOutboundPort(NMoutportUri, this) ;
		NMportOut.publishPort() ;
		this.NodePortIn = new NodeCIntboundPort(adress.getNodeUri() ,this);
		NodePortIn.publishPort();
		this.CMportIn = new ContentManagementCIIntbound(adress.getContentManagementURI(), this);
		CMportIn.publishPort();
		this.counter = 0 ;
		this.NMPortIn_facade = NMPortIn_facade;
		this.outPortsNodeC = new ConcurrentHashMap<ContentNodeAddressI,NodeCOutboundPort>();
		this.outPortsCM = new ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound>();
		this.contents = new ConcurrentHashMap<ContentTemplateI, ContentDescriptorI>();
	}

	/**
	 * @Function: Pair.java
	 * @Description:
	 *
	 * @param:
	 * @return：
	 * @throws：
	 *
	 * @version: v1.0.0
	 * @author: lyna & shuhan
	 * @throws Exception
	 * @date: 6 févr. 2023 17:44:32
	 *
	 *
	 */
	public ContentNodeAddressI connecte (ContentNodeAddressI p ) throws Exception
	{  
		if (p == null) {
			throw new Exception("Connection failed , pair is null");
		} else {
			//do connect entre pair et pair en NodeCI
			//System.out.println("\nIn connect: me "+ adress.getNodeUri()+ " neiber: "+ p.getNodeUri());
			String outportN = "myOutPortNodeCIpair"+ counter;
			NodeCOutboundPort NportOut = new NodeCOutboundPort(outportN, this);
			NportOut.publishPort();
			outPortsNodeC.put(p, NportOut);
			doPortConnection(outPortsNodeC.get(p).getPortURI(), p.getNodeUri(), NodeConnector.class.getCanonicalName());
			System.out.println("\nreverse:c'est ok " + p.getNodeUri() +" connecte avec "+ this.adress.getNodeUri() +" en "+ outPortsNodeC.get(p).getPortURI()+" en NodeCI" );

			//do connect entre pair et pair en ContentManagementCI
			String outportCM = "myOutportCMpair" + ++cpt ;
			ContentManagementCIOutbound CMportOut = new ContentManagementCIOutbound(outportCM,this );
			CMportOut.publishPort();
			outPortsCM.put(p, CMportOut);
			doPortConnection(outPortsCM.get(p).getPortURI(), p.getContentManagementURI(), ContentManagementConector.class.getCanonicalName());
			System.out.println("\nreverse:c'est ok " + p.getNodeidentifier() +" connecte avec "+ this.adress.getNodeidentifier()+" en "+ outPortsCM.get(p).getPortURI() +" en ContentManagementCI" );
		}
		return p;
	}

	/**
	 * @Function: Pair.java
	 * @Description:
	 *
	 * @param:
	 * @return：
	 * @throws：
	 *
	 * @version: v1.0.0
	 * @author: lyna & shuhan
	 * @date: 6 févr. 2023 17:45:00
	 *
	 *
	 */
	public void disconnectePair (ContentNodeAddressI p ) throws Exception
	{   	//get the outportNodeCI of this , which is connected with p
		    NodeCOutboundPort voisin= outPortsNodeC.get(p);
			voisin.unpublishPort();
			this.doPortDisconnection(voisin.getPortURI());
			outPortsNodeC.remove(p);
			System.out.println("\nc'est ok "+ p.getNodeUri() +" disconnect  avec " + this.adress.getNodeUri()+" en NodeCI");
			//get the outportContentManagementCI of this , which is connected with p
			ContentManagementCIOutbound voisin2= outPortsCM.get(p);
			voisin2.unpublishPort();
			this.doPortDisconnection(voisin2.getPortURI());
			outPortsCM.remove(p);
			System.out.println("\nc'est ok "+ p.getNodeUri() +" disconnect  avec " + this.adress.getNodeUri()+" en ContentManagementCI");
			
	}

	public ContentDescriptorI find(ContentTemplateI ct  ,int hops )throws Exception{
		System.out.println("\nc'est dans find in pair "+this.adress);
		if (hops == 0) {
			return null;
		}
		// Cherche parmi ses propres contenus
		for (ContentDescriptorI content : this.contents.values()) {
			if (content.match(ct)) {
				return content;
			}
		}
		// Si le contenu n'est pas dans le nœud courant, demande à un autre pair
		Set<ContentNodeAddressI> neighbors = outPortsCM.keySet();
		if (neighbors == null) {
			return null;
		}
		for (ContentNodeAddressI pair : neighbors) {
			try {
				ContentDescriptorI content = ((Pair)pair).find(ct, hops - 1);
				if (content != null) {
					return content;
				}
			} catch (Exception e) {
				System.err.println("Failed to contact neighbor pair: " + pair.getNodeUri());
			}
		}

		return null;

	}


	//-------------------------------------------------------------------------
	// Component life-cycle
	//-------------------------------------------------------------------------
	@Override
	public void start() throws ComponentStartException {
		super.start();
		try {
			this.doPortConnection(NMportOut.getPortURI(), NMPortIn_facade,NodeManagementConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
	}

	@Override
	public void			execute() throws Exception
	{   
		this.logMessage("executing consumer component.") ;
		try {
			//do join
			Set<ContentNodeAddressI> liste = this.NMportOut.join(this.adress);
			if(liste.size() == 0) {
				System.out.println("\n"+adress.getNodeidentifier() +" says : I don't have neigber yet!");
			}else{
				//do connection entre pair et pair en NodeCI
				for (ContentNodeAddressI p: liste ) {
					System.out.println("\nI am "+ adress.getNodeidentifier()+", I will connect with my neighber : "+p.getNodeidentifier());

					counter++;
					String outportN = "myOutPortNodeCIpair"+ counter;
					NodeCOutboundPort NportOut = new NodeCOutboundPort(outportN, this);
					NportOut.publishPort();
					outPortsNodeC.put(p, NportOut);
					doPortConnection(NportOut.getPortURI(),	p.getNodeUri(),NodeConnector.class.getCanonicalName());
					System.out.println("\nc'est ok " + p.getNodeidentifier() +" connecte avec "+ this.adress.getNodeidentifier() +" en "+ outPortsNodeC.get(p).getPortURI()+" en NodeCI" );
					
					String outportCM = "myOutportCMpair" + ++cpt ;
					ContentManagementCIOutbound CMportOut = new ContentManagementCIOutbound(outportCM,this );
					CMportOut.publishPort();
					outPortsCM.put(p, CMportOut);
					doPortConnection(outportCM,	p.getContentManagementURI(),ContentManagementConector.class.getCanonicalName());
					System.out.println("\nc'est ok "+ p.getNodeidentifier() +" connect  avec " +this.adress.getNodeidentifier() +" en "+ CMportOut.getPortURI() +" en ContentManagement");
					NportOut.connecte(this.adress);
	
					Thread.sleep(1000);
					//disconnect
					NportOut.disconnecte(this.adress);
				}
			}
			
			
				
			while ( ! outPortsNodeC.isEmpty())
			{
				//wait
			}
			//leave
			//NMportOut.leave(this.adress);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void	finalise() throws Exception
	{
		this.doPortDisconnection(NMportOut.getPortURI());
		super.finalise();
	}

}
