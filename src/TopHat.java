/**
 * Elliott Bolzan, January 2017
 */

import java.util.ArrayList;
import java.util.Arrays;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Create a bunny-themed paddle, composed of three rectangles.
 */
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
		setColor();
		center();
	}

	/**
	 * @return the hat's X-coordinate.
	 */
	public double getX() {
		return leftBrim.getX();
	}

	/**
	 * Setting the hat's X-coordinate involves setting it for three rectangles.
	 */
	public void setX(double value) {
		leftBrim.setX(value);
		core.setX(value + leftBrim.getWidth());
		rightBrim.setX(value + leftBrim.getWidth() + Settings.HAT_SIZE);
	}

	/**
	 * @return the hat's Y-coordinate.
	 */
	public double getY() {
		return leftBrim.getBoundsInParent().getMinY();
	}

	/**
	 * Set that hat's Y-coordinate.
	 */
	public void setY(double value) {
		leftBrim.setY(value);
		core.setY(value);
		rightBrim.setY(value);
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
		leftBrim.setX(leftBrim.getX() - (newBrimWidth - leftBrim.getBoundsInParent().getWidth()));
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
	public ArrayList<Rectangle> getRectangles() {
		return rectangles;
	}

	/**
	 * Set the hat's rectangles.
	 */
	public void setRectangles(ArrayList<Rectangle> rectangles) {
		this.rectangles = rectangles;
	}

	/**
	 * @return whether the hat is currently extended.
	 */
	public Boolean getExtended() {
		return extended;
	}

	/**
	 * Set the hat's extended variable.
	 */
	public void setExtended(Boolean extended) {
		this.extended = extended;
	}

	/**
	 * Set the hat's color.
	 */
	private void setColor() {
		for (Rectangle rect : rectangles) {
			rect.setFill(Settings.WHITE);
		}
	}

	/**
	 * Center the hat.
	 */
	public void center() {
		core.setX((Settings.WIDTH - core.getBoundsInParent().getWidth()) / 2);
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
			Timeline timeline = new Timeline(
					new KeyFrame(Duration.millis(Settings.DOUBLE_TOPHAT_DURATION), ae -> resetSize()));
			timeline.play();
		}
	}

	/**
	 * Reset the hat's size.
	 */
	public void resetSize() {
		extended = false;
		setWidth(Settings.HAT_SIZE + 2 * Settings.BRIM_WIDTH);
	}

	/**
	 * Move the hat based on a keycode.
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
	 */
	private void moveLeft() {
		if (getX() + getWidth() <= Settings.KEY_INPUT_SPEED) {
			setX(Settings.WIDTH - getWidth());
		} else {
			setX(getX() - Settings.KEY_INPUT_SPEED);
		}
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
	 * @return whether the hat's core was intersected.
	 */
	public Boolean coreIntersected(ImageView view) {
		return core.intersects(view.getX() + view.getBoundsInParent().getWidth() / 2, view.getY(), 1,
				view.getBoundsInParent().getHeight());
	}

	/**
	 * @param view an ImageView, specifically, the bunny or power-up.
	 * @return whether the hat's was intersected.
	 */
	public Boolean intersects(ImageView view) {
		return leftBrim.intersects(view.getBoundsInParent()) || rightBrim.intersects(view.getBoundsInParent())
				|| core.intersects(view.getBoundsInParent());
	}

}
