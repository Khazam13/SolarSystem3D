package datamodel;

import java.util.List;
import java.util.Map;

import util.SmartSphere;

/**
 * This interface represents a Star System data model. It lists methods
 * expected in such a model to ensure ease of data exchange.
 */
public interface StarSystemModel {
    Star getStar();

    List<Planet> getPlanets();

    List<AstronomicalObject> getSatellites();
    
    List<AstronomicalObject> getAstronomicalObjects();

    AstronomicalObject getObject(String name);

    Map<String, String> getProperties(String name);

    String[] getObjectNames();
    
    List<SmartSphere<AstronomicalObject>> getVisualDelegates();
}