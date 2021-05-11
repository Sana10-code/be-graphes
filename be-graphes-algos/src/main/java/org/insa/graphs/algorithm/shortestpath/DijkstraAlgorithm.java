package org.insa.graphs.algorithm.shortestpath;

import java.util.*;


import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Graph;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.PriorityQueue;


public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	public ArrayList<Double> cout_ancien = new ArrayList<>();  
	double cost;
	boolean tas_err = true;
	
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    public label[] initTab(int nbNode, Node base) {
    	label[] res=  new label[nbNode];
    	res[base.getId()] = new label(0,base,null);
    	return res;
    	
    }
    
    public label createLabel(double cout, Node dest, Arc cur) {
    	return new label(cout, dest, cur);
    }
    
    
    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        Graph graph = data.getGraph();
        final int nbNode = graph.size();
        Node base = data.getOrigin();
        PriorityQueue<label>labheap = new BinaryHeap<>();
        label lpm[] = this.initTab(nbNode, base);
        label cur,after;
        notifyOriginProcessed(data.getOrigin());
       
         
        // algorithm
              
        labheap.insert(lpm[base.getId()]);
        
        // main
        while(!labheap.isEmpty() && (lpm[data.getDestination().getId()] == null || !lpm[data.getDestination().getId()].isMarque())) {

        	cur = labheap.deleteMin();
        	cur.actualiseMarque(true);
        	cout_ancien.add(cur.getCost()); // on sauvegarde le cout des labels pour des test.
        	boolean tas_validity = labheap.isValid();
        	if (!tas_validity) {
        		tas_err = false;
        	}
        	for (Arc arc : cur.getCurrentNode().getSuccessors()) {
        		
        		int id_dest = arc.getDestination().getId();
        		if (lpm[id_dest] != null && lpm[id_dest].isMarque()) {
        			continue;
        		}
        		
        		if (!data.isAllowed(arc)) {
        			continue;
        		}
        		
        		after = lpm[id_dest];
        		if (after ==null) {
        			after = this.createLabel(cur.getCost() + data.getCost(arc), arc.getDestination(),arc);
        			lpm[id_dest] = after;
        			notifyNodeReached(arc.getDestination());
        			labheap.insert(after);
        		}
        		
        		else {
        			if (after.getCost() > cur.getCost() + data.getCost(arc)) {
        				after.ActualiseCost(cur.getCost() + data.getCost(arc));
        				after.ActualisePapa(arc);
        				labheap.remove(after);
        				labheap.insert(after); // actualise la position dans le tas
        				
        				
        			}
        			
        			        	
        		}
        		
        	}
        	
        }
        
        if (lpm[data.getDestination().getId()] == null) {
    		solution = new ShortestPathSolution(data, Status.INFEASIBLE);
    	}
        else {
        	ArrayList<Arc> solution_arcs= new ArrayList<>();
        	Arc arc = lpm[data.getDestination().getId()].givePere();
        	while (arc !=null) {
        		solution_arcs.add(arc);
        		arc = lpm[arc.getOrigin().getId()].givePere();
        	}
        	Collections.reverse(solution_arcs);
            notifyDestinationReached(data.getDestination());
        	Path newPath = new Path(graph, solution_arcs);
        	solution = new ShortestPathSolution(data, Status.OPTIMAL ,newPath, cout_ancien, cost, tas_err);
   
        
        }
        
        return solution;
    }

}
