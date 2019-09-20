package util;

import javafx.geometry.Point2D;

/**
 * Simple helper class to encapsulate the coordinates for scene translate operations
 * When a translate operation is initiated by pressing the right mouse button the current mouse coordinates (in scene coordinate system)
 * are saved to mouseAnchorX and mouseAnchorY.
 * Also the translate state for the root element of the scene is saved to translateAnchorX and translateAnchorY
 * 
 * This class holds only primitive data types so the caller is responsible for the handling of any changes.
 * 
 * @author RKastner
 *
 */
public class TranslateContext {
    private double mouseAnchorX;
    private double mouseAnchorY;

    private double translateAnchorX;
    private double translateAnchorY;
	private double translateAnchorZ;

    public double getMouseAnchorX() {
    	return mouseAnchorX;
    }
	public void setMouseAnchorX(double x) {
		mouseAnchorX = x;
	}
    public double getMouseAnchorY() {
    	return mouseAnchorY;
    }
	public void setMouseAnchorY(double y) {
		mouseAnchorY = y;
	}
    public void setMouseAnchor(Point2D p) {
    	mouseAnchorX = p.getX();
    	mouseAnchorY = p.getY();
    }
    public void setMouseAnchor(double x, double y) {
    	mouseAnchorX = x;
    	mouseAnchorY = y;
    }
    
	public double getTranslateAnchorX() {
		return translateAnchorX;
	}
	public void setTranslateAnchorX(double x) {
		translateAnchorX = x;
	}
	public double getTranslateAnchorY() {
		return translateAnchorY;
	}
	public void setTranslateAnchorY(double y) {
		translateAnchorY = y;
	}
	public double getTranslateAnchorZ() {
		return translateAnchorZ;
	}
	public void setTranslateAnchorZ(double z) {
		translateAnchorZ = z;
	}
	public void setTranslateAnchor(Point2D p) {
		setTranslateAnchorX(p.getX());
		setTranslateAnchorY(p.getY());
		setTranslateAnchorZ(0);
	}
	public void setTranslateAnchor(double x, double y, double z) {
		setTranslateAnchorX(x);
		setTranslateAnchorY(y);
		setTranslateAnchorZ(z);
	}
}
