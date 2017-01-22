
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

	public int getPoints() {
		return points;
	}

	public int getLives() {
		return lives;
	}

	public int getLevel() {
		return level;
	}

	public void removeLife() {
		lives -= 1;
	}

	public void addPoints(int amount) {
		points += amount;
	}

	public void changeLevel(int levelNumber) {
		level = levelNumber;
	}

	public String pointsString() {
		return new String("Points: " + Integer.toString(points));
	}

	public String livesString() {
		return new String("Lives: " + Integer.toString(lives));
	}

	public String levelString() {
		return new String("Level " + Integer.toString(level));
	}

}
