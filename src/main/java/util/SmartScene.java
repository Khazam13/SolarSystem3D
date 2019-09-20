package util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

/**
 * This abstract class extends the class javafx.scene.SubScene and provides the functionality
 * to control the translation and rotation of the Camera in a 3D Scene.
 * 
 * @author RKastner
 *
 */
public abstract class SmartScene extends SubScene {
	
	public static final int CAMERA_MODE_FREE_CAM = 1;
	public static final int CAMERA_MODE_CHASE_CAM = 2;
	public static final double CAMERA_ROTATE_SCALE = 0.1;
	public static final int PREF_MIN_WIDTH = 800;
	public static final int PREF_MIN_HEIGHT = 700;

	protected double borderHeight = 0;
    protected double borderWidth = 0;
    protected Affine cameraTransform = new Affine();

    private final TranslateContext translateContext = new TranslateContext();
    private final RotationContext rotationContext = new RotationContext();
    private final DoubleProperty scale = new SimpleDoubleProperty(1.0);
    private int cameraMode = CAMERA_MODE_FREE_CAM;
	private Stage window = null;
	
	public static final double SKYBOX_SIZE = 100000;
	public static final double FARCLIP = SKYBOX_SIZE * 2;
	public static final double MAX_ZOOM = SKYBOX_SIZE / 3;
//	public static final double SKYBOX_SIZE = 100000;
//	public static final double FARCLIP = SKYBOX_SIZE / Math.sqrt(3) * 2;
//	public static final double MAX_ZOOM = SKYBOX_SIZE / 3;

	public static final String BACKGROUND_RESOURCE = "8k_stars_milky_way2.jpg";
//	public static final String BACKGROUND_RESOURCE = "milchstrasse-dunkel-bg.jpg";
//	public static final String BACKGROUND_RESOURCE = "8k_stars.jpg";
	protected final Node backgroundSkyBox = createSkyBox();

	/**
	 * Constructor which takes the root scene and the reference to the window in which the scene will be displayed
	 * 
	 * @param root
	 * @param window
	 */
	public SmartScene(Parent root, Stage window) {
		super(root, (double) PREF_MIN_WIDTH, (double) PREF_MIN_HEIGHT, true, SceneAntialiasing.BALANCED);
		this.window = window;
		setFill(Color.BLACK);
		// add scale transform
	    root.scaleXProperty().bind(scale);
	    root.scaleYProperty().bind(scale);
	}

	/**
	 *  Get the window for this SubScene
	 * @return the window
	 */
	public Stage getWindow() {
		return window;
	}

	/**
	 * Get the Scale used for the display
	 * @return the scale factor
	 */
	public double getScale() {
        return scale.get();
    }

	/**
	 * Set the scale factor used for the display
	 * 
	 * @param s
	 */
    public void setScale(double s) {
        scale.set(s);
    }

    /**
     * Set the x, y and z values for the pivot point
     *  
     * @param x
     * @param y
     * @param z
     */
    public void setPivot(double x, double y, double z) {
    	rotationContext.setPivot(x, y, z);
    }

    /**
     * Set the pivot point
     * 
     * @param p
     */
    public void setPivot(Point3D p) {
    	rotationContext.setPivot(p);
    }

    /**
     * Set the pivot point. The point is the center of the bounds of the Sphere param
     * 
     * @param s
     */
    public void setPivot(Sphere s) {
    	rotationContext.setPivot(s);
    }

    /**
     * Get the Sphere object which is set as the pivot
     *  
     * @return the pivot Sphere
     */
    public Sphere getPivotSphere() {
    	return rotationContext.getPivotSphere();
    }

    /**
     * Get the pivot point
     * 
     * @return the pivot point
     */
    public Point3D getPivot() {
    	return rotationContext.getPivot();
    }

	/**
     * @return the background sphere
     */
	private Node createSkyBox() {
		Shape3D celestial = null;
		Image img = prepareBackground();
		PhongMaterial material = new PhongMaterial();
		material.setSpecularMap(img);
		material.setDiffuseMap(img);
		material.setSelfIlluminationMap(img);
		celestial = new Sphere(SKYBOX_SIZE);
//		celestial = new Box(SKYBOX_SIZE,SKYBOX_SIZE,SKYBOX_SIZE);
		//cull faces so we actually see the texture from the inside
		celestial.setCullFace(CullFace.NONE);
		celestial.setMaterial(material);

//		SkyBox skybox = new SkyBox(img, img, img, img, img, img, SKYBOX_SIZE);
//		return skybox;
		
		
		
		celestial.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
		return celestial;
	}
	

