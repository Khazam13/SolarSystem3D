package datamodel;

import java.util.LinkedHashMap;
import java.util.Map;

public class OrbitalCharacteristics {
	/**
	 * The astronomical object that the orbiting object belongs to (orbits).
	 */
	private AstronomicalObject centralObject;

	/**
	 * The maximal distance between the orbiting object and the central
	 * astronomical object that it orbits. Given in astronomical units (AU). Also
	 * known as elliptical major radius, apoapsis, apocentre, or apapsis.
	 */
	private double aphelion;

	/**
	 * The minimal distance between the orbiting object and the central
	 * astronomical object that it orbits. Given in astronomical units (AU). Also
	 * known as elliptical minor radius, periapsis, or pericentre.
	 */
	private double perihelion;

	/**
	 * The semi major axis of the ellipse with the central object at the F1 focus point.
	 * Given in astronomical units (AU).
	 */
	private double semiMajorAxis;

	/**
	 * The time needed for the orbiting object to make one complete orbit about the
	 * central astronomical object that it orbits. Given in days.
	 */
	private double orbitalPeriod;

	/**
	 * The average orbital speed of the astronomical object. Given in km/s.
	 */
	private double averageOrbitalSpeed;

	/**
	 * The inclination against the solar equator. Given in degree.
	 */
	private double inclination;

	/**
	 * The longitudeOfAscendingNode which defines the layout of the orbit of the astronomical object. Given in degree.
	 */
	private double longitudeOfAscendingNode;

	/**
	 * The argumentOfPerihelion which defines the layout of the orbit of the astronomical object. Given in degree.
	 */
	private double argumentOfPerihelion;

	/**
	 * The epoch which defines the time.
	 */
	private String epoch;

	
	public Map<String, String> getProperties() {
		Map<String, String> properties = new LinkedHashMap<>();
		properties.put("centralObject", centralObject.getName());
		properties.put("aphelion", String.valueOf(aphelion));
		properties.put("perihelion", String.valueOf(perihelion));
		properties.put("semiMajorAxis", String.valueOf(semiMajorAxis));
		properties.put("orbitalPeriod", String.valueOf(orbitalPeriod));
		properties.put("averageOrbitalSpeed", String.valueOf(averageOrbitalSpeed));
		properties.put("inclination", String.valueOf(inclination));
		properties.put("longitudeOfAscendingNode", String.valueOf(longitudeOfAscendingNode));
		properties.put("argumentOfPerihelion", String.valueOf(argumentOfPerihelion));
		properties.put("epoch", epoch);
		return properties;
	}

	public AstronomicalObject getCentralObject() {
		return centralObject;
	}

	public double getAphelion() {
		return aphelion;
	}

	public double getPerihelion() {
		return perihelion;
	}

	/**
	 * @return the semiMajorAxis
	 */
	public double getSemiMajorAxis() {
		return semiMajorAxis;
	}

	public double getOrbitalPeriod() {
		return orbitalPeriod;
	}

	/**
	 * @return the averageOrbitalSpeed
	 */
	public double getAverageOrbitalSpeed() {
		return averageOrbitalSpeed;
	}

	/**
	 * @return the inclination
	 */
	public double getInclination() {
		return inclination;
	}

	/**
	 * @return the longitudeOfAscendingNode
	 */
	public double getLongitudeOfAscendingNode() {
		return longitudeOfAscendingNode;
	}

	/**
	 * @return the argumentOfPerihelion
	 */
	public double getArgumentOfPerihelion() {
		return argumentOfPerihelion;
	}

	/**
	 * @return the epoch
	 */
	public String getEpoch() {
		return epoch;
	}

	public static class Builder {
		private AstronomicalObject centralObject;
		private double aphelion;
		private double perihelion;
		private double semiMajorAxis;
		private double orbitalPeriod;
		private double averageOrbitalSpeed;
		private double inclination;
		private double longitudeOfAscendingNode;
		private double argumentOfPerihelion;
		private String epoch;

		/**
		 * Initializes a new instance of the Builder class. Sets the mandatory bounding
		 * object for the to-be-built OrbitalCharacteristics object.
		 * 
		 * @param centralObject Central object (the one that is orbited).
		 */
		public Builder(AstronomicalObject centralObject) {
			this.centralObject = centralObject;
		}

