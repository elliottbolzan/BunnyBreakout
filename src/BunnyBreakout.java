import java.net.URL;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BunnyBreakout extends Application {

	private Stage myStage;
	private User myUser;
	private Level myLevel;
	private TopHat myTopHat;
	private Timeline myAnimation;
	private Status myStatus;
	private MessageView mySplashScreen;
	private ArrayList<Bunny> myBunnies = new ArrayList<Bunny>();

	private Boolean keysEnabled = true;
	private Boolean splashing = true;
	private Boolean showingMessage = false;

	/**
	 * Initialize what will be displayed and how it will be updated.
	 */
	@Override
	public void start(Stage s) {
		myStage = s;
		s.setTitle(Settings.TITLE);
		userSetup();
		Scene scene = setupSplashScreen(Settings.WIDTH, Settings.HEIGHT, Settings.SECONDARY_COLOR);
		attachScene(scene);
		createAnimation();
		playSong();
	}

	private void userSetup() {
		myUser = new User();
	}

	private void createAnimation() {
		KeyFrame frame = new KeyFrame(Duration.millis(Settings.MILLISECOND_DELAY), e -> step(Settings.SECOND_DELAY));
		myAnimation = new Timeline();
		myAnimation.setCycleCount(Timeline.INDEFINITE);
		myAnimation.getKeyFrames().add(frame);
	}

	private void attachScene(Scene scene) {
		scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		myStage.setScene(scene);
		myStage.show();
	}

	private Scene setupSplashScreen(int width, int height, Paint background) {
		mySplashScreen = new MessageView(true, false);
		return new Scene(mySplashScreen, width, height, background);
	}

	private Scene setupGame(int width, int height, Paint background, int levelNumber) {
		keysEnabled = false;
		myLevel = new Level(levelNumber);
		Scene scene = new Scene(myLevel, width, height, background);
		myTopHat = new TopHat();
		for (Rectangle rect : myTopHat.getRectangles()) {
			rect.setFill(Settings.WHITE);
			myLevel.add(rect);
		}
		myStatus = new Status(myUser.livesString(), myUser.levelString(), myUser.pointsString());
		myLevel.add(myStatus.getLevel());
		myLevel.add(myStatus.getLives());
		myLevel.add(myStatus.getPoints());
		myLevel.add(myStatus.getLine());
		resetBunnies();
		startTimer();
		return scene;
	}

	private void playSong() {
		URL resource = getClass().getResource("song.mp3");
		Media media = new Media(resource.toString());
		MediaPlayer player = new MediaPlayer(media);
		player.setCycleCount(MediaPlayer.INDEFINITE);
		player.play();
	}

	private void step(double elapsedTime) {
		for (int i = 0; i < myBunnies.size(); i++) {
			Bunny bunny = myBunnies.get(i);
			bunny.checkWalls();
			checkPaddle(bunny);
			checkBlocks(bunny);
			checkPowerUps();
			bunny.updateLocation(elapsedTime, myLevel.getBunnyXSpeed(), myLevel.getBunnyYSpeed());
		}
		myLevel.updatePowerUps(elapsedTime);
	}

	private void checkPaddle(Bunny bunny) {
		if (bunny.getBoundsInParent().getMaxY() <= myTopHat.getY() + Settings.BRIM_HEIGHT) {
			if (myTopHat.coreIntersected(bunny)) {
				bunny.setGoingDown(false);
			} else if (myTopHat.brimIntersected(bunny)) {
				bunny.setGoingDown(false);
				bunny.setGoingRight(!bunny.getGoingRight());
			}
		} else if (bunny.getY() >= Settings.HEIGHT) {
			myBunnies.remove(bunny);
			if (myBunnies.size() == 0) {
				updateLives();
			}
		}
	}

	private void checkBlocks(Bunny bunny) {
		for (int i = 0; i < myLevel.getBlocks().size(); i++) {
			Block block = myLevel.getBlocks().get(i);
			Boolean intersected = false;
			if (block.horizontalWallsIntersected(bunny)) {
				bunny.setGoingDown(!bunny.getGoingDown());
				intersected = true;
			} else if (block.verticalWallsIntersected(bunny)) {
				bunny.setGoingRight(!bunny.getGoingRight());
				intersected = true;
			}

			if (intersected) {
				if (bunny.getHitsEnabled()) {
					block.gotHit();
					bunny.disableHits();
					myLevel.newPowerUp(block.getX(), block.getBoundsInParent().getMaxY(), myTopHat.getExtended());
					if (block.getHits() == 0) {
						block.startShrinking();
						myLevel.getBlocks().remove(i);
						i -= 1;
						updatePoints(block.getWorth());
						checkLevelDone();
					}
					if (block.getType() != 1) {
						block.updateImage();
					}
				}
				return;
			}
		}
	}

	private void checkPowerUps() {
		for (int i = 0; i < myLevel.getPowerUps().size(); i++) {
			PowerUp power = myLevel.getPowerUps().get(i);
			if (myTopHat.intersects(power)
					&& power.getY() + power.getBoundsInParent().getHeight() <= myTopHat.getY() + Settings.BRIM_HEIGHT) {
				if (power.getType() == 1) {
					myTopHat.doubleSize();
				} else if (power.getType() == 2) {
					addBunny();
				} else if (power.getType() == 3) {
					updatePoints(myLevel.destroyColumn(power.getX() + power.getBoundsInParent().getWidth() / 2));
				}
				myLevel.getChildren().remove(power);
				myLevel.getPowerUps().remove(i);
				i -= 1;
			}
		}
	}
	

	private void checkLevelDone() {
		if (myLevel.done()) {
			updateLevel(myUser.getLevel() + 1);
		}
	}

	private void updateLives() {
		myUser.removeLife();
		myStatus.getLives().setText(myUser.livesString());
		if (myUser.getLives() == 0) {
			displayMessage(false);
		} else {
			reset();
		}
	}

	private void updatePoints(int amount) {
		myUser.addPoints(amount);
		myStatus.getPoints().setText(myUser.pointsString());
	}

	void updateLevel(int levelNumber) {
		if (levelNumber > Settings.LEVELS) {
			displayMessage(true);
			return;
		}
		myUser.changeLevel(levelNumber);
		myStatus.getLevel().setText(myUser.levelString());
		goToLevel(myUser.getLevel(), gotBonus(myLevel.getLevelDuration()));
		reset();
	}

	private void reset() {
		myAnimation.pause();
		keysEnabled = false;
		myTopHat.center();
		myLevel.removePowerUps();
		resetBunnies();
		myLevel.setSpeed(myUser.getLevel());
		startTimer();
	}

	private void startTimer() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(Settings.INTER_GAME_DELAY), ae -> play()));
		timeline.play();
	}

	private void play() {
		keysEnabled = true;
		myAnimation.play();
		myLevel.startStopwatch();
	}

	private void handleKeyInput(KeyCode code) {
		if (splashing) {
			if (!mySplashScreen.getOnSecondPage()) {
				mySplashScreen.showSecondPage();
			} else {
				splashing = false;
				goToLevel(1, false);
			}
			return;
		}
		if (showingMessage && keysEnabled) {
			showingMessage = false;
			userSetup();
			goToLevel(1, false);
			return;
		}
		checkDirectionals(code);
		cheatCodes(code);
	}

	private void checkDirectionals(KeyCode code) {
		if (keysEnabled) {
			checkRightKey(code);
			checkLeftKey(code);
		}
	}
	
	private void checkRightKey(KeyCode code) {
		if (code == KeyCode.RIGHT) {
			if (Settings.WIDTH - myTopHat.getX() <= Settings.KEY_INPUT_SPEED) {
				myTopHat.setX(0);
			} else {
				myTopHat.setX(myTopHat.getX() + Settings.KEY_INPUT_SPEED);
			}
		}
	}
	
	private void checkLeftKey(KeyCode code) {
		if (code == KeyCode.LEFT) {
			if (myTopHat.getX() + myTopHat.getWidth() <= Settings.KEY_INPUT_SPEED) {
				myTopHat.setX(Settings.WIDTH - myTopHat.getWidth());
			} else {
				myTopHat.setX(myTopHat.getX() - Settings.KEY_INPUT_SPEED);
			}
		}
	}

	private void cheatCodes(KeyCode code) {
		if (code == KeyCode.SPACE) {
			keysEnabled = false;
			myTopHat.extend();
		} else if (code == KeyCode.DIGIT1) {
			updateLevel(1);
		} else if (code == KeyCode.DIGIT2) {
			updateLevel(2);
		} else if (code == KeyCode.DIGIT3) {
			updateLevel(3);
		} else if (code == KeyCode.C) {
			myLevel.removeBlockOfType(1);
		} else if (code == KeyCode.S) {
			myLevel.removeBlockOfType(2);
		} else if (code == KeyCode.G) {
			myLevel.removeBlockOfType(3);
		}
		checkLevelDone();
	}
	
	private void goToLevel(int number, Boolean gotBonus) {
		attachScene(setupGame(Settings.WIDTH, Settings.HEIGHT, Settings.BACKGROUND, number));
		if (gotBonus) {
			myStatus.getPoints().setFill(Color.ORANGERED);
			Timeline timeline = new Timeline(
					new KeyFrame(Duration.millis(Settings.BONUS_ANIMATION_DURATION), ae -> myStatus.getPoints().setFill(Settings.WHITE)));
			timeline.play();
		}
	}

	private void displayMessage(Boolean won) {
		keysEnabled = false;
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(Settings.MESSAGE_DELAY), ae -> keysEnabled = true));
		timeline.play();
		showingMessage = true;
		myAnimation.pause();
		MessageView view = new MessageView(false, won);
		attachScene(new Scene(view, Settings.WIDTH, Settings.HEIGHT, Settings.SECONDARY_COLOR));
	}

	public void addBunny() {
		Bunny newBunny = new Bunny();
		myBunnies.add(newBunny);
		myLevel.add(newBunny);
	}

	public void resetBunnies() {
		while (!myBunnies.isEmpty()) {
			Bunny bunny = myBunnies.get(0);
			myLevel.remove(bunny);
			myBunnies.remove(bunny);
		}
		addBunny();
	}

	public Boolean gotBonus(double time) {
		if (time < Settings.LEVEL_DURATION_BASELINE) {
			int bonus = (int) (Settings.BONUS_MULTIPLIER * (Settings.LEVEL_DURATION_BASELINE - time));
			updatePoints(bonus);
			return true;
		}
		return false;
	}

	/**
	 * Start the program.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
