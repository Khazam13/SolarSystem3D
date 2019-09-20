package application;

import java.util.List;

import datamodel.AstronomicalObject;
import datamodel.SolarSystemModel;
import datamodel.StarSystemModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import util.CoordinateHelper;
import util.Debug;
import util.SmartScene;
import util.SmartSphere;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Sun - AxialTilt = 0 - plane of ecliptic = x,y
 * 
 * Planet - RotationAxis Z
 *  - put in a Group - apply AxialTilt around Y to the Group - apply Orbit to Group
 *  - add to another group - Inclination of Orbit - rotate around Z for longitude of ascending node
 *  - set the start point on the orbit to the argument of Perihelion
 * 
 * @author Roland Kastner
 *
 */

public class StarSystemScene extends SmartScene {
	protected double ORBITAL_PERIOD_SPEED = 40;

    /**
     * Planetary system model used to initialize the view.
     */
    protected StarSystemModel starSystem = null;
    
    /**
     * location for the star system data resource
     */
    protected String starSystemResource = null;

	public StarSystemScene(Parent root, Stage window) {
		super(root, window);
	}

	public StarSystemScene(Parent root, Stage window, String resource) {
		super(root, window);
		starSystemResource = resource;
	}

	/**
	 * Add WindowListener for Resize Event
	 * Window Width and Height are still not set!
	 */
	public void init() {
		starSystem = new SolarSystemModel(starSystemResource);

		Stage stage = (Stage)getWindow();
		ChangeListener<Number> sceneSizeListener = (observable, oldValue, newValue) ->
		{
			center();
		};
	    stage.widthProperty().addListener(sceneSizeListener);
	    stage.heightProperty().addListener(sceneSizeListener); 
	    
	    stage.setMinHeight(PREF_MIN_HEIGHT+borderHeight);
	    stage.setMinWidth(PREF_MIN_WIDTH+borderWidth);
	    
	    createCelestial();
	    center();
	    
	    initCamera();
	    
	    ((Group)getRoot()).getChildren().add(backgroundSkyBox);
	    resetBackground();
	    
	    
	    PointLight pLight = new PointLight();
	    ((Group)getRoot()).getChildren().add(pLight);
	    pLight.setColor(Color.rgb(225,225,225,0.50));
	    
	    AmbientLight aLight = new AmbientLight();
	    ((Group)getRoot()).getChildren().add(aLight);
	    aLight.setColor(Color.rgb(75, 75, 75));

//	    PointLight pLight = new PointLight();
//	    ((Group)getRoot()).getChildren().add(pLight);
//
//	    PointLight camLight = new PointLight();
//	    ((Group)getRoot()).getChildren().add(camLight);
	    
	    
//	    camLight.setColor(Color.rgb(35,40,20,0.25));
//	    camLight.getTransforms().add(cameraTransform);

//	    getCamera().getChildren().add(camLight);

	}

