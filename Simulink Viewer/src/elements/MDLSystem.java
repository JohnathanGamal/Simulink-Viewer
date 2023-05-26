package elements;

import javafx.geometry.Point2D;

public class MDLSystem {
    private static double length;
    private static double height;
    private static Point2D scale;

    public static void setDimensions (Point2D topLeft, Point2D bottomRight) {
        scale  = new Point2D(topLeft.getX(), topLeft.getY());
        length = bottomRight.getX() - topLeft.getX();
        height = bottomRight.getY() - topLeft.getY() ;

    }
    public static Point2D modifyPoint(Point2D point) {
		double x = point.getX() - scale.getX();
		double y = point.getY() - scale.getY();
		
    	return new Point2D(x, y);
    	
    	
    }

    public static double getLength() {
        return length;
    }


    public static double getHeight() {
        return height;
    }



}