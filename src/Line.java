import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Line extends Rectangle {

	public static final int PADDING = 50;
    public static final int LINE_HEIGHT = 2;

	public Line() {
		super.setX(0);
		super.setY(PADDING - LINE_HEIGHT);
		super.setWidth(Controller.WIDTH);
		super.setHeight(LINE_HEIGHT);
		super.setFill(Color.TURQUOISE);
	}

}
