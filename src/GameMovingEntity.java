import processing.core.PImage;

import java.util.List;

public abstract class GameMovingEntity extends AnimatedEntity {
    private PathingStrategy strategy;

    public GameMovingEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, PathingStrategy strategy) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.strategy = strategy;
    }

    public PathingStrategy getStrategy() {
        return this.strategy;
    }

    protected abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

    protected abstract Point nextPosition(WorldModel world, Point destPos);

}
