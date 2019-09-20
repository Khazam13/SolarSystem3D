package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.stage.Stage;
import util.SmartApplication;

public class TestStage3D<T extends SmartApplication> extends Stage3D<T> {

	public TestStage3D(Stage owner) {
		super(owner);
		setTitle("Test Scene");
	}

	@Override
	protected void createSubScene() {
		subScene = new TestSystemScene(new Group(), this, "/datamodel/TestSystemData.json");
		Group root = (Group) getScene().getRoot();
		root.getChildren().add(subScene);
		subScene.init();
	}

	public void init() {
		super.init();
		
		Scene scene = getScene();
		Group root = (Group) scene.getRoot();

		Box box = new Box(200,60,10);
		root.getChildren().add(box);
		box.translateXProperty().set(box.getWidth()/2);
		box.translateYProperty().set(box.getHeight()/2);

		label1 = new Label();
		root.getChildren().add(label1);
		label1.translateXProperty().set(0);
		label1.translateYProperty().set(0);
		label1.setTextFill(Color.BLACK);
		
		label2 = new Label();
		root.getChildren().add(label2);
		label2.translateXProperty().set(0);
		label2.translateYProperty().set(20);
		label2.setTextFill(Color.BLUE);

		label3 = new Label();
		root.getChildren().add(label3);
		label3.translateXProperty().set(0);
		label3.translateYProperty().set(40);
		label3.setTextFill(Color.RED);

//		createXAxis(root);
		subScene.traverseChildren(root, 0);
		
		subScene.setOnMouseDragged(e -> debugOut(e) );
        subScene.setOnMouseClicked(e -> debugOut(e) );

//		Translate pivot = new Translate();
//		root.getChildrenUnmodifiable().stream().filter(node -> (node instanceof Sphere))
//        .forEach(node ->
//                node.setOnMouseClicked(e -> {
//                    pivot.setX(node.getTranslateX());
//                    pivot.setY(node.getTranslateY());
//                    pivot.setZ(node.getTranslateZ());
//
//                    System.out.println(e.getEventType() +
//							" Pivot x: " + String.format("%.0f", pivot.getX()) +
//							" y: " + String.format("%.0f", pivot.getY()) +
//							" z: " + String.format("%.0f", pivot.getZ()));
//                    
//                })
//        );
	}
	
	private Label label1 = null;
	private Label label2 = null;
	private Label label3 = null;
	
	private void debugOut(MouseEvent e) {
		label1.setText("Scene  X " + String.format("%.0f", e.getSceneX()) + 
				" Y " + String.format("%.0f", e.getSceneY()) +
				"");
		label2.setText("Screen X " + String.format("%.0f", e.getScreenX()) +
				" Y " + String.format("%.0f", e.getScreenY()) +
				"");
		label3.setText("CamPos X " + String.format("%.0f", subScene.camPosition().getX()) +
				" Y " + String.format("%.0f", subScene.camPosition().getY()) + 
				" Z " + String.format("%.0f", subScene.camPosition().getZ()) +
				"");
	}
	
//	private Pane createXAxis(Group g) {
//		Pane p = new Pane();
//		
//		g.getChildren().add(p);
//		return p;
//	}
}
