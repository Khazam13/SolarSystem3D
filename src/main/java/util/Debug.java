package util;


/**
 * Simple helper class for debug output in readable format
 * 
 * @author RKastner
 */
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public class Debug {
	public static void out(String caller, String info, Bounds b, boolean size) {
		if (!size) {
			out(caller, info, b);
			return;
		}

		Point3D center = CoordinateHelper.centerOfBounds(b);
		System.out.println(caller + "" + info +
				" CenterX: " + String.format("%.2f", center.getX()) +
				" Width: " + String.format("%.2f", b.getWidth()) + 
				" CenterY: " + String.format("%.2f", center.getY()) +
				" Height: " + String.format("%.2f", b.getHeight()) + 
				" CenterZ: " + String.format("%.2f", center.getZ()) +
				" Depth: " + String.format("%.2f", b.getDepth()) + 
				"");

	}
	public static void out(String caller, String info, Bounds b) {
		System.out.println(caller + "" + info +
				" MinX: " + String.format("%.2f", b.getMinX()) +
				" MaxX: " + String.format("%.2f", b.getMaxX()) + 
				" MinY: " + String.format("%.2f", b.getMinY()) +
				" MaxY: " + String.format("%.2f", b.getMaxY()) + 
				" MinZ: " + String.format("%.2f", b.getMinZ()) +
				" MaxZ: " + String.format("%.2f", b.getMaxZ()) + 
				"");

	}
	public static void out(String caller, String info, Point3D p) {
		System.out.println(caller + "" + info +
				" X: " + String.format("%.2f", p.getX()) +
				" Y: " + String.format("%.2f", p.getY()) + 
				" Z: " + String.format("%.2f", p.getZ()) +
				"");
	}
	public static void out(String caller, String info, Point2D p) {
		System.out.println(caller + "" + info +
				" X: " + String.format("%.2f", p.getX()) +
				" Y: " + String.format("%.2f", p.getY()) +
				"");
	}
	public static void out(String caller, String info, double value) {
		System.out.println(caller + "" + info +
				"" + String.format("%.2f", value) +
				"");
	}

	public static void err(String caller, String info, Bounds b) {
		System.err.println(caller + "" + info +
				" MinX: " + String.format("%.2f", b.getMinX()) +
				" MaxX: " + String.format("%.2f", b.getMaxX()) + 
				" MinY: " + String.format("%.2f", b.getMinY()) +
				" MaxY: " + String.format("%.2f", b.getMaxY()) + 
				" MinZ: " + String.format("%.2f", b.getMinZ()) +
				" MaxZ: " + String.format("%.2f", b.getMaxZ()) + 
				"");

	}
	public static void err(String caller, String info, Point3D p) {
		System.err.println(caller + "" + info +
				" X: " + String.format("%.2f", p.getX()) +
				" Y: " + String.format("%.2f", p.getY()) + 
				" Z: " + String.format("%.2f", p.getZ()) +
				"");
	}
	public static void err(String caller, String info, Point2D p) {
		System.err.println(caller + "" + info +
				" X:    " + String.format("%.2f", p.getX()) +
				"              Y:    " + String.format("%.2f", p.getY()) +
				"");
	}
	public static void err(String caller, String info, double value) {
		System.err.println(caller + "" + info +
				"" + String.format("%.2f", value) +
				"");
	}
}
