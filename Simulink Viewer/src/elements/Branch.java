package elements;

import javafx.geometry.Point2D;

public class Branch {
    private int Zorder;
    private Point2D breakingPoint;
    private int dest;
    private int inputOrder;

    public final int getInputOrder() {
		return inputOrder;
	}
	public final void setInputOrder(int inputOrder) {
		this.inputOrder = inputOrder;
	}
	public int getZOrder() {
        return Zorder;
    }
    public void setZOrder(int zorder) {
        Zorder = zorder;
    }
    public Point2D getBreakingPoint() {
        return breakingPoint;
    }
    public void setBreakingPoint(Point2D branchingPoint) {
        this.breakingPoint = branchingPoint;
    }
    public int getDest() {
        return dest;
    }
    public void setDest(int dest) {
        this.dest = dest;
    }


}