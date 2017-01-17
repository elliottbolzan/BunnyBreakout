import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller extends Application {
	
    private Stage myStage;
    public Scene myScene;
    private Timeline animation;
    private Status myStatus;
    private MessageView splashScreen;
    private Boolean keysEnabled = false;
    private Boolean splashing = true;
    private Boolean showingMessage = false;
    
    public static User myUser;
    public static Level myLevel;
    public static TopHat myTopHat;
    
    private ArrayList<Bunny> bunnies = new ArrayList<Bunny>();

    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage s) {
        // attach scene to the stage and display it
    	userSetup();
        Scene scene = setupGame(Settings.WIDTH, Settings.HEIGHT, Settings.SECONDARY_COLOR, 1);
        s.setScene(scene);
        s.setTitle(Settings.TITLE);
        s.show();
        myStage = s;
        // attach "game loop" to timeline to play it
        KeyFrame frame = new KeyFrame(Duration.millis(Settings.MILLISECOND_DELAY),
                                      e -> step(Settings.SECOND_DELAY));
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
                
        playSong();
    }
    
    private void userSetup() {
    	myUser = new User();
    }

    private Scene setupGame (int width, int height, Paint background, int levelNumber) {
    	if (splashing) {
        	splashScreen = new MessageView(true, false);
        	myScene = new Scene(splashScreen, width, height, background);
    	}
    	else {
            myLevel = new Level(levelNumber);
            myScene = new Scene(myLevel, width, height, background);
            myTopHat = new TopHat();
            myStatus = new Status();
        	resetBunnies();
            startTimer();
    	}
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return myScene;
    }
    
    private void playSong() {
    	URL resource = getClass().getResource("song.mp3");
    	final Media media = new Media(resource.toString());
        MediaPlayer player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
    }
    
    private void step (double elapsedTime) {
    	for (int i = 0; i < bunnies.size(); i++) {
    		Bunny bunny = bunnies.get(i);
	    	checkVerticalWalls(bunny);
	    	checkTopWall(bunny);
	    	checkPaddle(bunny);
	    	checkBlocks(bunny);
	    	checkPowerUps();
	    	updateX(bunny, elapsedTime);
	    	updateY(bunny, elapsedTime);
    	}
    	updatePowerUps(elapsedTime);
    }

    private void checkVerticalWalls (Bunny bunny) {
    	if (bunny.getX() >= Settings.WIDTH - bunny.getBoundsInLocal().getWidth()) {
        	bunny.goingRight = false;
        }
        if (bunny.getX() <= 0) {
        	bunny.goingRight = true;
        }
    }
    
    private void checkTopWall (Bunny bunny) {
        if (bunny.getY() <= Status.PADDING) {
        	bunny.goingDown = true;
        }
    }

    private void checkPaddle (Bunny bunny) {
    	if (bunny.bottom() <= myTopHat.getY() + myTopHat.BRIM_HEIGHT) {
        	if (myTopHat.coreIntersected(bunny)) {
            	bunny.goingDown = false;
        	}
        	else if (myTopHat.brimIntersected(bunny)) {
            	bunny.goingDown = false;
            	bunny.goingRight = !bunny.goingRight;
        	}
    	}
    	else if (bunny.getY() >= Settings.HEIGHT) {
    		bunnies.remove(bunny);
    		if (bunnies.size() == 0) {
        		updateLives();
    		}
    	}
    }
    
    private void checkBlocks (Bunny bunny) {
    	for (int i = 0; i < myLevel.blocks.size(); i++) {
    		Block block = myLevel.blocks.get(i);
    		Boolean intersected = false;
    		if (block.verticalWallsIntersected(bunny)) {
    			bunny.goingRight = !bunny.goingRight;
    			intersected = true;
    		}
    		else if (block.horizontalWallsIntersected(bunny)) {
    			bunny.goingDown = !bunny.goingDown;
    			intersected = true;
    		}
    		if (intersected) {
    			block.hits -= 1;
    			if (block.hits == 0) {
    				block.animate();
    				determinePowerUp(block);
        			myLevel.blocks.remove(i);
        			i -= 1;
        			updatePoints(block.worth);
        			checkLevelDone();
        			return;
    			}
    			if (block.type != 1) {
    				block.updateImage();
    			}
    		}
    	}
    }
    
    private void checkPowerUps () {
    	for (int i = 0; i < myLevel.powerUps.size(); i++) {
    		PowerUp power = myLevel.powerUps.get(i);
    		if (myTopHat.intersects(power) && power.getY() + power.getBoundsInParent().getHeight() <= myTopHat.getY() + myTopHat.BRIM_HEIGHT) {
    			if (power.type == 1) {
    				myTopHat.doubleSize();
    			}
    			else if (power.type == 2) {
    				addBunny();
    			}
    			else if (power.type == 3) {
    				destroyColumnOfBlocks(power);
    			}
    			myLevel.getChildren().remove(power);
    			myLevel.powerUps.remove(i);
    			i -= 1;
        	}
    	}
    }
    
    private void updateX (Bunny bunny, double elapsedTime) {
        if (bunny.goingRight) {
        	bunny.setX(bunny.getX() + myLevel.BUNNY_X_SPEED * elapsedTime);
        }
        else {
        	bunny.setX(bunny.getX() - myLevel.BUNNY_X_SPEED * elapsedTime);
        }
    }
    
    private void updateY(Bunny bunny, double elapsedTime) {
        if (bunny.goingDown) {
        	bunny.setY(bunny.getY() + myLevel.BUNNY_Y_SPEED * elapsedTime);
        }
        else {
        	bunny.setY(bunny.getY() - myLevel.BUNNY_Y_SPEED * elapsedTime);
        }
    }
    
    private void updatePowerUps(double elapsedTime) {
    	for (int i = 0; i < myLevel.powerUps.size(); i++) {
    		PowerUp power = myLevel.powerUps.get(i);
    		if (power.getY() >= Settings.HEIGHT) {
    			myLevel.getChildren().remove(power);
    			myLevel.powerUps.remove(i);
    			i -= 1;
    			return;
    		}
        	power.setY(power.getY() + 60 * elapsedTime);
    	}
    }
    
    private void checkLevelDone() {
    	if (myLevel.blocks.size() == 0) {
    		updateLevel(myUser.level + 1);
    	}
    }
    
    private void updateLives() {
		myUser.removeLife();
    	myStatus.lives.setText(myUser.livesString());
    	if (myUser.lives == 0) {
    		displayMessage(false);
    	}
    	else {
    		reset();
    	}
    }
    
    private void updatePoints(int amount) {
		myUser.addPoints(amount);
    	myStatus.points.setText(myUser.pointsString());
    }
    
    private void updateLevel(int levelNumber) {
    	if (levelNumber > Settings.LEVELS) {
    		displayMessage(true);
    		return;
    	}
		myUser.changeLevel(levelNumber);
    	myStatus.level.setText(myUser.levelString());
		goToLevel(myUser.level);
		reset();
    }
    
    private void reset() {
    	animation.pause();
    	myTopHat.center();
    	removePowerUps();
    	resetBunnies();
    	myLevel.setSpeed(myUser.level);
        keysEnabled = false;
        startTimer();
    }
    
    private void startTimer() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> play()));
        timeline.play();
    }
    
    private void play() {
    	keysEnabled = true;
        animation.play();
    }
    
    private void handleKeyInput (KeyCode code) {
    	if (splashing) {
    		if (!splashScreen.startGame) {
    			splashScreen.showCheatCodes();
    		}
    		else {
    			splashing = false;
    			goToLevel(1);
    		}
    		return;
    	}
    	if (showingMessage) {
    		showingMessage = false;
    		userSetup();
    		goToLevel(1);
    		return;
    	}
    	checkDirectionals(code);
    	cheatCodes(code);
    }
    
    private void checkDirectionals (KeyCode code) {
    	if (keysEnabled) {
		    if (code == KeyCode.RIGHT) {
		    	if (Settings.WIDTH - myTopHat.getX() <= Settings.KEY_INPUT_SPEED) {
		    		myTopHat.setX(0);
		    	}
		    	else {
		    		myTopHat.setX(myTopHat.getX() + Settings.KEY_INPUT_SPEED);
		    	}
		    }
		    else if (code == KeyCode.LEFT) {
		    	if (myTopHat.getX() + myTopHat.getWidth() <= Settings.KEY_INPUT_SPEED) {
		    		myTopHat.setX(Settings.WIDTH - myTopHat.getWidth());
		    	}
		    	else {
		    		myTopHat.setX(myTopHat.getX() - Settings.KEY_INPUT_SPEED);
		    	}
		    }
    	}
    }

    private void cheatCodes (KeyCode code) {
	    if (code == KeyCode.SPACE) {
	    	keysEnabled = false;
	    	myTopHat.extend();
	    	myTopHat.center();
	    }
	    else if (code == KeyCode.DIGIT1) {
	    	updateLevel(1);
	    }
	    else if (code == KeyCode.DIGIT2) {
	    	updateLevel(2);
	    }
	    else if (code == KeyCode.DIGIT3) {
	    	updateLevel(3);    
	    }
	    else if (code == KeyCode.C) {
	    	for (int i = 0; i < myLevel.blocks.size(); i++) {
	    		Block block = myLevel.blocks.get(i);
	    		if (block.type == 1) {
	    			myLevel.getChildren().remove(block);
	    			myLevel.blocks.remove(i);
	    			i -= 1;
	    		}
	    	}
	    }
	    else if (code == KeyCode.S) {
	    	for (int i = 0; i < myLevel.blocks.size(); i++) {
	    		Block block = myLevel.blocks.get(i);
	    		if (block.type == 2) {
	    			myLevel.getChildren().remove(block);
	    			myLevel.blocks.remove(i);
	    			i -= 1;	    		
	    		}
	    	}
	    }
	    else if (code == KeyCode.G) {
	    	for (int i = 0; i < myLevel.blocks.size(); i++) {
	    		Block block = myLevel.blocks.get(i);
	    		if (block.type == 3) {
	    			myLevel.getChildren().remove(block);
	    			myLevel.blocks.remove(i);
	    			i -= 1;
	    			}
	    	}
	    }
	    checkLevelDone();
    }
    
    private void goToLevel(int number) {
    	Scene scene = setupGame(Settings.WIDTH, Settings.HEIGHT, Settings.BACKGROUND, number);
        myStage.setScene(scene);
        myStage.show();
    }
    
    private void displayMessage(Boolean won) {
    	showingMessage = true;
    	animation.pause();
    	MessageView view = new MessageView(false, won);
		myScene = new Scene(view, Settings.WIDTH, Settings.HEIGHT, Settings.SECONDARY_COLOR);
	    myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		myStage.setScene(myScene);
	    myStage.show();
    }
    
	public void determinePowerUp(Block block) {
		Random rand = new Random();
		int n = rand.nextInt(100) + 1;
		if (n > Settings.PERCENT_POWER_UP) {
			return;
		}
		PowerUp power = new PowerUp(block.getX(), block.getY() + block.getBoundsInParent().getHeight());
		if (n <= Settings.PERCENT_POWER_UP_1 && !myTopHat.extended) {
			power.setType(1);
		}
		else if (n > Settings.PERCENT_POWER_UP_1 && n <= Settings.PERCENT_POWER_UP_1 + Settings.PERCENT_POWER_UP_2) {
			power.setType(2);
		}
		else if (n > Settings.PERCENT_POWER_UP_1 + Settings.PERCENT_POWER_UP_2 && n <= Settings.PERCENT_POWER_UP) {
			power.setType(3);
		}
		myLevel.powerUps.add(power);
		myLevel.getChildren().add(power);
	}
	
	public void removePowerUps() {
    	for (int i = 0; i < myLevel.powerUps.size(); i++) {
    		PowerUp power = myLevel.powerUps.get(i);
    		myLevel.getChildren().remove(power);
    		myLevel.powerUps.remove(power);
    		i -= 1;
    	}
	}
	
	public void addBunny() {
		Bunny newBunny = new Bunny();
		bunnies.add(newBunny);
        myLevel.getChildren().add(newBunny);
	}
	
	public void resetBunnies() {
        while (!bunnies.isEmpty()) {
        	Bunny bunny = bunnies.get(0);
        	myLevel.getChildren().remove(bunny);
        	bunnies.remove(bunny);
        }
		addBunny();
	}
	
	public void destroyColumnOfBlocks(PowerUp power) {
		for (int i = 0; i< myLevel.blocks.size(); i++) {
    		Block block = myLevel.blocks.get(i);
    		if (block.intersects(power.getX() + power.getBoundsInParent().getWidth() / 2, 0, 1, Settings.HEIGHT)) {
				block.animate();
				updatePoints(block.worth);
        		myLevel.blocks.remove(i);
        		i -= 1;
    		}
    	}
	}

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
