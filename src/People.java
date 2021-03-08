//import processing.core.PImage;
//
//import java.util.List;
//import java.util.Optional;
//
//public class People extends MovingObstacle {
//
//    public People (String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
//        super(id, position, images, actionPeriod, animationPeriod, new SingleStepPathingStrategy());
//    }
//
//    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
//        scheduler.scheduleEvent(this,
//                new Activity(this, world, imageStore),
//                getActionPeriod());
//    }
//
//    public boolean moveTo(WorldModel world,
//                          Entity target, EventScheduler scheduler)
//    {
//        if (getPosition().adjacent(target.getPosition()))
//        {
//            return true;
//        }
//        else
//        {
//            Point nextPos = nextPosition(world, target.getPosition());
//
//            if (!getPosition().equals(nextPos))
//            {
//                Optional<Entity> occupant = world.getOccupant(nextPos);
//                if (occupant.isPresent())
//                {
//                    scheduler.unscheduleAllEvents(occupant.get());
//                }
//
//                world.moveEntity(this, nextPos);
//            }
//            return false;
//        }
//    }
//
//    public Point nextPosition(WorldModel world,
//                              Point destPos)
//    {
//        List<Point> points;
//
////      while (!neighbors(pos, goal))
////      {
//        points = getStrategy().computePath(getPosition(), destPos,
//                p -> world.withinBounds(p) && !world.isOccupied(p),
//                (p1, p2) -> p1.adjacent(p2),
//                PathingStrategy.CARDINAL_NEIGHBORS);
////                PathingStrategy.DIAGONAL_NEIGHBORS);
////                PathingStrategy.DIAGONAL_CARDINAL_NEIGHBORS);
//
//        if (points.size() == 0)
//        {
//            return getPosition();
//        }
//
//        Point endPoint = points.get(0);
//        endPoint.setPriorNode(null);
//        return endPoint;
//    }
//
//    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
//        scheduler.scheduleEvent(this,
//                new Activity(this, world, imageStore), getActionPeriod());
//        scheduler.scheduleEvent(this, new Animation(this, 0),
//                getAnimationPeriod());
//    }
//}
