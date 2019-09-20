package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import javafx.scene.image.Image;

/**
 * ResourceLoader provides the basic functionality to load and save property data
 * and to load resource objects like images
 * 
 * Implementation is as Singleton pattern
 * 
 * @author RKastner
 *
 */
public class ResourceLoader {
	private final static ResourceLoader instance = new ResourceLoader();

	private Properties properties = new Properties();
	private OutputStream out = null;
	private FileInputStream in = null;


	private ResourceLoader() {
		load();
	}

	public synchronized static ResourceLoader getInstance() {
		return instance;
	}
	
	public static String getResource(Object o, String name) {
		URL url = o.getClass().getResource(name);
		System.out.println("URL:" + url);
		return url.toExternalForm();
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key, "DEFAULT");
	}
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}

	/**
	 * Loads an Image file from file system
	 * 
	 * May return null if image was not found
	 * @param resource
	 * @return
	 */
	public Image loadImage(String resource) {
		Image img = null;
    	InputStream in = null;

    	try {
			in = getResourceAsStream(resource);
		} catch (FileNotFoundException e1) {
			// not serious as we expect some files not to be found *,jpg or *.png
			// we throw this exception - no need to handle it here
		}

    	if (in == null) return img;
        
    	try {
        	img = new Image(in);
        } catch (IllegalArgumentException|NullPointerException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return img;
	}

	/**
	 * loads resources depending on the given resource String
	 * First the ClassLoader is tried without any leading '/'
	 * next the Class itself tries to load from absolute path
	 * 
	 * @throws FileNotFoundException if the resource is not found
	 */
	private InputStream getResourceAsStream(String resource) throws FileNotFoundException {
	    String stripped = resource.startsWith("/") ? resource.substring(1) : resource;
	    InputStream stream = null;
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    if (classLoader != null) {
	        stream = classLoader.getResourceAsStream(stripped);
	    }
	    if (stream == null) {
	        stream = getClass().getResourceAsStream(resource);
	    }
	    if (stream == null) {
            System.out.println("Resource: " + resource);
	        throw new FileNotFoundException("File not found: " + resource);
	    }
	    return stream;
	}

	/**
	 * Load application properties from file system (not the .jar file)
	 * 
	 * If no properties file exist yet one will be generated once the application exits.
	 */
	private void load() {
		try {
	    	String resourceName = "config.properties";
	    	in = new FileInputStream(resourceName);
			properties.load(in);
		} catch(IOException io) {
			io.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Save application user data like window size and position
	 * 
	 * saved to file system file "config.properties"  in the root directory of the application
	 */
	public void save() {
		try {
			out = new FileOutputStream("config.properties");
			properties.store(out, null);
		} catch(IOException io) {
			io.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
