package org.insa.algo.shortestpath;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Random;

public class OracleShortestPathTest {
	
	
	//graph used for the test
    private static Graph graph;	
    
    // map used for test
	private static String mapName;
    
    // GraphReader used for map
    private static GraphReader reader;

    @BeforeClass
    public static void initAll() throws IOException {
    	
    	// The name of the file to open.
        String fileName = "path-location.txt";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            if ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
        }
    	
    	mapName = line+"europe/france/haute-garonne.mapgr";
    	
    	reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
    	
    	
    	graph = reader.read();
    	
    }

    @Test
	public void NoFilterTest() {
    	//defining the roads and type of evaluation for our scenario
		List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
	    ArcInspector arcI = filters.get(0);
	    
	    // random choice of origin and destination
	    Random rand = new Random();
	    int origin = rand.nextInt(graph.size());
	    int destination = rand.nextInt(graph.size());
	    
	    Node n_origin = graph.get(origin);
	    Node n_destination = graph.get(destination);
	    
	    ShortestPathData data = new ShortestPathData(graph, n_origin, n_destination, arcI);
	    
	    //creating our Bellman-Ford and Dijkstra algorithm solutions
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
		ShortestPathSolution dijkstraSolution = dijkstra.doRun();
		BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm(data);
		ShortestPathSolution bellmanFordSolution = bellmanFord.doRun();
		
		int bellmanFordCost = 0;
		int dijkstraCost = 0;
		
		//adding the costs of every arc in the solution path
		if(bellmanFordSolution.getPath() != null  && dijkstraSolution.getPath() != null) {
			for(Arc a: dijkstraSolution.getPath().getArcs()) {
				dijkstraCost += data.getCost(a);
    		}
    		for(Arc a: bellmanFordSolution.getPath().getArcs()) {
    			bellmanFordCost += data.getCost(a);     			
    		}
    		//comparing the results of the two algorithms
    		assertEquals(dijkstraCost, bellmanFordCost);
    		
		}
		
	    
	}
	
	@Test
	public void OnlyCarsLengthTest() {
		//defining the roads and type of evaluation for our scenario
		List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
	    ArcInspector arcI = filters.get(1);
	    
	    // random choice of origin and destination
	    Random rand = new Random();
	    int origin = rand.nextInt(graph.size());
	    int destination = rand.nextInt(graph.size());

	    Node n_origin = graph.get(origin);
	    Node n_destination = graph.get(destination);
	    
	    ShortestPathData data = new ShortestPathData(graph, n_origin, n_destination, arcI);
	    
	    //creating our Bellman-Ford and Dijkstra algorithm solutions
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
		ShortestPathSolution dijkstraSolution = dijkstra.doRun();
		BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm(data);
		ShortestPathSolution bellmanFordSolution = bellmanFord.doRun();
		
		int bellmanFordCost = 0;
		int dijkstraCost = 0;

		//adding the costs of every arc in the solution path
		if(bellmanFordSolution.getPath() != null  && dijkstraSolution.getPath() != null) {
		    
			for(Arc a: dijkstraSolution.getPath().getArcs()) {
				dijkstraCost += data.getCost(a);
    		}
    		for(Arc a: bellmanFordSolution.getPath().getArcs()) {
    			bellmanFordCost += data.getCost(a);
    		}
    		//comparing the results of the two algorithms
    		assertEquals(dijkstraCost, bellmanFordCost);
    		
		}
	}
    
	@Test
	public void AllRoadsTimeTest() {
		//defining the roads and type of evaluation for our scenario
		List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
	    ArcInspector arcI = filters.get(2);
	    
	    // random choice of origin and destination
	    Random rand = new Random();
	    int origin = rand.nextInt(graph.size());
	    int destination = rand.nextInt(graph.size());

	    Node n_origin = graph.get(origin);
	    Node n_destination = graph.get(destination);
	    
	    ShortestPathData data = new ShortestPathData(graph, n_origin, n_destination, arcI);
	    
	    //creating our Bellman-Ford and Dijkstra algorithm solutions
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
		ShortestPathSolution dijkstraSolution = dijkstra.doRun();
		BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm(data);
		ShortestPathSolution bellmanFordSolution = bellmanFord.doRun();
		
		int bellmanFordCost = 0;
		int dijkstraCost = 0;

		//adding the costs of every arc in the solution path
		if(bellmanFordSolution.getPath() != null  && dijkstraSolution.getPath() != null) {
		    
			for(Arc a: dijkstraSolution.getPath().getArcs()) {
				dijkstraCost += data.getCost(a);
    		}
    		for(Arc a: bellmanFordSolution.getPath().getArcs()) {
    			bellmanFordCost += data.getCost(a);
    		}
    		//comparing the results of the two algorithms
    		assertEquals(dijkstraCost, bellmanFordCost);
    		
		}
	}

	@Test
	public void OnlyCarsTimeTest() {
		//defining the roads and type of evaluation for our scenario
		List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
	    ArcInspector arcI = filters.get(3);
	    
	    // random choice of origin and destination
	    Random rand = new Random();
	    int origin = rand.nextInt(graph.size());
	    int destination = rand.nextInt(graph.size());
	
	    Node n_origin = graph.get(origin);
	    Node n_destination = graph.get(destination);
	    
	    ShortestPathData data = new ShortestPathData(graph, n_origin, n_destination, arcI);
	    
	    //creating our Bellman-Ford and Dijkstra algorithm solutions
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
		ShortestPathSolution dijkstraSolution = dijkstra.doRun();
		BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm(data);
		ShortestPathSolution bellmanFordSolution = bellmanFord.doRun();
		
		int bellmanFordCost = 0;
		int dijkstraCost = 0;
	
		//adding the costs of every arc in the solution path
		if(bellmanFordSolution.getPath() != null  && dijkstraSolution.getPath() != null) {
		    
			for(Arc a: dijkstraSolution.getPath().getArcs()) {
				dijkstraCost += data.getCost(a);
			}
			for(Arc a: bellmanFordSolution.getPath().getArcs()) {
				bellmanFordCost += data.getCost(a);
			}
			//comparing the results of the two algorithms
			assertEquals(dijkstraCost, bellmanFordCost);
		}
	}

	@Test
	public void PedestrianTimeTest() {
		//defining the roads and type of evaluation for our scenarioc
		List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
	    ArcInspector arcI = filters.get(4);
	    
	    // random choice of origin and destination
	    Random rand = new Random();
	    int origin = rand.nextInt(graph.size());
	    int destination = rand.nextInt(graph.size());
	
	    Node n_origin = graph.get(origin);
	    Node n_destination = graph.get(destination);
	    
	    ShortestPathData data = new ShortestPathData(graph, n_origin, n_destination, arcI);
	    
	    //creating our Bellman-Ford and Dijkstra algorithm solutions
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
		ShortestPathSolution dijkstraSolution = dijkstra.doRun();
		BellmanFordAlgorithm bellmanFord = new BellmanFordAlgorithm(data);
		ShortestPathSolution bellmanFordSolution = bellmanFord.doRun();
		
		int bellmanFordCost = 0;
		int dijkstraCost = 0;
	
		//adding the costs of every arc in the solution path
		if(bellmanFordSolution.getPath() != null  && dijkstraSolution.getPath() != null) {
		    
			for(Arc a: dijkstraSolution.getPath().getArcs()) {
				dijkstraCost += data.getCost(a);
			}
			for(Arc a: bellmanFordSolution.getPath().getArcs()) {
				bellmanFordCost += data.getCost(a);
			}
			//comparing the results of the two algorithms
			assertEquals(dijkstraCost, bellmanFordCost);			
		}
	}
}
