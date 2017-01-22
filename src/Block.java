import javafx.animation.ScaleTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Block extends ImageView {

	private int type;
	private int hits;
	private int worth;

	public Block(int type) {
		this.type = this.hits = type;
		setWorth();
		setImage(new String(Integer.toString(type) + ".gif"));
	}

	public int getType() {
		return type;
	}

	public int getHits() {
		return hits;
	}

	public int getWorth() {
		return worth;
	}

	public double getMinX() {
		return getBoundsInParent().getMinX();
	}

	public double getMaxX() {
		return getBoundsInParent().getMaxX();
	}

	public double getMinY() {
		return getBoundsInParent().getMinY();
	}

	public double getMaxY() {
		return getBoundsInParent().getMaxY();
	}

	public double getWidth() {
		return getBoundsInParent().getWidth();
	}

	public double getHeight() {
		return getBoundsInParent().getHeight();
	}

	public void setWorth() {
		if (type == 1) {
			worth = Settings.BLOCK_1_WORTH;
		} else if (type == 2) {
			worth = Settings.BLOCK_2_WORTH;
		} else if (type == 3) {
			worth = Settings.BLOCK_3_WORTH;
		}
	}

	public void setImage(String name) {
		setImage(new Image(getClass().getClassLoader().getResourceAsStream(name)));
	}

	/**
	 * Make blocks look broken.
	 */
	private void updateImage() {
		if (type != 1) {
			if (type == 3 && hits == 1) {
				setImage(Integer.toString(type) + "_broken_twice.gif");
			} else {
				setImage(Integer.toString(type) + "_broken.gif");
			}
		}
	}

	public Boolean verticalWallsIntersected(Bunny bunny) {
		return bunny.getBoundsInParent().intersects(getMinX(), getMinY(), 1, getHeight())
				|| bunny.getBoundsInParent().intersects(getMaxX(), getMinY(), 1, getHeight());
	}

	public Boolean horizontalWallsIntersected(Bunny bunny) {
		return bunny.getBoundsInParent().intersects(getMinX(), getMaxY(), getWidth(), 1)
				|| bunny.intersects(getMinX(), getMinY(), getWidth(), 1);
	}

	/**
	 * Called upon destruction.
	 */
	public void startShrinking() {
		final ScaleTransition shrink = new ScaleTransition();
		shrink.setNode(this);
		shrink.setFromX(1.0);
		shrink.setFromY(1.0);
		shrink.setToX(0.0);
		shrink.setToY(0.0);
		shrink.setDuration(Duration.millis(Settings.SHRINK_ANIMATION_DURATION));
		shrink.play();
	}

	public void gotHit() {
		hits -= 1;
		updateImage();
	}

}
