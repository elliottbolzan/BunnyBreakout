import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CustomText extends Text {
	
	public CustomText(double x, double y, String text, Boolean bold) {
		this.setX(x);
		this.setY(y);
		initialSetup(text, bold);
	}
	
	public CustomText(String text, Boolean bold) {
		initialSetup(text, bold);
	}
	
	private void initialSetup(String text, Boolean bold) {
		this.setText(text);
		if (bold) {
			this.setFont(Font.font(Settings.FONT, FontWeight.BOLD, Settings.FONT_SIZE));
		}
		else {
			this.setFont(Font.font(Settings.FONT, Settings.FONT_SIZE));
		}
        this.setFill(Settings.WHITE);
	}

}
