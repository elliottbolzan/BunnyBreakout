import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PowerUp extends ImageView {

	private int type;

	public PowerUp(int type, double x, double y) {
		setX(x);
		setY(y);
		setType(type);
	}

	public int getType() {
		return type;
	}

	public void setType(int value) {
		type = value;
		setImage();
	}

	private void setImage() {
		String path = new String("power_" + Integer.toString(type) + ".gif");
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(path));
		setImage(image);
	}

}
