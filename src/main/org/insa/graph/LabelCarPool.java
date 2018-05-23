package org.insa.graph;

public class LabelCarPool extends LabelStar {
	// represents the current node of the other car
	protected Node other;
	
	// static variable indicating where the two paths meet
	protected static Node meetingNode = null;
	
	public LabelCarPool(boolean marque, Node sommet_courant, Node pere, double cout) {		
		super(marque, sommet_courant, pere, cout);
		this.other = null;		
	}	
	
	public double getCoutMidPointDest() {
		Point midPoint = Point.midPoint(this.sommet_courant.getPoint(), this.other.getPoint());
		return (Point.distance(midPoint, LabelStar.dest.getPoint()) + Point.distance(this.sommet_courant.getPoint(), midPoint)/2)/maxSpeed;
	}
	
	public void setOther(Node n) {
		this.other = n;
	}
	
	public static void setMeetingNode(Node n) {
		LabelCarPool.meetingNode = n;
	}
	
	public static Node getMeetingNode() {
		return LabelCarPool.meetingNode;
	}
}
