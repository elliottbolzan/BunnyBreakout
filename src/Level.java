import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.Group;

public class Level extends Group {
	
    public static final int VERTICAL_OFFSET = 15;
    public static final int CARROT_HORIZONTAL_SPACING = 20;
    public static final int OTHER_BLOCK_HORIZONTAL_SPACING = 2;
    public static final int BUNNY_START_SPEED = 100;
    public static final int BUNNY_INCREASE_INTERVAL = 20;
    
    public int BUNNY_X_SPEED;
    public int BUNNY_Y_SPEED;

	private ArrayList<String> configuration = new ArrayList<String>();
	public ArrayList<Block> blocks = new ArrayList<Block>();
	public ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();

	public Level(int number) {
		setSpeed(number);
		String path = new String(System.getProperty("user.dir") + "/levels/" + Integer.toString(number));
		try {
			for (String line: Files.readAllLines(Paths.get(path))) {
			    configuration.add(line);
			}
			parse();
		}
		catch (IOException e) {
			System.out.println(e.toString());
		}
	}
	
	public void setSpeed(int levelNumber) {
		Random rand = new Random();
		int maximum = 2 * (BUNNY_START_SPEED + (levelNumber - 1) * BUNNY_INCREASE_INTERVAL);
		BUNNY_X_SPEED = rand.nextInt(2 * BUNNY_INCREASE_INTERVAL) - BUNNY_INCREASE_INTERVAL + maximum / 2;
		BUNNY_Y_SPEED = maximum - BUNNY_X_SPEED;
	}
	
	private void parse() {
		for (int i = 0; i < configuration.size(); i++) {
			String line = configuration.get(i);
			for (int j = 0; j < line.length(); j++) {
			    char c = line.charAt(j); 
			    if (c != ' ') {
			    	addBlock(Character.getNumericValue(c), i, j);
			    }
			}
		}
	}
	
	private void addBlock(int type, int row, int column) {
		Block block = new Block(type);
		block.setX(column * (block.getBoundsInParent().getWidth() + horizontalSpacing(type)));
    	block.setY(Status.PADDING + VERTICAL_OFFSET * (row + 1));
        blocks.add(block);
		super.getChildren().add(block);
	}
	
	private int horizontalSpacing(int type) {
		if (type == 1) {
			return CARROT_HORIZONTAL_SPACING;
		}
		else {
			return OTHER_BLOCK_HORIZONTAL_SPACING;
		}
	}

}
