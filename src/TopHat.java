import java.util.ArrayList;
import java.util.Arrays;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class TopHat {

	private Rectangle leftBrim;
	private Rectangle core;
	private Rectangle rightBrim;

	private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
	private Boolean extended = false;

	public TopHat() {
		leftBrim = new Rectangle(Settings.BRIM_WIDTH, Settings.BRIM_HEIGHT);
		core = new Rectangle(Settings.HAT_SIZE, Settings.HAT_SIZE);
		rightBrim = new Rectangle(Settings.BRIM_WIDTH, Settings.BRIM_HEIGHT);
		rectangles.addAll(Arrays.asList(leftBrim, core, rightBrim));
		center();
	}

	public double getX() {
		return leftBrim.getX();
	}

	public void setX(double value) {
		leftBrim.setX(value);
		core.setX(value + leftBrim.getWidth());
		rightBrim.setX(value + leftBrim.getWidth() + Settings.HAT_SIZE);
	}

	public double getY() {
		return leftBrim.getY();
	}

	public void setY(double value) {
		leftBrim.setY(value);
		core.setY(value);
		rightBrim.setY(value);
	}

	public double getWidth() {
		return leftBrim.getWidth() + core.getWidth() + rightBrim.getWidth();
	}

	public void setWidth(double value) {
		double newBrimWidth = (value - Settings.HAT_SIZE) / 2;
		leftBrim.setX(leftBrim.getX() - (newBrimWidth - leftBrim.getBoundsInParent().getWidth()));
		leftBrim.setWidth(newBrimWidth);
		rightBrim.setWidth(newBrimWidth);
	}

	public double getHeight() {
		return Settings.HAT_SIZE;
	}

	public ArrayList<Rectangle> getRectangles() {
		return rectangles;
	}

	public void setRectangles(ArrayList<Rectangle> rectangles) {
		this.rectangles = rectangles;
	}

	public Boolean getExtended() {
		return extended;
	}

	public void setExtended(Boolean extended) {
		this.extended = extended;
	}

	public void center() {
		core.setX((Settings.WIDTH - core.getBoundsInParent().getWidth()) / 2);
		core.setY(Settings.HEIGHT - core.getHeight() - Settings.HAT_OFFSET);
		leftBrim.setX(core.getX() - leftBrim.getWidth());
		leftBrim.setY(core.getY());
		rightBrim.setX(core.getX() + core.getWidth());
		rightBrim.setY(core.getY());
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
			Timeline timeline = new Timeline(
					new KeyFrame(Duration.millis(Settings.DOUBLE_TOPHAT_DURATION), ae -> resetSize()));
			timeline.play();
		}
	}

	public void resetSize() {
		extended = false;
		setWidth(Settings.HAT_SIZE + 2 * Settings.BRIM_WIDTH);
	}

	public Boolean brimIntersected(ImageView view) {
		return leftBrim.intersects(view.getBoundsInParent()) || rightBrim.intersects(view.getBoundsInParent());
	}

	public Boolean coreIntersected(ImageView view) {
		return core.intersects(view.getBoundsInParent());
	}

	public Boolean intersects(ImageView view) {
		return brimIntersected(view) || coreIntersected(view);
	}

}
