import javafx.scene.Group;
import javafx.scene.text.TextAlignment;

public class Results extends Group {
	
	public static final int TEXT_WIDTH = 360;
	public static final int TEXT_HEIGHT = 360;
	public static final int TITLE_Y_PADDING = 180;
	public static final int TITLE_X_PADDING = 60;
	public static final int TITLE_HEIGHT = 50;
	public static final int PADDING = 20;

	public Results(Boolean won) {
		setupResult(won);
		setupInstructions();
	}

	private void setupResult(Boolean won) {
		String message = new String();
		if (won) {
			message = "Congratulations! You won!";
		}
		else {
			message = "You lost...";
		}
		CustomText result = new CustomText(TEXT_WIDTH, TEXT_HEIGHT, message, true);
		result.setX(TITLE_X_PADDING);
		result.setY(TITLE_Y_PADDING);
		super.getChildren().add(result);
	}
	
	private void setupInstructions() {
		String text = new String("\n\nPress any key to play again.");
		CustomText instructions = new CustomText(TEXT_WIDTH, TEXT_HEIGHT, text, false);
		instructions.setX(PADDING);
		instructions.setY(TITLE_Y_PADDING + PADDING);
		instructions.setWrappingWidth(TEXT_WIDTH);
		instructions.setTextAlignment(TextAlignment.JUSTIFY);
		super.getChildren().add(instructions);
	}
	
}
