package util;

import javafx.print.Printer;
import javafx.scene.Node;

public interface Printable {
	public void setPrinter(Printer p);
	public boolean print(Node node);
}
