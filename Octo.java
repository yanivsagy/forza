import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Octo extends MovingEntity {

    public static final String OCTO_KEY = "octo";
    private final int resourceLimit;
    private int resourceCount;

    public Octo(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod,
                int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animationPeriod, new AStarPathingStrategy());
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

    protected void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, new Animation(this, 0),
                getAnimationPeriod());
    }

    protected Point nextPosition(WorldModel world,
                                   Point destPos)
    {
        List<Point> points;

//      while (!neighbors(pos, goal))
//      {
        points = getStrategy().computePath(getPosition(), destPos,
                p -> world.withinBounds(p) && !world.isOccupied(p),
                (p1, p2) -> p1.adjacent(p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
//                PathingStrategy.DIAGONAL_NEIGHBORS);
//                PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);

        if (points.size() == 0)
        {
            return getPosition();
        }

        Point endPoint = points.get(0);
        endPoint.setPriorNode(null);
        return endPoint;
    }
}
