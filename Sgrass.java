import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Sgrass extends ActionEntity {

    public static final String SGRASS_KEY = "seaGrass";

    public Sgrass(String id, Point position, int actionPeriod, List<PImage> images) {
        super(id, position, images, actionPeriod);
    }

    protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Point> openPt = world.findOpenAround(getPosition());

        if (openPt.isPresent())
        {
            Fish fish = new Fish(Fish.FISH_ID_PREFIX + getID(),
                    openPt.get(), Fish.FISH_CORRUPT_MIN +
                    WorldModel.rand.nextInt(Fish.FISH_CORRUPT_MAX - Fish.FISH_CORRUPT_MIN),
                    imageStore.getImageList(Fish.FISH_KEY));
            world.addEntity(fish);
            fish.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                getActionPeriod());
    }

    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore), getActionPeriod());
    }
}
