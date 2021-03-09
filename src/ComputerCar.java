import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ComputerCar extends GameMovingEntity {

    public static final String COMPUTER_CAR_KEY = "computerCar";

    public ComputerCar(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod, new AStarPathingStrategy());
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        List<Entity> entityPeople = world.getEntities().stream()
                .filter(p -> p.getID().equals("people"))
                .collect(Collectors.toList());

        List<People> peopleList = new ArrayList<>();

        for (int i = 0; i < entityPeople.size(); i++) {
            peopleList.add((People)entityPeople.get(i));
        }

        Collections.sort(peopleList);

        People ppl = peopleList.get(0);

        moveTo(world, ppl, scheduler);

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                getActionPeriod());
    }

    public boolean moveTo(WorldModel world,
                             Entity target, EventScheduler scheduler)
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
            System.out.println("no path");
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
