import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * A control panel for the entire project.
 */
public class Settings {

	public static final String TITLE = "BunnyBreakout";
	public static final int WIDTH = 400;
	public static final int HEIGHT = 480;
	public static final int FRAMES_PER_SECOND = 60;
	public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
	public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
	public static final int KEY_INPUT_SPEED = 20;

	public static final int HIT_TIMEOUT = 10;
	public static final int INTER_GAME_DELAY = 800;
	public static final int MESSAGE_DELAY = 750;
	public static final int BONUS_ANIMATION_DURATION = 1500;
	public static final int SHRINK_ANIMATION_DURATION = 300;
	public static final int DOUBLE_TOPHAT_DURATION = 7000;

	public static final int BONUS_MULTIPLIER = 10;

	public static final Paint BACKGROUND = Color.BLACK;
	public static final Paint SECONDARY_COLOR = Color.TURQUOISE;
	public static Color WHITE = Color.WHITESMOKE;
	public static final String FONT = "Courier";
	public static final int FONT_SIZE = 18;

	public static final int LEVELS = 3;
	public static final int DEFAULT_NUMBER_LIVES = 5;
	public static final int LEVEL_DURATION_BASELINE = 60;

	public static final int BLOCK_1_WORTH = 15;
	public static final int BLOCK_2_WORTH = 10;
	public static final int BLOCK_3_WORTH = 5;

	// These percentages had to be modified from my plan. I was simply giving
	// too many power-ups!
	public static final int PERCENT_POWER_UP_1 = 16;
	public static final int PERCENT_POWER_UP_2 = 8;
	public static final int PERCENT_POWER_UP_3 = 4;
	public static final int PERCENT_POWER_UP = PERCENT_POWER_UP_1 + PERCENT_POWER_UP_2 + PERCENT_POWER_UP_3;
	public static final int CLEAR_BLOCKS_OF_WIDTH = 20;

	public static final int HAT_OFFSET = 10;
	public static final int HAT_SIZE = 30;
	public static final int BRIM_WIDTH = 15;
	public static final int BRIM_HEIGHT = 6;

	public static final String BUNNY_IMAGE = "bunny.gif";
	public static final int BUNNY_X_OFFSET = 26;
	public static final int BUNNY_Y_OFFSET = 2;
	public static final int BUNNY_START_SPEED = 100;
	public static final int BUNNY_INCREASE_INTERVAL = 20;

}
