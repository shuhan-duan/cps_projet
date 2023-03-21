package interfaces;

import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface ProbingCI extends RequiredCI , OfferedCI{
	public void probe(ApplicationNodeAdressI facade , int remainghops , String requestURI ) throws Exception;
	

}
