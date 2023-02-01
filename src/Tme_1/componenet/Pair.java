package Tme_1.componenet;

import java.util.Set;

import Tme_1.interfaces.PeerNodeAddressI;
import fr.sorbonne_u.components.AbstractComponent;

/**
 * @author lyna & shuhan 
 *
 */
public class Pair  extends AbstractComponent{
	
	/**   
	* @Function: Pair.java
	* @Description: 
	*
	* @param: NodeUri
	* @param: nbThreads
	* @param: nbSchedulableThreads
	* @version: 
	* @author: lyna & shuhan 
	* @date: 30 janv. 2023 20:34:23 
	*/
	protected Pair(String NodeUri, int nbThreads, int nbSchedulableThreads) {
		super(NodeUri, nbThreads, nbSchedulableThreads);
	}

	protected String NodeUri;
	
	
	/**   
	* @Function: Pair.java
	* @Description: 
	*
	* @param: PeerNodeAddressI p
	* @return：Set<PeerNodeAddressI>
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 30 janv. 2023 20:34:57 
	*
	* 
	*/
	public Set<PeerNodeAddressI>  Join   (PeerNodeAddressI p) 
	throws Exception{
	   System.out.print("c'est ok join ");
	   return  null; 
	}
	
	/**   
	* @Function: Pair.java
	* @Description: 
	*
	* @param: PeerNodeAddressI p
	* @return：void
	* @throws：Exception
	*
	* @version: v1.0.0
	* @author: lyna & shuhan 
	* @date: 30 janv. 2023 20:35:22 
	*
	* 
	*/
	public void leave (PeerNodeAddressI p)
	throws Exception{
	  System.out.print("c'est ok leave  ");
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
		this.runTask(
			new AbstractComponent.AbstractTask() {
				@Override
				public void run() {
					try {
						((Pair)this.getTaskOwner()).Join(null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}) ;
	}

}
