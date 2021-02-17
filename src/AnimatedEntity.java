import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActionEntity {

    private final int animationPeriod;

    public AnimatedEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    protected abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

    protected int getAnimationPeriod() {
        return animationPeriod;
    }
}
