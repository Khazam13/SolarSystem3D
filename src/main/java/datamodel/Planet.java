package datamodel;

import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;
import util.SmartSphere;

/**
 * This class represents a planet model.
 */
public class Planet extends AstronomicalObject {
    /**
     * Initializes a new instance of the Planet class. Sets the name of this object as well as
     * the bounding object.
     * @param name Name for the new object.
     * @param physicalCharacteristics
     * @param orbitalCharacteristics
     */
    public Planet(final String name,
                  final PhysicalCharacteristics physicalCharacteristics,
                  final OrbitalCharacteristics orbitalCharacteristics) {
        super(name, "Planet", physicalCharacteristics, orbitalCharacteristics);
    }
    
    /**
     * Creates the actual Shape3D object representing the astronomical object.
     * For stars, planets and moons we use a javafx.shape.Sphere class
     * For rings we use javafx.shape.Cylinder
     * For everything else this method needs to be overwritten.
     * 
     * @see datamodel.AstronomicalObject#createAstronomicalDelegate()
     */
    @Override
	protected Shape3D createAstronomicalDelegate() {
		/*
		 * All the data necessary to create the actual astronomical object as a sphere
		 */
		String name = getName();
		double radius = getRadius();
		double rotationPeriod = getRotationPeriod();
		double axialTilt = getAxialTilt();

		double scale = SCALE_RADIUS;
		Shape3D celestial = null;

		Image img = loadObjectLocalImage(name);
		PhongMaterial material = new PhongMaterial();
		material.setDiffuseMap(img);

		celestial = new SmartSphere<Planet>(radius / scale, this);

		celestial.setMaterial(material);
		celestial.setPickOnBounds(false);

		applyAxialTilt(celestial, axialTilt);
		prepareAnimation(celestial, rotationPeriod);

		return celestial;
	}
}