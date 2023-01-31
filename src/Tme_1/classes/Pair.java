package Tme_1.classes;

import java.util.Set;

import Tme_1.interfaces.PeerNodeAddressI;
import fr.sorbonne_u.components.AbstractComponent;

public class Pair  extends AbstractComponent{
	protected Pair(String NodeUri, int nbThreads, int nbSchedulableThreads) {
		super(NodeUri, nbThreads, nbSchedulableThreads);
	}

	protected String NodeUri;
	
	
	public Set<PeerNodeAddressI>  join   ( PeerNodeAddressI p) {
	   System.out.print("c'est ok  join ");
	   return  null; 
	}
  public void leave (PeerNodeAddressI p)
  {
	  System.out.print("c'est ok leave  ");
  }

}
