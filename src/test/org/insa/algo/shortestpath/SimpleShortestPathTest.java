package org.insa.algo.shortestpath;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.RoadInformation;
import org.insa.graph.RoadInformation.RoadType;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimpleShortestPathTest {
	
	//graph used for the test
	private static Graph graph;
	
	//list of nodes
	private static Node[] nodes;
	
	// List of arcs in the graph, x12 is the arc from node x1 to x2.
    @SuppressWarnings("unused")
    private static Arc x12, x13, x24, x25, x26, x31, x32, x36, x53, x54, x56, x65;
    
    @BeforeClass
    public static void initAll() throws IOException {
    
        // Create nodes
        nodes = new Node[6];
        for (int i = 0; i < nodes.length; ++i) {
            nodes[i] = new Node(i, null);
        };
        
        RoadInformation speed10 = new RoadInformation(RoadType.MOTORWAY, null, true, 36, "");
        
        // Add arcs...
        x12 = Node.linkNodes(nodes[0], nodes[1], 7, speed10, null);
        x13 = Node.linkNodes(nodes[0], nodes[2], 8, speed10, null);
        x24 = Node.linkNodes(nodes[1], nodes[3], 4, speed10, null);
        x25 = Node.linkNodes(nodes[1], nodes[4], 1, speed10, null);
        x26 = Node.linkNodes(nodes[1], nodes[5], 5, speed10, null);
        x31 = Node.linkNodes(nodes[2], nodes[0], 7, speed10, null);
        x32 = Node.linkNodes(nodes[2], nodes[1], 2, speed10, null);
        x36 = Node.linkNodes(nodes[2], nodes[5], 2, speed10, null);
        x53 = Node.linkNodes(nodes[4], nodes[2], 2, speed10, null);
        x54 = Node.linkNodes(nodes[4], nodes[3], 2, speed10, null);
        x56 = Node.linkNodes(nodes[4], nodes[5], 3, speed10, null);
        x65 = Node.linkNodes(nodes[5], nodes[4], 3, speed10, null);
        
        graph = new Graph("ID", "", Arrays.asList(nodes), null);
    }

	@Test
	public void testShortestPath() {
		
        List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
        ArcInspector arcI = filters.get(0);
		
        String [][] results = new String[nodes.length+1][nodes.length+1];
        String s = "";
        
		//loop through the nodes of the graph
        for(int j = 0; j < nodes.length; j++) {
        	//for every node we find the shortest path to every node
        	results[j][0] = "x"+(j+1);
        	s += results[j][0] + "	";
        	for(int k = 0; k < nodes.length; k++) {
        		if(j != k) {
        			//creating our Bellman-Ford and Dijkstra algorithm solutions
        			ShortestPathData data = new ShortestPathData(graph, nodes[j], nodes[k], arcI);
            		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
            		ShortestPathSolution dijkstraSolution = dijkstra.doRun();
            		BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm(data);
            		ShortestPathSolution bellmanFordSolution = bellmanFord.doRun();
            		int bellmanFordCost = 0;
            		int dijkstraCost = 0;
            		//adding the costs of every arc in the solution path
            		if(bellmanFordSolution.getPath() != null  && dijkstraSolution.getPath() != null) {
            			for(Arc a: dijkstraSolution.getPath().getArcs()) {
            				dijkstraCost += a.getLength();
	            		}
	            		for(Arc a: bellmanFordSolution.getPath().getArcs()) {
	            			bellmanFordCost += a.getLength();            			
	            		}
	            		//comparing the results of the two algorithms
	            		assertEquals(dijkstraCost, bellmanFordCost);
	            		String strng = Integer.toString(dijkstraCost);
	            		results[j+1][k+1] = strng;
            		}
            		else {
            			results[j+1][k+1] = "-";
            		}
        		}
        		else {
        			results[j+1][k+1] = "-";
        		}
            	s += results[j+1][k+1] + "	";
        	}
        	System.out.println(s);
        	s = "";
        }
	}
}
