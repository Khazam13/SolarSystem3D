package util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

/**
 * POJO (Plain Old Java Object) with only Getter and Setter.
 * Manages the state of the rotation of a 3D object.
 * 
 * @author RKastner
 *
 */
public class RotationContext {
	double anchorAngleX = 0;
	double anchorAngleY = 0;
	double anchorAngleZ = 0;

	private final DoubleProperty angleX = new SimpleDoubleProperty(0);
	private final DoubleProperty angleY = new SimpleDoubleProperty(0);
	private final DoubleProperty angleZ = new SimpleDoubleProperty(0);

	private final DoubleProperty pivotX = new SimpleDoubleProperty(0);
	private final DoubleProperty pivotY = new SimpleDoubleProperty(0);
	private final DoubleProperty pivotZ = new SimpleDoubleProperty(0);

	private Sphere pivot = null;
	
	private Rotate rotateX; 
	private Rotate rotateY; 
	private Rotate rotateZ; 

	public void setAngle(double x, double y, double z) {
		angleX.set(x);
		angleY.set(y);
		angleZ.set(z);
    }
	
	public void setAngleX(double x) {
		angleX.set(x);
	}
	
	public void setAngleY(double y) {
		angleY.set(y);
	}
	
	public void setAngleZ(double z) {
		angleZ.set(z);
	}
	
	public double getAngleX() {
		return angleX.get();
	}

	public double getAngleY() {
		return angleY.get();
	}

	public double getAngleZ() {
		return angleZ.get();
	}

	public DoubleProperty anglePropertyX() {
		return angleX;
	}

	public DoubleProperty anglePropertyY() {
		return angleY;
	}

	public DoubleProperty anglePropertyZ() {
		return angleZ;
	}

	/**
	 * Sets the x,y and z coordinates for the pivot point.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	public void setPivot(double x, double y, double z) {
		pivotX.set(x);
		pivotY.set(y);
		pivotZ.set(z);
    }
	
	/**
	 * Sets the coordinates of the pivot point to the coordinates of the given point.
	 * 
	 * @param p the point whose coordinates are the new pivot coordinates.
	 */
	public void setPivot(Point3D p) {
		if (p == null) {
			setPivot(0, 0, 0);
		} else {
			setPivot(p.getX(), p.getY(), p.getZ());
		}
    }
	
	/**
	 * Sets the pivot sphere and the pivot coordinates
	 * 
	 * @param s the pivot sphere
	 */
	public void setPivot(Sphere s) {
		pivot = s;
		
		if (s == null) {
			setPivot(0, 0, 0);
			return;
		}
		
		Bounds localBounds = s.getBoundsInLocal();
		Bounds sceneBounds = s.localToScene(localBounds);
		Point3D p = CoordinateHelper.centerOfBounds(sceneBounds);
		
		setPivot(p.getX(), p.getY(), p.getZ());
		
//		pivotX.set(p.getX());
//		pivotY.set(p.getY());
//		pivotZ.set(p.getZ());
		
//		System.out.println("(setPivot)" + ((SmartSphere<AstronomicalObject>)pivot).getAstronomicalObject().getName());
//		Debug.out("(setPivot)", " Pivot: ", p);
    }
	
	public Point3D getPivot() {
		if (this.pivot != null) {
//			System.out.println("(getPivot)" + ((SmartSphere<AstronomicalObject>)pivot).getAstronomicalObject().getName());
			
//			Bounds localBounds = pivot.getBoundsInLocal();

//			Debug.out("(getPivot)", " localBounds: ", localBounds);
			
//			Bounds sceneBounds = pivot.localToScene(localBounds);

//			Debug.out("(getPivot)", " sceneBounds: ", sceneBounds);
			
//			Point3D p = CoordinateHelper.centerOfBounds(sceneBounds);
			
//			Debug.out("(getPivot)", " center: ", p);

			return CoordinateHelper.centerOfBounds(pivot.localToScene(pivot.getBoundsInLocal()));
		}
		return new Point3D(pivotX.get(), pivotY.get(), pivotZ.get());
	}

	public Sphere getPivotSphere() {
		return this.pivot;
	}

	public double getPivotX() {
		if (this.pivot != null) {
			return CoordinateHelper.centerOfBounds(pivot.localToScene(pivot.getBoundsInLocal())).getX();
		}
		return pivotX.get();
	}

	public double getPivotY() {
		if (this.pivot != null) {
			return CoordinateHelper.centerOfBounds(pivot.localToScene(pivot.getBoundsInLocal())).getY();
		}
		return pivotY.get();
	}

	public double getPivotZ() {
		if (this.pivot != null) {
			return CoordinateHelper.centerOfBounds(pivot.localToScene(pivot.getBoundsInLocal())).getZ();
		}
		return pivotZ.get();
	}

	public DoubleProperty pivotPropertyX() {
		return pivotX;
	}

	public DoubleProperty pivotPropertyY() {
		return pivotY;
	}

	public DoubleProperty pivotPropertyZ() {
		return pivotZ;
	}

	public Rotate getRotateX() {
		return rotateX;
	}

	public void setRotateX(Rotate rotateX) {
		this.rotateX = rotateX;
	}

	public Rotate getRotateY() {
		return rotateY;
	}

	public void setRotateY(Rotate rotateY) {
		this.rotateY = rotateY;
	}

	public Rotate getRotateZ() {
		return rotateZ;
	}

	public void setRotateZ(Rotate rotateZ) {
		this.rotateZ = rotateZ;
	}
}
