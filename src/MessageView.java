/**
 * Elliott Bolzan, January 2017
 */

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.TextAlignment;

/**
 * Encapsulates possible messages: splash screens or results.
 */
class Message {

	private int title_Y;
	private int instructions_Y;
	private int wrapping_width = 360;
	private String title;
	private String firstPage;
	private String secondPage;
	private Boolean justify;

	/**
	 * @param splash whether this message is a splash message.
	 * @param whether the user just won.
	 */
	public Message(Boolean splash, Boolean won) {
		if (splash) {
			setupSplash();
		} else {
			setupResults(won);
		}
	}

	/**
	 * @return the Y-coordinate of the title.
	 */
	public int getTitleY() {
		return title_Y;
	}

	/**
	 * @return the Y-coordinate of the instructions.
	 */
	public int getInstructionsY() {
		return instructions_Y;
	}

	/**
	 * @return the wrapping width for the text.
	 */
	public int getWrappingWidth() {
		return wrapping_width;
	}

	/**
	 * @return the title String.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the first page text.
	 */
	public String getFirstPage() {
		return firstPage;
	}

	/**
	 * @return the second page text.
	 */
	public String getSecondPage() {
		return secondPage;
	}

	/**
	 * @return whether the text is justified.
	 */
	public Boolean getJustify() {
		return justify;
	}

	/**
	 * Sets values for message when on splash screen.
	 */
	private void setupSplash() {
		title_Y = 40;
		instructions_Y = 90;
		title = "Welcome to BunnyBreakout";
		firstPage = "Bounce your bunny off your top hat." + "\n\nMake it eat all the carrots, soil, and grass."
				+ "\n\nTo move your top hat left and right, use the ← and → keys."
				+ "\n\nLand in the middle of the top hat? Bounce in the expected direction. Land on the brims of the hat? Bounce back where you came from."
				+ "\n\nExit the screen on one side to appear on the other."
				+ "\n\nPress any key to continue.";
		secondPage = "Here are a few cheat codes:"
				+ "\n\nPress the space bar to extend your top hat the length of the screen."
				+ "\n\nPress 1, 2, or 3 to go to the corresponding level."
				+ "\n\nPress C to remove all carrots; S to remove all soil; and G to remove all grass."
				+ "\n\nPress any key to begin playing.";
		justify = true;
	}

	/**
	 * Sets values for message when displaying results.
	 */
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

/**
 * Returns a message, with an optional second page.
 */
public class MessageView extends Group {

	private CustomText instructions;
	private Boolean onSecondPage = false;
	private Message message;

	/**
	 * @param splash whether this message is a splash message.
	 * @param whether the user just won.
	 */
	public MessageView(Boolean splash, Boolean won) {
		message = new Message(splash, won);
		onSecondPage = !splash;
		setupTitle();
		setupInstructions();
	}

	/**
	 * @return whether the user is viewing the second page.
	 */
	public Boolean getOnSecondPage() {
		return onSecondPage;
	}

	/**
	 * Shortcut to add to Group.
	 */
	private void add(Node e) {
		getChildren().add(e);
	}

	/**
	 * Sets up the title label.
	 */
	private void setupTitle() {
		CustomText topText = new CustomText(message.getTitle(), true);
		topText.setX((Settings.WIDTH - topText.getBoundsInParent().getWidth()) / 2);
		topText.setY(message.getTitleY());
		super.getChildren().add(topText);
	}

	/**
	 * Sets up the instruction label.
	 */
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

	/**
	 * Trigger appearance of second page.
	 */
	public void showSecondPage() {
		onSecondPage = true;
		instructions.setText(message.getSecondPage());
	}

}
