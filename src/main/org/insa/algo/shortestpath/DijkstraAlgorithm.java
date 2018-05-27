package org.insa.algo.shortestpath;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;


import org.insa.algo.AbstractSolution.Status;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Label;
import org.insa.graph.Node;
import org.insa.graph.Path;
import org.insa.algo.utils.BinaryHeap;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
       	
    	// variable when all nodes are marked
    	int countMarques = 0;
    	
    	BinaryHeap<Label> heap = new BinaryHeap<Label>();
        ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;  
        
        Graph graph = data.getGraph();
        
    	// Generate labels
		ArrayList<Label> labels = Label.genLabels(graph);
        
    	final int nbNodes = graph.size();

        // Notify observers about the first event (origin processed).
     	notifyOriginProcessed(data.getOrigin());	
     	
     	// initialize algorithm
     	
     	// set the cost of the origin node to 0
     	
     	// get Label of origin
     	Label oLabel = labels.get(data.getOrigin().getId());
     	oLabel.setCout(0);
     	
     	
     	// add origin to heap
     	heap.insert(oLabel);
     	
     	
     	
     	// we have to visit each node, and stop the algorithm if
     	// ...no predecessors are found
     	while (countMarques < nbNodes && !heap.isEmpty()) {
     		countMarques++;
     		

     		// retrieve minimum cost node
     		Label xLabel = heap.deleteMin();
     		
     		//if current node is destination, we stop
 			if(xLabel.getSommetCourant() == data.getDestination()) {
 				break;
 			}
     		
     		// mark the minimum node
     		xLabel.setMarque();
     		
     		// notify that we have marked it 
     		notifyNodeMarked(xLabel.getSommetCourant());
     		
     		
     		// iterate over successors
     		for (Arc arc : xLabel.getSommetCourant()) {
     			// get the successor node
     			Node y = arc.getDestination();
     			
     			// Small test to check allowed roads...
				if (!data.isAllowed(arc)) {
					continue;
				}
     			
         		// notify that we have reached it 
         		notifyNodeReached(xLabel.getSommetCourant());
     			
     			Label yLabel = labels.get(y.getId());
     			
     			if (!yLabel.getMarque()) {
     				double yCout = yLabel.getCout();
     				double xCout = xLabel.getCout();
     				
     				yLabel.setCout(Math.min(yCout, xCout + data.getCost(arc)));
     				
     				if (yLabel.getCout() != yCout) {
     					// insert node in heap	 	
     					if (yCout == Double.POSITIVE_INFINITY) {
     						heap.insert(yLabel);
     					}
     					//set the father parameter
     					yLabel.setPere(xLabel.getSommetCourant());
     					
     					// set father arc
     					yLabel.setArc(arc);
     				}     				
     			}     			
     		}     		
     	}
        // The destination has been found, notify the observers.
     	notifyDestinationReached(data.getDestination());		

		// Destination has no predecessor, the solution is infeasible...
		if (labels.get(data.getDestination().getId()).getPere() == null) {
			solution = new ShortestPathSolution(data, Status.INFEASIBLE);
		} else {

			// The destination has been found, notify the observers.
			notifyDestinationReached(data.getDestination());

			// Create the path from the array of predecessors...
			ArrayList<Arc> arcs = new ArrayList<>();
			
			// start with the destination node label
			Label currentLabel = labels.get(data.getDestination().getId());
			while (currentLabel.getPere() != null) {
				arcs.add(currentLabel.getArc());
				currentLabel = labels.get(currentLabel.getPere().getId());
			}

			// Reverse the path...
			Collections.reverse(arcs);

			// Create the final solution.
			solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs), countMarques);
		} 
     	
        return solution;
    }

}