	/**
	 * Add ChangeListener to the Rotation and orbit speed sliders
	 */
	public void initListener() {
		Stage stage = (Stage)getWindow();
		Stage parent = (Stage)stage.getOwner();
		Scene parentScene = parent.getScene();
		Slider orbitSpeedSlider = (Slider) parentScene.lookup("#orbitSpeedSlider");
		Label orbitSpeedValue = (Label) parentScene.lookup("#orbitSpeedValue");

		orbitSpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            	//@TODO set the speed in StarSystemScene
            	ORBITAL_PERIOD_SPEED = new_val.doubleValue();
            	orbitSpeedValue.setText(String.format("%.0f", new_val));
            }
        });	

	}
	
	public void center() {
		Stage stage = (Stage)getWindow();
		double sceneHeight = getHeight();
	    double sceneWidth = getWidth();
		double stageHeight = stage.getHeight();
	    double stageWidth = stage.getWidth();
		if (stageHeight > 0 && stageWidth > 0) {
			if (borderHeight == 0) {
				borderHeight = stageHeight-sceneHeight;
			    stage.setMinHeight(PREF_MIN_HEIGHT+borderHeight);
			}
			if (borderWidth == 0) {
				borderWidth = stageWidth-sceneWidth;
			    stage.setMinWidth(PREF_MIN_WIDTH+borderWidth);
			}
			setHeight(stageHeight-borderHeight);
			setWidth(stageWidth-borderWidth);
		}
	}

	/**
	 * recursive call to get the node which includes the mouse coordinates in its bounds
	 * of course the top most node will always include the coordinates
	 * traverse down until we find the last sub node which still includes the coordinates
	 * this traverse must either return a Shape3D object or null 
	 */
	@Override
	protected Shape3D traverseNodes(Node node, Point2D screenPoint) {

		List<SmartSphere<AstronomicalObject>> visualDelegates = starSystem.getVisualDelegates();

		double viewPort = ((PerspectiveCamera)getCamera()).getFieldOfView();
		SmartSphere<AstronomicalObject> sphere = null;
		Bounds localBounds = null;
		Bounds screenBounds = null;
		Bounds sceneBounds = null;
		double angle = 0;
		double distance = Double.MAX_VALUE;
		Point3D center = null;
		Point2D center2D = null;
		Point3D camPos = camPosition();
		Point3D viewDirection = viewDirection();
		viewDirection = viewDirection.add(camPos);

		Debug.out("(traverseNodes)", "screenPoint:  ", screenPoint);

		for(SmartSphere<AstronomicalObject> s : visualDelegates) {
			if (s instanceof SmartSphere<?>) {
				localBounds = s.getBoundsInLocal();
				sceneBounds = s.localToScene(localBounds);
				screenBounds = s.localToScreen(localBounds);
				
				Point2D scenePt = localToScene(screenToLocal(screenPoint));
				Debug.out("(traverseNodes)", "scenePt: ", scenePt);
				Debug.out("", "sceneBounds:  ", sceneBounds);

				center = CoordinateHelper.centerOfBounds(sceneBounds);
				angle = camPos.angle(viewDirection, center);

				if (angle < viewPort) {
			        if (screenPoint.getX() >= screenBounds.getMinX() && screenPoint.getX() <= screenBounds.getMaxX()
			        		&& screenPoint.getY() >= screenBounds.getMinY() && screenPoint.getY() <= screenBounds.getMaxY()) {
			        	if (camPos.distance(center) < distance) {
			        		distance = camPos.distance(center);
			        		sphere = s;
			        	}
			        	
			        }
					
				}
			}
		}

		if (sphere != null) {
			localBounds = sphere.getBoundsInLocal();
			screenBounds = sphere.localToScreen(localBounds);
			sceneBounds = sphere.localToScene(localBounds);
			center2D = CoordinateHelper.centerOfBounds2D(screenBounds);

			Debug.err("(traverseNodes)", sphere.getAstronomicalObject().getName() + "\n" + "sceneBounds : ", sceneBounds);
			Debug.err("(traverseNodes)", "screenBounds: ", screenBounds);
			Debug.err("(traverseNodes)", "center2D    : ", center2D);
			Debug.err("(traverseNodes)", "screenPoint:  ", screenPoint);

		} else {
			System.out.println("X-Y not matching!");
			for(SmartSphere<AstronomicalObject> s : visualDelegates) {
				localBounds = s.getBoundsInLocal();
				screenBounds = s.localToScreen(localBounds);
				sceneBounds = s.localToScene(localBounds);
				center2D = new Point2D(screenBounds.getMinX()+screenBounds.getWidth()/2,
						screenBounds.getMinY()+screenBounds.getHeight()/2);

			}
		}

		return sphere;
	}

	/**
	 * Hook up the astronomical delegates to the scene
	 */
	private void createCelestial() {
		System.out.println("StarSystemScene.createCelestial");

		Pane starPane = starSystem.getStar().getVisualAstronomicalDelegate();

		Pane cassiniPane = createSpaceShip("src/main/resources/Cassini/Cassini_66.obj");
		cassiniPane.translateXProperty().set(100);
		cassiniPane.translateYProperty().set(100);
		cassiniPane.setMouseTransparent(true);

		starPane.getChildren().addAll(cassiniPane);

		Group root = (Group)getRoot();
		root.getChildren().addAll(starPane);
	}
	
	private Pane createSpaceShip(String meshResource) {
		MeshView[] spaceShip = MeshShape.loadMeshViews(meshResource);
		Pane g = new Pane();
		if (spaceShip != null) g.getChildren().addAll(spaceShip);
		g.scaleXProperty().set(1);
		g.scaleYProperty().set(1);
		g.scaleZProperty().set(1);
		Rotate xRotate = new Rotate(45.0, Rotate.X_AXIS);
		g.getTransforms().add(xRotate);
		g.setRotationAxis(Rotate.Z_AXIS);
	
	//	prepareAnimation(g, rotation);
		return g;
	}

}
