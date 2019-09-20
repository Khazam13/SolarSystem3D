package util;

import javafx.application.Application;
import javafx.stage.Stage;

public class SmartController<T1 extends Application, T2 extends Stage> {
	private T1 application = null;
	private T2 stage = null;

	public SmartController() {
	}

	public T1 getApplication() {
		return application;
	}

	public void setApplication(T1 application) {
		this.application = application;
	}

	public T2 getStage() {
		return stage;
	}

	public void setStage(T2 stage) {
		this.stage = stage;
	}

}
