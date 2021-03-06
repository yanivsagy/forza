import processing.core.PImage;

import java.util.List;

public class Obstacle extends Entity {

    public static final String OBSTACLE_KEY = "obstacle";

    public Obstacle(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}
