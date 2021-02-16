import processing.core.PImage;

import java.util.List;

public class Fish extends Entity {

    public Fish(String id, Point position, int actionPeriod, List<PImage> images) {
        super(id, position, images, 0, 0, actionPeriod);
    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Point pos = getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Crab crab = new Crab(getID() + CRAB_ID_SUFFIX,
                pos, getActionPeriod() / CRAB_PERIOD_SCALE,
                CRAB_ANIMATION_MIN +
                        Functions.rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
                imageStore.getImageList(CRAB_KEY));

        world.addEntity(crab);
        crab.scheduleActions(scheduler, world, imageStore);
    }


    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                scheduler.createActivityAction(this, world, imageStore), getActionPeriod());
    }
}
