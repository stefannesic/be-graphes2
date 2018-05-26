package org.insa.algo.carpooling;

import org.insa.algo.AbstractInputData;
import org.insa.algo.ArcInspector;
import org.insa.graph.Graph;
import org.insa.graph.Node;

public class CarPoolingData extends AbstractInputData {
	
	private final Node originA, originB, destinationA, destinationB;
	
    public CarPoolingData(Graph graph, ArcInspector arcFilter, Node originA, Node originB, Node destA, Node destB) {
        super(graph, arcFilter);
        this.originA = originA;
        this.originB = originB;
        this.destinationA = destA;
        this.destinationB = destB;
    }
    
    public Node getOriginPed() {
    	return this.originA;
    }
    
    public Node getOriginCar() {
    	return this.originB;
    }
    
    public Node getDestination() {
    	return this.destinationA;
    }
    
 }
