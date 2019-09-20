package datamodel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import application.TestSuite;
import javafx.scene.shape.Shape3D;
import util.SmartSphere;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class represents a Solar System data model.
 */
public class SolarSystemModel implements StarSystemModel {
	private static final String DEFAULT_RESOURCE_LOCATION = "/datamodel/SolarSystemData.json";
	private static final String TYPE_STAR = "Star";
	private static final String TYPE_PLANET = "Planet";
	private static final String TYPE_RING = "Ring";
	private static final String TYPE_SATELLITE = "Satellite";

	private Map<String, AstronomicalObject> objects = new HashMap<>();

    /**
     * Initializes a new instance of the SolarSystemModel class. Reads data from the JSON
     * document and creates models.
     */
    public SolarSystemModel() {
    	this(DEFAULT_RESOURCE_LOCATION);
    }

    /**
     * Initializes a new instance of the SolarSystemModel class. Reads data from the JSON
     * document and creates models.
     * The order of the object creation is relevant as we need the central object before
     * we create the orbiting objects.
     */
    public SolarSystemModel(String resource) {
    	if (resource == null || resource.isEmpty()) {
    		resource = DEFAULT_RESOURCE_LOCATION;
    	}
        JSONParser parser = new JSONParser();
        try {
        	InputStream in = TestSuite.class.getResourceAsStream(resource);
        	InputStreamReader inReader = new InputStreamReader(in);
            Object obj = parser.parse(inReader);
            JSONObject solarSystemData = (JSONObject) obj;

            JSONObject sunData = (JSONObject) solarSystemData.get(TYPE_STAR);
            createStar(sunData);
            JSONArray planetsData = (JSONArray) solarSystemData.get(TYPE_PLANET);
            createPlanets(planetsData);
            JSONArray satellitesData = (JSONArray) solarSystemData.get(TYPE_SATELLITE);
            createAstronomicalObjects(satellitesData, TYPE_SATELLITE);
            JSONArray ringsData = (JSONArray) solarSystemData.get(TYPE_RING);
            createAstronomicalObjects(ringsData, TYPE_RING);
            
            createOrbits();
            buildHierarchy();
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void buildHierarchy() {
        for (AstronomicalObject object : getAstronomicalObjects()) {
            object.buildHierarchy();
        }
    }
    
    //create the orbits in order from satellites to planets as the planetary orbits need the size of the satellite orbits
    private void createOrbits() {
    	for (AstronomicalObject object : getSatellites()) {
            object.createOrbitalDelegate();
    	}
        for (AstronomicalObject object : getPlanets()) {
            object.createOrbitalDelegate();
        }
	}

	/**
     * Creates the Solar System star (the Sun) model. Data is being read from the JSON object given
     * as a parameter.
     * @param sunData Data for the Sun model, in a JSON object.
     */
    private void createStar(JSONObject sunData) {
        PhysicalCharacteristics physicalCharacteristics = readPhysicalCharacteristics(sunData);
        Star sun = new Star((String)sunData.get("name"), physicalCharacteristics, null);
        objects.put(sun.getName(), sun);
    }

    /**
     * Creates the Solar System satellites. Data is being read from the JSON array given as a
     * parameter.
     * @param data Data for the satellites models, in a JSON array.
     */
    private void createAstronomicalObjects(JSONArray data, String type) {
        for (Object object : data) {
            JSONObject satelliteData = (JSONObject) object;
            createAstronomicalObject(satelliteData, type);
        }
    }

    /**
     * Creates a Solar System planet. Data is being read from the JSON object given as a parameter.
     * @param planetData Data for the planet model, in a JSON object.
     */
    private void createAstronomicalObject(JSONObject data, String type) {
        PhysicalCharacteristics physicalCharacteristics = readPhysicalCharacteristics(data);
        OrbitalCharacteristics orbitalCharacteristics = readOrbitCharacteristics((JSONObject)data.get("orbit"));
        AstronomicalObject object = new AstronomicalObject((String)data.get("name"), type, physicalCharacteristics, orbitalCharacteristics);
        objects.put(object.getName(), object);
        AstronomicalObject parent = objects.get(object.getOrbit().getCentralObject().getName());
        parent.addSatellite(object);
    }

    /**
     * Creates the Solar System planets. Data is being read from the JSON array given as a
     * parameter.
     * @param planetsData Data for the planets models, in a JSON array.
     */
    private void createPlanets(JSONArray planetsData) {
        for (Object object : planetsData) {
            JSONObject planetData = (JSONObject) object;
            createPlanet(planetData);
        }
    }

    /**
     * Creates a Solar System planet. Data is being read from the JSON object given as a parameter.
     * @param planetData Data for the planet model, in a JSON object.
     */
    private void createPlanet(JSONObject planetData) {
        PhysicalCharacteristics physicalCharacteristics = readPhysicalCharacteristics(planetData);
        OrbitalCharacteristics orbitalCharacteristics = readOrbitCharacteristics((JSONObject)planetData.get("orbit"));
        Planet planet = new Planet((String)planetData.get("name"), physicalCharacteristics, orbitalCharacteristics);
        objects.put(planet.getName(), planet);
    }

    /**
     * Reads physical characteristics held in a JSON object given as a parameter.
     * @param source Data with physical characteristics, in a JSON object.
     * @return New PhysicalCharacteristics object.
     */
    private PhysicalCharacteristics readPhysicalCharacteristics(JSONObject source) {
        return new PhysicalCharacteristics
                .Builder(((Number)source.get("radius")).doubleValue())
                .mass(((Number) source.get("mass")).doubleValue())
                .rotationPeriod(((Number) source.get("rotationPeriod")).doubleValue())
                .axialTilt(((Number) source.get("axialTilt")).doubleValue())
                .build();
    }

    /**
     * Reads orbital characteristics held in a JSON object given as a parameter.
     * @param source Data with orbital characteristics, in a JSON object.
     * @return New OrbitalCharacteristics object.
     */
    private OrbitalCharacteristics readOrbitCharacteristics(JSONObject source) {
        return new OrbitalCharacteristics
                .Builder(objects.get((source.get("centralObject")).toString()))
                .aphelion(((Number) source.get("aphelion")).doubleValue())
                .perihelion(((Number) source.get("perihelion")).doubleValue())
                .semiMajorAxis(((Number) source.get("semiMajorAxis")).doubleValue())
                .orbitalPeriod(((Number) source.get("orbitalPeriod")).doubleValue())
                .averageOrbitalSpeed(((Number) source.get("averageOrbitalSpeed")).doubleValue())
                .inclination(((Number) source.get("inclination")).doubleValue())
                .longitudeOfAscendingNode(((Number) source.get("longitudeOfAscendingNode")).doubleValue())
                .argumentOfPerihelion(((Number) source.get("argumentOfPerihelion")).doubleValue())
                .epoch((source.get("epoch")).toString())
                .build();

    }

    /**
     * Gets the Solar System star (the Sun) model.
     * @return Sun model.
     */
    @Override
    public Star getStar() {
        for (AstronomicalObject data : getAstronomicalObjects()) {
        	if (data.getType().equals(TYPE_STAR)) return (Star)data;
        }
        return null;
    }

    /**
     * Gets models of all planets.
     * @return Planets models.
     */
    @Override
    public List<Planet> getPlanets() {
        List<Planet> planets = new LinkedList<>();
        for (AstronomicalObject object : objects.values()) {
            if (Planet.class.isAssignableFrom(object.getClass())) {
                planets.add((Planet)object);
            }
        }
        return planets;
    }
    
    /**
     * Gets models of all astronomical objects.
     * @return Planets models.
     */
    @Override
    public List<AstronomicalObject> getAstronomicalObjects() {
        List<AstronomicalObject> astronomicalObjects = new LinkedList<>();
        for (AstronomicalObject object : objects.values()) {
           	astronomicalObjects.add(object);
        }
        return astronomicalObjects;
    }
    
    /**
     * Gets model of an astronomical object of name given as a parameter.
     * @param name Name of the astronomical object.
     * @return Astronomical object model.
     */
    @Override
    public AstronomicalObject getObject(String name) {
        return objects.get(name);
    }

    /**
     * Gets properties' map of an astronomical object of name given as a parameter.
     * @param name Name of the astronomical object.
     * @return Astronomical object model properties' map.
     */
    @Override
    public Map<String, String> getProperties(String name) {
        return getObject(name).getProperties();
    }

    /**
     * Gets the names of Solar System objects as an array of Strings.
     * @return Names of Solar System objects as an array of Strings.
     */
    @Override
    public String[] getObjectNames() {
    	String[] names = new String[objects.size()];
    	int i=0;
        for (AstronomicalObject data : getAstronomicalObjects()) {
        	names[i++] = data.getName();
        }
        return names;
    }

	@Override
	public List<AstronomicalObject> getSatellites() {
        List<AstronomicalObject> satellites = new LinkedList<AstronomicalObject>();
        for (AstronomicalObject object : objects.values()) {
        	if (object.getType() == TYPE_SATELLITE) {
                satellites.add(object);
        	}
        }
        return satellites;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SmartSphere<AstronomicalObject>> getVisualDelegates() {
        List<SmartSphere<AstronomicalObject>> visualDelegates = new LinkedList<>();
        for (AstronomicalObject object : objects.values()) {
        	Shape3D shape = object.getVisualDelegate();
        	if (shape instanceof SmartSphere) visualDelegates.add((SmartSphere<AstronomicalObject>)shape);
        }
        return visualDelegates;
	}
}