import java.util.ArrayList;
import java.util.Arrays;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class TopHat {
	
	public final int OFFSET = 10;
	public final int HAT_DEPTH = 30;
	public final int BRIM_WIDTH = 25;
	public final int BRIM_HEIGHT = 3;
	
	private Rectangle leftBrim;
	private Rectangle core;
	private Rectangle rightBrim;
	
	public ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
	public Boolean extended = false;

	public TopHat() {
        leftBrim = new Rectangle(BRIM_WIDTH, BRIM_HEIGHT);
        core = new Rectangle(HAT_DEPTH, HAT_DEPTH);
        rightBrim = new Rectangle(BRIM_WIDTH, BRIM_HEIGHT);
        rectangles.addAll(Arrays.asList(leftBrim, core, rightBrim));
        for (Rectangle rect: rectangles) {
        	rect.setFill(Settings.WHITE);
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
		return leftBrim.getWidth() + core.getWidth() + rightBrim.getWidth();
	}
	
	public double getHeight() {
		return HAT_DEPTH;
	}

	public void center() {
    	core.setX((Settings.WIDTH - core.getBoundsInParent().getWidth()) / 2);
    	core.setY(Settings.HEIGHT - core.getHeight() - OFFSET);
    	leftBrim.setX(core.getX() - leftBrim.getWidth());
    	leftBrim.setY(core.getY());
    	rightBrim.setX(core.getX() + core.getWidth());
    	rightBrim.setY(core.getY());
	}
	
	public void setX(double value) {
    	leftBrim.setX(value);
    	core.setX(value + leftBrim.getWidth());
    	rightBrim.setX(value + leftBrim.getWidth() + HAT_DEPTH);
	}
	
	public void setY(double value) {
    	leftBrim.setY(value);
    	core.setY(value);
    	rightBrim.setY(value);
	}
	
	public void setWidth(double value) {
		double newBrimWidth = (value - HAT_DEPTH) / 2;
		leftBrim.setX(leftBrim.getX() - (newBrimWidth - leftBrim.getBoundsInParent().getWidth()));
		leftBrim.setWidth(newBrimWidth);
		rightBrim.setWidth(newBrimWidth);
	}
	
	public void extend() {
		extended = true;
		setWidth(Settings.WIDTH);
		center();
	}
	
	public void doubleSize() {
		if (!extended) {
			extended = true;
			setWidth(getWidth() * 2);
	        Timeline timeline = new Timeline(new KeyFrame(
	                Duration.millis(7000),
	                ae -> resetSize()));
	        timeline.play();
		}
	}
	
	public void resetSize() {
		extended = false;
		setWidth(HAT_DEPTH + 2 * BRIM_WIDTH);
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
	
	public Boolean intersects(ImageView view) {
		if (brimIntersected(view) || coreIntersected(view)) {
			return true;
		}
		return false;
	}

}
