import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CustomText extends Text {
	
	public CustomText(double x, double y, String text, Boolean bold) {
		super(x, y, text);
		if (bold) {
			super.setFont(Font.font(Controller.FONT, FontWeight.BOLD, Controller.FONT_SIZE));
		}
		else {
			super.setFont(Font.font(Controller.FONT, Controller.FONT_SIZE));
		}
        super.setFill(Color.FLORALWHITE);
	}

}
