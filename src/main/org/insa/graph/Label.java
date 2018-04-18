package org.insa.graph;


import java.util.ArrayList;

public class Label implements Comparable<Label> {
	private boolean marque;
	private Node sommet_courant;
	private Node pere;
	private double cout;
	
	// arc between father and son node
	private Arc arc;
	
	public Label(boolean marque, Node sommet_courant, Node pere, double cout) {
		this.marque = marque;
		this.sommet_courant = sommet_courant;
		this.pere = pere;
		this.cout = cout;
		this.arc = null;
	}
	
	// mark a node
	public void setMarque() {
		this.marque = true;
	}
	
	// set the father
	public void setPere(Node pere) {
		this.pere = pere;
	}
	
	// set the arc
	public void setArc(Arc a) {
		this.arc = a;
	}
	
	// set cost
	public void setCout(double cout) {
		this.cout = cout;
	}
	
	public Node getPere() {
		return this.pere;
	}
	
	public Arc getArc() {
		return this.arc; 
	}
	
	
	// get Node
	
	public Node getSommetCourant() {
		return this.sommet_courant;
	}
	// get cost
	public double getCout() {
		return this.cout;
	}
	
	// check if node is marked
	public boolean getMarque() {
		return this.marque;
	}
	
	
	public int compareTo(Label other) {
		return Double.compare(this.cout, other.cout);
	}
	
	
	// generates labels set to not marked and 0 cost for each node in a graph
	public static ArrayList<Label> genLabels(Graph g) {
		ArrayList<Label> labels = new ArrayList<Label>();
		for (int i = 0; i < g.size(); i++) {
			labels.add(new Label(false, g.get(i), null, Double.POSITIVE_INFINITY));
		}
		return labels;
	}
	
	
}
