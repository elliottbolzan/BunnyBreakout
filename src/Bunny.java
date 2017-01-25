// This entire file is part of my masterpiece.
// Elliott Bolzan

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.util.Random;

/**
 * A class designed to represent one bouncing bunny.
 * Enables the movement of the bunny.
 * Permits the bouncing of the bunny off of walls, blocks, and the top hat.
 * Includes a block-destruction delay function, to smooth out the game play.
 * 
 * I believe it is well designed for these reasons:
 * 1. The methods are short and their purpose is clear.
 * 2. The class is self-contained: other than constants from Settings, it does not depend on other classes.
 * 3. I thought my use of the ternary operator  was in line with what we learned in class: use a language to make code more clear and concise. 
 * 4. I got my image for the bunny without using an absolute path.
 * 5. Most importantly, the implementation is opaque. A user of the class does not need to worry about how or where the bunny is bouncing: the class takes care of it.
 * 
 * I'm aware that the last two methods exhibit some redundancy, but combining them would have made for unreadable logic, I think.
 * 
 */

public class Bunny extends ImageView {

	private Boolean goingRight;
	private Boolean goingDown = false;
	private Boolean hitsEnabled = true;

	/**
	 * Should be called without arguments.
	 */
	public Bunny() {
		setImage(new Image(getClass().getClassLoader().getResourceAsStream(Settings.BUNNY_IMAGE)));
		setStartDirection();
		setY();
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
	 * Checks whether the bunny was missed by the user.
	 * @return whether the bunny fell through.
	 */
	public Boolean fellThrough() {
		return getY() >= Settings.HEIGHT;
	}

	/**
	 * Randomly determines which direction to start in.
	 */
	private void setStartDirection() {
		goingRight = new Random().nextInt(2) == 0;
	}
	
	/**
	 * Always set the bunny's Y-coordinate to this value.
	 */
	private void setY() {
		setY(Settings.HEIGHT - Settings.HAT_OFFSET - Settings.HAT_SIZE - getBoundsInParent().getHeight() - Settings.BUNNY_Y_OFFSET);
	}
	
	/**
	 * Centers the bunny.
	 */
	public void center() {
		setX((Settings.WIDTH - getBoundsInParent().getWidth()) / 2);
	}

	/**
	 * Updates the bunny's position.
	 */
	public void updateLocation(double elapsedTime, double x_speed, double y_speed) {
		updateX(x_speed * elapsedTime);
		updateY(y_speed * elapsedTime);
	}

	/**
	 * Updates the X position.
	 */
	private void updateX(double value) {
		setX(goingRight ? getX() + value : getX() - value);
	}

	/**
	 * Updates the Y position.
	 */
	private void updateY(double value) {
		setY(goingDown ? getY() + value : getY() - value);
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
	 * Note: the Boolean cannot simply be set to the value in the if-statement, as this is a switch.
	 */
	private void checkLeftWall() {
		if (getX() <= 0) {
			goingRight = true;
		}
	}

	/**
	 * Checks for intersection with the right wall.
	 * Note: the Boolean cannot simply be set to the value in the if-statement, as this is a switch.
	 */
	private void checkRightWall() {
		if (getBoundsInParent().getMaxX() >= Settings.WIDTH) {
			goingRight = false;
		}
	}

	/**
	 * Checks for intersection with the top wall.
	 * Note: the Boolean cannot simply be set to the value in the if-statement, as this is a switch.
	 */
	private void checkTopWall() {
		if (getY() <= Status.PADDING) {
			goingDown = true;
		}
	}

	/**
	 * Disables hits by the bunny for a very brief amount of time.
	 * Without this feature, blocks seem to vanish when destroyed in very rapid
	 * succession.
	 */
	public void disableHits() {
		hitsEnabled = false;
		new Timeline(new KeyFrame(Duration.millis(Settings.HIT_TIMEOUT), ae -> hitsEnabled = true)).play();
	}

	/**
	 * If the bunny hits the hat, bounce back in direction we came from.
	 * @param hat the top hat in question.
	 */
	public void bounceOffPaddle(TopHat hat) {
		if (hat.intersects(this) && getBoundsInParent().getMaxY() <= (hat.getY() + Settings.BRIM_HEIGHT)) {
			goingDown = false;
			if (hat.brimIntersected(this) && !hat.coreIntersected(this)) {
				goingRight = !goingRight;
			}
		}
	}
	
	/**
	 * @param block the block to bounce off of.
	 */
	public void bounceOffBlock(Block block) {
		if (block.horizontalWallsIntersected(this)) {
			goingDown = !goingDown;
		} else if (block.verticalWallsIntersected(this)) {
			goingRight = !goingRight;
		}
	}

}
