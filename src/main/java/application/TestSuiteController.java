package application;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import datamodel.AstronomicalObject;
import datamodel.PhysicalCharacteristics;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Box;
import javafx.scene.shape.Ellipse;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import util.SmartController;
import util.SmartStage;


public class TestSuiteController extends SmartController<TestSuite, SmartStage<Application>>{
	@FXML
	private ResourceBundle resources;
	@FXML
	private Button modelViewButton;
	@FXML
	private Button closeButton;
	@FXML
	private Button solarSystemButton;
	@FXML
	private Button testSystemButton;
	@FXML
	private Button eventTestButton;
	@FXML
	private Button startStopButton;
	@FXML
	private Button clearCanvasButton;
	@FXML
	private MenuBar menu;
	@FXML
	private Pane pane;
	@FXML
	private Canvas canvas;
	@FXML
	private AmbientLight light;
	@FXML
	private Box box;
	@FXML
	private Slider rotationSpeedSlider;
	@FXML
	private Slider orbitSpeedSlider;
	@FXML
	private Label orbitSpeedCaption;
	@FXML
	private Label orbitSpeedValue;
	
	
	private Scene starScene = null;
	
	public TestSuiteController() {
	}

	@FXML
	private void initialize() {
	}
	
	/**
	 * Takes a screenshot of the 3D scene and saves it to the specified file
	 * @param event
	 */
	@FXML
	private void modelViewAction(ActionEvent event) 
	{
		try {
            ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            fileChooser.getExtensionFilters().add(extFilter);
            //Show save file dialog
            File file = fileChooser.showSaveDialog(getStage());
             
            if(file != null){
                try {
                    WritableImage writableImage = new WritableImage((int) (starScene.getWidth()), (int)(starScene.getHeight()));
                    starScene.snapshot(writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {
                    Logger.getLogger(TestSuiteController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Open the sol-system simulation
	 * @param event
	 */
	@FXML
	public void solarSystemViewAction(ActionEvent event) {
		try {
			Stage owner = getStage();
		    Stage3D<TestSuite> stage = new Stage3D<TestSuite>(owner);
		    stage.setApplication(getApplication());
		    stage.init();
		    AnimationHandler.getInstance().start();
		    starScene = stage.getScene();
		    stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	TestStage3D<TestSuite> testStage;
	/**
	 * Open a test-system simulation
	 * @param event
	 */
	@FXML
	public void testSystemViewAction(ActionEvent event) {
		try {
			Stage owner = getStage();
			TestStage3D<TestSuite> stage = new TestStage3D<TestSuite>(owner);
			stage.setApplication(getApplication());
			stage.init();
		    AnimationHandler.getInstance().start();
		    stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Open a test-window for event capturing
	 * @param event
	 */
	@FXML
	public void eventTestAction(ActionEvent event) {
		try {
			Stage owner = getStage();
			Stage stage = new Stage();
			stage.initOwner(owner);

			Scene scene = new Scene(new Group(), 800, 800);
			scene.setFill(Color.BLACK);
			stage.setScene(scene);
			Group root = (Group) scene.getRoot();
			
			Box box = new Box(200,60,10);
			root.getChildren().add(box);
			box.translateXProperty().set(box.getWidth()/2);
			box.translateYProperty().set(box.getHeight()/2);

			AstronomicalObject a1 = new AstronomicalObject("Back Test", "Star", new PhysicalCharacteristics
	                .Builder(100.0*500)
	                .mass(0.0)
	                .rotationPeriod(0.0)
	                .axialTilt(0.0)
	                .build(), null);
			
			root.getChildren().add(a1.getVisualAstronomicalDelegate());

			a1.getVisualDelegate().translateXProperty().set(400);
			a1.getVisualDelegate().translateYProperty().set(400);

			AstronomicalObject a2 = new AstronomicalObject("Front Test", "Star", new PhysicalCharacteristics
	                .Builder(150.0*500)
	                .mass(0.0)
	                .rotationPeriod(0.0)
	                .axialTilt(0.0)
	                .build(), null);
			
			root.getChildren().add(a2.getVisualAstronomicalDelegate());

			a2.getVisualDelegate().translateXProperty().set(300);
			a2.getVisualDelegate().translateYProperty().set(300);
			a2.getVisualDelegate().translateZProperty().set(200);

			
			Label label1 = new Label();
			root.getChildren().add(label1);
			label1.translateXProperty().set(0);
			label1.translateYProperty().set(0);
			label1.setTextFill(Color.BLACK);
			label1.setText("Test 1");
			
			Label label2 = new Label();
			root.getChildren().add(label2);
			label2.translateXProperty().set(0);
			label2.translateYProperty().set(20);
			label2.setTextFill(Color.BLUE);
			label2.setText("Test 2");

			Label label3 = new Label();
			root.getChildren().add(label3);
			label3.translateXProperty().set(0);
			label3.translateYProperty().set(40);
			label3.setTextFill(Color.RED);
			label3.setText("Test 3");

//			root.setOnMouseClicked((e) -> {
//				System.out.println("(CLICKED) root scene! "+ root);
//			});
//			pane.setOnMouseClicked((e) -> {
//				System.out.println("(CLICKED) pane! "+ pane);
//			});
//			box.setOnMouseClicked((e) -> {
//				System.out.println("(CLICKED) box! "+ box);
//			});
//			label1.setOnMouseClicked((e) -> {
//				System.out.println("(CLICKED) label1! "+ label1);
//			});
//			a.getVisualDelegate().setOnMouseClicked((e) -> {
//				System.out.println("(CLICKED) sphere! "+ a.getVisualDelegate());
//			});
			
			
			
			
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void close() {
//		System.out.println(closeButton.getText());
	}
	@FXML
	private void startStopAction(ActionEvent event) 
	{
		AnimationHandler.getInstance().toggle();
	}
	@FXML
	private void clearCanvas() 
	{
		final GraphicsContext gc = canvas.getGraphicsContext2D();
		// Canvas-Hintergrund als Rechteck löschen
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	@FXML
	public void handleCloseButtonAction(ActionEvent event) {
//	    Stage stage = (Stage) closeButton.getScene().getWindow();
		Stage stage = getStage();
	    stage.close();
	}
	@FXML
	private void drawOnCanvas() {
		final GraphicsContext gc = canvas.getGraphicsContext2D();

		// Canvas-Hintergrund als Rechteck löschen
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

		// Oval zeichnen
		gc.setStroke(Color.DARKGOLDENROD);
		gc.setLineWidth(4);
		gc.strokeOval(10, 20, 40, 60);

		// Abgerundetes Rechteck füllen
		gc.setFill(Color.BLUE);
		gc.fillRoundRect(60, 20, 40, 40, 10, 10);

		// Pfad definieren
		gc.setStroke(Color.FIREBRICK);
		gc.beginPath();
		gc.moveTo(110, 30);
		gc.lineTo(170, 20);
		gc.bezierCurveTo(150, 110, 130, 30, 110, 40);
		gc.closePath();

		// Pfad malen
		gc.stroke();

		// Gefülltes Tortenstück darstellen
		gc.setFill(Color.web("dodgerblue"));
		gc.fillArc(180, 30, 30, 30, 45, 270, ArcType.ROUND);
		
		

		
		Ellipse ellipse = new Ellipse();
        double sMajorA = 200;
        double sMinorA = 150;
        
        ellipse.setStroke(Color.BLACK);
//        ellipse.setFill(Color.AZURE);
        ellipse.setCenterX(50);
        ellipse.setCenterY(100);
        ellipse.setRadiusX(sMajorA);
        ellipse.setRadiusY(sMinorA);

		Stage stage = getStage();
		Scene scene = stage.getScene();
		Parent root = scene.getRoot();

		if (root instanceof Pane) {
			((Pane)root).getChildren().add(ellipse);
		}
		if (root instanceof Group) {
			((Group)root).getChildren().add(ellipse);
		}
	}

	@FXML
	private void toggleLight() 
	{
		System.out.println("toggleLight: "+light.getId()+" "+light.getColor()+" "+light.isLightOn());
		light.setLightOn(!light.isLightOn());
	}

	@FXML
	private void changeLight() 
	{
		System.out.println("changeLight: "+light.getId()+" "+light.getColor()+" "+light.isLightOn());
		Color newColor, oldColor;
		oldColor = light.getColor();
		System.out.println(oldColor.toString() +" Brightness: " + oldColor.getBrightness());
		newColor = oldColor.darker();
		System.out.println(newColor.toString());
		light.setColor(newColor);
	}

}
