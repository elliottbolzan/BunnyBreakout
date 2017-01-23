/**
 * Elliott Bolzan, January 2017
 */

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * A class designed to represent an individual power-up.
 */
public class PowerUp extends ImageView {

	private int type;

	/**
	 * @param type the type of the power-up (1, 2, or 3)
	 * @param x the X-coordinate of the power-up
	 * @param y the Y-coordinate of the power-up
	 */
	public PowerUp(int type, double x, double y) {
		setX(x);
		setY(y);
		setType(type);
	}

	/**
	 * @return power-up type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets type of power-up.
	 */
	public void setType(int value) {
		type = value;
		setImage();
	}

	/**
	 * Sets image of power-up based on type.
	 */
	private void setImage() {
		String path = new String("power_" + Integer.toString(type) + ".gif");
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(path));
		setImage(image);
	}

	/**
	 * Randomly decide existence and type of new power-up.
	 */
	public static int determineType() {
		int n = new Random().nextInt(100) + 1;
		if (n <= Settings.PERCENT_POWER_UP_1) {
			return 1;
		} else if (n > Settings.PERCENT_POWER_UP_1 && n <= Settings.PERCENT_POWER_UP_1 + Settings.PERCENT_POWER_UP_2) {
			return 2;
		} else if (n > Settings.PERCENT_POWER_UP_1 + Settings.PERCENT_POWER_UP_2 && n <= Settings.PERCENT_POWER_UP) {
			return 3;
		}
		return 0;
	}

}
