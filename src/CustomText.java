/**
 * Elliott Bolzan, January 2017
 */

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * A class that creates a specific text object.
 * Used for stylistic purposes and uniformness of design.
 */
public class CustomText extends Text {

	public CustomText(double x, double y, String text, Boolean bold) {
		this(text, bold);
		setX(x);
		setY(y);
	}

	/**
	 * Constructor provided in case (X, Y) coordinates depend on text width.
	 */
	public CustomText(String text, Boolean bold) {
		setText(text);
		setStyle(bold);
	}

	/**
	 * Set whether text should be bold.
	 * @param whether the text should be bold or not.
	 */
	private void setStyle(Boolean bold) {
		if (bold) {
			setFont(Font.font(Settings.FONT, FontWeight.BOLD, Settings.FONT_SIZE));
		} else {
			setFont(Font.font(Settings.FONT, Settings.FONT_SIZE));
		}
		setFill(Settings.WHITE);
	}

}
