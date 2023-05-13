package withplugin;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import withplugin.components.Facade;
import withplugin.components.Pair;

public class DistributesCVM   extends  AbstractDistributedCVM{
	protected static final String	NMInboundPortURI = "inportNMfacade"; // 
	protected static final String	FacadeCMInPortFacadeURI = "inportFCMfacade";
	
	/** URI of the consumer inbound port (simplifies the connection).		*/
	protected static final String	FacadeCMInPortClientURI = "inportFCMclient";
	
	/** URI of the pair inbound port (simplifies the connection).		*/
	protected static final String	NodeCInPortPairURI = "inportNodeCpair";
	protected static final String	CMInPortPairURI = "inportCMpair";
	
	protected final int NB_PEER = 10;  
	protected final int NB_FACADE = 2 ;
	private String f;
	private String p;
	
	protected static final String	Facade_jvm = "Facade_jvm";
	protected static final String	Pair_jvm = "vecot_jvm";

	
	public DistributesCVM(String[] args) throws Exception {
		super(args);
	}
	
	
	// i have the boolen methode but not the void one  
	public void isIntantiatedAndPublish() {
		if (AbstractCVM.getThisJVMURI().equals(Facade_jvm))
		{
			 //creation du composatnt facade  i=1 pour le moment 
			this.f =AbstractComponent.createComponent(
					Facade.class.getCanonicalName(),
					new Object[]{1,
							NMInboundPortURI,
							FacadeCMInPortFacadeURI,
							NB_FACADE,// for the interconnection of facades
							FacadeCMInPortClientURI // for call accept in client
					});
		}else {
			if (AbstractCVM.getThisJVMURI().equals(Pair_jvm))
			{
				//creation du composatnt Pair   i=1 pour le moment 
				this.p=AbstractComponent.createComponent(
						Pair.class.getCanonicalName(),
						new Object[]{1,
								NodeCInPortPairURI,
								CMInPortPairURI,
								NMInboundPortURI+ 1%NB_FACADE // choose a facade
								});
			}else {
						System.out.println("erreur ");
			}
		}
		  super.isIntantiatedAndPublish();
	  
	
	}
	
	@Override
	public void interconnect() throws Exception {
		if (AbstractCVM.getThisJVMURI().equals(Facade_jvm))
		{
			// doPort connection 			
		}
		super.interconnect();
	}



	public static void		main(String[] args)
	{
		try {
			DistributesCVM dcvm = new DistributesCVM(args);
			dcvm.startStandardLifeCycle(50000L);
			Thread.sleep(10000L);	
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
