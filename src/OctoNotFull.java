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
                    !moveTo(world, notFullTarget.get(), scheduler) ||
                    !transformNotFull(world, scheduler, imageStore))
            {
                scheduler.scheduleEvent(this,
                        new Activity(this, world, imageStore),
                        getActionPeriod());
            }
        }
    }

    protected boolean moveTo(WorldModel world,
                                    Entity target, EventScheduler scheduler)
    {
        if (getPosition().adjacent(target.getPosition()))
        {
            setResourceCount(getResourceCount() + 1);
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
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
}
