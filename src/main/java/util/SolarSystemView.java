package util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.Node;
import javafx.scene.Parent;

public class SolarSystemView implements StarSystemView {
    /**
     * List of all graphical representations for each astronomical object.
     * Each entry is a <Pane> with sub pane and eventually a <Shape3D> object
     */
    private Map<String, Parent> objects = new LinkedHashMap<>();

	@Override
	public Parent getObject(String name) {
		if (name == null || name.isEmpty()) {
			throw new NullPointerException("Name may not be null or empty.");
		}
		return objects.get(name);
	}

	@Override
	public Map<String, Parent> getElements() {
		Map<String, Parent> elements = new LinkedHashMap<>();
		elements.putAll(objects);
		return elements;
	}

	@Override
	public String[] getNames() {
    	String[] names = new String[objects.size()];
    	int i=0;
        for (String key : objects.keySet()) {
        	names[i++] = key;
        }
        return names;
	}

	@Override
	public String getName(Node node) {
		if (node == null) {
			throw new NullPointerException("Null values are not allowed.");
		}
		String name = null;
		for(Entry<String, Parent> e : objects.entrySet()) {
			Node p = e.getValue();
			if (e.getValue().equals(node)) {
				name = e.getKey();
				break;
			}
			
			while (p instanceof Parent) {
				p = ((Parent)p).getChildrenUnmodifiable().get(0);
				if (p.equals(node)) {
					name = e.getKey();
					break;
				}
			}
		}
		return name;
	}

	@Override
	public void put(String key, Parent element) {
		if (key == null || key.isEmpty()) {
			throw new NullPointerException("Key may not be null or empty.");
		}
		if (element == null) {
			throw new NullPointerException("View element may not be null.");
		}
		if (objects.containsKey(key)) {
			System.out.println("Duplicate entry for " + key + ". List already has element: " + objects.get(key));
			return;
		}
		if (objects.containsValue(element)) {
			String duplicateKey = null;
			for(Entry<String, Parent> e : objects.entrySet() ) {
				if (e.getValue().equals(element)) {
					duplicateKey = e.getKey();
					break;
				}
			}
			System.out.println("Duplicate entry for " + element + ". List already has element with key: " + duplicateKey);
			return;
		}
		objects.put(key, element);
	}

}
