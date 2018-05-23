package org.insa.graph;

public class LabelStar extends Label implements Comparable<Label>  {
	protected static Node dest;
	protected static int maxSpeed = 1;
	public LabelStar(boolean marque, Node sommet_courant, Node pere, double cout) {
		super(marque, sommet_courant, pere, cout);
	}
	// get cost redefined to include destination cost for A* algorithm
	public double getCout() {
		return this.cout+(Point.distance(this.sommet_courant.getPoint(), LabelStar.dest.getPoint())/maxSpeed);
	}
	
	// get cost without destination cost
	public double getCoutSansDest() {
		return this.cout;
	}
	
	public static void setDest(Node d) {
		LabelStar.dest = d;
	}
	
	public static void setMaxSpeed(int sp) {
		LabelStar.maxSpeed = sp;
	}
}
