package application;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.SmartApplication;
import util.SmartScene;
import util.SmartSphere;
import util.SmartStage;

public class obj3DModelStage<T extends SmartApplication> extends SmartStage<T> {
	private static final String OBJ_FILENAME = "src/res/Cassini/Cassini_66.obj";


	static MeshView[] loadMeshViews() {
//		File file = new File(OBJ_FILENAME);
//		System.out.println(file.getAbsolutePath());

		ObjModelImporter importer = new ObjModelImporter();
		importer.read(OBJ_FILENAME);
		return importer.getImport();

//		StlMeshImporter importer = new StlMeshImporter();
//		importer.read(OBJ_FILENAME);
//		Mesh mesh = importer.getImport();
//	 	return new MeshView[] { new MeshView(mesh) };
	}

	private Group buildScene() {
		MeshView[] meshViews = loadMeshViews();

		Color ambientColor = Color.rgb(80, 80, 80, 0);
		AmbientLight ambient = new AmbientLight(ambientColor);

		Group root = new Group(meshViews);
		root.getChildren().add(ambient);

		return root;
	}

	private PerspectiveCamera addCamera(SubScene scene) {
		PerspectiveCamera camera = new PerspectiveCamera(true);
		camera.translateZProperty().set(-100);
		camera.setNearClip(1);
		camera.setFarClip(1000000);
		scene.setCamera(camera);
		return camera;
	}

	public void init() {
		Scene s = new Scene(new Group());
		setScene(s);
		createSubScene();
	}

	protected void createSubScene() {
		SmartScene subScene = new SmartScene(buildScene(), this) {
			@Override
			protected void removeChaseCam() {
				Sphere pivot = getPivotSphere();
				if (pivot == null) return;
//				((SmartPane)pivot.getParent()).removeCamera();
			}


			protected void setChaseCam(SmartSphere<?> sphere) {}
			protected Shape3D traverseNodes(Node node, Point2D point) { return null; }
			@Override
			public void init() {
			    initCamera();
			}
		};
		Group root = (Group) getScene().getRoot();
		root.getChildren().add(subScene);

		subScene.setFill(Color.rgb(10, 10, 40));
		addCamera(subScene);
		setTitle("3D Model View");
	}

	public obj3DModelStage(Stage owner) {
		initStyle(StageStyle.DECORATED);
		initModality(Modality.NONE);
		initOwner(owner);
	}

	@Override
	public void initEventHandler() {
	}
}