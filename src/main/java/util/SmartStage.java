package util;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class SmartStage<T extends Application> extends Stage {
	protected SmartScene subScene = null;

	/**
	 * @return the subScene
	 */
	public SmartScene getSubScene() {
		return subScene;
	}
	private T application = null;
	
	public T getApplication() {
		return application;
	}
	public void setApplication(T application) {
		this.application = application;
	}
	public void init() {
		setScene(new Scene(new Group()));
		createSubScene();
		initEventHandler();
	}
	public abstract void initEventHandler();
	protected abstract void createSubScene();
}
