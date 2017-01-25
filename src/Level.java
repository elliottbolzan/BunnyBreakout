/**
 * Elliott Bolzan, January 2017
 */

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

/**
 * A class designed to represent one level.
 */
public class Level extends Group {

	private static final int VERTICAL_OFFSET = 15;
	private static final int CARROT_HORIZONTAL_SPACING = 20;
	private static final int OTHER_BLOCK_HORIZONTAL_SPACING = 2;

	private int BUNNY_X_SPEED;
	private int BUNNY_Y_SPEED;
	private double startTime;

	private ArrayList<Block> blocks = new ArrayList<Block>();
	private ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();
	private ArrayList<Bunny> bunnies = new ArrayList<Bunny>();
	private TopHat topHat;

	/**
	 * @param level the new level's type.
	 */
	public Level(int level) {
		setSpeed(level);
		parse(readInput(level));
		createTopHat();
		resetBunnies();
		startStopwatch();
	}

	/**
	 * @return an ArrayList of the level's blocks.
	 */
	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	/**
	 * @return an ArrayList of the level's power-ups.
	 */
	public ArrayList<PowerUp> getPowerUps() {
		return powerUps;
	}

	/**
	 * @return the level's topHat.
	 */
	public TopHat getTopHat() {
		return topHat;
	}

	/**
	 * @return whether the level is done.
	 */
	public Boolean done() {
		return blocks.size() == 0;
	}

	/**
	 * Obtain level configuration from text file.
	 */
	private ArrayList<String> readInput(int level) {
		ArrayList<String> lines = new ArrayList<String>();
		Scanner input = new Scanner(getClass().getClassLoader().getResourceAsStream(Integer.toString(level)));
		while (input.hasNext()) {
			lines.add(input.nextLine());
		}
		input.close();
		return lines;
	}

	/**
	 * Determine speed based on the current level.
	 */
	public void setSpeed(int level) {
		int maximum = 2 * (Settings.BUNNY_START_SPEED + (level - 1) * Settings.BUNNY_INCREASE_INTERVAL);
		BUNNY_X_SPEED = new Random().nextInt(2 * Settings.BUNNY_INCREASE_INTERVAL) - Settings.BUNNY_INCREASE_INTERVAL
				+ maximum / 2;
		BUNNY_Y_SPEED = maximum - BUNNY_X_SPEED;
	}
	
	/**
	 * @return whether a life was lost.
	 */
	public Boolean lifeLost() {
		return bunnies.size() == 0;
	}
	
	/**
	 * Shortcut to add to Group.
	 */
	public void add(Node node) {
		getChildren().add(node);
	}

	/**
	 * Shortcut to remove from Group.
	 */
	public void remove(Node node) {
		getChildren().remove(node);
	}

	/**
	 * Parse blocks based on level configuration.
	 */
	private void parse(ArrayList<String> configuration) {
		for (int row = 0; row < configuration.size(); row++) {
			String line = configuration.get(row);
			for (int column = 0; column < line.length(); column++) {
				char c = line.charAt(column);
				if (c != ' ') {
					addBlock(Character.getNumericValue(c), row, column);
				}
			}
		}
	}
	
	/**
	 * Add and configure block to level.
	 */
	private void addBlock(int type, int row, int column) {
		Block block = new Block(type);
		block.setX(column * (block.getBoundsInParent().getWidth() + horizontalSpacingBetweenBlocks(type)));
		block.setY(Status.PADDING + VERTICAL_OFFSET * (row + 1));
		blocks.add(block);
		add(block);
	}

	/**
	 * @param type the block's type.
	 * @return the horizontal spacing between blocks.
	 */
	private int horizontalSpacingBetweenBlocks(int type) {
		if (type == 1) {
			return CARROT_HORIZONTAL_SPACING;
		} else {
			return OTHER_BLOCK_HORIZONTAL_SPACING;
		}
	}
	
	/**
	 * Create the top hat.
	 */
	private void createTopHat() {
		topHat = new TopHat();
		for (Rectangle rect : topHat.getRectangles()) {
			add(rect);
		}
	}

	/**
	 * Needed to determine bonus.
	 */
	private void startStopwatch() {
		startTime = System.currentTimeMillis();
	}

	/**
	 * @return the duration fo the level.
	 */
	private double getLevelDuration() {
		return (System.currentTimeMillis() - startTime) / 1000;
	}

	/**
	 * @return bonus, which is proportional to user's speed.
	 */
	public int getBonus() {
		double levelDuration = getLevelDuration();
		if (levelDuration < Settings.LEVEL_DURATION_BASELINE) {
			int bonus = (int) (Settings.BONUS_MULTIPLIER * (Settings.LEVEL_DURATION_BASELINE - levelDuration));
			return bonus;
		}
		return 0;
	}

	/**
	 * Removes the blocks aligned with an X-coordinate.
	 * @return the worth of the removed blocks.
	 */
	private int removeBlocksAlignedWith(double x) {
		return removeBlocks(0, x);
	}

