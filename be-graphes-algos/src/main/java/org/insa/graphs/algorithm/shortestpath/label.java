package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.List;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;


	public class label implements Comparable<label> {

		private boolean marque;
		private double cout;
		private Arc pere;
		private Node sommet_courant;
	    
		public label(double cout, Node sommet_courant, Arc pere) {
			this.cout = cout;
	        this.marque = false;
	        this.sommet_courant = sommet_courant;
	        this.pere = pere;
		}
		public double getCost() {
			return this.cout;
		}
		public double getTotalCost() {
			return this.cout;
		}
		
		public void actualiseMarque(boolean change) {
			this.marque = change;
		}
		public boolean isMarque() {
			return this.marque;
		}
		
		public void ActualiseCost(double newCost) {
			this.cout = newCost;
		}
		
		public void ActualisePapa(Arc newPapa) {
			this.pere = newPapa;
		}
		
		public Arc givePere() {
			return this.pere;
		}
		
		
		public Node getCurrentNode() {
			return this.sommet_courant;
		}
		public int compareTo(label other) {
			return Double.compare(this.getTotalCost(), other.getTotalCost());
	    }
		
		public int getSommetId() {
			return this.sommet_courant.getId();
		}
		

	}
}
