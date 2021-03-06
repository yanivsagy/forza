import processing.core.PImage;

import java.util.List;

public class Quake extends AnimatedEntity {

    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;
    public static final String QUAKE_KEY = "quake";
    public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(Point position, List<PImage> images) {
        super(Quake.QUAKE_ID, position, images,
                Quake.QUAKE_ACTION_PERIOD, Quake.QUAKE_ANIMATION_PERIOD);
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
