package withplugin.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import classes.ContentDescriptor;
import classes.ContentNodeAdress;
import connector.ContentManagementConector;
import connector.FacadeContentManagementConector;
import connector.NodeConnector;
import connector.NodeManagementConnector;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.AbstractPort;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionCI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import fr.sorbonne_u.cps.p2Pcm.dataread.ContentDataManager;
import fr.sorbonne_u.utils.aclocks.ClocksServerOutboundPort;
import interfaces.ApplicationNodeAdressI;
import interfaces.ContentDescriptorI;
import interfaces.ContentNodeAddressI;
import interfaces.ContentTemplateI;
import interfaces.MyCMI;
import interfaces.MyThreadServiceI;
import interfaces.NodeCI;
import interfaces.NodeManagementCI;
import ports.ContentManagementCIOutbound;
import ports.FacadeContentManagementCOutbound;
import ports.NodeCOutboundPort;
import ports.NodeManagementOutboundPort;
import withplugin.ports.ContentManagementCIIntboundPlugin;
import withplugin.ports.NodeCIntboundPortForPlugin;
import withplugin.CVM;

public class PairPlugin extends AbstractPlugin implements MyCMI {
	
		private static final long serialVersionUID = 1L;
		
		// -------------------------------------------------------------------------
		// Plug-in variables and constants
		// -------------------------------------------------------------------------
	
		
		private NodeCIntboundPortForPlugin	inPortNodeC ;
		private ContentManagementCIIntboundPlugin inPortCM;
		
		
		//out port to call acceptFound and acceptMatched in facade initial
		private FacadeContentManagementCOutbound outPortFCM ;
		//out port to call acceptProbed in facade initial
		private NodeManagementOutboundPort outPortNM ;
		
		private ContentNodeAdress adress;
		private ArrayList<ContentDescriptorI> contents;
		
		private Set<ContentNodeAddressI> voisins;
		
		//stock the neighbor pairs connected with this and the outPortNodeCodeC
		private ConcurrentHashMap<ContentNodeAddressI,NodeCOutboundPort> outPortsNodeC;  
		
		//stock the neighbor pairs connected with this pair and the outportCMpair of me
		//ContentMangement 
		private ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound> outPortsCM;
		
		private boolean flag;

		
		// -------------------------------------------------------------------------
		// Life cycle
		// -------------------------------------------------------------------------
		
		/**
		 * create a pair plug-in.
		 * 
		 * <p><strong>Contract</strong></p>
		 * 
		 * <pre>
		 * pre	{@code executorServiceURI != null}
		 * post	true			// no postcondition.
		 * </pre>
		 * @throws Exception 
		 *
		 */
		public	PairPlugin(ContentNodeAdress adress, ArrayList<ContentDescriptorI> contents ) throws Exception
		{
			super();
			
			this.contents = contents;
			this.adress = adress;
			
			this.outPortsNodeC = new ConcurrentHashMap<ContentNodeAddressI,NodeCOutboundPort>();
			this.outPortsCM = new ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound>();
			this.voisins = new HashSet<ContentNodeAddressI>();
			this.flag = false;
		
		}
		
		
		@Override
		public void installOn(ComponentI owner) throws Exception {
		    super.installOn(owner);
		    
		    // Add the interface
		    this.addOfferedInterface(NodeCI.class);
		    this.addRequiredInterface(NodeCI.class);
		    this.addRequiredInterface(NodeManagementCI.class);
		
		  }
		
		@Override
		public void initialise() throws Exception {
			 super.initialise();
			 
			 // Create the in bound port
			 this.inPortNodeC = new NodeCIntboundPortForPlugin(adress.getNodeUri(),
					 this.getOwner(),
					 this.getPluginURI(), 
					 ((MyThreadServiceI)this.getOwner()).
		    		 get_THREAD_POOL_URI());
			 inPortNodeC.publishPort();
			 
			 this.inPortCM = new ContentManagementCIIntboundPlugin(adress.getContentManagementURI(),
					 this.getOwner(),
					 this.getPluginURI(),
					 ((MyThreadServiceI)this.getOwner()).
		    		 get_CM_THREAD_POOL_URI());
			 inPortCM.publishPort();
			 
			 //Create the out bound port
			 this.outPortNM = new NodeManagementOutboundPort(this.getOwner());
			 outPortNM.publishPort();
			 this.outPortFCM = new FacadeContentManagementCOutbound(this.getOwner());
	         outPortFCM.publishPort();
		}
		
		@Override
		public void			finalise() throws Exception
		{
			this.contents.clear();
			this.contents = null;
			super.finalise();
		}
		
		@Override
		public void			uninstall() throws Exception
		{
			
			this.inPortNodeC.unpublishPort();
			this.inPortCM.unpublishPort();
			
			// remove the interface
		    this.removeOfferedInterface(NodeCI.class);
		    this.removeOfferedInterface(NodeCI.class);
		    this.removeOfferedInterface(NodeManagementCI.class);
		}
		
		
		// -------------------------------------------------------------------------
		// Plug-in services implementation
		// -------------------------------------------------------------------------
		
		
	
