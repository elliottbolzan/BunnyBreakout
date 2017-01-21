import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javafx.scene.Group;
import javafx.scene.Node;

public class Level extends Group {

	private static final int VERTICAL_OFFSET = 15;
	private static final int CARROT_HORIZONTAL_SPACING = 20;
	private static final int OTHER_BLOCK_HORIZONTAL_SPACING = 2;

	private int BUNNY_X_SPEED;
	private int BUNNY_Y_SPEED;
	private double startTime;

	private ArrayList<Block> blocks = new ArrayList<Block>();
	private ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();

	public Level(int level) {
		setSpeed(level);
		parse(readInput(level));
	}

	public int getBunnyXSpeed() {
		return BUNNY_X_SPEED;
	}

	public int getBunnyYSpeed() {
		return BUNNY_Y_SPEED;
	}

	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public ArrayList<PowerUp> getPowerUps() {
		return powerUps;
	}

	private ArrayList<String> readInput(int level) {
		ArrayList<String> lines = new ArrayList<String>();
		Scanner input = new Scanner(getClass().getClassLoader().getResourceAsStream(Integer.toString(level)));
		while (input.hasNext()) {
			lines.add(input.nextLine());
		}
		input.close();
		return lines;
	}

	public void setSpeed(int level) {
		int maximum = 2 * (Settings.BUNNY_START_SPEED + (level - 1) * Settings.BUNNY_INCREASE_INTERVAL);
		BUNNY_X_SPEED = new Random().nextInt(2 * Settings.BUNNY_INCREASE_INTERVAL) - Settings.BUNNY_INCREASE_INTERVAL
				+ maximum / 2;
		BUNNY_Y_SPEED = maximum - BUNNY_X_SPEED;
	}

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

	public void add(Node node) {
		getChildren().add(node);
	}

	public void remove(Node node) {
		getChildren().remove(node);
	}

	private void addBlock(int type, int row, int column) {
		Block block = new Block(type);
		block.setX(column * (block.getBoundsInParent().getWidth() + horizontalSpacing(type)));
		block.setY(Status.PADDING + VERTICAL_OFFSET * (row + 1));
		blocks.add(block);
		add(block);
	}

	private int horizontalSpacing(int type) {
		if (type == 1) {
			return CARROT_HORIZONTAL_SPACING;
		} else {
			return OTHER_BLOCK_HORIZONTAL_SPACING;
		}
	}

	public void startStopwatch() {
		startTime = System.currentTimeMillis();
	}

	public double getLevelDuration() {
		return (System.currentTimeMillis() - startTime) / 1000;
	}

	public int destroyColumn(double x) {
		int total = 0;
		for (int i = 0; i < blocks.size(); i++) {
			Block block = blocks.get(i);
			if (block.intersects(x, 0, 1, Settings.HEIGHT)) {
				total += block.getWorth();
				block.startShrinking();
				blocks.remove(i);
				i -= 1;
			}
		}
		return total;
	}

	public void removePowerUps() {
		for (int i = 0; i < powerUps.size(); i++) {
			PowerUp power = powerUps.get(i);
			remove(power);
			powerUps.remove(power);
			i -= 1;
		}
	}

	private int determineType() {
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

	public void newPowerUp(double x, double y, Boolean extendedTopHat) {
		int type = determineType();
		if (type != 0) {
			PowerUp power = new PowerUp(type, x, y);
			if (power != null && !(type == 1 && extendedTopHat)) {
				powerUps.add(power);
				add(power);
			}
		}
	}

	public void updatePowerUps(double elapsedTime) {
		for (int i = 0; i < powerUps.size(); i++) {
			PowerUp power = powerUps.get(i);
			power.setY(power.getY() + 60 * elapsedTime);
			if (power.getY() >= Settings.HEIGHT) {
				remove(power);
				powerUps.remove(i);
				i -= 1;
			}
		}
	}
	
	public Boolean done() {
		return blocks.size() == 0;
	}
	
	public void removeBlockOfType(int type) {
		for (int i = 0; i < blocks.size(); i++) {
			Block block = blocks.get(i);
			if (block.getType() == type) {
				remove(block);
				blocks.remove(i);
				i -= 1;
			}
		}
	}

}
