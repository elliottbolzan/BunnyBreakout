import javafx.scene.Group;
import javafx.scene.text.TextAlignment;

public class SplashScreen extends Group {
	
	public static final int TEXT_WIDTH = 360;
	public static final int TEXT_HEIGHT = 360;
	public static final int TITLE_Y_PADDING = 30;
	public static final int TITLE_X_PADDING = 60;
	public static final int TITLE_HEIGHT = 50;
	public static final int PADDING = 20;
    
    private CustomText instructions;
    
    public Boolean startGame = false;

	public SplashScreen() {
		setupTitle();
		setupInstructions();
	}
	
	public void showCheatCodes() {
		startGame = true;
		instructions.setText(new String("\n\nHere are a few cheat codes:\n\nPress the space bar to extend your top hat the length of the screen.\n\nPress 1, 2, or 3 to go to the corresponding level.\n\nPress C to remove all carrots; S to remove all soil; and G to remove all grass.\n\nPress any key to begin playing."));
	}
	
	private void setupTitle() {
		CustomText title = new CustomText(TEXT_WIDTH, TEXT_HEIGHT, "Welcome to BunnyBreakout", true);
		title.setX(TITLE_X_PADDING);
		title.setY(TITLE_Y_PADDING);
		super.getChildren().add(title);
	}
	
	private void setupInstructions() {
		String text = new String("\n\nBounce your bunny off your top hat.\n\nMake it eat all the carrots, soil, and grass.\n\nTo move your top hat left and right, use the ← and → keys.\n\nLand in the middle of the top hat? Bounce in the expected direction. Land on the brims of the hat? Bounce back where you came from.\n\nPress any key to continue.");
		instructions = new CustomText(TEXT_WIDTH, TEXT_HEIGHT, text, false);
		instructions.setX(PADDING);
		instructions.setY(TITLE_Y_PADDING + PADDING);
		instructions.setWrappingWidth(TEXT_WIDTH);
		instructions.setTextAlignment(TextAlignment.JUSTIFY);
		super.getChildren().add(instructions);
	}

}
