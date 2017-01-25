/**
 * Elliott Bolzan, January 2017
 */

import java.util.Arrays;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

class HatPart extends Rectangle {
	public HatPart(double width, double height) {
		super(width, height);
		setFill(Settings.WHITE);
	}
}

/**
 * Create a paddle in the shape of a top hat, composed of three rectangles.
 */

public class TopHat {

	private HatPart leftBrim = new HatPart(Settings.BRIM_WIDTH, Settings.BRIM_HEIGHT);
	private HatPart core = new HatPart(Settings.HAT_SIZE, Settings.HAT_SIZE);
	private HatPart rightBrim = new HatPart(Settings.BRIM_WIDTH, Settings.BRIM_HEIGHT);
	private List<HatPart> rectangles = Arrays.asList(leftBrim, core, rightBrim);
	private Boolean extended = false;

	public TopHat() {
		center();
	}

	/**
	 * @return the hat's X-coordinate.
	 */
	public double getX() {
		return leftBrim.getX();
	}
	
	/**
	 * @return the maximum X-coordinate for the hat.
	 */
	public double getMaxX() {
		return getX() + getWidth();
	}

	/**
	 * Setting the hat's X-coordinate involves setting it for three rectangles.
	 */
	public void setX(double value) {
		leftBrim.setX(value);
		core.setX(leftBrim.getBoundsInParent().getMaxX());
		rightBrim.setX(core.getBoundsInParent().getMaxX());
	}

	/**
	 * @return the hat's Y-coordinate.
	 */
	public double getY() {
		return leftBrim.getY();
	}

	/**
	 * Set that hat's Y-coordinate.
	 */
	public void setY(double value) {
		for (Rectangle rect : rectangles) {
			rect.setY(value);
		}
	}

	/**
	 * @return the hat's width.
	 */
	public double getWidth() {
		return leftBrim.getWidth() + core.getWidth() + rightBrim.getWidth();
	}

	/**
	 * Set the hat's width.
	 */
	public void setWidth(double value) {
		double newBrimWidth = (value - Settings.HAT_SIZE) / 2;
		leftBrim.setX(leftBrim.getBoundsInParent().getMaxX() - newBrimWidth);
		leftBrim.setWidth(newBrimWidth);
		rightBrim.setWidth(newBrimWidth);
	}

	/**
	 * @return the hat's height.
	 */
	public double getHeight() {
		return Settings.HAT_SIZE;
	}

	/**
	 * Get the hat's three rectangles.
	 */
	public List<HatPart> getRectangles() {
		return rectangles;
	}

	/**
	 * @return whether the hat is currently extended.
	 */
	public Boolean getExtended() {
		return extended;
	}

	/**
	 * Center the hat.
	 */
	public void center() {
		core.setX((Settings.WIDTH - core.getWidth()) / 2);
		core.setY(Settings.HEIGHT - core.getHeight() - Settings.HAT_OFFSET);
		leftBrim.setX(core.getX() - leftBrim.getWidth());
		leftBrim.setY(core.getY());
		rightBrim.setX(core.getX() + core.getWidth());
		rightBrim.setY(core.getY());
	}

	/**
	 * Extend the top hat the full width of the screen.
	 */
	public void extend() {
		extended = true;
		setWidth(Settings.WIDTH);
		center();
	}

	/**
	 * Double the top hat.
	 */
	public void doubleSize() {
		if (!extended) {
			extended = true;
			setWidth(getWidth() * 2);
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(Settings.DOUBLE_TOPHAT_DURATION), ae -> resetSize()));
			timeline.play();
		}
	}
	
	/**
	 * Reset the hat's size.
	 */
	public void resetSize() {
		extended = false;
		setWidth(originalSize());
	}
	
	/**
	 * @return the original size of the top hat.
	 */
	private double originalSize() {
		return Settings.HAT_SIZE + 2 * Settings.BRIM_WIDTH;
	}

	/**
	 * Move the hat based on a key code.
	 * @param code the key pressed by the user.
	 */
	public void move(KeyCode code) {
		if (code == KeyCode.RIGHT) {
			moveRight();
		} else if (code == KeyCode.LEFT) {
			moveLeft();
		}
	}

	/**
	 * Move the hat to the right.
	 * Wraps around if necessary.
	 */
	private void moveRight() {
		if (Settings.WIDTH - getX() <= Settings.KEY_INPUT_SPEED) {
			setX(0);
		} else {
			setX(getX() + Settings.KEY_INPUT_SPEED);
		}
	}

	/**
	 * Move the hat to the left.
	 * Wraps around if necessary.
	 */
	private void moveLeft() {
		if (getMaxX() <= Settings.KEY_INPUT_SPEED) {
			setX(Settings.WIDTH - getWidth());
		} else {
			setX(getX() - Settings.KEY_INPUT_SPEED);
		}
	}
	
	/**
	 * @param view the view who's middle we seek.
	 * @return the midpoint of the view on the X-axis.
	 */
	private double getMidpoint(ImageView view) {
		return view.getX() + view.getBoundsInParent().getWidth() / 2;
	}

	/**
	 * @param view an ImageView, specifically, the bunny or power-up.
	 * @return whether the hat's brims were intersected.
	 */
	public Boolean brimIntersected(ImageView view) {
		return intersects(view) && !coreIntersected(view);
	}

	/**
	 * @param view an ImageView, specifically, the bunny or power-up.
	 * @return whether the hat's core was intersected by the middle of the ImageView.
	 */
	public Boolean coreIntersected(ImageView view) {
		return core.intersects(getMidpoint(view), view.getY(), 1, view.getBoundsInParent().getHeight());
	}

	/**
	 * @param view an ImageView, specifically, the bunny or power-up.
	 * @return whether the hat's was intersected.
	 */
	public Boolean intersects(ImageView view) {
		return leftBrim.intersects(view.getBoundsInParent()) ||
				rightBrim.intersects(view.getBoundsInParent()) ||
				core.intersects(view.getBoundsInParent());
	}

}
