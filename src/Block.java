/**
 * Elliott Bolzan, January 2017
 */

import javafx.animation.ScaleTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * A class designed to represent individual blocks.
 */
public class Block extends ImageView {

	private int type;
	private int hits;
	private int worth;

	/**
	 * @param type the type of the block to create (1, 2, or 3).
	 */
	public Block(int type) {
		this.type = this.hits = type;
		setWorth();
		setImage(new String(Integer.toString(type) + ".gif"));
	}

	/**
	 * @return the block's type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the hits remaining before the block is destroyed.
	 */
	public int getHits() {
		return hits;
	}

	/**
	 * @return the block's worth.
	 */
	public int getWorth() {
		return worth;
	}

	/**
	 * @return block's minX().
	 */
	public double getMinX() {
		return getBoundsInParent().getMinX();
	}
	
	/**
	 * @return block's maxX().
	 */
	public double getMaxX() {
		return getBoundsInParent().getMaxX();
	}

	/**
	 * @return block's minY().
	 */
	public double getMinY() {
		return getBoundsInParent().getMinY();
	}

	/**
	 * @return block's maxY().
	 */
	public double getMaxY() {
		return getBoundsInParent().getMaxY();
	}

	/**
	 * @return block's width.
	 */
	public double getWidth() {
		return getBoundsInParent().getWidth();
	}

	/**
	 * @return block's height.
	 */
	public double getHeight() {
		return getBoundsInParent().getHeight();
	}

	/**
	 * Set worth of block based on type.
	 */
	public void setWorth() {
		if (type == 1) {
			worth = Settings.BLOCK_1_WORTH;
		} else if (type == 2) {
			worth = Settings.BLOCK_2_WORTH;
		} else if (type == 3) {
			worth = Settings.BLOCK_3_WORTH;
		}
	}

	/**
	 * Set image for block based on a String.
	 */
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

	/**
	 * Determine whether the vertical walls were intersected.
	 * @return whether the verticals walls of the block were hit.
	 */
	public Boolean verticalWallsIntersected(Bunny bunny) {
		return bunny.getBoundsInParent().intersects(getMinX(), getMinY(), 1, getHeight())
				|| bunny.getBoundsInParent().intersects(getMaxX(), getMinY(), 1, getHeight());
	}

	/**
	 * Determine whether the horizontal walls were intersected.
	 * @return whether the horizontal walls of the block were hit.
	 */
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

	/**
	 * Called when a block is hit.
	 * Updates the hit count for the block and updates its image.
	 */
	public void gotHit() {
		hits -= 1;
		updateImage();
	}

}
