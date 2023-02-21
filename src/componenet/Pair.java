package componenet;


import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import classes.ContentDescriptor;
import classes.ContentNodeAdress;
import classes.PeerNodeAddress;
import connector.ContentManagementCIConector;
import connector.ManagementConnector;
import connector.NodeC_conector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.components.ports.InboundPortI;
import interfaces.ContentDescriptorI;
import interfaces.ContentManagementCI;
import interfaces.ContentNodeAddressI;
import interfaces.ContentTemplateI;
import interfaces.NodeCI;
import interfaces.NodeManagementCI;
import interfaces.PeerNodeAddressI;
import ports.*;

/**
 * @author lyna & shuhan 
 *
 */
@RequiredInterfaces(required = { ContentManagementCI.class})
@OfferedInterfaces(offered = { NodeCI.class, ContentManagementCI.class })

public class Pair  extends AbstractComponent  {

	public static int cpt = 0;

	/**	the outbound port used to call the service.							*/
	protected ManagementOutboundPort	NMportOut ;
	protected String NMPortIn_facade;
	protected NodeCIntboundPort	NodePortIn ;
	protected ContentManagementCIIntbound CMportIn;
	/**	counting service invocations.										*/
	protected static int counter ;
	protected PeerNodeAddress adress;

	//stock the pairs connected with this pair and the outportNodeC of me
	private ConcurrentHashMap<PeerNodeAddressI,NodeCOutboundPort> outPortsNodeC;
	//stock the neighber pairs connected with this pair and the outportCMpair of me
	private ConcurrentHashMap<PeerNodeAddressI,ContentManagementCIOutbound> outPortsCM;
	private ConcurrentHashMap<ContentTemplateI, ContentDescriptorI> contents;


