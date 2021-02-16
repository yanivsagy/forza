import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoNotFull extends Octo {

    public OctoNotFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod,
                    List<PImage> images) {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit, 0);

    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        {
            Optional<Entity> notFullTarget = world.findNearest(getPosition(),
                    Fish.class);

            if (!notFullTarget.isPresent() ||
                    !moveToNotFull(world, notFullTarget.get(), scheduler) ||
                    !transformNotFull(world, scheduler, imageStore))
            {
                scheduler.scheduleEvent(this,
                        scheduler.createActivityAction(this, world, imageStore),
                        getActionPeriod());
            }
        }
    }

    private boolean transformNotFull(WorldModel world,
                                     EventScheduler scheduler, ImageStore imageStore)
    {
        if (getResourceCount() >= getResourceLimit())
        {
            OctoFull octo = new OctoFull(getID(), getResourceLimit(),
                    getPosition(), getActionPeriod(), getAnimationPeriod(),
                    getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);
            octo.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

//    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
//        scheduler.scheduleEvent(this,
//                scheduler.createActivityAction(this, world, imageStore), getActionPeriod());
//        scheduler.scheduleEvent(this,
//                scheduler.createAnimationAction(this, 0), getAnimationPeriod());
//    }
}
