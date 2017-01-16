import java.net.URL;
import java.util.Iterator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller extends Application {
    public static final String TITLE = "BunnyBreakout";
    public static final int WIDTH = 400;
    public static final int HEIGHT = 440;
    public static final Paint BACKGROUND = Color.BLACK;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int KEY_INPUT_SPEED = 20;
    public static final double GROWTH_RATE = 1.1;
    public static final String FONT = "Courier";
    public static final int FONT_SIZE = 18; 

    private Stage myStage;
    public Scene myScene;
    private Timeline animation;
    private Bunny bunny;
    private Status status;
    private SplashScreen splashScreen;
    private Boolean keysEnabled = false;
    private Boolean splashing = true;
    private Boolean showingMessage = false;
    
    public static User myUser;
    public static Level myLevel;
    public static TopHat myTopHat;

    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage s) {
        // attach scene to the stage and display it
    	userSetup();
        Scene scene = setupGame(WIDTH, HEIGHT, BACKGROUND, 1);
        s.setScene(scene);
        s.setTitle(TITLE);
        s.show();
        myStage = s;
        // attach "game loop" to timeline to play it
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),
                                      e -> step(SECOND_DELAY));
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        
        playSong();
    }
    
    private void userSetup() {
    	myUser = new User();
    }

    private Scene setupGame (int width, int height, Paint background, int levelNumber) {
    	Line line = new Line();
    	if (splashing) {
        	splashScreen = new SplashScreen();
        	splashScreen.getChildren().add(line);
        	myScene = new Scene(splashScreen, width, height, background);
    	}
    	else {
            myLevel = new Level(levelNumber);
            myScene = new Scene(myLevel, width, height, background);
            myTopHat = new TopHat();
            status = new Status();
        	bunny = new Bunny();
            myLevel.getChildren().add(bunny);
        	myLevel.getChildren().add(line);
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
    	checkVerticalWalls(bunny);
    	checkTopWall(bunny);
    	checkPaddle(bunny);
    	checkBlocks(bunny);
    	checkFailedToCatch(bunny);
    	updateX(bunny, elapsedTime);
    	updateY(bunny, elapsedTime);
    }
    
    private void checkVerticalWalls (Bunny bunny) {
    	if (bunny.getX() >= WIDTH - bunny.getBoundsInLocal().getWidth()) {
        	bunny.goingRight = false;
        }
        if (bunny.getX() <= 0) {
        	bunny.goingRight = true;
        }
    }
    
    private void checkTopWall (Bunny bunny) {
        if (bunny.getY() <= Status.HEIGHT) {
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
    }
    
    private void checkBlocks (Bunny bunny) {
    	for (Iterator<Block> iterator = myLevel.blocks.iterator(); iterator.hasNext();) {
    		Block block = iterator.next();
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
        			myLevel.getChildren().remove(block);
        			iterator.remove();
        			updatePoints(block.worth);
        			checkLevelDone();
        			return;
    			}
    		}
    	}
    }
    
    private void checkFailedToCatch (Bunny bunny) {
    	if (bunny.getY() >= HEIGHT) {
    		updateLives();
    		reset();
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
    
    private void updateY (Bunny bunny, double elapsedTime) {
        if (bunny.goingDown) {
        	bunny.setY(bunny.getY() + myLevel.BUNNY_Y_SPEED * elapsedTime);
        }
        else {
        	bunny.setY(bunny.getY() - myLevel.BUNNY_Y_SPEED * elapsedTime);
        }
    }
    
    private void checkLevelDone() {
    	if (myLevel.blocks.size() == 0) {
    		updateLevel(myUser.level + 1);
    	}
    }
    
    private void updateLives() {
		myUser.removeLife();
    	status.lives.setText(myUser.livesString());
    	if (myUser.lives == 0) {
    		displayMessage(false);
    	}
    }
    
    private void updatePoints(int amount) {
		myUser.addPoints(amount);
    	status.points.setText(myUser.pointsString());
    }
    
    private void updateLevel(int levelNumber) {
    	if (levelNumber == 4) {
    		displayMessage(true);
    		return;
    	}
		myUser.changeLevel(levelNumber);
    	status.level.setText(myUser.levelString());
		goToLevel(myUser.level);
		reset();
    }
    
    private void reset() {
    	animation.pause();
    	myTopHat.center();
    	bunny.center();
        bunny.goingDown = false;
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
		    	if (WIDTH - myTopHat.getX() <= KEY_INPUT_SPEED) {
		    		myTopHat.setX(0);
		    	}
		    	else {
		    		myTopHat.setX(myTopHat.getX() + KEY_INPUT_SPEED);
		    	}
		    }
		    else if (code == KeyCode.LEFT) {
		    	if (myTopHat.getX() + myTopHat.getWidth() <= KEY_INPUT_SPEED) {
		    		myTopHat.setX(WIDTH - myTopHat.getWidth());
		    	}
		    	else {
		    		myTopHat.setX(myTopHat.getX() - KEY_INPUT_SPEED);
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
	    	for (Iterator<Block> iterator = myLevel.blocks.iterator(); iterator.hasNext();) {
	    		Block block = iterator.next();
	    		if (block.type == 1) {
	    			myLevel.getChildren().remove(block);
	    			iterator.remove();
	    		}
	    	}
	    }
	    else if (code == KeyCode.S) {
	    	for (Iterator<Block> iterator = myLevel.blocks.iterator(); iterator.hasNext();) {
	    		Block block = iterator.next();
	    		if (block.type == 2) {
	    			myLevel.getChildren().remove(block);
	    			iterator.remove();
	    		}
	    	}
	    }
	    else if (code == KeyCode.G) {
	    	for (Iterator<Block> iterator = myLevel.blocks.iterator(); iterator.hasNext();) {
	    		Block block = iterator.next();
	    		if (block.type == 3) {
	    			myLevel.getChildren().remove(block);
	    			iterator.remove();
	    		}
	    	}
	    }
	    checkLevelDone();
    }
    
    private void goToLevel(int number) {
    	Scene scene = setupGame(WIDTH, HEIGHT, BACKGROUND, number);
        myStage.setScene(scene);
        myStage.show();
    }
    
    private void displayMessage(Boolean won) {
    	Results results = new Results(won);
    	showingMessage = true;
		myScene = new Scene(results, WIDTH, HEIGHT, BACKGROUND);
	    myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		myStage.setScene(myScene);
	    myStage.show();
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
