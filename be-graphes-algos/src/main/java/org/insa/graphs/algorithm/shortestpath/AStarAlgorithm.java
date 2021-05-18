package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.utils.PriorityQueue;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    @Override
    public label[] initTab(int nbNode, Node base) {
    	label[] res=  new labelStar[nbNode];
    	res[base.getId()] = new labelStar(0,base,null, getInputData().getDestination());
    	return res;
    	
    }
    
    @Override
    public label createLabel(double cout, Node som_c, Arc pere) {
    	return new labelStar(cout, som_c, pere, getInputData().getDestination());
    }
    
    
    

}