package util;

import javafx.application.Application;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Extends Application for some extended abilities.
 * 
 * @author RKastner
 *
 */
public abstract class SmartApplication extends Application implements Printable {
	private Node root = null;
	private Printer selectedPrinter = null;

	@Override
	public void start(Stage primaryStage) throws Exception {

	}

	public Printer getSelectedPrinter() {
		return selectedPrinter;
	}

	public void setSelectedPrinter(Printer selectedPrinter) {
		this.selectedPrinter = selectedPrinter;
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public boolean print(Node node) {
		boolean success = false;
		// Create the JobStatus Label
		Label jobStatus = (Label) getRoot().getScene().lookup("#labelPrintJobStatus");
		// Define the Job Status Message
		jobStatus.textProperty().unbind();
		jobStatus.setText("Creating a printer job...");
		// Create a printer job for the default printer
		PrinterJob job = PrinterJob.createPrinterJob(selectedPrinter);

		if (job != null) {
			// Show the printer job status
			jobStatus.textProperty().bind(job.jobStatusProperty().asString());
			// Print the node
			boolean printed = job.printPage(node);
			if (printed) {
				// End the printer job
				job.endJob();
				success = true;
			} else {
				// Write Error Message
				jobStatus.textProperty().unbind();
				jobStatus.setText("Printing failed.");
			}
		} else {
			// Write Error Message
			jobStatus.setText("Could not create a printer job.");
		}
		return success;
	}

	public void setPrinter(Printer p) {
		setSelectedPrinter(p);
	}
}