	private Image prepareBackground() {
    	ResourceLoader rl = ResourceLoader.getInstance();
    	Image img;
    	String resourceName = BACKGROUND_RESOURCE;
    	
//    	String resourceName = "4k_milky_way.png";
//    	String resourceName = "16k12k_stars_milky_way.jpg";
//    	String resourceName = "2k_sun.jpg";

    	img = rl.loadImage(resourceName);
       	if (img == null) {
       		System.out.println("No jpg resource for Star System object of name " + resourceName + ".");
       	}
       	return img;
	}

    
    public abstract void init();

    /**
     * Initializes the camera to a set position.
     * We want to look at the scene from the side and slightly from above.
     * 
     * @param camera
     */
    protected void initCameraPosition(Camera camera) {
		cameraTransform.setToIdentity();
    	camera.getTransforms().add(cameraTransform);

    	cameraRotate(Point3D.ZERO, 0, -800, 0);
    	cameraRotate(Point3D.ZERO, 0, 1800, 0);

    	zoom(-500);
    }
    
    /**
     * Initializes the camera for the chaseCam mode.
     * This method is only called when the camera mode is
     * switched from freeCam to chaseCam and/or the the object the chaseCam is bound to is changed.
     *  
     *  All previously set transforms for the camera are removed (rotations/translations) and the
     *  viewDirection and distance are set to the default values.
     *  
     * @param camera
     */
    protected void initChaseCameraPosition(Camera camera) {
		cameraTransform.setToIdentity();
		
		cameraRotate(getPivot(), 0, 100 / CAMERA_ROTATE_SCALE);
		zoom(-200);
    }
    
	/**
	 * initializes the camera.
	 * 
	 * called by init() from the derived class
	 */
	protected void initCamera() {
		PerspectiveCamera camera = new PerspectiveCamera(true);
		camera.setFieldOfView(30);
		camera.setNearClip(0.1);
		camera.setFarClip(FARCLIP);
		setCamera(camera);
		initCameraPosition(camera);
	}

	/**
	 * Rotate the scene around the x, y and z axis for a set angle
	 * 
	 * @param pivot the pivot point for the rotation
	 * @param dX the angle in degree we rotate around the X-Axis
	 * @param dY the angle in degree we rotate around the Y-Axis
	 * @param dZ the angle in degree we rotate around the Z-Axis
	 */
	public void rotate(Point3D pivot, double dX, double dY, double dZ) {
		if (dX != 0) {
			cameraTransform.appendRotation(dX, pivot, Rotate.X_AXIS);
		}
		if (dY != 0) {
			cameraTransform.appendRotation(dY, pivot, Rotate.Y_AXIS);
		}
		if (dZ != 0) {
			cameraTransform.appendRotation(dZ, pivot, Rotate.Z_AXIS);
		}
	}
	
	/**
	 * Basic rotate for the free camera
	 * 
	 * @param p the pivot point for the rotation
	 * @param dX the angle in degree we rotate around the X-Axis
	 * @param dY the angle in degree we rotate around the Y-Axis
	 * @param dZ the angle in degree we rotate around the Z-Axis
	 */
	private void freeCamRotate(Point3D p, double dX, double dY, double dZ) {
		rotate(p, -dY*CAMERA_ROTATE_SCALE, dX*CAMERA_ROTATE_SCALE, dZ*CAMERA_ROTATE_SCALE);
	}

	/**
	 * Basic rotate for the chase camera.
	 * A chaseCam rotate is basically a rotation and a translation.
	 * A simple solution is to rotate around the pivot planet.
	 * 
	 * @param p the pivot point for the rotation
	 * @param dX the angle in degree we rotate around the X-Axis
	 * @param dY the angle in degree we rotate around the Y-Axis
	 * @param dZ the angle in degree we rotate around the Z-Axis
	 */
	private void chaseCamRotate(Point3D p, double dX, double dY, double dZ) {
		Point3D camPos = camPosition();
		double distance = camPos.distance(p);
		translateNoConstraint(0, 0, distance);
		freeCamRotate(Point3D.ZERO, dX, dY, dZ);
		translateNoConstraint(0, 0, -distance);
	}

