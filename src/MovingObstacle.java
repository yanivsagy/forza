import processing.core.PImage;

import java.util.List;

public abstract class MovingObstacle extends GameMovingEntity {

    public MovingObstacle (String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, PathingStrategy strategy) {
        super(id, position, images, actionPeriod, animationPeriod, strategy);
    }
}
