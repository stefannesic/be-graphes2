package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Label;
import org.insa.graph.LabelStar;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    @Override
    protected ShortestPathSolution doRun() {
   
    	
    	// variable when all nodes are marked
    	int countMarques = 0;
    	
    	BinaryHeap<Label> heap = new BinaryHeap<>();
        ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;  
        
        Graph graph = data.getGraph();
        
    	// Generate labels
        ArrayList<LabelStar> labels = new ArrayList<LabelStar>();
		for (int i = 0; i < graph.size(); i++) {
			labels.add(new LabelStar(false, graph.get(i), null, Double.POSITIVE_INFINITY));
		}
        
    	final int nbNodes = graph.size();

        // Notify observers about the first event (origin processed).
     	notifyOriginProcessed(data.getOrigin());	
     	
     	// initialize algorithm
     	
     	// set the cost of the origin node to 0
     	
     	// get Label of origin
     	LabelStar.setDest(data.getDestination());
     	LabelStar oLabel = labels.get(data.getOrigin().getId());
     	oLabel.setCout(0);  	
     	
     	// add origin to heap
     	heap.insert(oLabel);
     	
     	
     	// we have to visit each node, and stop the algorithm if
     	// ...no predecessors are found
     	while (countMarques < nbNodes && !heap.isEmpty()) {
     		countMarques++;
     		

     		// retrieve minimum cost node
     		LabelStar xLabel = (LabelStar)heap.deleteMin();
     		
     		//if current node is destination, we stop
 			if(xLabel.getSommetCourant() == data.getDestination()) {
 				countMarques = nbNodes;
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
     			
     			LabelStar yLabel = labels.get(y.getId());
     			// set destination cost for Label

     			
     			if (!yLabel.getMarque()) {
     				double yCout = yLabel.getCoutSansDest();
     				double xCout = xLabel.getCoutSansDest();
     				yLabel.setCout(Math.min(yCout, xCout + data.getCost(arc)));
     				
     				if (yLabel.getCoutSansDest() != yCout) {
     					// insert node in heap and set the father parameter
     					yLabel.setPere(xLabel.getSommetCourant());
     					
     					// set father arc
     					yLabel.setArc(arc);
     					heap.insert(yLabel);
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
			LabelStar currentLabel = labels.get(data.getDestination().getId());
			while (currentLabel.getPere() != null) {
				arcs.add(currentLabel.getArc());
				currentLabel = labels.get(currentLabel.getPere().getId());
			}

			// Reverse the path...
			Collections.reverse(arcs);

			// Create the final solution.
			solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
		} 
     	
        return solution;
    }


}
