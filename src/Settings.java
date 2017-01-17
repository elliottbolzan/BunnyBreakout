import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Settings {
	
	public static final String TITLE = "BunnyBreakout";
    public static final int WIDTH = 400;
    public static final int HEIGHT = 480;
    public static final int FRAMES_PER_SECOND = 400;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int KEY_INPUT_SPEED = 20;
    
    public static final Paint BACKGROUND = Color.BLACK;
    public static final Paint SECONDARY_COLOR = Color.TURQUOISE;
	public static Color WHITE = Color.WHITESMOKE;
    public static final String FONT = "Courier";
    public static final int FONT_SIZE = 18; 
    
    public static final int LEVELS = 3;
    public static final int PERCENT_POWER_UP_1 = 16; 
    public static final int PERCENT_POWER_UP_2 = 8; 
    public static final int PERCENT_POWER_UP_3 = 4; 
    public static final int PERCENT_POWER_UP = PERCENT_POWER_UP_1 + PERCENT_POWER_UP_2 + PERCENT_POWER_UP_3;
   
}
