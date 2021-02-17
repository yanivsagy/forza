import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Octo extends MovingEntity {

    public static final String OCTO_KEY = "octo";
    private final int resourceLimit;
    private int resourceCount;

    public Octo(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod,
                int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    protected int getResourceLimit() {
        return resourceLimit;
    }

    protected int getResourceCount() {
        return resourceCount;
    }

    protected void setResourceCount(int c) {
        resourceCount = c;
    }

    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, new Animation(this, 0),
                getAnimationPeriod());
    }

    protected Point nextPosition(WorldModel world,
                                   Point destPos)
    {
        int horiz = Integer.signum(destPos.x - getPosition().x);
        Point newPos = new Point(getPosition().x + horiz,
                getPosition().y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.y - getPosition().y);
            newPos = new Point(getPosition().x,
                    getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = getPosition();
            }
        }

        return newPos;
    }
}
