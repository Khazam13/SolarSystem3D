package util;

import javafx.scene.Camera;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SmartPane extends Pane {
	public SmartPane() {
		super();
		// default behavior for pickOnBounds might be false BUT this does NOT ensure default behavior to be executed
		// we NEED to manually set pickOnBounds(false)
		setPickOnBounds(false);
	}

	public void addBackgroundPane(SmartPane p) {
//		if (!(getChildren().get(0) instanceof SmartSphere<?>)) throw new IllegalStateException("THIS IS NOT A TRANSLATION PANE");
//		if (p == null) return;
//		if (getChildren().contains(p) == true) {
//			System.out.println("(addBackgroundPane) " + "BackgroundPane already child of node " +
//					((SmartSphere<?>)getChildren().get(0)).getAstronomicalObject().toString());
//			return;
//		}
//		System.out.println("(addBackgroundPane) " + "BackgroundPane added to node " +
//				((SmartSphere<?>)getChildren().get(0)).getAstronomicalObject().toString());
//		getChildren().add(p);
	}
	public void removeBackgroundPane(SmartPane p) {
//		if (!(getChildren().get(0) instanceof SmartSphere<?>)) throw new IllegalStateException("THIS IS NOT A TRANSLATION PANE");
//		if (p == null) return;
//		if (getChildren().contains(p) == true) {
//			System.out.println("(removeBackgroundPane) " + "BackgroundPane is child of node and will be removed " +
//					((SmartSphere<?>)getChildren().get(0)).getAstronomicalObject().toString());
//			getChildren().remove(p);
//		}
	}
	

	
	public void addCamera(Camera camera) {
		if (!(getChildren().get(0) instanceof SmartSphere<?>)) throw new IllegalStateException("THIS IS NOT A TRANSLATION PANE");
		//Assert this container is not already the camera focus
		if (getChildren().contains(camera) == true) {
			System.out.println("(addCamera) " + "Camera already child of node " +
					((SmartSphere<?>)getChildren().get(0)).getAstronomicalObject().toString());
			return;
		}
		System.out.println("(addCamera) " + "Camera added to node " +
				((SmartSphere<?>)getChildren().get(0)).getAstronomicalObject().toString());
		getChildren().add(camera);
	}
	
	public void removeCamera() {
		if (!(getChildren().get(0) instanceof SmartSphere<?>)) throw new IllegalStateException("THIS IS NOT A TRANSLATION PANE");
		// nothing needs to be done when changing either CamMode or ChaseCam target
		// the cam gets auto removed from the previous target and we actually don't want to remove it when
		// changing to FreeCam so we don't need to calculate the new CamPos
		//
		// we need it when we change from ChaseCam to FreeCam
		// no need for ChaseCam to ChaseCam but no harm done either
		//maybe not needed, seems system manages this for us already
		//see doc @Node silent removal of nodes which are present in the tree when added 
		Stage stage = (Stage) getScene().getWindow();
		Camera camera = null;
		if (stage instanceof SmartStage<?>) {
			camera = ((SmartStage<?>)stage).getSubScene().getCamera();
		}
		if (getChildren().contains(camera) == true) {
			System.out.println("(removeCamera) " + "Camera is child of node " +
					((SmartSphere<?>)getChildren().get(0)).getAstronomicalObject().toString());
			getChildren().remove(camera);
			return;
		}
//		getChildren().remove(camera);
	}
}
