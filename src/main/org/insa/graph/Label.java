package org.insa.graph;

import java.util.ArrayList;

public class Label {
	private boolean marque;
	private Node sommet_courant;
	private Node pere;
	private double cout;
	
	public Label(boolean marque, Node sommet_courant, Node pere, double cout) {
		this.marque = marque;
		this.sommet_courant = sommet_courant;
		this.pere = pere;
		this.cout = cout;
	}
	
	// generates labels set to not marked and 0 cost for each node in a graph
	public ArrayList<Label> genLabels(Graph g) {
		ArrayList<Label> labels = new ArrayList<Label>();
		for (int i = 0; i < g.size(); i++) {
			labels.add(new Label(false, g.get(i), g.get(i-1), 0));
		}
		return labels;
	}
}