	/**
	 * Switch for the rotate depending on the camera mode.
	 * 
	 * @param p the pivot point for the rotation
	 * @param dX the angle in degree we rotate around the X-Axis
	 * @param dY the angle in degree we rotate around the Y-Axis
	 * @param dZ the angle in degree we rotate around the Z-Axis
	 */
	private void cameraRotate(Point3D p, double dX, double dY, double dZ) {
		if (cameraMode == CAMERA_MODE_FREE_CAM) {
			freeCamRotate(p, dX, dY, dZ);
		}
		if (cameraMode == CAMERA_MODE_CHASE_CAM) {
			//translate > to pivot | rotate | translate < pivot
			chaseCamRotate(p, dX, dY, dZ);
		}
		
	}
	
	private void cameraRotate(Point3D p, double dX, double dY) {
		cameraRotate(p, dX, dY, 0);
	}
	
	/**
	 * Reset the background SkyBox to the root of the SubScene graph
	 */
	protected void resetBackground() {
//		starPane.addBackgroundPane(backgroundPane);
	}

	protected void removeChaseCam() {
		Sphere pivot = getPivotSphere();
		if (pivot == null) return;

		Transform localToScene = getCamera().getLocalToSceneTransform();
		((SmartPane)pivot.getParent()).removeCamera();
		cameraTransform.setToIdentity();
		cameraTransform.append(localToScene);
	}

	protected void setChaseCam(SmartSphere<?> sphere) {
		Parent p = sphere.getParent();
		if (!(p instanceof SmartPane)) throw new IllegalStateException("THIS IS NOT A SmartPane: " + p);
		((SmartPane)p).addCamera(getCamera());
	}

	/**
	 * recursive call to get the node which includes the mouse coordinates in its bounds
	 * of course the top most node will always include the coordinates
	 * traverse down until we find the last sub node which still includes the coordinates
	 * this traverse must either return a Shape3D object or null 
	 * 
	 * @param node the node from which we traverse all children
	 * @param point the point which must be included in the node to traverse
	 */
	abstract protected Shape3D traverseNodes(Node node, Point2D point);

	/**
	 * Calculate the view direction in scene coordinates
	 * 
	 * @return point which is the direction the camera is facing in scene coordinates
	 */
	protected Point3D viewDirection() {
		Point3D viewDirection = cameraTransform.deltaTransform(new Point3D(0,0,1));
		return viewDirection;
	}

	/**
	 * Returns the position of the camera in scene coordinates
	 * 
	 * @return the camera position in scene coordinates
	 */
	public Point3D camPosition() {
		Camera camera = getCamera();
		Bounds local = camera.getBoundsInLocal();
		return camera.localToScene(local.getMinX(), local.getMinY(), local.getMinZ());
	}

	/**
	 * Heavyweight method which checks for any translation of the camera if a boundary violation is detected.
	 * Camera can not be moved outside the SkyBox.
	 * 
	 * @param dX the amount we translate along X-Axis
	 * @param dY the amount we translate along Y-Axis
	 * @param dZ the amount we translate along Z-Axis
	 * @return true only if the resulting position of the camera wont violate the boundary check.
	 */
	private boolean checkClipDistance(double dX, double dY, double dZ) {
		Affine distCheck = new Affine();
		distCheck.setToTransform(cameraTransform);
		distCheck.appendTranslation(dX, dY, dZ);
		Point3D checkPoint = distCheck.transform(Point3D.ZERO);
		double distance = checkPoint.distance(Point3D.ZERO);
		
		if (distance > MAX_ZOOM) return false;

		return true;
	}

	/**
	 * A fast translation without boundary checks.
	 * Only to be called from {@link #chaseCamRotatePoint3D p, double dX, double dY, double dZ)}
	 * 
	 * @param dX the amount we translate along X-Axis
	 * @param dY the amount we translate along Y-Axis
	 * @param dZ the amount we translate along Z-Axis
	 */
	private void translateNoConstraint(double dX, double dY, double dZ) {
		cameraTransform.appendTranslation(dX, dY, dZ);
	}
	
	/**
	 * A translate with boundary check.
	 * If the boundary check returns false no action is taken.
	 * 
	 * @param dX the amount we translate along X-Axis
	 * @param dY the amount we translate along Y-Axis
	 * @param dZ the amount we translate along Z-Axis
	 */
	public void translate(double dX, double dY, double dZ) {
		if (checkClipDistance(dX, dY, dZ) == false)
			return;
		cameraTransform.appendTranslation(dX, dY, dZ);
	}
	
	
	/**
	 * Translation of the camera in the direction the camera is pointed
	 * At the start the camera should point in direction (0, 0, 1)
	 * 
	 * @param delta
	 */
	public void zoom(double delta) {
		translate(0, 0, delta);
	}

