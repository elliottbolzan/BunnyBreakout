import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.TextAlignment;

class Message {

	private int title_Y;
	private int instructions_Y;
	private int wrapping_width = 360;
	private String title;
	private String firstPage;
	private String secondPage;
	private Boolean justify;

	public Message(Boolean splash, Boolean won) {
		if (splash) {
			setupSplash();
		} else {
			setupResults(won);
		}
	}

	public int getTitleY() {
		return title_Y;
	}

	public int getInstructionsY() {
		return instructions_Y;
	}

	public int getWrappingWidth() {
		return wrapping_width;
	}

	public String getTitle() {
		return title;
	}

	public String getFirstPage() {
		return firstPage;
	}

	public String getSecondPage() {
		return secondPage;
	}

	public Boolean getJustify() {
		return justify;
	}

	private void setupSplash() {
		title_Y = 40;
		instructions_Y = 90;
		title = "Welcome to BunnyBreakout";
		firstPage = "Bounce your bunny off your top hat." + "\n\nMake it eat all the carrots, soil, and grass."
				+ "\n\nTo move your top hat left and right, use the ← and → keys."
				+ "\n\nLand in the middle of the top hat? Bounce in the expected direction. Land on the brims of the hat? Bounce back where you came from."
				+ "\n\nPress any key to continue.";
		secondPage = "Here are a few cheat codes:"
				+ "\n\nPress the space bar to extend your top hat the length of the screen."
				+ "\n\nPress 1, 2, or 3 to go to the corresponding level."
				+ "\n\nPress C to remove all carrots; S to remove all soil; and G to remove all grass."
				+ "\n\nPress any key to begin playing.";
		justify = true;
	}

	private void setupResults(Boolean won) {
		title_Y = Settings.HEIGHT / 2;
		instructions_Y = Settings.HEIGHT - 40;
		firstPage = "Press any key to play again.";
		justify = false;
		if (won) {
			title = "Congratulations! You won.";
		} else {
			title = "Sorry, you lost.";
		}
	}

}

public class MessageView extends Group {

	private CustomText instructions;
	private Boolean onSecondPage = false;
	private Message message;

	public MessageView(Boolean splash, Boolean won) {
		message = new Message(splash, won);
		onSecondPage = !splash;
		setupTitle();
		setupInstructions();
	}

	public Boolean getOnSecondPage() {
		return onSecondPage;
	}

	private void add(Node e) {
		getChildren().add(e);
	}

	private void setupTitle() {
		CustomText topText = new CustomText(message.getTitle(), true);
		topText.setX((Settings.WIDTH - topText.getBoundsInParent().getWidth()) / 2);
		topText.setY(message.getTitleY());
		super.getChildren().add(topText);
	}

	private void setupInstructions() {
		instructions = new CustomText(message.getFirstPage(), false);
		if (message.getJustify()) {
			instructions.setWrappingWidth(message.getWrappingWidth());
			instructions.setTextAlignment(TextAlignment.JUSTIFY);
		}
		instructions.setX((Settings.WIDTH - instructions.getBoundsInParent().getWidth()) / 2);
		instructions.setY(message.getInstructionsY());
		add(instructions);
	}

	public void showSecondPage() {
		onSecondPage = true;
		instructions.setText(message.getSecondPage());
	}

}
