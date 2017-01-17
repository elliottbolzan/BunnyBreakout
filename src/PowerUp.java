import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PowerUp extends ImageView {
	
	public int type;

	public PowerUp(double x, double y) {
		super.setX(x);
		super.setY(y);
	}
	
	public void setType(int value) {
		type = value;
		String path = new String("power_" + Integer.toString(type) + ".gif");
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(path));
		super.setImage(image);
	}
	
}