		public void probe(ApplicationNodeAdressI facadeInitial, int remainingHops, String requestURI) throws Exception {
		    //System.out.println(requestURI + " demande do prob in " + this.adress.getNodeidentifier() + " hops " + remainingHops);
		    if (remainingHops <= 0 || voisins.isEmpty()) {
		        // Return its own address to the facade that initiated the probe
		    	
		        this.getOwner().doPortConnection(outPortNM.getPortURI(),
		                facadeInitial.getNodeManagementUri(),
		                NodeManagementConnector.class.getCanonicalName());
			      		    	
		        outPortNM.acceptProbed(this.adress, requestURI);
		        outPortNM.doDisconnection();		       
		    } else {
		        // If there are neighbors
		        ContentNodeAddressI neighbor = getRandomNeighbor();
		        NodeCOutboundPort outPortNodeC = outPortsNodeC.get(neighbor);
		        outPortNodeC.probe(facadeInitial, remainingHops - 1, requestURI);
		    }
		}
		
		
		public void acceptNeighbours(Set<ContentNodeAddressI> neighbours) throws Exception {
		   
		    if (neighbours.isEmpty()) {
		        //System.out.println("\n" + adress.getNodeidentifier() + " says : I don't have neighbors yet!");
		    } else {
		        for (ContentNodeAddressI neighbor : neighbours) {
		            //System.out.println("I am " + this.adress.getNodeidentifier() + " I have neighbor: " + neighbor.getNodeidentifier());
		            connectToNode(neighbor);
		            // call neighbor to connect this
		            outPortsNodeC.get(neighbor).connect(this.adress);
		        }
		    }
		}
		
		public void connect(ContentNodeAddressI newNodeAddress) throws Exception {
			
		    int leastNeighborCount = Integer.MAX_VALUE;
		    ContentNodeAddressI leastNeighbor = null;
		    
		    if (!voisins.isEmpty()) {
		    	for (ContentNodeAddressI neighbor : voisins) {
			        NodeCOutboundPort neighborOutPort = outPortsNodeC.get(neighbor);
			        int neighborCount = neighborOutPort.getNeighborCount();
			        
			        if (neighborCount < leastNeighborCount) {
			            leastNeighbor = neighbor;
			            leastNeighborCount = neighborCount;
			        }
			    }
			}else {
				System.out.println(this.adress.getNodeidentifier() +" not has neighbor yet ,can not pass "+ newNodeAddress.getNodeidentifier()+" to leastNeighbor");
			}
		    
		    
		    if (leastNeighbor != null && leastNeighborCount < outPortsNodeC.get(adress).getNeighborCount()) {
		    	System.out.println("leastNeighbor of "+this.adress.getNodeidentifier()+ " is "+leastNeighbor.getNodeidentifier()+" "+leastNeighborCount);
		        NodeCOutboundPort leastNeighborOutPort = outPortsNodeC.get(leastNeighbor);
		        
		        leastNeighborOutPort.connect(newNodeAddress);
		        System.out.println("send to leastNeighbor");
		    } else {
		        // Connect to the new node directly
		        connectToNode(newNodeAddress);
		        synchronized (voisins) {
			    	//System.out.println(adress.getNodeidentifier()+ " has new neighbor : "+ neighbor.getNodeidentifier());
			        voisins.add(newNodeAddress);
			    }
		    }
		}

		
		
		public void acceptConnected(ContentNodeAddressI p) throws Exception {
			synchronized (voisins) {
				//System.out.println(adress.getNodeidentifier()+ " has new neighbor : "+ p.getNodeidentifier());
				voisins.add(p);
		    }
		}


		
		public void disconnect(ContentNodeAddressI p) throws Exception {
			//disconnect pair and pair in NodeC and CM
			
			//get the outPortNodeC of this , which is connected with p
		    NodeCOutboundPort outPortNodeC= outPortsNodeC.get(p);
		    if (outPortNodeC!=null) { 
		    	this.getOwner().doPortDisconnection(outPortNodeC.getPortURI());
				outPortNodeC.unpublishPort();
				outPortsNodeC.remove(p);
				
				//get the outportContentManagementCI of this , which is connected with p			
				ContentManagementCIOutbound outportCM= outPortsCM.get(p);
				this.getOwner().doPortDisconnection(outportCM.getPortURI());
				outportCM.unpublishPort();
				outPortsCM.remove(p);
				System.out.println("\nc'est ok "+this.adress.getNodeidentifier() +" --disconnect--> " + p.getNodeidentifier());
				voisins.remove(p);
		    }	
			
		}
		
		// -------------------------------------------------------------------------
		// Plug-in services implementation
		// -------------------------------------------------------------------------
	
