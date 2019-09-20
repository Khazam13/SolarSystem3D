package application;

import javafx.scene.Group;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.SmartApplication;
import util.SmartStage;

public class Stage3D<T extends SmartApplication> extends SmartStage<T> {

	public Stage3D(Stage owner) {
		initStyle(StageStyle.DECORATED);
		initModality(Modality.NONE);
		initOwner(owner);
		setTitle("Sol Scene");
	}

	protected void createSubScene() {
		subScene = new StarSystemScene(new Group(), this);
		Group root = (Group) getScene().getRoot();
		root.getChildren().add(subScene);
		subScene.init();
	}

	@Override
	public void initEventHandler() {
		subScene.initEventHandler();
	}

	@Override
	public void init() {
		super.init();
	}
}
