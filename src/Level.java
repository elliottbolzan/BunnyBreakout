import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.scene.Group;

public class Level extends Group {
	
    public static final int VERTICAL_OFFSET = 15;
    
    public int BUNNY_X_SPEED;
    public int BUNNY_Y_SPEED;
	
	private ArrayList<String> configuration = new ArrayList<String>();
	public ArrayList<Block> blocks = new ArrayList<Block>();

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
	
	private void setSpeed(int number) {
		BUNNY_X_SPEED = BUNNY_Y_SPEED = 80 + (number - 1) * 40;
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
    	block.setY(Status.HEIGHT + VERTICAL_OFFSET * (row + 1));
        blocks.add(block);
		super.getChildren().add(block);
	}
	
	private int horizontalSpacing(int type) {
		if (type == 1) {
			return 20;
		}
		else {
			return 2;
		}
	}

}
