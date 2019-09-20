package application;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.scene.shape.MeshView;

public class MeshShape {

	static MeshView[] loadMeshViews(String filename) {
		ObjModelImporter importer = new ObjModelImporter();
		MeshView[] meshViews = null;
		try {
			importer.read(filename);
			meshViews = importer.getImport();
		} catch (Exception e){
			e.printStackTrace();
//			meshViews = new MeshView[1];
		}
		return meshViews;
	}
}