	/**
	 * Removes the blocks of a certain type.
	 * @return the worth of the removed blocks.
	 */
	public int removeBlocksOfType(int type) {
		return removeBlocks(type, -10);
	}

	/**
	 * Remove blocks based either on their type or location.
	 * @return the worth of the removed blocks.
	 */
	private int removeBlocks(int type, double x) {
		int total = 0;
		for (int i = 0; i < blocks.size(); i++) {
			Block block = blocks.get(i);
			if (block.getType() == type || block.intersects(x, 0, Settings.CLEAR_BLOCKS_OF_WIDTH, Settings.HEIGHT)) {
				total += block.getWorth();
				block.startShrinking();
				blocks.remove(block);
				i--;
			}
		}
		return total;
	}
	
	/**
	 * @return points gained in one time step.
	 */
	public int pointsGainedIn(double elapsedTime) {
		return bunnyCollisionCheck(elapsedTime) + updatePowerUps(elapsedTime, false);
	}

	/**
	 * Add bunny to level.
	 */
	private void addBunny() {
		Bunny newBunny = new Bunny();
		bunnies.add(newBunny);
		add(newBunny);
	}

	/**
	 * Remove bunny from level.
	 */
	private void removeBunny(Bunny bunny) {
		remove(bunny);
		bunnies.remove(bunny);
	}

	/**
	 * Remove extra bunnies.
	 */
	private void resetBunnies() {
		while (!bunnies.isEmpty()) {
			removeBunny(bunnies.get(0));
		}
		addBunny();
	}

	/**
	 * Check whether rabbit collided with walls, the top hat, blocks, or
	 * power-ups. Return the corresponding amount of points.
	 * @return the points gained by colliding.
	 */
	public int bunnyCollisionCheck(double elapsedTime) {
		int total = 0;
		for (int i = 0; i < bunnies.size(); i++) {
			Bunny bunny = bunnies.get(i);
			bunny.updateLocation(elapsedTime, BUNNY_X_SPEED, BUNNY_Y_SPEED);
			if (bunny.fellThrough()) {
				bunnies.remove(bunny);
				continue;
			}
			bunny.checkWalls();
			bunny.bounceOffPaddle(topHat);
			total += checkBlocks(bunny);
		}
		return total;
	}

	/**
	 * Checks whether blocks were hit by bunny.
	 */
	private int checkBlocks(Bunny bunny) {
		int total = 0;
		for (int i = 0; i < blocks.size(); i++) {
			Block block = blocks.get(i);
			if (bunny.intersects(block.getBoundsInParent())) {
				bunny.bounceOffBlock(block);
				total += modifyBlock(block, bunny);
				return total;
			}
		}
		return total;
	}

	/**
	 * Hit block, update it, and generate potential new power-ups.
	 * @return points gained in the process.
	 */
	private int modifyBlock(Block block, Bunny bunny) {
		if (bunny.getHitsEnabled()) {
			bunny.disableHits();
			block.gotHit();
			newPowerUp(block.getX(), block.getBoundsInParent().getMaxY(), topHat.getExtended());
			if (block.getHits() == 0) {
				block.startShrinking();
				blocks.remove(block);
				return block.getWorth();
			}
		}
		return 0;
	}

	/**
	 * Potentially create a new power-up.
	 */
	private void newPowerUp(double x, double y, Boolean extendedTopHat) {
		int type = PowerUp.determineType();
		if (type != 0) {
			PowerUp power = new PowerUp(type, x, y);
			if (power != null && !(type == 1 && extendedTopHat)) {
				powerUps.add(power);
				add(power);
			}
		}
	}

	/**
	 * Update location, check for intersection, and remove power-ups.
	 * @return the points gained from power-ups.
	 */
	private int updatePowerUps(double elapsedTime, Boolean remove) {
		int total = 0;
		for (int i = 0; i < powerUps.size(); i++) {
			PowerUp power = powerUps.get(i);
			power.setY(power.getY() + 60 * elapsedTime);
			if (power.getY() >= Settings.HEIGHT || remove) {
				remove(power);
				powerUps.remove(power);
				i--;
				continue;
			}
			if (topHat.intersects(power) && power.getBoundsInParent().getMaxY() <= topHat.getY()) {
				total += applyPowerUp(power);
				i--;
			}
		}
		return total;
	}
	
	/**
	 * @return the points gained thanks to power-ups.
	 */
	private int applyPowerUp(PowerUp power) {
		int points = 0;
		if (power.getType() == 1) {
			topHat.doubleSize();
		} else if (power.getType() == 2) {
			addBunny();
		} else if (power.getType() == 3) {
			points = removeBlocksAlignedWith(power.getX() + power.getBoundsInParent().getWidth() / 2);
		}
		remove(power);
		powerUps.remove(power);
		return points;
	}

	/**
	 * Return to starting point of level.
	 */
	public void reset(int level) {
		topHat.center();
		updatePowerUps(0, true);
		resetBunnies();
		setSpeed(level);
	}

}
