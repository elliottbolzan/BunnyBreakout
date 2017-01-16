import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bunny extends ImageView {
	
    public static final String BUNNY_IMAGE = "bunny.gif";
    public static final int BUNNY_X_OFFSET = 12;
    public static final int BUNNY_Y_OFFSET = 2;

    public Boolean goingRight = true;
    public Boolean goingDown = false;
    
    public Bunny() {
		Image image = new Image(getClass().getClassLoader().getResourceAsStream(BUNNY_IMAGE));
		super.setImage(image);
		center();
	}
    
    public double bottom() {
    	return super.getY() + super.getBoundsInParent().getHeight();
    }
    
    public void center() {
    	super.setX((Controller.WIDTH - Controller.myTopHat.getWidth()) / 2 + BUNNY_X_OFFSET);
        super.setY(Controller.HEIGHT - Controller.myTopHat.OFFSET - Controller.myTopHat.getHeight() - super.getBoundsInParent().getHeight() - BUNNY_Y_OFFSET);
    }
    
}
