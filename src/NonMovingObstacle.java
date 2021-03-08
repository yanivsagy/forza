import processing.core.PImage;

import java.util.List;

public class NonMovingObstacle extends Entity {

    public static final String OBSTACLE_KEY = "obstacle";

    public NonMovingObstacle(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}