		@Override
		public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
		    //System.out.println("\nwill do find in "+this.adress.getNodeidentifier());
		
			// Check among its own contents
		    for (ContentDescriptorI content : this.contents) {
		        if (content.match(cd)) {
		            System.out.println("found in " + this.adress.getNodeidentifier());
		            // create connection enter this pair and requester initial
		            outPortFCM.publishPort();

		            this.getOwner().doPortConnection(outPortFCM.getPortURI(),
		                    requester.getContentManagementURI(),
		                    FacadeContentManagementConector.class.getCanonicalName());

		            outPortFCM.acceptFound(content, this.adress.getNodeidentifier());
		            return;
		        }
		    }
			    
		    // >1 because it first check its own contents then check the remaining hops
		    if (hops > 1) {
		        // Check among neighbors
		        System.out.println(this.adress.getNodeidentifier() + " not found ");

		        if (voisins.isEmpty()) {
		            System.out.println(this.adress.getNodeidentifier() + " has no neighbors");
		        } else {
		            Set<ContentNodeAddressI> randomNeighbors = getRandomSubset(voisins, 3);
		            for (ContentNodeAddressI voisin : randomNeighbors) {
		            	 ContentManagementCIOutbound outportCM = outPortsCM.get(voisin);
	                     outportCM.find(cd, hops - 1, requester, voisin.getNodeidentifier());
		            }

		        }

		    }
		}

		
		@Override
		public void match(ContentTemplateI cd, Set<ContentDescriptorI> matched, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
			System.out.println("\nwill do find in "+this.adress.getNodeidentifier());

		    // Check among its own contents
		    for (ContentDescriptorI content : this.contents) {
		        if (content.match(cd)) {
		            System.out.println("matched in " + this.adress.getNodeidentifier());
		            // create connection enter this pair and requester initial
		            outPortFCM.publishPort();

		            this.getOwner().doPortConnection(outPortFCM.getPortURI(),
		                    requester.getContentManagementURI(),
		                    FacadeContentManagementConector.class.getCanonicalName());

		            outPortFCM.acceptMatched(matched, this.adress.getNodeidentifier());
		            return;
		        }
		    }
		    
		    // >1 because it first check its own contents then check the remaining hops
		    if (hops > 1) {
		        // Check among neighbors
		        System.out.println(this.adress.getNodeidentifier() + " not matched ");

		        if (voisins.isEmpty()) {
		            System.out.println(this.adress.getNodeidentifier() + " has no neighbors");
		        } else {
		            Set<ContentNodeAddressI> randomNeighbors = getRandomSubset(voisins, 3);
		           
		            for (ContentNodeAddressI voisin : randomNeighbors) {
		            	ContentManagementCIOutbound outportCM = outPortsCM.get(voisin);
                        outportCM.match(cd, matched, hops -1, requester, voisin.getNodeidentifier());
		            }
		        }

		    }
		}

		private void connectToNode(ContentNodeAddressI neighbor) throws Exception {
			// connect in NodeCI
		    String outPortNodeCURI = AbstractOutboundPort.generatePortURI();
		    NodeCOutboundPort outPortNodeC = new NodeCOutboundPort(outPortNodeCURI, this.getOwner());
		    outPortNodeC.publishPort();
		    outPortsNodeC.put(neighbor, outPortNodeC);
		    this.getOwner().doPortConnection(outPortNodeC.getPortURI(), 
		    		neighbor.getNodeUri(),
		    		NodeConnector.class.getCanonicalName());

		    // connect in ContentManagementCI
		    String outPortCMURI = AbstractOutboundPort.generatePortURI();
		    ContentManagementCIOutbound outPortCM = new ContentManagementCIOutbound(outPortCMURI, this.getOwner());
		    outPortCM.publishPort();
		    outPortsCM.put(neighbor, outPortCM);
		    this.getOwner().doPortConnection(outPortCM.getPortURI(),
		    		neighbor.getContentManagementURI(), 
		    		ContentManagementConector.class.getCanonicalName());

		    System.out.println(this.adress.getNodeidentifier() + " -> " + neighbor.getNodeidentifier());
		}

		private ContentNodeAddressI getRandomNeighbor() {
		    int randomIndex = new Random().nextInt(voisins.size());
		    Iterator<ContentNodeAddressI> iterator = voisins.iterator();
		    for (int i = 0; i < randomIndex; i++) {
		        iterator.next();
		    }
		    return iterator.next();
		}

		private Set<ContentNodeAddressI> getRandomSubset(Set<ContentNodeAddressI> set, int subsetSize) {
		    List<ContentNodeAddressI> list = new ArrayList<>(set);
		    Collections.shuffle(list);

		    int actualSubsetSize = Math.min(subsetSize, set.size());
		    return new HashSet<>(list.subList(0, actualSubsetSize));
		}


		public Integer getNeighborCount() {			
			return voisins.size();
		}

		

}
