package org.insa.graph;

public class LabelStar extends Label implements Comparable<Label>  {
	private static Node dest;
	private static int maxSpeed = 1;
	public LabelStar(boolean marque, Node sommet_courant, Node pere, double cout) {
		super(marque, sommet_courant, pere, cout);
	}
	
	// get total cost
	public double getCoutTotal() {
		double d = Point.distance(this.getSommetCourant().getPoint(), dest.getPoint());
		/*System.out.println("Distance is "+d);
		System.out.println("Speed is "+maxSpeed);
		System.out.println("Distance over speed is "+(d/maxSpeed));
		System.out.println("Distance over speed with cast is "+(float)(d/maxSpeed));
		System.out.println("Cost + heuristic is "+(this.cout+(d/maxSpeed)));*/
		return this.cout+(d/maxSpeed);
	}
	
	public static void setDest(Node d) {
		LabelStar.dest = d;
	}
	
	public static void setMaxSpeed(int sp) {
		LabelStar.maxSpeed = sp;
	}
	
}
