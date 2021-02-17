import processing.core.PImage;

import java.util.List;

public class Quake extends AnimatedEntity {

    public Quake(Point position, List<PImage> images) {
        super(Functions.QUAKE_ID, position, images,
                Functions.QUAKE_ACTION_PERIOD, Functions.QUAKE_ANIMATION_PERIOD);
    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this,
                new Animation(this, QUAKE_ANIMATION_REPEAT_COUNT),
                getAnimationPeriod());
    }
}
