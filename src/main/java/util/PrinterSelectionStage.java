package util;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.print.Printer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class PrinterSelectionStage<T extends Application> extends SmartStage<T> {
	private Printer selectedPrinter = null;
	private ObservableSet<Printer> listPrinters = Printer.getAllPrinters();
	
	public PrinterSelectionStage(Stage owner) {
		this(owner, new Group());
	}

	public PrinterSelectionStage(Stage owner, Parent root) {
		initStyle(StageStyle.DECORATED);
		initModality(Modality.NONE);
		initOwner(owner);

		setTitle("Printer Selection");
	}

	public void init() {
		ListView<String> printerListView = new ListView<String>();
		ObservableList<String> listPrinterNames = FXCollections.observableArrayList();

		for (Printer printer : listPrinters) {
			listPrinterNames.add(printer.getName());
		}
		printerListView.setItems(listPrinterNames);

		// Get the Default Printer
		Printer defaultprinter = Printer.getDefaultPrinter();
		if (defaultprinter != null) {
			String name = defaultprinter.getName();
			printerListView.getSelectionModel().select(name);
			selectedPrinter = defaultprinter;
		}

		printerListView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				if (new_val.intValue() < 0) return;
				for (Printer printer : listPrinters) {
					if (printer.getName().equalsIgnoreCase(printerListView.getSelectionModel().getSelectedItem().trim())) {
						selectedPrinter = printer;
						break;
					}
				}
			}
		});

		printerListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		        if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && event.getTarget() instanceof Text
		        		/* &&
		           (event.getTarget() instanceof LabeledText || ((GridPane) event.getTarget()).getChildren().size() > 0) */
		           ) {
		        	System.out.println("EventTarget: " + event.getTarget().getClass());

		        	for (Printer printer : listPrinters) {
						if (printer.getName().equalsIgnoreCase(printerListView.getSelectionModel().getSelectedItem().trim())) {
							selectedPrinter = printer;
							WindowEvent we = new WindowEvent(PrinterSelectionStage.this, WindowEvent.WINDOW_CLOSE_REQUEST);
							fireEvent(we);
							break;
						}
					}
		         }    
		    }
		});
		
		// Create the VBox with a 10px spacing
		VBox root = new VBox(10);
		// Add the Children to the VBox
		root.getChildren().addAll(printerListView);
		// Set the Size of the VBox
		root.setPrefSize(400, 250);

		Scene s = new Scene(new Group());
		setScene(s);
		
		// Create the Scene
		SmartScene scene = new SmartScene(root, this) {
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
				// TODO Auto-generated method stub
				
			}
		};
		Group r = (Group) s.getRoot();
		r.getChildren().add(scene);
		// Add the scene to the Stage

		//no need for the 3D eventhandler
//		initEventHandler(scene);
	}
	
	public void hide() {
		((SmartApplication) getApplication()).setPrinter(selectedPrinter);
    	System.out.println("selectedPrinter: " + selectedPrinter);
		super.hide();
	}

	public void close() {
		((SmartApplication) getApplication()).setPrinter(selectedPrinter);
    	System.out.println("selectedPrinter: " + selectedPrinter);
		super.close();
	}

	@Override
	public void initEventHandler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void createSubScene() {
		// TODO Auto-generated method stub
		
	}
}
