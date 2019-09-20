package application;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import util.ResourceLoader;
import util.SmartApplication;
import util.SmartController;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class TestSuite extends SmartApplication {
	ResourceLoader rl = ResourceLoader.getInstance();

	
	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage stage) {
		try {
			URL resource = getClass().getResource("TestSuite.fxml");
			FXMLLoader loader = new FXMLLoader(resource);
			System.out.println("Resource location: " + resource);
			loader.setLocation(resource);
			Parent root = loader.load();
			SmartController<SmartApplication, Stage> controller = (SmartController<SmartApplication, Stage>)loader.getController();
			controller.setApplication(this);
			controller.setStage(stage);
			
			Scene scene = new Scene(root,400,400);
			stage.setScene(scene);
			stage.setTitle("TestSuite");
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		try {
			super.stop();
			rl.save();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
