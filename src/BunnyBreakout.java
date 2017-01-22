import java.net.URL;

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

public class BunnyBreakout extends Application {

	private Stage myStage;
	private User myUser;
	private Level myLevel;
	private Status myStatus;
	private MessageView mySplashScreen;
	private Timeline myAnimation;

	private Boolean keysEnabled = true;
	private Boolean showingSplashScreen = true;
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
		myStatus = new Status(myUser.livesString(), myUser.levelString(), myUser.pointsString());
	}

	private void createAnimation() {
		KeyFrame frame = new KeyFrame(Duration.millis(Settings.MILLISECOND_DELAY), e -> step(Settings.SECOND_DELAY));
		myAnimation = new Timeline();
		myAnimation.setCycleCount(Timeline.INDEFINITE);
		myAnimation.getKeyFrames().add(frame);
	}

	/**
	 * Mechanism to set the scene.
	 */
	private void attachScene(Scene scene) {
		scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
		myStage.setScene(scene);
		myStage.show();
	}

	private Scene setupSplashScreen(int width, int height, Paint background) {
		mySplashScreen = new MessageView(true, false);
		return new Scene(mySplashScreen, width, height, background);
	}

	private Scene setupLevel(int width, int height, Paint background, int levelNumber) {
		keysEnabled = false;
		myLevel = new Level(levelNumber);
		addStatus();
		startTimer();
		return new Scene(myLevel, width, height, background);
	}

	private void addStatus() {
		myLevel.add(myStatus.getLevel());
		myLevel.add(myStatus.getLives());
		myLevel.add(myStatus.getPoints());
		myLevel.add(myStatus.getLine());
	}

	private void displayMessage(Boolean won) {
		keysEnabled = false;
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.millis(Settings.MESSAGE_DELAY), ae -> keysEnabled = true));
		timeline.play();
		showingMessage = true;
		myAnimation.pause();
		MessageView view = new MessageView(false, won);
		attachScene(new Scene(view, Settings.WIDTH, Settings.HEIGHT, Settings.SECONDARY_COLOR));
	}

	private void playSong() {
		URL resource = getClass().getResource("song.mp3");
		Media media = new Media(resource.toString());
		MediaPlayer player = new MediaPlayer(media);
		player.setCycleCount(MediaPlayer.INDEFINITE);
		player.play();
	}

	/**
	 * Called every frame. Check for collisions and major game events.
	 */
	private void step(double elapsedTime) {
		updatePoints(myLevel.pointsGainedIn(elapsedTime));
		if (myLevel.lifeLost()) {
			updateLives();
		}
		checkLevelDone();
	}

	/**
	 * Used to delay the start of the game and let the user get ready.
	 */
	private void startTimer() {
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.millis(Settings.INTER_GAME_DELAY), ae -> startGameplay()));
		timeline.play();
	}

	private void startGameplay() {
		keysEnabled = true;
		myAnimation.play();
	}

	private void checkLevelDone() {
		if (myLevel.done()) {
			updateLevel(myUser.getLevel() + 1);
			addBonus();
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

	private void updateLevel(int levelNumber) {
		if (levelNumber > Settings.LEVELS) {
			displayMessage(true);
			return;
		}
		myUser.changeLevel(levelNumber);
		myStatus.getLevel().setText(myUser.levelString());
		goToLevel(levelNumber);
		reset();
	}

	private void goToLevel(int number) {
		attachScene(setupLevel(Settings.WIDTH, Settings.HEIGHT, Settings.BACKGROUND, number));
	}

	private void addBonus() {
		int bonus = myLevel.getBonus();
		if (bonus != 0) {
			updatePoints(bonus);
			myStatus.getPoints().setFill(Color.ORANGERED);
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(Settings.BONUS_ANIMATION_DURATION),
					ae -> myStatus.getPoints().setFill(Settings.WHITE)));
			timeline.play();
		}
	}

	private void handleKeyInput(KeyCode code) {
		if (!startKey()) {
			if (keysEnabled) {
				myLevel.getTopHat().move(code);
			}
			cheatCodes(code);
		}
	}

	/**
	 * Check whether a message was dismissed using a key.
	 */
	private Boolean startKey() {
		if (showingSplashScreen) {
			if (!mySplashScreen.getOnSecondPage()) {
				mySplashScreen.showSecondPage();
			} else {
				showingSplashScreen = false;
				goToLevel(1);
			}
		}
		if (showingMessage && keysEnabled) {
			showingMessage = false;
			userSetup();
			goToLevel(1);
		}
		return showingSplashScreen || (showingMessage && keysEnabled);
	}

	private void cheatCodes(KeyCode code) {
		if (code == KeyCode.SPACE) {
			keysEnabled = false;
			myLevel.getTopHat().extend();
		} else if (code == KeyCode.DIGIT1) {
			updateLevel(1);
		} else if (code == KeyCode.DIGIT2) {
			updateLevel(2);
		} else if (code == KeyCode.DIGIT3) {
			updateLevel(3);
		} else if (code == KeyCode.C) {
			updatePoints(myLevel.removeBlocksOfType(1));
		} else if (code == KeyCode.S) {
			updatePoints(myLevel.removeBlocksOfType(2));
		} else if (code == KeyCode.G) {
			updatePoints(myLevel.removeBlocksOfType(3));
		}
		checkLevelDone();
	}

	/**
	 * Used after life lost.
	 */
	private void reset() {
		keysEnabled = false;
		myAnimation.pause();
		startTimer();
		myLevel.reset(myUser.getLevel());
	}

	/**
	 * Start the program.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
