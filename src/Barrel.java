import processing.core.PImage;

import java.util.*;
import java.util.stream.Collectors;

public class Barrel extends MovingObstacle {

    public static final String BARREL_KEY = "barrel";

    public Barrel (String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod, new DijkstraPathingStrategy());
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        List<Entity> entityRoadStart = world.getEntities().stream()
                .filter(p -> p.getID().equals("roadstart"))
                .collect(Collectors.toList());

        Entity target = null;

        if (getID().endsWith("1")) {
            target = entityRoadStart.get(0);
        }
        else if (getID().endsWith("2")) {
            target = entityRoadStart.get(1);
        }
        else if (getID().endsWith("3")) {
            target = entityRoadStart.get(2);
        }
        else if (getID().endsWith("4")) {
            target = entityRoadStart.get(3);
        }

        moveTo(world, target, scheduler, imageStore);

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                getActionPeriod());
    }

    public boolean moveTo(WorldModel world,
                          Entity target, EventScheduler scheduler, ImageStore imageStore)
    {
        if (getPosition().adjacent(target.getPosition()))
        {
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

                changeDirection(nextPos, imageStore);
                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    protected void changeDirection(Point pos, ImageStore imageStore) {
        if (pos.x > getPosition().x) {
            setImages(imageStore.getImageList("barrelx"));
        }
        else if (pos.x < getPosition().x) {
            setImages(imageStore.getImageList("barrelx"));
        }
        else if (pos.y > getPosition().y) {
            setImages(imageStore.getImageList("barrel"));
        }
        else if (pos.y < getPosition().y) {
            setImages(imageStore.getImageList("barrel"));
        }
    }

    public Point nextPosition(WorldModel world,
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

//        System.out.println(points.size());

        if (points.size() == 0)
        {
            return getPosition();
        }

        Point endPoint = points.get(0);
        endPoint.setPriorNode(null);
        return endPoint;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, new Animation(this, 0),
                getAnimationPeriod());
    }
}
