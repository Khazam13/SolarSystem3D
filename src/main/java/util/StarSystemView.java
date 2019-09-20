package util;

import java.util.Map;

import javafx.scene.Node;
import javafx.scene.Parent;

/**
 * This interface represents a Star System View model.
 * Implementing class will hold the view objects for the star system like spheres for planets and stars or MeshView[]
 * for complex shapes like probes and satellites.
 * We store not the objects directly, we store their enveloping parent object (Pane). 
 * It lists methods expected in such a model to ensure ease of data exchange.
 * 
 */
public interface StarSystemView {
	Parent getObject(String name);

    Map<String, Parent> getElements();

    String[] getNames();

    String getName(Node node);
    
    void put(String key, Parent element);
}