		/**
		 * Sets the maximal distance between the orbiting object and the bounding
		 * astronomical object that it orbits (given in astronomical units).
		 * 
		 * @param aphelion Maximal distance between the orbiting object and the bounding
		 *            astronomical object that it orbits.
		 */
		public Builder aphelion(double aphelion) {
			this.aphelion = aphelion;
			return this;
		}

		/**
		 * Sets the minimal distance between the orbiting object and the bounding
		 * astronomical object that it orbits (given in astronomical units).
		 * 
		 * @param perihelion Minimal distance between the orbiting object and the bounding
		 *            astronomical object that it orbits.
		 */
		public Builder perihelion(double perihelion) {
			this.perihelion = perihelion;
			return this;
		}

		/**
		 * Sets the semiMajorAxis of the orbit (given in astronomical units).
		 * 
		 * @param semiMajorAxis semiMajorAxis of the orbit.
		 */
		public Builder semiMajorAxis(double semiMajorAxis) {
			this.semiMajorAxis = semiMajorAxis;
			return this;
		}

		/**
		 * Sets the time needed for the orbiting object to make one complete orbit about
		 * the bounding astronomical object that it orbits (given in Julian years).
		 * 
		 * @param orbitalPeriod Time needed for the orbiting object to make one complete orbit
		 *            about the bounding astronomical object that it orbits.
		 */
		public Builder orbitalPeriod(double orbitalPeriod) {
			this.orbitalPeriod = orbitalPeriod;
			return this;
		}

		/**
		 * Sets the averageOrbitalSpeed of the orbiting object (given in km/s).
		 * 
		 * @param averageOrbitalSpeed averageOrbitalSpeedof the orbiting object.
		 */
		public Builder averageOrbitalSpeed(double averageOrbitalSpeed) {
			this.averageOrbitalSpeed = averageOrbitalSpeed;
			return this;
		}

		/**
		 * Sets the inclination of the orbiting object (given in degree).
		 * 
		 * @param inclination inclination of the orbit.
		 */
		public Builder inclination(double inclination) {
			this.inclination = inclination;
			return this;
		}

		/**
		 * Sets the longitudeOfAscendingNode of the orbiting object (given in degree).
		 * 
		 * @param longitudeOfAscendingNode longitudeOfAscendingNode of the orbit.
		 */
		public Builder longitudeOfAscendingNode(double longitudeOfAscendingNode) {
			this.longitudeOfAscendingNode = longitudeOfAscendingNode;
			return this;
		}

		/**
		 * Sets the argumentOfPerihelion of the orbiting object (given in degree).
		 * 
		 * @param argumentOfPerihelion argumentOfPerihelion of the orbit.
		 */
		public Builder argumentOfPerihelion(double argumentOfPerihelion) {
			this.argumentOfPerihelion = argumentOfPerihelion;
			return this;
		}

		/**
		 * Sets the epoch of the orbiting object (given in degree).
		 * 
		 * @param epoch epoch of the orbit.
		 */
		public Builder epoch(String epoch) {
			this.epoch = epoch;
			return this;
		}

		/**
		 * Creates a new PhysicalCharacteristics object with the data set to this
		 * Builder object.
		 * 
		 * @return A new PhysicalCharacteristics object.
		 */
		public OrbitalCharacteristics build() {
			return new OrbitalCharacteristics(this);
		}
	}

	/**
	 * Initializes a new instance of the OrbitalCharacteristics class. Sets all the
	 * fields using the data set to the Builder object given as a parameter.
	 * 
	 * @param builder Builder object from which data will be used.
	 */
	private OrbitalCharacteristics(Builder builder) {
		this.centralObject = builder.centralObject;
		this.aphelion = builder.aphelion;
		this.perihelion = builder.perihelion;
		this.semiMajorAxis = builder.semiMajorAxis;
		this.orbitalPeriod = builder.orbitalPeriod;
		this.averageOrbitalSpeed = builder.averageOrbitalSpeed;
		this.inclination = builder.inclination;
		this.longitudeOfAscendingNode = builder.longitudeOfAscendingNode;
		this.argumentOfPerihelion = builder.argumentOfPerihelion;
		this.epoch = builder.epoch;
	}
}