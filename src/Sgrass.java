import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Sgrass extends Entity {

    public Sgrass(String id, Point position, int actionPeriod, List<PImage> images) {
        super(id, position, images, 0, 0, actionPeriod);
    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Point> openPt = world.findOpenAround(getPosition());

        if (openPt.isPresent())
        {
            Fish fish = new Fish(FISH_ID_PREFIX + getID(),
                    openPt.get(), FISH_CORRUPT_MIN +
                    Functions.rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                    imageStore.getImageList(Functions.FISH_KEY));
            world.addEntity(fish);
            fish.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                scheduler.createActivityAction(this, world, imageStore),
                getActionPeriod());
    }

    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                scheduler.createActivityAction(this, world, imageStore), getActionPeriod());
    }
}
