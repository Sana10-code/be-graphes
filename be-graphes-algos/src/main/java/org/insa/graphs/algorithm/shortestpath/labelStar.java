package org.insa.graphs.algorithm.shortestpath;
import java.util.ArrayList;
import java.util.List;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class labelStar extends label {
	private double cout_estime;
		
		public labelStar(double cout, Node sommet_courant, Arc pere, Node dest) {
			super(cout,sommet_courant,pere);
			this.cout_estime =  sommet_courant.getPoint().distanceTo(dest.getPoint());
			//this.cout_estime = 0; 
		}
		
		@Override
		public double getTotalCost() {
			return this.getCost() + this.cout_estime;
		}
		
		public double getEstimatedCost() {
			return this.cout_estime;
		}
		
		 @Override
		    public int compareTo(label second) {
		        labelStar other = (labelStar) second;
		        if (Double.compare(this.getTotalCost(), other.getTotalCost()) == 0) {
		            return Double.compare(this.getEstimatedCost(), other.getEstimatedCost());
		        }
		        return Double.compare(this.getTotalCost(), other.getTotalCost());
		    }
	

}