	/**
	 * Eventhandler which will be called by the registered objects from the eventqueue
	 * This handler is for mouse clicks only and therefore handles the switch to the chase camera mode
	 * 
	 * @param sphere
	 */
	public void onMouseClicked(SmartSphere<?> sphere) {
		if (getPivotSphere() == sphere && cameraMode == CAMERA_MODE_CHASE_CAM) return;

		cameraMode = CAMERA_MODE_CHASE_CAM;
		removeChaseCam();
		setChaseCam(sphere);

		setPivot(sphere);
		initChaseCameraPosition(getCamera());
	}
	
	/**
	 * Initializes the eventhandlers for the different input events we want to react on
	 */
	public void initEventHandler() {
		// Define an event handler for mouse wheel scrolling
		// scrolling the mouse wheel should act as a zoom in and out
		// There is a bug where event.getDeltaY() always returns 0 when shift is down
		// as it switches from vertical to horizontal scrolling 
		// we use the deltaX value with shift for now
		EventHandler<ScrollEvent> scrollHandler = new EventHandler<ScrollEvent>() {
			public void handle(ScrollEvent event) {
				double delta = event.getDeltaY();
				double scrollMultiplier = 25;
				if(event.isShiftDown()) {
					scrollMultiplier = 5;
					delta = event.getDeltaX();
				}
				if (event.isControlDown()) {
					scrollMultiplier = 0.2;
					delta = event.getDeltaY();
					System.out.println("(SCROLL) " + delta);
				}
				delta *= scrollMultiplier;
				zoom(delta);
			}
		};

		/**
		 * A Click on a object will be handled by their onClick!
		 * 
		 * Left Mouse button down, we want to rotate the scene
		 * as this is the start of our rotate operation we need to get the starting mouse position
		 * while the drag event we calculate the rotation on the amount the mouse was moved since the drag started
		 */
		EventHandler<MouseEvent> pressedHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				double mouseX = event.getSceneX();
				double mouseY = event.getSceneY();
				
				translateContext.setMouseAnchor(mouseX, mouseY);
			}
		};

		/**
		 * Here we handle drag events.
		 * Drag with left mouse button down means a rotation.
		 * Drag while right mouse button is down means a translation.
		 * 
		 * First we need for both operations the delta for the mouse coordinates since the drag operation started.
		 * These coordinates are saved in the dragContext. 
		 * 
		 */
		EventHandler<MouseEvent> dragHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				double mouseX = event.getSceneX();
				double mouseY = event.getSceneY();
				double dX = mouseX - translateContext.getMouseAnchorX();
				double dY = mouseY - translateContext.getMouseAnchorY();
				
				/**
				 * Left Mouse button down, we want to rotate the scene
				 * 
				 * There is a distinct difference between the screen coordinate system and the 3D coordinate system.
				 * The 3D coordinate system is transformed for a top-down view on the solar system so the Y and Z axis are rotated 90°
				 * around the X axis. The 3D coordinate system is as following: 
				 * X-axis left-right
				 * Y-axis toward user-into screen
				 * Z-axis up-down
				 * 
				 * The screen coordinate system remains as is with the X-axis left-right and Y-axis up-down
				 * 
				 * A mouse movement along the X-axis in screen coordinate system is interpreted as a rotation around the Z-axis
				 * A mouse movement along the Y-axis in screen coordinate system is interpreted as a rotation around the X-axis
				 * 
				 */
				if (event.isPrimaryButtonDown()) {
					translateContext.setMouseAnchor(mouseX, mouseY);
					rotationContext.setAngleY(rotationContext.anchorAngleY + dX);
					rotationContext.setAngleX(rotationContext.anchorAngleX - dY);
					cameraRotate(getPivot(), dX, dY);
				}

				/**
				 * Right Mouse button down, we want to move the scene
				 * 
				 * A mouse movement along the X-axis in screen coordinate system is interpreted as a translation along the X-axis
				 * A mouse movement along the Y-axis in screen coordinate system is interpreted as a translation along the Y-axis
				 * 
				 * This translation is indifferent to the current rotation so the user will always see a movement of the scene
				 * as expected.
				 * 
				 */
				if (event.isSecondaryButtonDown()) {
					if (cameraMode == CAMERA_MODE_CHASE_CAM) {
						Debug.out("(dragHandler) ", "Pivot: ", getPivot());
						Debug.out("(dragHandler) ", "CamPos: ", camPosition());
						removeChaseCam();
						setPivot((Sphere)null);
						cameraMode = CAMERA_MODE_FREE_CAM;
					}
					translate(-dX, -dY, 0);
					translateContext.setMouseAnchor(mouseX, mouseY);
				}
			}
		};

		/**
		 * Simple event handler to rotate the scene in all 3 axis with key commands
		 * Each key press is equivalent to a 10° rotation
		 * 
		 * There is a distinct difference between the screen coordinate system and the 3D coordinate system.
		 * The 3D coordinate system is transformed for a top-down view on the solar system so the Y and Z axis are rotated 90°
		 * around the X axis. The 3D coordinate system is as following: 
		 * X-axis left-right
		 * Y-axis toward user-into screen
		 * Z-axis up-down
		 * 
		 * W S - rotate around the X-axis
		 * Q E - rotate around the Y-axis
		 * A D - rotate around the Z-axis
		 * 
		 */
		EventHandler<KeyEvent> keyHandler = new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
				// around X-axis
				case W:
					cameraRotate(getPivot(), 0, -10, 0);
					break;
				case S:
					cameraRotate(getPivot(), 0, 10, 0);
					break;
				// Y-axis
				case Q:
					cameraRotate(getPivot(), 0, 0, -10);
					break;
				case E:
					cameraRotate(getPivot(), 0, 0, 10);
					break;
				// Z-axis
				case A:
					cameraRotate(getPivot(), -10, 0, 0);
					break;
				case D:
					cameraRotate(getPivot(), 10, 0, 0);
					break;
				default:
				}
			}
		};

		/*
		 * add the eventHandler to the scene and NOT to the root container as the root
		 * will only occupy a limited amount of visible space in the stage and by
		 * "zooming" in further then the z-coordinate of the plane we can loose our
		 * eventHendler
		 */
		//Mouse event Handler for panning the scene or rotating, depending on the button pressed
		//Left button means a rotate while right button panes
		window.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, dragHandler);
		window.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, pressedHandler);
		//Scroll Handler for mouse wheel scrolling which means a Zoom for the scene
		window.getScene().addEventHandler(ScrollEvent.SCROLL, scrollHandler);
		//Key event Handler for keys A D Q E W S which will rotate the scene around the X,Y and Z axis clockwise and counterclockwise
		//each key press will rotate for 10 degree around the corresponding axis 
		window.getScene().addEventHandler(KeyEvent.KEY_PRESSED, keyHandler);
	}

	/**
	 * Debug level function
	 * 
	 * @param node
	 * @param level
	 */
	public void traverseChildren(Node node, int level) {
		System.out.println("SmartScene.traverseChildren");
		if (node == null) return;
		String s = "";
		for(int i=0; i<level; i++) s += "-";

		//-------------
		// find my Rings!!
		if (node instanceof Cylinder) {
			Cylinder c = (Cylinder)node;
			System.out.println(s + "Cylinder: " + c.getBoundsInLocal().getMinX() + " : " + c.getBoundsInLocal().getMaxX() +
					" : " + c.getBoundsInLocal().getWidth());
			System.out.println(s + "Screen Coords: "+ c.localToScreen(0,0));
		}
		if (node instanceof Sphere) {
			Sphere c = (Sphere)node;
			System.out.println(s + "Sphere: " + c.getBoundsInLocal().getMinX() + " : " + c.getBoundsInLocal().getMaxX() +
					" : " + c.getBoundsInLocal().getWidth());
			System.out.println(s + "Screen Coords: "+ c.localToScreen(0,0));
		}
		//*************
		
		if (node instanceof Shape3D ) {
	        System.out.println(s + "Shape3D: " + node);
			return;
		} else {
	        System.out.println(s + "Node: " + node);

	        if (node instanceof SubScene) {
	        	node = ((SubScene) node).getRoot();
	        }
	        if (!(node instanceof Parent)) return;
			for (Node n : ((Parent)node).getChildrenUnmodifiable()) {
				traverseChildren(n, level + 1);
			}
		}
	}
}
