import processing.core.PImage;

import java.util.List;

public abstract class MovingEntity extends AnimatedEntity {

    public MovingEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }

    protected abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    protected abstract Point nextPosition(WorldModel world, Point destPos);
}
