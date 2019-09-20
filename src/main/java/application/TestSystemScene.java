package application;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class TestSystemScene extends StarSystemScene {

	public TestSystemScene(Parent root, Stage window, String resource) {
		super(root, window, resource);
	}

//	/**
//	 * Add WindowListener for Resize Event
//	 * Window Width and Height are still not set!
//	 */
//	public void init() {
//		super.init();
//
////		Pane starPane = starSystem.getStar().getVisualAstronomicalDelegate();
////		starPane.getChildren().add(createBackgroundSphere());
////		Group root = (Group)getRoot();
////		root.getChildren().add(new ImageView(prepareBackground()));
////		root.getChildren().addAll(starPane);
////		SmartPane.clipChildren(getCamera(), 0);
////		starPane.getChildren().addAll(cassiniPane);
//	}

	
//	/**
//     * @return the background sphere
//     */
//	protected Shape3D createBackgroundSphere() {
//		double radius = 50000;
//		Shape3D celestial = null;
//		Image img = prepareBackground();
//		PhongMaterial material = new PhongMaterial();
//		material.setDiffuseMap(img);
//		material.setSelfIlluminationMap(img);
//		celestial = new Sphere(radius);
//		celestial.setCullFace(CullFace.NONE);
//		celestial.setMaterial(material);
//		return celestial;
//	}
//	
//
//	protected Image prepareBackground() {
//    	ResourceLoader rl = ResourceLoader.getInstance();
//    	Image img;
//    	String resourceName = "2k_stars_milky_way.jpg";
//       	img = rl.loadImage(resourceName);
//       	if (img == null) {
//       		System.out.println("No jpg resource for Star System object of name " + resourceName + ".");
//       	}
//       	ImageView imageView = new ImageView(img);
//       	imageView.setPreserveRatio(true);
//       	return img;
//	}
//	

	
	public void traverseChildren(Node node, int level) {
//		System.out.println("TestSystemScene.traverseChildren");
		if (node == null) return;
		String s = "";
		for(int i=0; i<level; i++) s += "-";

		//-------------
		// find my Rings!!
		if (node instanceof Cylinder) {
			Cylinder c = (Cylinder)node;
			System.out.println(s + "Cylinder: " + c.getBoundsInLocal().getMinX() + " : " + c.getBoundsInLocal().getMaxX() +
					" : " + c.getBoundsInLocal().getWidth());
			System.out.println(s + "Screen Coords: "+ c.localToScreen(0,0));
		}
		if (node instanceof Sphere) {
			Sphere c = (Sphere)node;
			System.out.println(s + "Sphere: " + c.getBoundsInLocal().getMinX() + " : " + c.getBoundsInLocal().getMaxX() +
					" : " + c.getBoundsInLocal().getWidth());
			System.out.println(s + "Screen Coords: "+ c.localToScreen(0,0));
		}
		//*************
		
		if (node instanceof Shape3D ) {
	        System.out.println(s + "Shape3D: " + node);
			return;
		}
        System.out.println(s + "Node: " + node);

        if (node instanceof SubScene) {
        	node = ((SubScene) node).getRoot();
        }
        if (!(node instanceof Parent)) return;
		for (Node n : ((Parent)node).getChildrenUnmodifiable()) {
			traverseChildren(n, level + 1);
		}
	}

	
	/**
	 * 1. Find the node which should be the new pivot - the node under the mouse
	 * 2. stop the animation
	 * 3. save the animation status (timer)
	 * 4. set the pivot
	 * 5. center the scene on the new pivot
	 * 6. disable the translation for the new pivot node
	 * 7. un-hook the orbit from parent and hook on pivot node
	 * 8. set translation for parent
	 * 9. load animation status
	 * 10. restart animation
	 * 
	 */
	protected void setNewPivot() {
		AnimationHandler.getInstance().stop();
	
		//unhook visual orbit from parent
		String name = "";
		starSystem.getObject(name);
		
		AnimationHandler.getInstance().start();
	}
}
