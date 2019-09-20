package util;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

/**
 * Simple helper class to calculate the center of a 2D or 3D Bounds object
 * 
 * @author RKastner
 *
 */
public class CoordinateHelper {
	public static Point3D centerOfBounds(Bounds b) {
		if (b == null) return new Point3D(0,0,0);
		return new Point3D(b.getMinX()+b.getWidth()/2, b.getMinY()+b.getHeight()/2, b.getMinZ()+b.getDepth()/2);
	}
	public static Point2D centerOfBounds2D(Bounds b) {
		if (b == null) return new Point2D(0,0);
		return new Point2D(b.getMinX()+b.getWidth()/2, b.getMinY()+b.getHeight()/2);
	}

}
