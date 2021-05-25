package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;

import org.insa.graphs.algorithm.shortestpath.*;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.junit.Test;



public class AlgorithmTest {
	
	static Graph graph = null;
	static final String mapName =  "C:\\Users\\titip\\Desktop\\INSA\\graphe\\Bee graph\\insa.mapgr";
	static GraphReader reader;
	static ShortestPathData data;
	static final List<ArcInspector> filter =  ArcInspectorFactory.getAllFilters();
	static ShortestPathAlgorithm[] algo = new ShortestPathAlgorithm[3];
	static ShortestPathSolution[] solve = new ShortestPathSolution[3];
	static double[] longueur = new double[3];
	static long[] temps = new long[3];
	static Node origin;
	static Node dest;
	static ArcInspector mode;

	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	    // Read the graph.
		reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
	    graph = reader.read();
	    
	
  	}
	
	
	public static boolean labelRising(ShortestPathSolution algoSol) {
			  ArrayList<Double> old_cost = new ArrayList<>();
			  old_cost = algoSol.get_old_cout();
			  // le cout des labels est-il croissant ? 
	    	  double min = old_cost.get(0);
	    	  boolean err=true;
	    	  for (int j=0; j<old_cost.size(); j++) {
	    		  if( old_cost.get(j) < min) { // si un label n'est pas croissant
	    			  err= false;
	    		  }
	    	  }
	    	  return err;
	        	
	}
	
	@Test
	public void testAlgoAvecOracle() {
		for (int i=0; i<100; i++) {
	    	origin = graph.get((int) Math.random() * 1348);
	    	dest = graph.get((int) Math.random() * 1348);
	    	if (origin == dest) {
	    		continue;
	    	}
	    	for (int c=0 ; c < filter.size(); c++) { 
	    		mode = filter.get(c);
	    	}
	    	runAlgo();
	    	
	    }
	}
	
	@Test
	public void testAlgoSansOracle() {
		mode = filter.get(0);
		//Test 1
	    origin =  graph.get(636);
	    dest = graph.get(882);
	    runAlgoNoOracle(2630, 540, true);
	    
	  //Test 2
	    origin =  graph.get(1037);
	    dest = graph.get(364);
	    runAlgoNoOracle(0, 0, false);
	     
	  //Test 3
	    origin =  graph.get(985);
	    dest = graph.get(227);
	    runAlgoNoOracle(2745, 1146, true);
	}
	
		

	public static void runAlgo() { // test ORACLE : on se base sur bellmanford
		
	       ShortestPathAlgorithm djikstra ;
	       ShortestPathAlgorithm bellmanford ;
	       ShortestPathAlgorithm astar;
	       djikstra = new DijkstraAlgorithm(new ShortestPathData(graph, origin, dest, mode));
	       bellmanford = new BellmanFordAlgorithm(new ShortestPathData(graph, origin, dest, mode));
	       astar = new AStarAlgorithm(new ShortestPathData(graph, origin, dest, mode));
	       ShortestPathSolution djikstraSol = djikstra.run();
	       ShortestPathSolution bellmanSol = bellmanford.run();
	       ShortestPathSolution astarSol = astar.run();
	       Path djikstraSolution = djikstraSol.getPath();
	       Path bellmanFordSolution =  bellmanSol.getPath();
	       Path astarSolution =  astarSol.getPath();
	       if(!bellmanSol.isFeasible()) {
	             assertTrue(!djikstraSol.isFeasible());
	             assertTrue(!astarSol.isFeasible());
	       }
	       else {
	            assertTrue(djikstraSol.isFeasible());
	            assertTrue(djikstraSolution.isValid());
	            assertEquals(djikstraSolution.getMinimumTravelTime(), bellmanFordSolution.getMinimumTravelTime(), 0.01); 
	            assertEquals(djikstraSolution.getLength(), bellmanFordSolution.getLength(), 0.01);
	            assertTrue(djikstraSol.isHeapValid());
	            assertTrue(astarSol.isFeasible());
	            assertTrue(astarSolution.isValid());
	            assertEquals(astarSolution.getMinimumTravelTime(), bellmanFordSolution.getMinimumTravelTime(), 0.01); 
	            assertEquals(astarSolution.getLength(), bellmanFordSolution.getLength(), 0.01);
	            assertTrue(astarSol.isHeapValid());
	            assertTrue(labelRising(djikstraSol));
	            assertTrue(labelRising(astarSol));
	       }
	}
	
	 private void runAlgoNoOracle(double expectedValueDistance, double expectedValueTime, boolean feasible) {  // test Sans oracle : pas de bellman ford
	      ShortestPathAlgorithm djikstra = new DijkstraAlgorithm(new ShortestPathData(graph, origin, dest, filter.get(0)));
	      ShortestPathAlgorithm astar = new AStarAlgorithm(new ShortestPathData(graph, origin, dest, filter.get(0)));
	      ShortestPathSolution djikstraSol = djikstra.run();
	      ShortestPathSolution AStarSol = astar.run();
	      Path djikstraSolution = djikstraSol.getPath();
	      Path AStarSolution =  AStarSol.getPath();
	      if(!feasible) {
	         assertTrue(!djikstraSol.isFeasible());
	         assertTrue(!AStarSol.isFeasible());
	      }
	      else {
	         assertTrue(djikstraSol.isFeasible());
	         assertTrue(djikstraSolution.isValid());
	         assertEquals(djikstraSolution.getMinimumTravelTime(), expectedValueTime, 1); //check our solution's cost
	         assertEquals(djikstraSolution.getLength(), expectedValueDistance, 1);
	         assertTrue(AStarSol.isFeasible());
	         assertTrue(AStarSolution.isValid());
	         assertEquals(AStarSolution.getMinimumTravelTime(), expectedValueTime, 1); //check our solution's cost
	         assertEquals(AStarSolution.getLength(), expectedValueDistance, 1);
	      }
	 }

	

	

	
}
