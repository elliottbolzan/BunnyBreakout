import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;

public class Bunny extends ImageView {

    private Boolean goingRight;
    private Boolean goingDown = false;
    private Boolean hitsEnabled = true;
    
    public Bunny() {
		setImage(new Image(getClass().getClassLoader().getResourceAsStream(Settings.BUNNY_IMAGE)));
		goingRight = startDirection();
		center();
	}
    
    public Boolean getGoingRight() {
		return goingRight;
	}

	public Boolean getGoingDown() {
		return goingDown;
	}

	public Boolean getHitsEnabled() {
		return hitsEnabled;
	}

	public void setGoingRight(Boolean goingRight) {
		this.goingRight = goingRight;
	}

	public void setGoingDown(Boolean goingDown) {
		this.goingDown = goingDown;
	}

	public void setHitsEnabled(Boolean hitsEnabled) {
		this.hitsEnabled = hitsEnabled;
	}

	private Boolean startDirection() {
		return new Random().nextInt(2) == 0;
    }
    
    public void updateLocation(double elapsedTime, double x_speed, double y_speed) {
    	updateX(elapsedTime, x_speed);
    	updateY(elapsedTime, y_speed);
    }
        
    private void updateX(double elapsedTime, double x_speed) {
        if (goingRight) {
        	setX(getX() + x_speed * elapsedTime);
        }
        else {
        	setX(getX() - x_speed * elapsedTime);
        }
    }
    
    private void updateY(double elapsedTime, double y_speed) {
        if (goingDown) {
        	setY(getY() + y_speed * elapsedTime);
        }
        else {
        	setY(getY() - y_speed * elapsedTime);
        }
    }
    
    public void checkWalls() {
    	checkLeftWall();
    	checkRightWall();
    	checkTopWall();
    }
    
    private void checkLeftWall() {
    	if (getX() <= 0) {
    		goingRight = true;
    	}
    }

    private void checkRightWall() {
    	if (getBoundsInParent().getMaxX() >= Settings.WIDTH) {
    		goingRight = false;
    	}
    }

    private void checkTopWall() {
    	if (getY() <= Status.PADDING) {
    		goingDown = true;
    	}
    }
    
    public void center() {
    	setX(Settings.WIDTH / 2 - Settings.BUNNY_X_OFFSET);
        setY(Settings.HEIGHT - Settings.HAT_OFFSET - Settings.HAT_SIZE - getBoundsInParent().getHeight() - Settings.BUNNY_Y_OFFSET);
    }
    
	public void disableHits() {
		hitsEnabled = false;
	    Timeline timeline = new Timeline(new KeyFrame(
	            Duration.millis(Settings.HIT_TIMEOUT),
	            ae -> enableHits()));
	    timeline.play();   
	}
	
	private void enableHits() {
		hitsEnabled = true;
	}
    
}
