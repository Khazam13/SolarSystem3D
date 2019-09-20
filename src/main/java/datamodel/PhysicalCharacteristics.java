package datamodel;

import java.util.LinkedHashMap;
import java.util.Map;

public class PhysicalCharacteristics {
	/**
	 * Mean radius of the astronomical object. Given in kilometers.
	 */
	private final double radius;

	/**
	 * Mass of the astronomical object. Given in masses of the planet Earth (M_E).
	 */
	private final double mass;
	
	/**
	 * Sidereal rotation (one complete 360 degree rotation) time. Given in days.
	 */
	private final double rotationPeriod;
	
	/**
	 * Tilt of the rotation axis against the orbital plane. Given in degree.
	 */
	private final double axialTilt;
	
	public Map<String, String> getProperties() {
		Map<String, String> properties = new LinkedHashMap<>();
		properties.put("radius", String.valueOf(radius));
		properties.put("mass", String.valueOf(mass));
		properties.put("rotationPeriod", String.valueOf(rotationPeriod));
		properties.put("axialTilt", String.valueOf(axialTilt));
		return properties;
	}

	/**
	 * Gets the mean radius of the astronomical object (given in kilometers).
	 * 
	 * @return Mean radius of the astronomical object.
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Gets the mass of the astronomical object (given in masses of the planet Earth).
	 * 
	 * @return Mass of the astronomical object.
	 */
	public double getMass() {
		return mass;
	}

	/**
	 * Gets the rotationPeriod of the astronomical object (Given in days.).
	 * 
	 * @return rotationPeriod of the astronomical object.
	 */
	public double getRotationPeriod() {
		return rotationPeriod;
	}

	/**
	 * Gets the axialTilt of the astronomical object (Given in degree).
	 * 
	 * @return axialTilt of the astronomical object.
	 */
	public double getAxialTilt() {
		return axialTilt;
	}

	public static class Builder {
		private final double radius;
		private double mass;
		private double rotationPeriod;
		private double axialTilt;

		/**
		 * Initializes a new instance of the Builder class. Sets the mandatory radius
		 * for the to-be-built PhysicalCharacteristics object.
		 * 
		 * @param radius Mean radius for the new object (in kilometers).
		 */
		public Builder(final double radius) {
			this.radius = radius;
		}

		/**
		 * Sets the mass (given in masses of the planet Earth) for the to-be-built
		 * PhysicalCharacteristics object.
		 * 
		 * @param mass Mass for the new object (in masses of the planet Earth).
		 * 
		 * @return This same Builder object.
		 */
		public Builder mass(final double mass) {
			this.mass = mass;
			return this;
		}

		/**
		 * Sets the rotationPeriod for the to-be-built PhysicalCharacteristics object.
		 * 
		 * @param rotationPeriod Mean rotationPeriod for the new object (in days).
		 */
		public Builder rotationPeriod(final double rotationPeriod) {
			this.rotationPeriod = rotationPeriod;
			return this;
		}

		/**
		 * Sets the axialTilt for the to-be-built PhysicalCharacteristics object.
		 * 
		 * @param axialTilt AxialTilt for the new object (in degree).
		 */
		public Builder axialTilt(final double axialTilt) {
			this.axialTilt = axialTilt;
			return this;
		}

		/**
		 * Creates a new PhysicalCharacteristics object with the data set to this
		 * Builder object.
		 * 
		 * @return A new PhysicalCharacteristics object.
		 */
		public PhysicalCharacteristics build() {
			return new PhysicalCharacteristics(this);
		}
	}

	/**
	 * Initializes a new instance of the PhysicalCharacteristics class. Sets all the
	 * fields using the data set to the Builder object given as a parameter.
	 * 
	 * @param builder Builder object from which data will be used.
	 */
	private PhysicalCharacteristics(Builder builder) {
		this.radius = builder.radius;
		this.mass = builder.mass;
		this.rotationPeriod = builder.rotationPeriod;
		this.axialTilt = builder.axialTilt;
	}
}