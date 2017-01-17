import javafx.scene.shape.Rectangle;

public class Status {

    public static final int TEXT_HEIGHT = 30;
    public static final int LINE_HEIGHT = 2;
	public static final int PADDING = 50;
	public static final int LIVES_X = 20;
	public static final int LEVEL_X = 160;
	public static final int POINTS_X = 260;
    
    public CustomText lives;
    public CustomText level;
    public CustomText points;
    
    private Rectangle line;
    
	public Status() {
        lives = new CustomText(LIVES_X, TEXT_HEIGHT, Controller.myUser.livesString(), false);
        level = new CustomText(LEVEL_X, TEXT_HEIGHT, Controller.myUser.levelString(), true);
        points = new CustomText(POINTS_X, TEXT_HEIGHT, Controller.myUser.pointsString(), false);
        line = new Rectangle(0, PADDING - LINE_HEIGHT, Settings.WIDTH, LINE_HEIGHT);
        line.setFill(Settings.SECONDARY_COLOR);
        showStatus();
	}
	
	private void showStatus() {
        Controller.myLevel.getChildren().add(lives);
        Controller.myLevel.getChildren().add(points);
        Controller.myLevel.getChildren().add(level);
        Controller.myLevel.getChildren().add(line);
	}
	
}
