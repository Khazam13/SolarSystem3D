package datamodel;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import application.AnimationHandler;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import util.ResourceLoader;
import util.SmartPane;
import util.SmartScene;
import util.SmartSphere;
import util.SmartStage;

/**
 * This is the base class for all astronomical objects.
 * This class is responsible for the following tasks:
 * holds all data for the physical characteristics
 * holds all data for the orbital characteristics
 * holds the visual representation of the astronomical object e.g. a Sphere for a planet
 * holds the visual representation of the orbit for the astronomical object it represents
 * 
 * @author RKastner
 *
 */
public class AstronomicalObject {
	/**
	 * 1 AU is defined as 149597870700m 
	 */
	protected static final double AU = 149597870.7;

	/**
	 * Scale factor for the distance of all astronomical objects from their central object
	 * Distance values are given in AU's
	 * Scale factor is used as divisor and bigger values are used for a overall smaller solar system 
	 */
	protected static final double SCALE_DISTANCE = 4000000;
//	protected static final double SCALE_DISTANCE = 5000000;
	
	/**
	 * Scale factor for the size of the astronomical objects.
	 * Size of astronomical objects is given in km.
	 * Scale factor is used as divisor and bigger values are used for a overall smaller solar system objects 
	 */
	protected static final double SCALE_RADIUS = 1000;
//	protected static final double SCALE_RADIUS = 2000;

	/**
	 * Special scale factor to apply only to the sun - its just too big
	 */
	protected static final double SCALE_SOL_RADIUS = 7;
//	protected static final double SCALE_SOL_RADIUS = 10;
	
	// test scale factor for the inclination of the orbits
	protected static final double SCALE_INCLINATION = 1.0;

	/**
	 * Multiplier for rotation period of all objects, higher absolute values speed up the rotation.
	 * This value is used as divisor for the rotation speed of all astronomical objects.
	 * Rotation of astronomical objects is given as number of days for one full rotation.
	 * 
	 * we negate the value for a inverse rotation which is the actual rotation we want to show
	 * astronomical objects in the solar system usually rotate counter clockwise
	 */
	protected double ROTATION_PERIOD_SPEED = 86400/10;
//	protected double ROTATION_PERIOD_SPEED = 20000;
	
	/**
	 * Orbital period is given in days and is the time it takes a astronomical object to complete
	 * one full orbit.
	 * Our internal computation is in seconds.
	 * For example the orbital period for earth is 365 days.
	 * 60 * 60 * 24 * 365 = 86400 * 365 is the time in seconds
	 * Multiplier for orbital period, higher values increase the orbital speed
	 * 0.0 for no orbital movement
	 * 10 for default
	 */
	protected double ORBITAL_PERIOD_SPEED = 86400/1;
//	protected double ORBITAL_PERIOD_SPEED = 1086400;

	/**
	 * Name of this astronomical object.
	 */
	private final String name;

	/**
	 * Type of this astronomical object.
	 */
	private final String type;

	/**
	 * The physical data (radius, mass) of the astronomical object.
	 */
	private final PhysicalCharacteristics physicalCharacteristics;

	/**
	 * The orbit of this astronomical object.
	 */
	private final OrbitalCharacteristics orbit;

	/**
	 * The astronomical objects that orbit this object.
	 */
	private List<AstronomicalObject> satellites;

	private Pane _visualAstronomicalDelegate;
	/**
	 * @return the _visualAstronomicalDelegate
	 */
	public Pane getVisualAstronomicalDelegate() {
		return _visualAstronomicalDelegate;
	}

	private Pane _visualOrbitalDelegate;
	/**
	 * @return the _visualOrbitalDelegate
	 */
	public Pane getVisualOrbitalDelegate() {
		return _visualOrbitalDelegate;
	}

	/**
	 * Initializes a new instance of the AstronomicalObject class. Sets the name of
	 * the new object and its physical properties, such as radius, mass and so on.
	 * 
	 * @param name                    Name for the new object.
	 * @param type                    Type of the new object ("Star", "Planet", "Satellite", "Ring")
	 * @param physicalCharacteristics Physical characteristics for the new object.
	 * @param orbitalCharacteristics  Orbit characteristics for the new object.
	 */
	public AstronomicalObject(final String name, final String type, final PhysicalCharacteristics physicalCharacteristics,
			final OrbitalCharacteristics orbitalCharacteristics) {
		this.name = name;
		this.type = type;
		this.physicalCharacteristics = physicalCharacteristics;
		this.orbit = orbitalCharacteristics;
		
		this._visualAstronomicalDelegate = createDelegate();
	}

