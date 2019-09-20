package util;

import javafx.scene.Node;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;

public class SmartSphere<T> extends Sphere {
	private T astronomicalObject = null;

    /**
     * Creates a new instance of {@code SmartSphere} of a given radius.
     *
     * @param radius Radius
     */
    public SmartSphere(double radius, T astronomicalObject) {
        super(radius);
		setPickOnBounds(false);
        setAstronomicalObject(astronomicalObject);
    }
	
	public T getAstronomicalObject() {
		return astronomicalObject;
	}

	public void setAstronomicalObject(T astronomicalObject) {
		this.astronomicalObject = astronomicalObject;
	}
	
	public Box getBoundingBox() {
		Box boundingBox = new Box(getBoundsInLocal().getWidth(), getBoundsInLocal().getHeight(), getBoundsInLocal().getDepth());
		boundingBox.setDrawMode(DrawMode.LINE);
		
		return boundingBox;
	}

	/**
	 * Rotation parameter is the DURATION of 1 full rotation in days!!
	 * as the Animation timer needs the increment in degrees we have the following formula
	 * 
	 * angleForOneNano = 360° / 24 / 60 / 60 / 1000 / 1000 / 1000 / rotation
	 * 
	 * @see application.StarSystemScene#prepareAnimation(javafx.scene.Node, double)
	 */
	protected void prepareAnimation(Node node, double rotation) {
//		System.out.println("PrepareAnimation: " + " Rotation: " + rotation);
//		// now we scale - the higher our scale factor the less time a full rotation needs
//		rotation = rotation / ROTATION_PERIOD_SPEED;
//		// the angle in degree for exactly 1 nano
//		double angleForOneNano = 360 / rotation / 24 / 60 / 60 / 1000 / 1000 / 1000;
//		
//		AnimationTimer timer = new AnimationTimer() {
//			long oldTimeStamp = 0;
//
//			@Override
//			public void handle(long now) {
//				long passed = now - oldTimeStamp;
//				oldTimeStamp = now;
//				double angle = angleForOneNano * passed;
//				node.rotateProperty().set(node.getRotate() +  angle);
//			}
//		};
//		AnimationHandler.getInstance().addAnimationTimer(timer);
	}
}
