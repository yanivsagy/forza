import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends Entity {

    private final int animationPeriod;

    public AnimatedEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    protected abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

    private void executeAnimationAction(EventScheduler scheduler)
    {
        entity.nextImage();

        if (repeatCount != 1)
        {
            scheduler.scheduleEvent(entity,
                    scheduler.createAnimationAction(entity,
                            Math.max(repeatCount - 1, 0)),
                    entity.getAnimationPeriod());
        }
    }

    protected int getAnimationPeriod() {
        return animationPeriod;
    }
}
