public class Status {

	public static final int HEIGHT = 50;
    public static final int TEXT_HEIGHT = 30;
    
    public CustomText lives;
    public CustomText level;
    public CustomText points;
    
	public Status() {
        lives = new CustomText(10, 50, Controller.myUser.livesString(), false);
        lives.setX(20);
        lives.setY(TEXT_HEIGHT);
        level = new CustomText(10, 50, Controller.myUser.levelString(), true);
        level.setX(160);
        level.setY(TEXT_HEIGHT);
        points = new CustomText(10, 50, Controller.myUser.pointsString(), false);
        points.setX(260);
        points.setY(TEXT_HEIGHT);
        addToGroup();
	}
	
	private void addToGroup() {
        Controller.myLevel.getChildren().add(lives);
        Controller.myLevel.getChildren().add(points);
        Controller.myLevel.getChildren().add(level);
	}

}
