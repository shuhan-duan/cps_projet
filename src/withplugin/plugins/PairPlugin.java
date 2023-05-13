package withplugin.plugins;

import java.util.ArrayList;
import java.util.Collections;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import classes.ContentNodeAdress;
import connector.ContentManagementConector;
import connector.FacadeContentManagementConector;
import connector.NodeConnector;
import connector.NodeManagementConnector;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
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
		
		
		//stock the neighbor pairs connected with this and the outPortNodeCodeC
		private ConcurrentHashMap<ContentNodeAddressI,NodeCOutboundPort> outPortsNodeC;  
		
		//stock the neighbor pairs connected with this pair and the outportCMpair of this
		//ContentMangement 
		private ConcurrentHashMap<ContentNodeAddressI,ContentManagementCIOutbound> outPortsCM;
 
		private static ConcurrentHashMap<String, Boolean> visitedPairs = new ConcurrentHashMap<>();
		private static AtomicBoolean found = new AtomicBoolean(false);
		
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
		    if (remainingHops <= 0 || outPortsNodeC.isEmpty()) {
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
		
		//there is a problem because of the async task , the list of neighbors is updated lately 
		public void probe(ApplicationNodeAdressI facadeInitial, int remainingHops, String requestURI, ContentNodeAddressI leastNeighbor, int leastNeighborCount) throws Exception {
		    if (remainingHops <= 0 || outPortsNodeC.isEmpty()) {
		        // Return the address of the least neighbor with minimum count to the facade that initiated the probe
		        if (leastNeighbor != null) {
		            this.getOwner().doPortConnection(outPortNM.getPortURI(),
		                    facadeInitial.getNodeManagementUri(),
		                    NodeManagementConnector.class.getCanonicalName());
		            outPortNM.acceptProbed(leastNeighbor, requestURI);
		            outPortNM.doDisconnection();
		        } else {
		            // If no least neighbor found, return its own address
		        	//System.out.println("no least neighbor for "+ this.adress.getNodeidentifier());
		            this.getOwner().doPortConnection(outPortNM.getPortURI(),
		                    facadeInitial.getNodeManagementUri(),
		                    NodeManagementConnector.class.getCanonicalName());
		            outPortNM.acceptProbed(this.adress, requestURI);
		            outPortNM.doDisconnection();
		        }
		    } else {
		        // If there are neighbors
		        ContentNodeAddressI neighbor = getRandomNeighbor();
		        NodeCOutboundPort outPortNodeC = outPortsNodeC.get(neighbor);
		        
		        // Update the least neighbor information
		        int neighborCount = outPortsNodeC.get(neighbor).getNeighborCount();
		        if (leastNeighbor == null || neighborCount < leastNeighborCount) {
		            leastNeighbor = neighbor;
		            leastNeighborCount = neighborCount;
		        }
		        
		        outPortNodeC.probe(facadeInitial, remainingHops - 1, requestURI, leastNeighbor, leastNeighborCount);
		    }
		}

		
		public void acceptNeighbours(Set<ContentNodeAddressI> neighbours) throws Exception {
		   
		    if (neighbours.isEmpty()) {
		        //System.out.println("\n" + adress.getNodeidentifier() + " says : I don't have neighbors yet!");
		    } else {
		        for (ContentNodeAddressI neighbor : neighbours) {
		            //System.out.println("I am " + this.adress.getNodeidentifier() + " I have neighbor: " + neighbor.getNodeidentifier());
		            
		            // connect to neighbors
		            this.connect(neighbor);
		        }
		    }
		}
		
		public void connect(ContentNodeAddressI newNodeAddress) throws Exception {
			connectToNode(newNodeAddress);
			//call neighbor to connect this
			NodeCOutboundPort outPortNodeC= outPortsNodeC.get(newNodeAddress);
			outPortNodeC.acceptConnected(this.adress);
		}

		
		
		public void acceptConnected(ContentNodeAddressI p) throws Exception {
			connectToNode(p);
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
		    }	
			
		}
		
		// -------------------------------------------------------------------------
		// Plug-in services implementation
		// -------------------------------------------------------------------------
	
		public void find(ContentTemplateI cd, int hops, ApplicationNodeAdressI requester, String requestURI) throws Exception {
	        synchronized (PairPlugin.class) {
	            if (found.get()) {
	                return;
	            }

	            for (ContentDescriptorI content : this.contents) {
	                if (content.match(cd)) {
	                    System.out.println("found in " + this.adress.getNodeidentifier());
	                    this.getOwner().doPortConnection(outPortFCM.getPortURI(),
	                            requester.getContentManagementURI(),
	                            FacadeContentManagementConector.class.getCanonicalName());

	                    outPortFCM.acceptFound(content, this.adress.getNodeidentifier());
	                    found.set(true);
	                    return;
	                }
	            }

	            if (hops > 1 && !found.get()) {
	                System.out.println(this.adress.getNodeidentifier() + " not found ");

	                if (outPortsCM.isEmpty()) {
	                    System.out.println(this.adress.getNodeidentifier() + " has no neighbors");
	                } else {
	                    Set<ContentNodeAddressI> randomNeighbors = getRandomSubset(outPortsNodeC.keySet(), 3);
	                    for (ContentNodeAddressI voisin : randomNeighbors) {
	                    	Boolean alreadyVisited = visitedPairs.putIfAbsent(voisin.getNodeidentifier(), true);
	                        if (alreadyVisited == null || !alreadyVisited) {
	                            visitedPairs.put(voisin.getNodeidentifier(), true);
	                            ContentManagementCIOutbound outportCM = outPortsCM.get(voisin);
	                            outportCM.find(cd, hops - 1, requester, voisin.getNodeidentifier());
	                        }
	                    }
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

		        if (outPortsNodeC.isEmpty()) {
		            System.out.println(this.adress.getNodeidentifier() + " has no neighbors");
		        } else {
		            Set<ContentNodeAddressI> randomNeighbors = getRandomSubset(outPortsNodeC.keySet(), 3);
		           
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
		    int randomIndex = new Random().nextInt(outPortsNodeC.size());
		    Iterator<ContentNodeAddressI> iterator = outPortsNodeC.keySet().iterator();
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
			return outPortsNodeC.size();
		}

		

}
