import processing.core.PImage;

import java.util.List;

public class NonMovingObstacle extends GameEntity {

    public static final String OBSTACLE_KEY = "obstacle";

    public NonMovingObstacle(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}
