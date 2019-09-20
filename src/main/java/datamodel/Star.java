package datamodel;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;
import util.SmartSphere;

public class Star extends AstronomicalObject {
    /**
     * Initializes a new instance of the Star class. Sets the name of this object as well as the
     * bounding object.
     * @param name Name for the new object.
     */
    public Star(final String name,
                final PhysicalCharacteristics physicalCharacteristics,
                final OrbitalCharacteristics orbitalCharacteristics) {
        super(name, "Star", physicalCharacteristics, orbitalCharacteristics);
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

		// some extra effects for lighting for a star type
		// parameterize the lighting e.g. red dwarfs or super giants do have different colors and light effects 
		material.setDiffuseColor(Color.WHITE);
		material.setSpecularColor(Color.WHITE);
		material.setSelfIlluminationMap(img);
		material.setSpecularPower(10000.0);
		//special case for objects of type "Star" - no way we can show stars - planets and moons on the same scale
		scale *= SCALE_SOL_RADIUS;

		celestial = new SmartSphere<Star>(radius / scale, this);

		celestial.setMaterial(material);
		celestial.setPickOnBounds(false);

		applyAxialTilt(celestial, axialTilt);
		prepareAnimation(celestial, rotationPeriod);

		return celestial;
	}
}
