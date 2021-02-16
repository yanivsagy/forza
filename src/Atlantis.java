import processing.core.PImage;

import java.util.List;

public class Atlantis extends AnimatedEntity {

    public Atlantis(String id, Point position, List<PImage> images) {
        super(id, position, images, 0, 0);
    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                scheduler.createAnimationAction(this, ATLANTIS_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }
}
