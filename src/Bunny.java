import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

public class Bunny extends ImageView {
	
    public static final String BUNNY_IMAGE = "bunny.gif";
    public static final int BUNNY_X_OFFSET = 12;
    public static final int BUNNY_Y_OFFSET = 2;

    public Boolean goingRight;
    public Boolean goingDown = false;
    
    public Bunny() {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(BUNNY_IMAGE));
		super.setImage(image);
		center();
		Random rand = new Random();
		int option = rand.nextInt(2);
		if (option == 0) {
			goingRight = true;
		}
		else {
			goingRight = false;
		}
	}
    
    public double bottom() {
    	return super.getY() + super.getBoundsInParent().getHeight();
    }
    
    public void center() {
    	super.setX((Settings.WIDTH - Controller.myTopHat.getWidth()) / 2 + BUNNY_X_OFFSET);
        super.setY(Settings.HEIGHT - Controller.myTopHat.OFFSET - Controller.myTopHat.getHeight() - super.getBoundsInParent().getHeight() - BUNNY_Y_OFFSET);
    }
    
}
