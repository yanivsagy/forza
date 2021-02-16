import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoFull extends Octo {

    public OctoFull(String id, int resourceLimit, Point position, int actionPeriod, int animationPeriod,
                    List<PImage> images) {
        super(id, position, images, actionPeriod, animationPeriod, resourceLimit, resourceLimit);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(getPosition(),
                Atlantis.class);

        if (fullTarget.isPresent() &&
                moveToFull(world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            fullTarget.get().scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    scheduler.createActivityAction(this, world, imageStore),
                    getActionPeriod());
        }
    }

    private void transformFull(WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore)
    {
        OctoNotFull octo = new OctoNotFull(getID(), getResourceLimit(),
                getPosition(), getActionPeriod(), getAnimationPeriod(),
                getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        octo.scheduleActions(scheduler, world, imageStore);
    }

//    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
//        scheduler.scheduleEvent(this,
//                scheduler.createActivityAction(this, world, imageStore), getActionPeriod());
//        scheduler.scheduleEvent(this, scheduler.createAnimationAction(this, 0),
//                getAnimationPeriod());
//    }
}
