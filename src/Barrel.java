import processing.core.PImage;

import java.util.*;
import java.util.stream.Collectors;

public class Barrel extends MovingObstacle {

    public static final String BARREL_KEY = "barrel";

    public Barrel (String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod, new DijkstraPathingStrategy());
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        List<Entity> entityPeople = world.getEntities().stream()
                .filter(p -> p.getID().equals("people"))
                .collect(Collectors.toList());

        List<People> peopleList = new ArrayList<>();

        for (int i = 0; i < entityPeople.size(); i++) {
            peopleList.add((People)entityPeople.get(i));
        }

        Comparator<People> compPplX = (p1, p2) -> {
            if (p1.getPosition().x - p2.getPosition().x > 0) return -1;
            else if (p1.getPosition().x - p2.getPosition().x < 0) return 1;
            else return 0;
        };

        Comparator<People> compPplY = (p1, p2) -> {
            if (p1.getPosition().y - p2.getPosition().y > 0) return 1;
            else if (p1.getPosition().y - p2.getPosition().y < 0) return -1;
            else return 0;
        };

        Collections.sort(peopleList, compPplX.thenComparing(compPplY));

        moveTo(world, peopleList.get(0), scheduler, imageStore);

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

                world.moveEntity(this, nextPos);
            }
            return false;
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

        if (points.size() == 0)
        {
            return getPosition();
        }

        System.out.println(points.get(0));

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
