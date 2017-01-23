/**
 * Elliott Bolzan, January 2017
 */

/**
* Encapsulates the user's current state.
*/
public class User {

	private int points;
	private int lives;
	private int level;

	public User() {
		lives = Settings.DEFAULT_NUMBER_LIVES;
		level = 1;
	}

	/**
	 * @return the user's point tally.
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * @return the user's number of lives.
	 */
	public int getLives() {
		return lives;
	}

	/**
	 * @return the user's current level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Removes one life from the user.
	 */
	public void removeLife() {
		lives -= 1;
	}

	/**
	 * Adds to user's points.
	 * @param amount value of increase in points.
	 */
	public void addPoints(int amount) {
		points += amount;
	}
	/**
	 * Modify user's level.
	 * @param new level to switch to.
	 */
	public void changeLevel(int levelNumber) {
		level = levelNumber;
	}

	/**
	 * @return a String describing the user's point total.
	 */
	public String pointsString() {
		return new String("Points: " + Integer.toString(points));
	}
	
	/**
	 * @return a String describing the user's lives.
	 */
	public String livesString() {
		return new String("Lives: " + Integer.toString(lives));
	}

	/**
	 * @return a String describing the user's current level.
	 */
	public String levelString() {
		return new String("Level " + Integer.toString(level));
	}

}
