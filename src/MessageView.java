import javafx.scene.Group;
import javafx.scene.text.TextAlignment;

class Message {
	
	public int title_Y;
	public int instructions_Y;
	public int wrapping_width = 360;
    public String title;
    public String firstPage;
    public String secondPage;
    public Boolean justify;
    
    public Message(Boolean splash, Boolean won) {
    	if (splash) {
    		title_Y = 40;
    		instructions_Y = 90;
    		title = "Welcome to BunnyBreakout";
    	    firstPage = "Bounce your bunny off your top hat.\n\nMake it eat all the carrots, soil, and grass.\n\nTo move your top hat left and right, use the ← and → keys.\n\nLand in the middle of the top hat? Bounce in the expected direction. Land on the brims of the hat? Bounce back where you came from.\n\nPress any key to continue.";
    	    secondPage = "Here are a few cheat codes:\n\nPress the space bar to extend your top hat the length of the screen.\n\nPress 1, 2, or 3 to go to the corresponding level.\n\nPress C to remove all carrots; S to remove all soil; and G to remove all grass.\n\nPress any key to begin playing.";   
    	    justify = true;
    	}
    	else {
    		title_Y = 200;
    		instructions_Y = Settings.HEIGHT - 40;
    	    firstPage = "Press any key to play again.";
    	    justify = false;
        	if (won) {
        		title = "Congratulations! You won.";
        	}
        	else {
        		title = "Sorry, you lost.";
        	}
    	}
    }
    
}

public class MessageView extends Group {

    public CustomText instructions;
    public Boolean startGame = false;
    private Message message;

	public MessageView(Boolean splash, Boolean won) {
		message = new Message(splash, won);
		if (!splash) {
			startGame = true;
		}
		setupTitle();
		setupInstructions();
	}
	
	private void setupTitle() {
		CustomText topText = new CustomText(message.title, true);
		topText.setX((Settings.WIDTH - topText.getBoundsInParent().getWidth()) / 2);
		topText.setY(message.title_Y);
		super.getChildren().add(topText);
	}
	
	private void setupInstructions() {
		instructions = new CustomText(message.firstPage, false);
		if (message.justify) {
			instructions.setWrappingWidth(message.wrapping_width);
			instructions.setTextAlignment(TextAlignment.JUSTIFY);
		}
		instructions.setX((Settings.WIDTH - instructions.getBoundsInParent().getWidth()) / 2);
		instructions.setY(message.instructions_Y);
		super.getChildren().add(instructions);
	}
	
	public void showCheatCodes() {
		startGame = true;
		instructions.setText(message.secondPage);
	}

}
