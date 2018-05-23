package org.insa.algo.carpooling;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.algo.AbstractInputData.Mode;
import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.carpooling.CarPoolingData;
import org.insa.algo.carpooling.CarPoolingSolution;
import org.insa.algo.utils.BinaryHeap;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Label;
import org.insa.graph.LabelCarPool;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class AStarCarPooling extends CarPoolingAlgorithm {

	protected AStarCarPooling(CarPoolingData data) {
		super(data);
	}

	@Override
	protected CarPoolingSolution doRun() {
    	// variable when all nodes are marked
    	int countMarques = 0;
    	
    	// we need two heaps for each origin
    	BinaryHeap<Label> cpPedHeap = new BinaryHeap<>();
    	BinaryHeap<Label> spPedHeap = new BinaryHeap<>();
    	BinaryHeap<Label> cpCarHeap = new BinaryHeap<>();
    	BinaryHeap<Label> spCarHeap = new BinaryHeap<>();
    	
    	// father node of meeting node used to create second path
    	Node fatherNode = null;
    	
        CarPoolingData data = getInputData();
        CarPoolingSolution solution = null;  
        
        Graph graph = data.getGraph();
        
    	// Generate labels
        ArrayList<LabelCarPool> labels = new ArrayList<LabelCarPool>();
		for (int i = 0; i < graph.size(); i++) {
			labels.add(new LabelCarPool(false, graph.get(i), null, Double.POSITIVE_INFINITY));
		}
        
    	final int nbNodes = graph.size();

        // Notify observers about the first event (both origins processed).
     	notifyOriginProcessed(data.getOriginPed());
     	notifyOriginProcessed(data.getOriginCar());
     	
     	// initialize algorithm
     	LabelCarPool.setDest(data.getDestination());
     	// for shortest time, set maximum speed in order to have lower bound
     	if (data.getMode() == Mode.TIME) 
     		LabelCarPool.setMaxSpeed(graph.getGraphInformation().getMaximumSpeed());	
    	// get Label of both origins
     	LabelCarPool originPedLabel = labels.get(data.getOriginPed().getId());
     	LabelCarPool originCarLabel = labels.get(data.getOriginCar().getId());
     	// set other Node for labels
     	originPedLabel.setOther(originCarLabel.getSommetCourant());
     	originCarLabel.setOther(originPedLabel.getSommetCourant());
     	// set the cost of the origins nodes to 0
     	originPedLabel.setCout(0);
     	originCarLabel.setCout(0);
     	
     	// add origins to both heaps
     	spPedHeap.insert(originPedLabel);
     	spCarHeap.insert(originCarLabel);
     	cpPedHeap.insert(originPedLabel);
     	cpCarHeap.insert(originCarLabel);
     	
 		LabelCarPool xPedLabel = null;
 		LabelCarPool xCarLabel = null;
     	
     	// we have to visit each node, and stop the algorithm if
     	// ...no predecessors are found
     	while (countMarques < nbNodes && !spPedHeap.isEmpty() && !cpPedHeap.isEmpty() && spCarHeap.isEmpty() && !cpCarHeap.isEmpty()) {
     		countMarques++;
     		
     		// retrieve minimum cost node for each origin
     		if(xPedLabel.getCout() < xPedLabel.getCoutMidPointDest() || xPedLabel == null) {
     			xPedLabel = (LabelCarPool)spPedHeap.deleteMin();
     		}
     		else {
     			xPedLabel = (LabelCarPool)cpPedHeap.deleteMin();
     		}
     		
     		if(xCarLabel.getCout() < xCarLabel.getCoutMidPointDest() || xCarLabel == null) {
     			xCarLabel = (LabelCarPool)spCarHeap.deleteMin();
     		}
     		else {
     			xCarLabel = (LabelCarPool)cpCarHeap.deleteMin();
     		}
     		
     		//if the nodes are destination, we stop
 			if(xPedLabel.getSommetCourant() == data.getDestination() && xCarLabel.getSommetCourant() == data.getDestination()) {
 				countMarques = nbNodes;
 			}
 			
 			// if the two nodes are the same set meeting node
 			if(xPedLabel.getSommetCourant() == xCarLabel.getSommetCourant())
 				LabelCarPool.setMeetingNode(xPedLabel.getSommetCourant());
 			
 			//set father of meeting Node to create second path
 			fatherNode = xCarLabel.getPere();
     		
     		// mark the minimum node
     		xPedLabel.setMarque();
     		
     		// notify that we have marked it 
     		notifyNodeMarked(xPedLabel.getSommetCourant());
     		     		
     		// iterate over successors
     		for (Arc arc : xPedLabel.getSommetCourant()) {
     			// get the successor node
     			Node y = arc.getDestination();
     			
     			// Small test to check allowed roads...
				if (!data.isAllowed(arc)) {
					continue;
				}
     			
         		// notify that we have reached it 
         		notifyNodeReached(xPedLabel.getSommetCourant());
     			
     			LabelCarPool yLabel = labels.get(y.getId());
     			
     			// set other Node on yLabel
     			yLabel.setOther(xCarLabel.getSommetCourant());
     			
     			// set destination cost for Label     			
     			if (!yLabel.getMarque()) {
     				double yCout = yLabel.getCoutSansDest();
     				double xCout = xPedLabel.getCoutSansDest();
     				yLabel.setCout(Math.min(yCout, xCout + data.getCost(arc)));
     				
     				if (yLabel.getCoutSansDest() != yCout) { 
     					// insert node in heap
     					if (yCout == Double.POSITIVE_INFINITY) {
     						if(yLabel.getCout() < yLabel.getCoutMidPointDest()) {
     							spPedHeap.insert(yLabel);
     						}
     						else {
     							cpPedHeap.insert(yLabel);
     						}
     					}     					
     					// set the father parameter	
     					yLabel.setPere(xPedLabel.getSommetCourant());     					
     					// set father arc
     					yLabel.setArc(arc);
     				}         				
     			}         			
     		}
     		// we only explore both paths if they have not met each other
     		if(LabelCarPool.getMeetingNode() == null) {         
         		// mark the minimum nodes
         		xCarLabel.setMarque();
         		
         		// notify that we have marked them 
         		notifyNodeMarked(xCarLabel.getSommetCourant());
         		         		
         		// then we iterate over the successors of the second node
         		for (Arc arc : xCarLabel.getSommetCourant()) {
         			// get the successor node
         			Node y = arc.getDestination();
         			
         			// Small test to check allowed roads...
    				if (!data.isAllowed(arc)) {
    					continue;
    				}
         			
             		// notify that we have reached it 
             		notifyNodeReached(xCarLabel.getSommetCourant());
         			
         			LabelCarPool yLabel = labels.get(y.getId());  
         			// set other Node for yLabel
         			yLabel.setOther(xPedLabel.getSommetCourant());
         			         	
         			// set destination cost for Label
         			if (!yLabel.getMarque()) {
         				double yCout = yLabel.getCoutSansDest();
         				double xCout = xPedLabel.getCoutSansDest();
         				yLabel.setCout(Math.min(yCout, xCout + data.getCost(arc)));
         				
         				if (yLabel.getCoutSansDest() != yCout) { 
         					// insert node in heap
         					if (yCout == Double.POSITIVE_INFINITY) {
         						if(yLabel.getCout() < yLabel.getCoutMidPointDest()) {
         							spCarHeap.insert(yLabel);
         						}
         						else {
         							cpCarHeap.insert(yLabel);
         						}
         					}     					
         					// set the father parameter	
         					yLabel.setPere(xCarLabel.getSommetCourant());     					
         					// set father arc
         					yLabel.setArc(arc);
         				}         				
         			}         			
         		}
     		}     		
     	}
        // The destination has been found, notify the observers.
     	notifyDestinationReached(data.getDestination());		

		// Destination has no predecessor, the solution is infeasible...
		if (labels.get(data.getDestination().getId()).getPere() == null) {
			solution = new CarPoolingSolution(data, Status.INFEASIBLE);
		} else {
			// The destination has been found, notify the observers.
			notifyDestinationReached(data.getDestination());

			// Create the path from the array of predecessors...
			ArrayList<Arc> arcs1 = new ArrayList<>();
			ArrayList<Arc> arcs2 = new ArrayList<>();
			
			// start with the destination node label
			LabelCarPool currentLabel = labels.get(data.getDestination().getId());
			//creating path from destination to first origin
			while (currentLabel.getPere() != null) {
				arcs1.add(currentLabel.getArc());
				currentLabel = labels.get(currentLabel.getPere().getId());
			}
			
			//if the paths have met, create path from meeting point to second origin
			if(LabelCarPool.getMeetingNode() != null) {
				//start with meeting node
				LabelCarPool current2Label = labels.get(LabelCarPool.getMeetingNode().getId());
				//creating path from meeting node to second origin
				arcs2.add(current2Label.getArc());
				
				current2Label = labels.get(fatherNode.getId());
				while(current2Label.getPere() != null) {
					arcs2.add(current2Label.getArc());
					current2Label = labels.get(current2Label.getPere().getId());
				}
			}
			//if the paths have not met, create path from destination to second origin
			else {
				// start with the destination node label
				LabelCarPool current2Label = labels.get(data.getDestination().getId());
				//creating path from destination to second origin
				while (current2Label.getPere() != null) {
					arcs1.add(current2Label.getArc());
					current2Label = labels.get(current2Label.getPere().getId());
				}
			}

			// Reverse the paths...
			Collections.reverse(arcs1);
			Collections.reverse(arcs2);

			// Create the final solution.
			solution = new CarPoolingSolution(data, Status.OPTIMAL, new Path(graph, arcs1), new Path(graph, arcs2));
		} 
     	
        return solution;
    }

}

