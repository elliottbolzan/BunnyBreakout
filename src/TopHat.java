import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TopHat {
	
	public final int OFFSET = 10;
	public final int HAT_DEPTH = 30;
	public final int BRIM_WIDTH = 25;
	public final int BRIM_HEIGHT = 3;

	private Color color = Color.ANTIQUEWHITE;
	
	private Rectangle leftBrim;
	private Rectangle core;
	private Rectangle rightBrim;
	
	public ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();

	public TopHat() {
        leftBrim = new Rectangle(BRIM_WIDTH, BRIM_HEIGHT);
        core = new Rectangle(HAT_DEPTH, HAT_DEPTH);
        rightBrim = new Rectangle(BRIM_WIDTH, BRIM_HEIGHT);
        rectangles.addAll(Arrays.asList(leftBrim, core, rightBrim));
        for (Rectangle rect: rectangles) {
        	rect.setFill(color);
        	Controller.myLevel.getChildren().add(rect);
        }
        center();
	}
	
	public double getX() {
		return leftBrim.getX();
	}
	
	public double getY() {
		return leftBrim.getY();
	}
	
	public double getWidth() {
		return HAT_DEPTH + 2 * BRIM_WIDTH;
	}
	
	public double getHeight() {
		return HAT_DEPTH;
	}

	public void center() {
    	core.setX((Controller.WIDTH - core.getBoundsInParent().getWidth()) / 2);
    	core.setY(Controller.HEIGHT - core.getHeight() - OFFSET);
    	leftBrim.setX(core.getX() - leftBrim.getWidth());
    	leftBrim.setY(core.getY());
    	rightBrim.setX(core.getX() + core.getWidth());
    	rightBrim.setY(core.getY());
	}
	
	public void setX(double d) {
    	leftBrim.setX(d);
    	core.setX(d + BRIM_WIDTH);
    	rightBrim.setX(d + BRIM_WIDTH + HAT_DEPTH);
	}
	
	public void setY(double value) {
    	leftBrim.setY(value);
    	core.setY(value);
    	rightBrim.setY(value);
	}
	
	public void extend() {
		leftBrim.setWidth((Controller.WIDTH - HAT_DEPTH) / 2);
		rightBrim.setWidth((Controller.WIDTH - HAT_DEPTH) / 2);
		center();
	}
	
	public Boolean brimIntersected(ImageView view) {
		if (view.getBoundsInParent().intersects(leftBrim.getBoundsInParent())
				|| view.getBoundsInParent().intersects(rightBrim.getBoundsInParent())) {
			return true;
		}
		return false;
	}
	
	public Boolean coreIntersected(ImageView view) {
		if (view.getBoundsInParent().intersects(core.getBoundsInParent())) {
			return true;
		}
		return false;
	}
}
