package org.insa.algo.shortestpath;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Scanner;

public class Performance {
	
	
	//graph used for the test
    private static Graph graph;	
    
    // map used for test
	private static String mapName;
    
    // GraphReader used for map
    private static GraphReader reader;
    
    // performance test input file name
    private static String fileName2;
    
    // performance test output file name
    private static String fileNameOut;
    
    // write to same file
	private static BufferedWriter bw;
	private static FileWriter fw;

    @BeforeClass
    public static void initAll() throws IOException {
    	    	
    	// The name of the file to open containing the path to maps
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

            line = bufferedReader.readLine();

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
        
        // map location path is now stored in variable line
        
        // Now read desired map name from the performance test file 
        
        // The name of the file to open containing the path to maps
        fileName2 = "performance.txt";
        
        // output file name
        fileNameOut = "performance-results.txt";

        // This will reference one line at a time
        String line2 = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName2);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            line2 = bufferedReader.readLine();
            
            // Always close files.
            bufferedReader.close(); 
      
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName2 + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName2 + "'");                  
        }
        
        // ex : line2 = "europe/france/haute-garonne.mapgr"
    	mapName = line+line2;
    	
    	reader = new BinaryGraphReader(
                new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
    	
    	
    	graph = reader.read();
    	
    	// write map name to results file
    	try {
			
	    	// initialize buffers for writing
	    	
			fw = new FileWriter(fileNameOut);
			bw = new BufferedWriter(fw);

			
			bw.write("Map : "+mapName);
			bw.write(System.getProperty("line.separator"));

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
 		
    	
    	
    }

    @Test
	public void NoFilterTest() {
    	
    	//defining the roads and type of evaluation for our scenario
    	List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
    	ArcInspector arcI = filters.get(0);
       
    	try {
    		// FileReader reads text files in the default encoding.
    		FileReader fileReader = new FileReader(fileName2);
             
             String line = null;

             // Always wrap FileReader in BufferedReader.
             BufferedReader bufferedReader = 
                 new BufferedReader(fileReader);

             // ignore the first line 
             line = bufferedReader.readLine();  
             
             line = null;
             
         	 // read 10 origin and destination points
             for (int i = 0; i < 10; i++) {
	        	line = bufferedReader.readLine();
	        	// set origin and destination nodes
	        	Scanner s = new Scanner(line);
	        	int origin = s.nextInt(); 
	        	int destination = s.nextInt(); 
	     	    
	     	    
	        	Node n_origin = graph.get(origin);
	     	    Node n_destination = graph.get(destination);
	     	    
	     	    ShortestPathData data = new ShortestPathData(graph, n_origin, n_destination, arcI);
	     	    
	     	    //creating our Bellman-Ford and Dijkstra algorithm solutions
	     		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
	     		
	     		AStarAlgorithm astar = new AStarAlgorithm(data);
	     		long startTime2 = System.currentTimeMillis();
	     		ShortestPathSolution astarSolution = astar.doRun();
	     		long endTime2 = System.currentTimeMillis();
	     		long duration2 = (endTime2 - startTime2);
	     		
	     		// we time the execution of each algorithm
	     		long startTime = System.currentTimeMillis();
	     		ShortestPathSolution dijkstraSolution = dijkstra.doRun();
	     		long endTime = System.currentTimeMillis();
	     		long duration = (endTime - startTime);
	     		
	     		

	    		try {
	    			
	    	    	fw = new FileWriter(fileNameOut, true);
	    			bw = new BufferedWriter(fw);

	    			String content = "Execution de Dijkstra de "+origin+" a "+destination+" : temps := "
	    			+duration+" nombre de noeuds parcourus := "+dijkstraSolution.getMarked();
	    			
	    			
	    			
	    			String content2 =  "Execution de AStar de "+origin+" a "+destination+" : temps := "
	    	    			+duration2+" nombre de noeuds parcourus := "+astarSolution.getMarked();

	    			bw.write("All roads, length test");
	    			bw.write(System.getProperty("line.separator"));
	    			bw.write(content);
	    			bw.write(System.getProperty("line.separator"));
	    			bw.write(content2);
	    			bw.write(System.getProperty("line.separator"));

	    		} catch (IOException e) {

	    			e.printStackTrace();

	    		} finally {

	    			try {

	    				if (bw != null)
	    					bw.close();

	    				if (fw != null)
	    					fw.close();

	    			} catch (IOException ex) {

	    				ex.printStackTrace();

	    			}
	    		}
	     		
	
	     		
	     		s.close();
	         		

             }
             
             // Always close files.
             bufferedReader.close(); 
       
         }
         catch(FileNotFoundException ex) {
             System.out.println(
                 "Unable to open file '" + 
                 fileName2 + "'");                
         }
         catch(IOException ex) {
             System.out.println(
                 "Error reading file '" 
                 + fileName2 + "'");                  
         }
	    
    	 
		
	    
	}
	
	@Test
	public void OnlyCarsLengthTest() {
		//defining the roads and type of evaluation for our scenario
		List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
	    ArcInspector arcI = filters.get(1);
	    

	    try {
    		// FileReader reads text files in the default encoding.
    		FileReader fileReader = new FileReader(fileName2);
             
             String line = null;

             // Always wrap FileReader in BufferedReader.
             BufferedReader bufferedReader = 
                 new BufferedReader(fileReader);

             // ignore the first line and first 10 points
             for (int i = 0; i < 11; i++) {
            	 line = bufferedReader.readLine();  
             }
             line = null;
             
         	 // read 10 origin and destination points
             for (int i = 0; i < 10; i++) {
	        	line = bufferedReader.readLine();
	        	// set origin and destination nodes
	        	Scanner s = new Scanner(line);
	        	int origin = s.nextInt(); 
	        	int destination = s.nextInt(); 
	     	    
	     	    
	        	Node n_origin = graph.get(origin);
	     	    Node n_destination = graph.get(destination);
	     	    
	     	    ShortestPathData data = new ShortestPathData(graph, n_origin, n_destination, arcI);
	     	    
	     	    //creating our Bellman-Ford and Dijkstra algorithm solutions
	     		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
	     		
	     		// we time the execution of each algorithm
	     		long startTime = System.currentTimeMillis();
	     		ShortestPathSolution dijkstraSolution = dijkstra.doRun();
	     		long endTime = System.currentTimeMillis();
	     		long duration = (endTime - startTime);
	     		
	     		AStarAlgorithm astar = new AStarAlgorithm(data);
	     		long startTime2 = System.currentTimeMillis();
	     		ShortestPathSolution astarSolution = astar.doRun();
	     		long endTime2 = System.currentTimeMillis();
	     		long duration2 = (endTime2 - startTime2);
	     		

	    		try {
	    			
	    	    	// initialize buffers for writing
	    	    	
	    			fw = new FileWriter(fileNameOut, true);
	    			bw = new BufferedWriter(fw);

	    			String content = "Execution de Dijkstra de "+origin+" a "+destination+" : temps := "
	    			+duration+" nombre de noeuds parcourus := "+dijkstraSolution.getMarked();
	    			
	    			
	    			
	    			String content2 =  "Execution de AStar de "+origin+" a "+destination+" : temps := "
	    	    			+duration2+" nombre de noeuds parcourus := "+astarSolution.getMarked();

	    			bw.write("Only cars, length test");
	    			bw.write(System.getProperty("line.separator"));
	    			bw.write(content);
	    			bw.write(System.getProperty("line.separator"));
	    			bw.write(content2);
	    			bw.write(System.getProperty("line.separator"));

	    		} catch (IOException e) {

	    			e.printStackTrace();

	    		} finally {

	    			try {

	    				if (bw != null)
	    					bw.close();

	    				if (fw != null)
	    					fw.close();

	    			} catch (IOException ex) {

	    				ex.printStackTrace();

	    			}
	    		}
	     		
	
	     		
	     		s.close();
	         		

             }
             
             // Always close files.
             bufferedReader.close(); 
       
         }
         catch(FileNotFoundException ex) {
             System.out.println(
                 "Unable to open file '" + 
                 fileName2 + "'");                
         }
         catch(IOException ex) {
             System.out.println(
                 "Error reading file '" 
                 + fileName2 + "'");                  
         }
	    
    	 
	    
	}
    
	@Test
	public void AllRoadsTimeTest() {
		//defining the roads and type of evaluation for our scenario
		List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
	    ArcInspector arcI = filters.get(2);
	    
	    try {
    		// FileReader reads text files in the default encoding.
    		FileReader fileReader = new FileReader(fileName2);
             
             String line = null;

             // Always wrap FileReader in BufferedReader.
             BufferedReader bufferedReader = 
                 new BufferedReader(fileReader);

             // ignore the first line and first 20 points
             for (int i = 0; i < 21; i++) {
            	 line = bufferedReader.readLine();  
             }
             line = null;
             
         	 // read 10 origin and destination points
             for (int i = 0; i < 10; i++) {
	        	line = bufferedReader.readLine();
	        	// set origin and destination nodes
	        	Scanner s = new Scanner(line);
	        	int origin = s.nextInt(); 
	        	int destination = s.nextInt(); 
	     	    
	     	    
	        	Node n_origin = graph.get(origin);
	     	    Node n_destination = graph.get(destination);
	     	    
	     	    ShortestPathData data = new ShortestPathData(graph, n_origin, n_destination, arcI);
	     	    
	     	    //creating our Bellman-Ford and Dijkstra algorithm solutions
	     		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
	     		
	     		// we time the execution of each algorithm
	     		long startTime = System.currentTimeMillis();
	     		ShortestPathSolution dijkstraSolution = dijkstra.doRun();
	     		long endTime = System.currentTimeMillis();
	     		long duration = (endTime - startTime);
	     		
	     		AStarAlgorithm astar = new AStarAlgorithm(data);
	     		long startTime2 = System.currentTimeMillis();
	     		ShortestPathSolution astarSolution = astar.doRun();
	     		long endTime2 = System.currentTimeMillis();
	     		long duration2 = (endTime2 - startTime2);
	     		

	    		try {
	    			
	    	    	// initialize buffers for writing
	    	    	
	    			fw = new FileWriter(fileNameOut, true);
	    			bw = new BufferedWriter(fw);

	    			String content = "Execution de Dijkstra de "+origin+" a "+destination+" : temps := "
	    			+duration+" nombre de noeuds parcourus := "+dijkstraSolution.getMarked();
	    			
	    			
	    			
	    			String content2 =  "Execution de AStar de "+origin+" a "+destination+" : temps := "
	    	    			+duration2+" nombre de noeuds parcourus := "+astarSolution.getMarked();

	    			bw.write("All roads, time test");
	    			bw.write(System.getProperty("line.separator"));
	    			bw.write(content);
	    			bw.write(System.getProperty("line.separator"));
	    			bw.write(content2);
	    			bw.write(System.getProperty("line.separator"));

	    		} catch (IOException e) {

	    			e.printStackTrace();

	    		} finally {

	    			try {

	    				if (bw != null)
	    					bw.close();

	    				if (fw != null)
	    					fw.close();

	    			} catch (IOException ex) {

	    				ex.printStackTrace();

	    			}
	    		}
	     		
	
	     		
	     		s.close();
	         		

             }
             
             // Always close files.
             bufferedReader.close(); 
       
         }
         catch(FileNotFoundException ex) {
             System.out.println(
                 "Unable to open file '" + 
                 fileName2 + "'");                
         }
         catch(IOException ex) {
             System.out.println(
                 "Error reading file '" 
                 + fileName2 + "'");                  
         }
	    
	   
	}

	@Test
	public void OnlyCarsTimeTest() {
		//defining the roads and type of evaluation for our scenario
		List<ArcInspector> filters = ArcInspectorFactory.getAllFilters();
	    ArcInspector arcI = filters.get(3);
	    

    	try {
    		// FileReader reads text files in the default encoding.
    		FileReader fileReader = new FileReader(fileName2);
             
             String line = null;

             // Always wrap FileReader in BufferedReader.
             BufferedReader bufferedReader = 
                 new BufferedReader(fileReader);

             // ignore the first line and first 30 points
             for (int i = 0; i < 31; i++) {
            	 line = bufferedReader.readLine();  
             }
             line = null;
             
         	 // read 10 origin and destination points
             for (int i = 0; i < 10; i++) {
	        	line = bufferedReader.readLine();
	        	// set origin and destination nodes
	        	Scanner s = new Scanner(line);
	        	int origin = s.nextInt(); 
	        	int destination = s.nextInt(); 
	     	    
	     	    
	        	Node n_origin = graph.get(origin);
	     	    Node n_destination = graph.get(destination);
	     	    
	     	    ShortestPathData data = new ShortestPathData(graph, n_origin, n_destination, arcI);
	     	    
	     	    //creating our Bellman-Ford and Dijkstra algorithm solutions
	     		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(data);
	     		
	     		// we time the execution of each algorithm
	     		long startTime = System.currentTimeMillis();
	     		ShortestPathSolution dijkstraSolution = dijkstra.doRun();
	     		long endTime = System.currentTimeMillis();
	     		long duration = (endTime - startTime);
	     		
	     		AStarAlgorithm astar = new AStarAlgorithm(data);
	     		long startTime2 = System.currentTimeMillis();
	     		ShortestPathSolution astarSolution = astar.doRun();
	     		long endTime2 = System.currentTimeMillis();
	     		long duration2 = (endTime2 - startTime2);
	     		

	    		try {
	    			
	    	    	// initialize buffers for writing
	    	    	
	    			fw = new FileWriter(fileNameOut, true);
	    			bw = new BufferedWriter(fw);

	    			String content = "Execution de Dijkstra de "+origin+" a "+destination+" : temps := "
	    			+duration+" nombre de noeuds parcourus := "+dijkstraSolution.getMarked();
	    			
	    			
	    			
	    			String content2 =  "Execution de AStar de "+origin+" a "+destination+" : temps := "
	    	    			+duration2+" nombre de noeuds parcourus := "+astarSolution.getMarked();

	    			bw.write("Only cars, time test");
	    			bw.write(System.getProperty("line.separator"));
	    			bw.write(content);
	    			bw.write(System.getProperty("line.separator"));
	    			bw.write(content2);
	    			bw.write(System.getProperty("line.separator"));

	    		} catch (IOException e) {

	    			e.printStackTrace();

	    		} finally {

	    			try {

	    				if (bw != null)
	    					bw.close();

	    				if (fw != null)
	    					fw.close();

	    			} catch (IOException ex) {

	    				ex.printStackTrace();

	    			}
	    		}
	     		
	
	     		
	     		s.close();
	         		

             }
             
             // Always close files.
             bufferedReader.close(); 
       
         }
         catch(FileNotFoundException ex) {
             System.out.println(
                 "Unable to open file '" + 
                 fileName2 + "'");                
         }
         catch(IOException ex) {
             System.out.println(
                 "Error reading file '" 
                 + fileName2 + "'");                  
         }
	    
	   
	}
}

	