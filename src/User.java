
public class User {
	
	public int points;
	public int lives;
	public int level;
	
	public User() {
	    points = 0;
	    lives = 10;
	    level = 1;
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
