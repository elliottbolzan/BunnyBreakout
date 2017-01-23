/**
 * Elliott Bolzan, January 2017
 */

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;

/**
 * A class designed to represent one bouncing bunny.
 */
public class Bunny extends ImageView {

	private Boolean goingRight;
	private Boolean goingDown = false;
	private Boolean hitsEnabled = true;

	/**
	 * Constructor.
	 * Should be called without arguments.
	 */
	public Bunny() {
		setImage(new Image(getClass().getClassLoader().getResourceAsStream(Settings.BUNNY_IMAGE)));
		goingRight = startDirection();
		center();
	}

	/**
	 * @return whether the bunny is going right.
	 */
	public Boolean getGoingRight() {
		return goingRight;
	}

	/**
	 * @return whether the bunny is going down.
	 */
	public Boolean getGoingDown() {
		return goingDown;
	}

	/**
	 * @return whether the bunny's hits are enabled.
	 */
	public Boolean getHitsEnabled() {
		return hitsEnabled;
	}

	/**
	 * Sets the bunny's lateral movement.
	 */
	public void setGoingRight(Boolean goingRight) {
		this.goingRight = goingRight;
	}

	/**
	 * Sets the bunny's vertical movement.
	 */
	public void setGoingDown(Boolean goingDown) {
		this.goingDown = goingDown;
	}

	/**
	 * Sets whether the bunny's hits are enabled.
	 */
	public void setHitsEnabled(Boolean hitsEnabled) {
		this.hitsEnabled = hitsEnabled;
	}

	/**
	 * Randomly determines which direction to start in.
	 */
	private Boolean startDirection() {
		return new Random().nextInt(2) == 0;
	}

	/**
	 * Updates the bunny's position.
	 */
	public void updateLocation(double elapsedTime, double x_speed, double y_speed) {
		updateX(elapsedTime, x_speed);
		updateY(elapsedTime, y_speed);
	}

	/**
	 * Updates the X position.
	 */
	private void updateX(double elapsedTime, double x_speed) {
		if (goingRight) {
			setX(getX() + x_speed * elapsedTime);
		} else {
			setX(getX() - x_speed * elapsedTime);
		}
	}

	/**
	 * Updates the Y position.
	 */
	private void updateY(double elapsedTime, double y_speed) {
		if (goingDown) {
			setY(getY() + y_speed * elapsedTime);
		} else {
			setY(getY() - y_speed * elapsedTime);
		}
	}

	/**
	 * Checks for intersection with the walls.
	 */
	public void checkWalls() {
		checkLeftWall();
		checkRightWall();
		checkTopWall();
	}

	/**
	 * Checks for intersection with the left wall.
	 */
	private void checkLeftWall() {
		if (getX() <= 0) {
			goingRight = true;
		}
	}

	/**
	 * Checks for intersection with the right wall.
	 */
	private void checkRightWall() {
		if (getBoundsInParent().getMaxX() >= Settings.WIDTH) {
			goingRight = false;
		}
	}

	/**
	 * Checks for intersection with the top wall.
	 */
	private void checkTopWall() {
		if (getY() <= Status.PADDING) {
			goingDown = true;
		}
	}

	/**
	 * Centers the bunny.
	 */
	public void center() {
		setX(Settings.WIDTH / 2 - Settings.BUNNY_X_OFFSET);
		setY(Settings.HEIGHT - Settings.HAT_OFFSET - Settings.HAT_SIZE - getBoundsInParent().getHeight()
				- Settings.BUNNY_Y_OFFSET);
	}

	/**
	 * Disables hits by the bunny for a very brief amount of time.
	 * Without this feature, blocks seem to vanish when destroyed in very rapid
	 * succession.
	 */
	public void disableHits() {
		hitsEnabled = false;
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(Settings.HIT_TIMEOUT), ae -> hitsEnabled = true));
		timeline.play();
	}

	/**
	 * Check for intersection with the paddle / top hat.
	 */
	public void bounceOffPaddle(TopHat hat) {
		if (getBoundsInParent().getMaxY() <= hat.getY() + Settings.BRIM_HEIGHT && hat.intersects(this)) {
			setGoingDown(false);
			if (hat.brimIntersected(this) && !hat.coreIntersected(this)) {
				setGoingRight(!goingRight);
			}
		}
	}

	/**
	 * Checks whether the bunny was missed by the user.
	 * @return whether the bunny fell through.
	 */
	public Boolean feelThrough() {
		return getY() >= Settings.HEIGHT;
	}

}
