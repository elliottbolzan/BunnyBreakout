import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Block extends ImageView {
	
	public static final int TYPE_1_WORTH = 15;
	public static final int TYPE_2_WORTH = 10;
	public static final int TYPE_3_WORTH = 5;
	
	public int type;
	public int hits;
	public int worth;
	
	public Block(int typeNumber) {
		type = typeNumber;
		hits = type;
		setWorth();
		String path = new String(Integer.toString(type) + ".gif");
		super.setImage(new Image(path));
	}
	
	public void setWorth() {
		if (type == 1) {
			worth = TYPE_1_WORTH;
		}
		else if (type == 2) {
			worth = TYPE_2_WORTH;
		}
		else if (type == 3) {
			worth = TYPE_3_WORTH;
		}
	}
	
	public Boolean verticalWallsIntersected(Bunny bunny) {
		if (bunny.getBoundsInParent().intersects(super.getBoundsInParent().getMinX(), super.getBoundsInParent().getMinY(), 1, super.getBoundsInParent().getHeight())
				|| bunny.getBoundsInParent().intersects(super.getBoundsInParent().getMaxX(), super.getBoundsInParent().getMinY(), 1, super.getBoundsInParent().getHeight())) {
			return true;
		}
		return false;
	}
	
	public Boolean horizontalWallsIntersected(Bunny bunny) {
		if (bunny.getBoundsInParent().intersects(super.getBoundsInParent().getMinX(), super.getBoundsInParent().getMinY(), super.getBoundsInParent().getWidth(), 1)
				|| bunny.getBoundsInParent().intersects(super.getBoundsInParent().getMinX(), super.getBoundsInParent().getMaxY(), super.getBoundsInParent().getWidth(), 1)) {
			return true;
		}
		return false;
	}

}
