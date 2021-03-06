
/**
 * Elliott Bolzan, January 2017
 */

import javafx.scene.shape.Rectangle;

/**
 * A class that contains the elements to display the user's status.
 */
public class Status {

	public static final int TEXT_HEIGHT = 30;
	public static final int LINE_HEIGHT = 2;
	public static final int PADDING = 50;
	public static final int LIVES_X = 20;
	public static final int LEVEL_X = 160;
	public static final int POINTS_X = 260;

	private CustomText lives;
	private CustomText level;
	private CustomText points;

	private Rectangle line;

	/**
	 * Create text views to indicate user progress.
	 */
	public Status(String livesString, String levelString, String pointsString) {
		lives = new CustomText(LIVES_X, TEXT_HEIGHT, livesString, false);
		level = new CustomText(LEVEL_X, TEXT_HEIGHT, levelString, true);
		points = new CustomText(POINTS_X, TEXT_HEIGHT, pointsString, false);
		line = new Rectangle(0, PADDING - LINE_HEIGHT, Settings.WIDTH, LINE_HEIGHT);
		line.setFill(Settings.SECONDARY_COLOR);
	}

	/**
	 * @return a CustomText object that displays the user's lives.
	 */
	public CustomText getLives() {
		return lives;
	}

	/**
	 * @return a CustomText object that displays the user's level.
	 */
	public CustomText getLevel() {
		return level;
	}

	/**
	 * @return a CustomText object that displays the user's points.
	 */
	public CustomText getPoints() {
		return points;
	}

	/**
	 * @return a Rectangle that serves as a dividing line.
	 */
	public Rectangle getLine() {
		return line;
	}

}