	/**
	 * @param NMoutportUri	URI of the outbound port NM.
	 * @throws Exception		<i>todo.</i>
	 */
	protected Pair( String NMoutportUri ,String NMPortIn_facade)throws Exception {
		super(NMoutportUri, 0, 1);

		this.adress = new PeerNodeAddress("CMuriIn"+ ++cpt, "NodeCuriIn"+cpt);
		this.NMportOut =new ManagementOutboundPort(NMoutportUri, this) ;
		NMportOut.publishPort() ;
		this.NodePortIn = new NodeCIntboundPort(adress.getNodeUri() ,this);
		NodePortIn.publishPort();
		this.CMportIn = new ContentManagementCIIntbound(adress.getNodeidentifier(), this);
		CMportIn.publishPort();
		this.counter = 0 ;
		this.NMPortIn_facade = NMPortIn_facade;
		this.outPortsNodeC = new ConcurrentHashMap<PeerNodeAddressI,NodeCOutboundPort>();
		this.outPortsCM = new ConcurrentHashMap<PeerNodeAddressI,ContentManagementCIOutbound>();
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
	public PeerNodeAddressI connectPair (PeerNodeAddressI p ) throws Exception
	{
		if (p == null) {
			throw new Exception("Connection failed , pair is null");
		} else {

			System.out.println("\nreverse: pair "+ p.getNodeUri() +" demande de connecter avec pair "+ this.adress.getNodeUri()+ " en NodeCI");
			//do connect entre pair et pair en NodeCI
			doPortConnection(outPortsNodeC.get(p).getPortURI(), p.getNodeUri(), NodeC_conector.class.getCanonicalName());
			System.out.println("\nreverse:c'est ok " + p.getNodeUri() +" connecte avec "+ this.adress.getNodeUri() +" en "+ outPortsNodeC.get(p).getPortURI()+" en NodeCI" );

			//do connect entre pair et pair en ContentManagementCI
			System.out.println("\nreverse:pair "+ p.getNodeidentifier() +" demande de connecter avec pair "+ this.adress.getNodeidentifier()+ " en ContentManagementCI");
			doPortConnection(outPortsCM.get(p).getPortURI(), p.getNodeidentifier(), ContentManagementCIConector.class.getCanonicalName());
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
	public void disconnectePair (PeerNodeAddressI p ) throws Exception
	{

		System.out.println("\n"+ p.getNodeUri()+" demande de disconnecter avec pair "+ this.adress+ " en NodeCI\n");
		//get the outportNodeCI of this , which is connected with p
		NodeCOutboundPort NodeCportOut = outPortsNodeC.get(p);
		NodeCportOut.unpublishPort();
		outPortsNodeC.remove(p);
		this.doPortDisconnection(NodeCportOut.getPortURI());
		System.out.println("\nc'est ok "+ p.getNodeUri() +" disconnect  avec " + this.adress +" en NodeCI\n");


		//do disconnection en CM
		System.out.println("\n"+ p.getNodeUri()+" demande de disconnecter avec pair "+ this.adress+ " en ContentManagementCI\n");
		//get the outportCM of this , which is connected with p
		ContentManagementCIOutbound CMportOut = outPortsCM.get(p);
		CMportOut.unpublishPort();
		outPortsCM.remove(p);
		this.doPortDisconnection(CMportOut.getPortURI());
		System.out.println("\nc'est ok "+ p.getNodeUri() +" disconnect  avec " + this.adress +" en ContentManagementCI\n");




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
		Set<PeerNodeAddressI> neighbors = outPortsCM.keySet();
		if (neighbors == null) {
			return null;
		}
		for (PeerNodeAddressI pair : neighbors) {
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
			this.doPortConnection(NMportOut.getPortURI(), NMPortIn_facade,
					ManagementConnector.class.getCanonicalName());
		} catch (Exception e) {
			throw new ComponentStartException(e);
		}
	}

	@Override
	public void			execute() throws Exception
	{
		// application execution code (similar to a main method in Java) is
		// put here.

		this.logMessage("executing consumer component.") ;

		// Run the first service method invocation; the code of the method run
		// below will be executed asynchronously as a separate task, hence this
		// method execute will be free to finish its execution and free the
		// thread that is executing it.

		try {
			//do join
			Set<PeerNodeAddressI> liste = this.NMportOut.join(this.adress);


			if(liste.size() == 1) {
				System.out.println("the list of adress of pair in facade is empty!");
			}
			//do connection entre pair et pair en NodeCI
			for (PeerNodeAddressI p: liste ) {
				if(p != this.adress && p != null){
					counter++;
					String outportN = "myOutPortNodeCIpair"+ counter;
					NodeCOutboundPort NportOut = new NodeCOutboundPort(outportN, this);
					NportOut.publishPort();
					outPortsNodeC.put(p, NportOut);
					doPortConnection(NportOut.getPortURI(),
							p.getNodeUri(),
							NodeC_conector.class.getCanonicalName());
					System.out.println("\nc'est ok "+ p.getNodeUri() +" connect  avec " +this.adress.getNodeUri() +" en "+ NportOut.getPortURI()+ " en NodeCI");

					String outportCM = "myOutportCMpair" + ++cpt ;
					ContentManagementCIOutbound CMportOut = new ContentManagementCIOutbound(outportCM,this );
					CMportOut.publishPort();
					outPortsCM.put(p, CMportOut);
					doPortConnection(outportCM,
							p.getNodeidentifier(),
							ContentManagementCIConector.class.getCanonicalName());
					System.out.println("\nc'est ok "+ p.getNodeidentifier() +" connect  avec " +this.adress.getNodeidentifier() +" en "+ CMportOut.getPortURI() +" en ContentManagement");

					//PeerNodeAddressI myID = NportOut.connecte(this.adress);

				}
			}



			Thread.sleep(1000);

			/*//disconnect
			for (PeerNodeAddressI p:  outPortsNodeC.keySet()) {
				if(p != this){
					this.outPortsNodeC.get(p).disconnecte(p);
				}
			}*/
			/*while ( ! outPortsNodeC.isEmpty())
			{
				//wait
			}
			outPortsNodeC.clear();*/

			//leave
			NMportOut.leave(this.adress);




		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@Override
	public void	finalise() throws Exception
	{
		// Port disconnections can be done here for static architectures
		// otherwise, they can be done in the finalise methods of components.
		this.doPortDisconnection(NMportOut.getPortURI());
		super.finalise();
	}

}