	/**
	 * Returns a map of strings describing the astronomical object 
	 * @return
	 */
	public Map<String, String> getProperties() {
		Map<String, String> properties = new LinkedHashMap<>();

		properties.put("name", name);
		properties.putAll(physicalCharacteristics().getProperties());

		if (orbit != null) {
			properties.putAll(orbit.getProperties());
		}

		if (satellites != null) {
			int i = 0;
			for (AstronomicalObject satellite : satellites) {
				properties.put("Satellite #" + i++, satellite.getName());
			}
		}

		return properties;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	private PhysicalCharacteristics physicalCharacteristics() {
		//safety check for null data - we don't want to actually set the physical characteristics
		if (physicalCharacteristics == null) {
			return new PhysicalCharacteristics
			                .Builder(0.0)
			                .mass(0.0)
			                .rotationPeriod(0.0)
			                .axialTilt(0.0)
			                .build();
		}
		return physicalCharacteristics;
	}
	
	public double getRadius() {
		return physicalCharacteristics().getRadius();
	}
	
	public double getMass() {
		return physicalCharacteristics().getMass();
	}

	public double getRotationPeriod() {
		return physicalCharacteristics().getRotationPeriod();
	}

	public double getAxialTilt() {
		return physicalCharacteristics().getAxialTilt();
	}

	/**
	 * Return the orbit data for the astronomical object, maybe null for stars.
	 * 
	 * @return the orbital characteristics
	 */
	public OrbitalCharacteristics getOrbit() {
		return orbit;
	}

	/**
	 * Return a list of AstronomicalObject, maybe null if no satellites are present
	 * 
	 * @return a list of AstronomicalObject holding all satellites
	 */
	public List<AstronomicalObject> getSatellites() {
		return satellites;
	}

	/**
	 * Add a satellite if this is either a star or a planet
	 * 
	 * @param satellite
	 */
	public void addSatellite(AstronomicalObject satellite) {
		//Lazy instantiation with double check for thread safety - just for programming exercise
		if (satellites == null) {
			synchronized (this) {
				if (satellites == null) {
					satellites = new LinkedList<>();
				}
			}
		}
		satellites.add(satellite);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// All functionality for the visual delegate
	//
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
    /**
     * Loads a local image for the astronomical object of name given as a parameter.
     * 
     * RESOURCE FILES IN .JAR ARE CASE SENSITIVE !!!!!!!!!!!!!!!!!!!
     * 
     * @param name Astronomical object unique name.
     */
    protected Image loadObjectLocalImage(String name) {
    	ResourceLoader rl = ResourceLoader.getInstance();
    	Image img;
    	// try for image in .jpg format
    	String resourceName = "2k_" + name.toLowerCase() + ".jpg";
       	img = rl.loadImage(resourceName);
       	if (img == null) {
        	//retry for .png format
        	//if no png found either the exception escalates
            System.out.println("No jpg resource for Star System object of name " + name + ". Retrying for png.");
        	resourceName = "2k_" + name.toLowerCase() + ".png";
//        	resourceName = "/res/2k_" + name.toLowerCase() + ".png";
           	img = rl.loadImage(resourceName);
           	if (img == null) {
           		System.out.println("No png resource for Star System object of name " + name + ".");
			}
       	}
        return img;
    }

    /**
     * Interface for the creation of the orbit delegate and the orbital translation
     * This is necessary as we can't create the orbit delegate at instantiation time as
     * we can't assure that all objects we need are already instantiated.
     * 
     * We need both the central object which this astronomical objects orbits and all
     * the satellites and rings so we can calculate the actual size of the orbit.
     */
	public void createOrbitalDelegate() {
		if (_visualOrbitalDelegate != null) {
			// ASSERT FAIL - may happen later when we can change our viewpoint, right now we throw exception
			throw new IllegalStateException("Visual Orbital Delegate already set!");
		}
		// This is legal as we don't set orbit for stars
    	if (getOrbit() == null || (getOrbit().getPerihelion() == 0.0 && getOrbit().getAphelion() == 0.0)) return;
		
		_visualOrbitalDelegate = new SmartPane();
		_visualOrbitalDelegate.setMouseTransparent(true);

    	Ellipse ellipse = createEllipticalOrbit(false);
    	if (getType() != "Satellite") {
    		_visualOrbitalDelegate.getChildren().addAll(ellipse);
    	} else {
    		_visualOrbitalDelegate.getChildren().addAll(ellipse);
            ellipse.setStroke(Color.TRANSPARENT);
//            ellipse.setStrokeWidth(1);
    	}
    	
		Pane translationPane = (Pane)getVisualAstronomicalDelegate().getChildrenUnmodifiable().get(0);
    	applyOrbitalTransition(translationPane);
    	applyInclination();
    	applyLongitudeOfAscendingNode();
	}
	
	public void buildHierarchy() {
		// for all but the star hook up the visual hierarchy
		OrbitalCharacteristics orbit = getOrbit();
		if (orbit == null) {
			System.out.println("orbit == null for " + getName());
			return;
		}
		AstronomicalObject centralObject = orbit.getCentralObject();
		if (centralObject == null) {
			System.out.println("centralObject == null for " + getName());
		}
		Pane centralVisualAstronomicalDelegate = centralObject.getVisualAstronomicalDelegate();
		if (centralVisualAstronomicalDelegate == null) {
			System.out.println("centralVisualAstronomicalDelegate == null for " + getName());
		}
		Pane centralObjectTranslationPane = (Pane)centralVisualAstronomicalDelegate.getChildren().get(0);
		if (centralObjectTranslationPane == null) {
			System.out.println("centralObjectTranslationPane == null for " + getName());
		}
		
		
//    	Pane centralObjectTranslationPane = (Pane)getOrbit().getCentralObject().getVisualAstronomicalDelegate().getChildren().get(0);
    	centralObjectTranslationPane.getChildren().addAll(getVisualAstronomicalDelegate());
    	if (getVisualOrbitalDelegate() != null) {
        	centralObjectTranslationPane.getChildren().addAll(getVisualOrbitalDelegate());
    	}
	}

	@SuppressWarnings("unchecked")
	private Pane createDelegate() {
		//the topmost enclosing container is used for the inclination
		//the 2nd container is for the orbital translation which will hold the actual visual object
		//and - if present the objects which are bound gravitationally to this one like rings and moons
		// the inclinationPane will be added to the central objects translationPane 
		SmartPane inclinationPane = new SmartPane();
		SmartPane translationPane = new SmartPane();

		Shape3D planet = createAstronomicalDelegate();

		translationPane.getChildren().addAll(planet);
		inclinationPane.getChildren().addAll(translationPane);

		
		
		if (!(planet instanceof SmartSphere<?>)) {
			planet.setMouseTransparent(true);
			return inclinationPane;
		}

//		Box boundingBox1 = ((SmartSphere<?>)planet).getBoundingBox();
//		translationPane.getChildren().addAll(boundingBox1);
//		boundingBox1.setMouseTransparent(true);

		planet.setOnMouseClicked((event) -> {
//			System.out.println("(CLICKED) Planet "+ planet + " " + ((SmartSphere<AstronomicalObject>)planet).getAstronomicalObject().getName());
//			System.out.println("(CLICKED) Planet "+ planet + " pickOnBounds: " + planet.pickOnBoundsProperty().get());

			SmartScene subScene = ((SmartStage<?>)planet.getScene().getWindow()).getSubScene();
			subScene.onMouseClicked((SmartSphere<AstronomicalObject>)planet);
		});
		System.out.println("Register (CLICKED) Planet "+ ((SmartSphere<AstronomicalObject>)planet).getAstronomicalObject().getName());
		
//		planet.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET, event ->
//			System.out.println(
//					event.getEventType() + " " +
//					String.format("%.0f", event.getScreenX()) + ":" +
//					String.format("%.0f", event.getScreenY()) + " " +
//					"Inc " + inclinationPane +
//					" Trans " + inclinationPane.getChildrenUnmodifiable().get(0) +
//					" Planet " + ((Pane)inclinationPane.getChildrenUnmodifiable().get(0)).getChildrenUnmodifiable().get(0) +
//					" " + ((SmartSphere<AstronomicalObject>)((Pane)inclinationPane.getChildrenUnmodifiable().get(0)).getChildrenUnmodifiable().get(0)).getAstronomicalObject().getName() +
//					""));
//		planet.addEventFilter(MouseEvent.MOUSE_EXITED_TARGET, event -> 
//			System.out.println(event.getEventType() + " " +
//			String.format("%.0f", event.getScreenX()) + ":" +
//			String.format("%.0f", event.getScreenY()) + " " +
//			"Planet " + planet));
		
		return inclinationPane;
	}

	/*
	 * Helper to find and return the actual sphere object representing the Star, Planet or Satellite.
	 * We just traverse the FIRST child in a given node until we find a  child which is not a instance of Parent.
	 * Its guaranteed that somewhere in the hierarchy of the first child we find a Shape3D object
	 */
	private Shape3D getVisualDelegateShape3D() {
		Node node = _visualAstronomicalDelegate;
		while (node instanceof Parent) node = ((Parent)node).getChildrenUnmodifiable().get(0);
		
		Shape3D s = null;
		if (node instanceof Shape3D) {
			s = (Shape3D)node;
		} else {
			throw new NullPointerException();
		}
		return s;
	}

	public Shape3D getVisualDelegate() {
		Node n = getVisualAstronomicalDelegate();
		while (n instanceof Parent) n = ((Parent)n).getChildrenUnmodifiable().get(0);
		if (n instanceof Shape3D) {
			return (Shape3D)n;
		} else {
			throw new IllegalStateException("Visual Delegate Shape3D Object not found! " + this.toString());
		}
	}

	private void applyInclination() {
		if (getOrbit() == null) return;

		double inclination = getOrbit().getInclination();
		// test scale
		inclination *= SCALE_INCLINATION * -1;

		System.out.println("(inclination): " + getName() + inclination);
		
		
		Rotate rotate = new Rotate(inclination, Rotate.Y_AXIS);
		getVisualAstronomicalDelegate().getTransforms().add(rotate);
		getVisualOrbitalDelegate().getTransforms().add(rotate);
	}
	
	/**
	 * Applies the longitude of the ascending node to the orbit. This is a rotation around the Y-Axis
	 * Now we have both the inclination and the longitude of ascending node applying rotation transforms
	 * to the same Node, inclination around the Z-Axis and longitude of ascending node around Y-Axis
	 * Therefore we need an Affine transform here.
	 * 
	 */
	private void applyLongitudeOfAscendingNode() {
		if (getOrbit() == null) return;

		double longitudeOfAscendingNode = getOrbit().getLongitudeOfAscendingNode();

		System.out.println("(applyLongitudeOfAscendingNode): " + getName() + " " + longitudeOfAscendingNode);
		
		
		Rotate rotate = new Rotate(longitudeOfAscendingNode, Rotate.Z_AXIS);
		getVisualAstronomicalDelegate().getTransforms().add(rotate);
		getVisualOrbitalDelegate().getTransforms().add(rotate);
	}
	
	/**
	 * Argument of Perihelion describes the angle at which the planet actually is at a given time on its orbit
	 * 
	 * The translationPane needs to be manipulated here
	 * 
	 */
	private void applyArgumentOfPerihelion(PathTransition transition, double orbitalPeriod) {
		if (getOrbit() == null) return;

		double argumentOfPerihelion = getOrbit().getArgumentOfPerihelion();
		orbitalPeriod = orbitalPeriod * argumentOfPerihelion / 360;
		
		Duration time = Duration.seconds(orbitalPeriod);

		transition.jumpTo(time);
		System.out.println("(applyArgumentOfPerihelion): " + getName() + " " + argumentOfPerihelion);
		
	}
	
	class Size2D {
		double width = 0;
		double height = 0;

		Size2D(double w, double h) {
			width = w;
			height = h;
		}
		public String toString() {
			return "Width: " + width + " Height: " + height; 
		}
	}
	
	private double calculateXTranslation() {
		double x = 0;
		//if no moons or rings we translate for half satellite size
		if (getSatellites() == null) {
			x = getVisualDelegateShape3D().getBoundsInLocal().getWidth() / 2;
			return x;
		}

		double width = getVisualDelegateShape3D().getBoundsInLocal().getWidth();
		double satelliteWidth = 0;
		
		for (AstronomicalObject o : getSatellites()) {
			// Rings - now handle rings
			if (o == null || o.getVisualOrbitalDelegate() == null) {
				double ringWidth = o.getVisualDelegateShape3D().getBoundsInLocal().getWidth();
				satelliteWidth = Math.max(satelliteWidth, ringWidth);

				System.out.println("(calculateXTranslation): " + getName() + " : " + o.getName() +
						" Width: " + o.getVisualDelegateShape3D().getBoundsInLocal().getWidth() +
						" Height: " + o.getVisualDelegateShape3D().getBoundsInLocal().getHeight() +
						" Depth: " + o.getVisualDelegateShape3D().getBoundsInLocal().getDepth());
			} else {
				double orbitWidth = o.getVisualOrbitalDelegate().getBoundsInLocal().getWidth();
				satelliteWidth = Math.max(satelliteWidth, orbitWidth);
			}
		}
		width = width / 2 + satelliteWidth;
		x = width / 2;
		
		return x;
	}
	private double calculateYTranslation() {
		double y = 0;
		//if no moons or rings we translate for half satellite size
		if (getSatellites() == null) {
			y = getVisualDelegateShape3D().getBoundsInLocal().getDepth() / 2;
			return y;
		}
		//with moons we need orbit + moon size / 2
		double height = getVisualDelegateShape3D().getBoundsInLocal().getHeight();
		double satelliteHeight = 0;
		
		System.out.println("(calculateYTranslation): " + getName() +
		" Width: " + getVisualDelegateShape3D().getBoundsInLocal().getWidth() +
		" Height: " + getVisualDelegateShape3D().getBoundsInLocal().getHeight() +
		" Depth: " + getVisualDelegateShape3D().getBoundsInLocal().getDepth());
		
		for (AstronomicalObject o : getSatellites()) {
			// Rings - very strange behavior
			// if moons with larger orbits are present, there is no problem - the size of moon orbit will take
			// precedence
			// without moons or moons inside the rings it gets quite strange as the ring size has no influence
			// on the Y-Translation but we need to add an extra satellite radius
			if (o == null || o.getVisualOrbitalDelegate() == null) {
				satelliteHeight = Math.max(satelliteHeight, height / 2);
			} else {
				double orbitHeight = o.getVisualOrbitalDelegate().getBoundsInLocal().getHeight();
				satelliteHeight = Math.max(satelliteHeight, orbitHeight);

				System.out.println("(calculateYTranslation): " + getName() + " : " + o.getName() +
						" Orbit Width: " + o.getVisualOrbitalDelegate().getBoundsInLocal().getWidth() +
						" Orbit Height: " + o.getVisualOrbitalDelegate().getBoundsInLocal().getHeight() +
						" Orbit Depth: " + o.getVisualOrbitalDelegate().getBoundsInLocal().getDepth());
			
				System.out.println("(calculateYTranslation): " + getName() + " : " + o.getName() +
						" Width: " + o.getVisualDelegateShape3D().getBoundsInLocal().getWidth() +
						" Height: " + o.getVisualDelegateShape3D().getBoundsInLocal().getHeight() +
						" Depth: " + o.getVisualDelegateShape3D().getBoundsInLocal().getDepth());
			
			}
		}
		height = height / 2 + satelliteHeight;
		y = height / 2;

		System.out.println("(calculateYTranslation): " + getName() +
				" Translation: " + y);

		return y;
	}
	private Size2D sizeWithSatellites() {
		double width = getVisualDelegateShape3D().getBoundsInLocal().getWidth();
		double height = getVisualDelegateShape3D().getBoundsInLocal().getHeight();

		double satelliteWidth = 0;
		double satelliteHeight = 0;
		
		if (getSatellites() != null) {
			for (AstronomicalObject o : getSatellites()) {
				// check this later o == null ????
				if (o == null || o.getVisualOrbitalDelegate() == null) {
					System.out.println("sizeWithSatellites NULL for " + getName() + " Satellite: " + o.getName());
					Bounds ringsBounds = o.getVisualDelegateShape3D().getBoundsInLocal();
					double ringSize = Math.max(Math.max(ringsBounds.getWidth(), ringsBounds.getHeight()), ringsBounds.getDepth());
					satelliteWidth = Math.max(satelliteWidth, ringSize);
					satelliteHeight = Math.max(satelliteHeight, ringSize);
				} else {
					satelliteWidth = Math.max(satelliteWidth, o.getVisualOrbitalDelegate().getBoundsInLocal().getWidth());
					satelliteHeight = Math.max(satelliteHeight, o.getVisualOrbitalDelegate().getBoundsInLocal().getHeight());
				}
			}
		}
		width = Math.max(width, satelliteWidth);
		height = Math.max(height, satelliteHeight);
		
		System.out.println("(sizeWithSatellites): " + getName() +
				" Width: " + width +
				" Height: " + height);
		return new Size2D(width, height);
	}

	/**
	 * Apply the orbital translation - movement of an astronomical object along its orbital path
	 * 
	 * @param translationPane
	 */
	private void applyOrbitalTransition(Pane translationPane) {
    	if (getOrbit() == null || (getOrbit().getPerihelion() == 0.0 && getOrbit().getAphelion() == 0.0)) return;

    	Ellipse ellipse = createEllipticalOrbit(false);
		double orbitalPeriod = getOrbit().getOrbitalPeriod() * 60 * 60 * 24 / ORBITAL_PERIOD_SPEED;
		

		// Do some translation to compensate for the size of the satellite - pathTransition is along the top left corner
//		double inclination = Math.toRadians(getOrbit().getInclination());
//		double translateX = Math.cos(inclination) * satelliteSize.width/2;
//		double translateY = Math.sin(inclination) * satelliteSize.height/2;

		ellipse.setCenterX(ellipse.getCenterX() + calculateXTranslation());
		ellipse.setCenterY(ellipse.getCenterY() + calculateYTranslation());
		
		PathTransition transition = new PathTransition();
		transition.setPath(ellipse);
        transition.setAutoReverse(false);
        transition.setNode(translationPane);
        transition.setInterpolator(Interpolator.LINEAR);
        transition.setDuration(Duration.seconds(orbitalPeriod));
        transition.setCycleCount(Timeline.INDEFINITE);
        
    	applyArgumentOfPerihelion(transition, orbitalPeriod);
        
        AnimationHandler.getInstance().addTransition(transition);
    }
//	/**
//	 */
//	public PathTransition getOrbitalTransition() {
//    	if (getOrbit() == null || (getOrbit().getPerihelion() == 0.0 && getOrbit().getAphelion() == 0.0)) return null;
//
//    	Ellipse ellipse = createEllipticalOrbit(false);
//		double orbitalPeriod = getOrbit().getOrbitalPeriod() / ORBITAL_PERIOD_SPEED;
//		
//		ellipse.setCenterX(ellipse.getCenterX() + calculateXTranslation());
//		ellipse.setCenterY(ellipse.getCenterY() + calculateYTranslation());
//		
//		PathTransition transition = new PathTransition();
//		transition.setPath(ellipse);
//        transition.setAutoReverse(false);
////        transition.setNode(translationPane);
//        transition.setInterpolator(Interpolator.LINEAR);
//        transition.setDuration(Duration.seconds(orbitalPeriod));
//        transition.setCycleCount(Timeline.INDEFINITE);
//        
//        return transition;
//    }

//	/**
//	 * Apply the orbital translation - movement of an astronomical object along its orbital path
//	 * 
//	 * @param translationPane the pane which will be translated along the elliptical orbit path
//	 */
//	private void applyOrbitalTransition(Pane translationPane, boolean invertedOrbit) {
//    	if (getOrbit() == null || (getOrbit().getPerihelion() == 0.0 && getOrbit().getAphelion() == 0.0)) return;
//
//    	Ellipse ellipse = createEllipticalOrbit(invertedOrbit);
//		double orbitalPeriod = getOrbit().getOrbitalPeriod() / ORBITAL_PERIOD_SPEED;
//		
//
//		// Do some translation to compensate for the size of the satellite - pathTransition is along the top left corner
////		double inclination = Math.toRadians(getOrbit().getInclination());
////		double translateX = Math.cos(inclination) * satelliteSize.width/2;
////		double translateY = Math.sin(inclination) * satelliteSize.height/2;
//
//		ellipse.setCenterX(ellipse.getCenterX() + calculateXTranslation());
//		ellipse.setCenterY(ellipse.getCenterY() + calculateYTranslation());
//		
//		PathTransition transition = new PathTransition();
//		transition.setPath(ellipse);
//        transition.setAutoReverse(false);
//        transition.setNode(translationPane);
//        transition.setInterpolator(Interpolator.LINEAR);
//        transition.setDuration(Duration.seconds(orbitalPeriod));
//        transition.setCycleCount(Timeline.INDEFINITE);
//        
//        AnimationHandler.getInstance().addTransition(transition);
//    }
//
	/**
	 * Creates the visual representation of the orbit.
	 * Adjustments are made to "fit" the ellipse to the size of the astronomical objects
	 * 
	 * @param translationPane
	 * @return the ellipse used both for visual representation of orbits and for path translation
	 */
	private Ellipse createEllipticalOrbit(boolean invertedOrbit) {
    	if (getOrbit() == null || (getOrbit().getPerihelion() == 0.0 && getOrbit().getAphelion() == 0.0)) return null;
		/*
		 * Orbital data to place the object at the correct space
		 */
		double aphelion = getOrbit().getAphelion() / SCALE_DISTANCE * AU;
		double perihelion = getOrbit().getPerihelion() / SCALE_DISTANCE * AU;
		
		if(aphelion < perihelion) {
			throw new IllegalArgumentException("The value of aphelion cannot be lower then perihelion! " + getName() +
					" Aphelion: " + aphelion + " Perihelion: " + perihelion);
		}
		
		/*
		 * use the parent size to adjust the distance of the orbiting object so we can actually see the objects
		 * with the SCALE_DISTANCE we use so we can see the outer planets this would result in all moons to orbit 
		 * within the planets.
		 */
		Pane parent = getOrbit().getCentralObject().getVisualAstronomicalDelegate();
		double parentSize = parent.getBoundsInLocal().getWidth();
		Size2D satelliteSize = sizeWithSatellites();
		double orbitAdaptationToSize = parentSize/2 + satelliteSize.width/2;

		Ellipse ellipse = createEllipticalOrbit(aphelion+orbitAdaptationToSize, perihelion+orbitAdaptationToSize, invertedOrbit);
		return ellipse;
	}

	/**
	 * Basic method to create a ellipse with the given parameters
	 * 
	 * @param aphelion
	 * @param perihelion
	 * @return the ellipse used both for visual representation of orbits and for path translation
	 */
	private Ellipse createEllipticalOrbit(double aphelion, double perihelion) {
        Ellipse ellipse = new Ellipse();
        
        /*
         * aphelion is the point where the 2 objects are farthest away from each other
         * perihelion is the point where both objects are closest
         * aphelion + perihelion = major axis of ellipse
         * center = (aphelion + perihelion) / 2
         * f1 = perihelion and f2 = aphelion
         */
        double sMajorA = (aphelion + perihelion) / 2;
        double focus = (aphelion - perihelion) / 2;
        // semiMinorAxis b = sqrt(a²-c²) where a = semiMajorAxis and c = focusDistance from center
        double sMinorA = Math.sqrt(Math.pow(sMajorA, 2.0)-Math.pow(focus, 2.0));
        
        ellipse.setCenterX(focus);
        ellipse.setCenterY(0);
        ellipse.setRadiusX(sMajorA);
        ellipse.setRadiusY(sMinorA);

        /*
         * The stroke width for the ellipse adds to the bounds!!!!
         * The stroke width adds in all directions -x +x and -y +y
         * but the drawn middle of the stroke is at the correct position
         * for a stroke width of 100 the bounds of the ellipse would be 100 bigger
         * -50 to +50 for minX and maxX
         * No need to recalculate the pathTransition for planets to "stay" in the middle of the "highway"
         */
        ellipse.setStroke(Color.SLATEGREY);
        ellipse.setFill(Color.TRANSPARENT);
        ellipse.setStrokeWidth(1);

        return ellipse;
	}

	/**
	 * Basic method to create a ellipse with the given parameters
	 * 
	 * @param aphelion
	 * @param perihelion
	 * @param invertedOrbit depending on invertedOrbit focus1 or focus2 which will be set as center
	 * @return the ellipse used both for visual representation of orbits and for path translation
	 */
	private Ellipse createEllipticalOrbit(double aphelion, double perihelion, boolean invertedOrbit) {
		Ellipse ellipse = createEllipticalOrbit(aphelion, perihelion);

		if (invertedOrbit == true) {
	        ellipse.setCenterX(-(ellipse.getCenterX()));
		}
        return ellipse;
	}
	
	/**
     * Creates the actual Shape3D object representing the astronomical object.
     * For stars, planets and moons we use a javafx.shape.Sphere class
     * For rings we use javafx.shape.Cylinder
     * For everything else this method needs to be overwritten.
     * 
     * 
     * @return the astronomical object
     */
	protected Shape3D createAstronomicalDelegate() {
		/*
		 * All the data necessary to create the actual astronomical object as a sphere
		 */
		String name = getName();
		String type = getType();
		double radius = getRadius();
		double rotationPeriod = getRotationPeriod();
		double axialTilt = getAxialTilt();

		double scale = SCALE_RADIUS;

		Shape3D celestial = null;

		Image img = loadObjectLocalImage(name);
		PhongMaterial material = new PhongMaterial();
		material.setDiffuseMap(img);

		if (type.equals("Ring")) {
			celestial = new Cylinder(radius / scale, 0.1);
//			material.setDiffuseColor(Color.WHITE);
//			material.setSpecularColor(Color.WHITE);
//			material.setSpecularMap(img);
//			material.setSelfIlluminationMap(img);
//			material.setSpecularPower(0);
//			celestial.setDrawMode(DrawMode.LINE);
//			celestial.setOpacity(90.0);
		} else {
			celestial = new SmartSphere<AstronomicalObject>(radius / scale, this);
		}
		celestial.setMaterial(material);

		applyAxialTilt(celestial, axialTilt);
		prepareAnimation(celestial, rotationPeriod);

		return celestial;
	}
	
	/**
	 * Apply the axial tilt to the celestial object. Both are given as parameter
	 * Set the rotation axis
	 * 
	 * @param s the 3D object which is rotated around the Rotate.Z_AXIS for the given factor
	 * @param angle the angle in degree the given 3D object is rotated
	 */
	protected void applyAxialTilt(Node s, double angle) {
		/*
		 * first calculate the angle in radians as the Math library functions only take radians as argument
		 * the angle increases counterclockwise but the axial tilt is given in degree clockwise to the Y-axis
		 * so we calculate with the negative angle
		 */
		double radians = Math.toRadians(angle);

		/*
		 * next we need the rotation axis, the rotation axis is defined by the axial tilt parameter
		 * 
		 */
		double x, y, z;

		x = Math.sin(radians);
		y = 0;
		z = Math.cos(radians);
		
		Point3D rotationAxis = new Point3D(x, y, z);
		s.setRotationAxis(rotationAxis);
		/*
		 * here we need the negative angle as we rotate around the Z-axis 
		 */
		Transform axialTiltRotate = new Rotate(-angle, Rotate.Z_AXIS);
		/*
		 * rotate the astronomical object around the X_AXIS 90 degree so we look at the north pol
		 * and all further modifications like inclination and orbit calculations are in-plane of ecliptic
		 * this is cause of the limitations of the javafx.shape.ellipse class which is only a 2D Shape
		 * a further limitation is the path transition along the ellipse path is clockwise only - as all objects
		 * in the solar system rotate counterclockwise we need to flip the entire thing again 180° around X-axis
		 */
		axialTiltRotate = new Rotate(90.0, Rotate.X_AXIS).createConcatenation(axialTiltRotate);
		axialTiltRotate = axialTiltRotate.createConcatenation(new Rotate(180.0, Rotate.X_AXIS));
		s.getTransforms().add(axialTiltRotate);
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
		System.out.println("PrepareAnimation: " + " Rotation: " + rotation);
		// now we scale - the higher our scale factor the less time a full rotation needs
		rotation = rotation / ROTATION_PERIOD_SPEED;
		// the angle in degree for exactly 1 nano
		double angleForOneNano = 360 / rotation / 24 / 60 / 60 / 1000 / 1000 / 1000;
		
		AnimationTimer timer = new AnimationTimer() {
			long oldTimeStamp = 0;

			@Override
			public void handle(long now) {
				long passed = now - oldTimeStamp;
				oldTimeStamp = now;
				double angle = angleForOneNano * passed;
				node.rotateProperty().set(node.getRotate() +  angle);
			}
		};
		AnimationHandler.getInstance().addAnimationTimer(timer);
	}
}