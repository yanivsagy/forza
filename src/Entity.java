import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


final class Entity
{
   public static final String CRAB_KEY = "crab";
   public static final String CRAB_ID_SUFFIX = " -- crab";
   public static final int CRAB_PERIOD_SCALE = 4;
   public static final int CRAB_ANIMATION_MIN = 50;
   public static final int CRAB_ANIMATION_MAX = 150;
   public static final String QUAKE_KEY = "quake";
   public static final String FISH_ID_PREFIX = "fish -- ";
   public static final int FISH_CORRUPT_MIN = 20000;
   public static final int FISH_CORRUPT_MAX = 30000;
   public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
   public static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

   public EntityKind kind;
   public String id;
   public Point position;
   public List<PImage> images;
   public int imageIndex;
   public int resourceLimit;
   public int resourceCount;
   public int actionPeriod;
   public int animationPeriod;

   public Entity(EntityKind kind, String id, Point position,
      List<PImage> images, int resourceLimit, int resourceCount,
      int actionPeriod, int animationPeriod)
   {
      this.kind = kind;
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }

   public int getAnimationPeriod()
   {
      switch (kind)
      {
         case OCTO_FULL:
         case OCTO_NOT_FULL:
         case CRAB:
         case QUAKE:
         case ATLANTIS:
            return animationPeriod;
         default:
            throw new UnsupportedOperationException(
                    String.format("getAnimationPeriod not supported for %s",
                            kind));
      }
   }

   public void nextImage()
   {
      imageIndex = (imageIndex + 1) % images.size();
   }

   public PImage getCurrentImage()
   {
      return images.get((this).imageIndex);
   }

   public boolean transformNotFull(WorldModel world,
                                          EventScheduler scheduler, ImageStore imageStore)
   {
      if (resourceCount >= resourceLimit)
      {
         Entity octo = world.createOctoFull(id, resourceLimit,
                 position, actionPeriod, animationPeriod,
                 images);

         world.removeEntity(this);
         scheduler.unscheduleAllEvents(this);

         world.addEntity(octo);
         octo.scheduleActions(scheduler, world, imageStore);

         return true;
      }

      return false;
   }

   public void transformFull(WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore)
   {
      Entity octo = world.createOctoNotFull(id, resourceLimit,
              position, actionPeriod, animationPeriod,
              images);

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      world.addEntity(octo);
      octo.scheduleActions(scheduler, world, imageStore);
   }

   public boolean moveToNotFull(WorldModel world,
                                       Entity target, EventScheduler scheduler)
   {
      if (position.adjacent(target.position))
      {
         resourceCount += 1;
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);

         return true;
      }
      else
      {
         Point nextPos = nextPositionOcto(world, target.position);

         if (!position.equals(nextPos))
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

   public boolean moveToFull(WorldModel world,
                                    Entity target, EventScheduler scheduler)
   {
      if (position.adjacent(target.position))
      {
         return true;
      }
      else
      {
         Point nextPos = nextPositionOcto(world, target.position);

         if (!position.equals(nextPos))
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

   public Point nextPositionOcto(WorldModel world,
                                        Point destPos)
   {
      int horiz = Integer.signum(destPos.x - position.x);
      Point newPos = new Point(position.x + horiz,
              position.y);

      if (horiz == 0 || world.isOccupied(newPos))
      {
         int vert = Integer.signum(destPos.y - position.y);
         newPos = new Point(position.x,
                 position.y + vert);

         if (vert == 0 || world.isOccupied(newPos))
         {
            newPos = position;
         }
      }

      return newPos;
   }

   public boolean moveToCrab(WorldModel world,
                                    Entity target, EventScheduler scheduler)
   {
      if (position.adjacent(target.position))
      {
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);
         return true;
      }
      else
      {
         Point nextPos = nextPositionCrab(world, target.position);

         if (!position.equals(nextPos))
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

   public Point nextPositionCrab(WorldModel world,
                                        Point destPos)
   {
      int horiz = Integer.signum(destPos.x - position.x);
      Point newPos = new Point(position.x + horiz,
              position.y);

      Optional<Entity> occupant = world.getOccupant(newPos);

      if (horiz == 0 ||
              (occupant.isPresent() && !(occupant.get().kind == EntityKind.FISH)))
      {
         int vert = Integer.signum(destPos.y - position.y);
         newPos = new Point(position.x, position.y + vert);
         occupant = world.getOccupant(newPos);

         if (vert == 0 ||
                 (occupant.isPresent() && !(occupant.get().kind == EntityKind.FISH)))
         {
            newPos = position;
         }
      }

      return newPos;
   }

   public void executeOctoFullActivity(WorldModel world,
                                              ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> fullTarget = world.findNearest(position,
              EntityKind.ATLANTIS);

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
                 actionPeriod);
      }
   }

   public void executeOctoNotFullActivity(WorldModel world, ImageStore imageStore,
                                          EventScheduler scheduler)
   {
      Optional<Entity> notFullTarget = world.findNearest(position,
              EntityKind.FISH);

      if (!notFullTarget.isPresent() ||
              !moveToNotFull(world, notFullTarget.get(), scheduler) ||
              !transformNotFull(world, scheduler, imageStore))
      {
         scheduler.scheduleEvent(this,
                 scheduler.createActivityAction(this, world, imageStore),
                 actionPeriod);
      }
   }

   public void executeFishActivity(WorldModel world,
                                          ImageStore imageStore, EventScheduler scheduler)
   {
      Point pos = position;  // store current position before removing

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      Entity crab = world.createCrab(id + CRAB_ID_SUFFIX,
              pos, actionPeriod / CRAB_PERIOD_SCALE,
              CRAB_ANIMATION_MIN +
                      Functions.rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
              imageStore.getImageList(CRAB_KEY));

      world.addEntity(crab);
      crab.scheduleActions(scheduler, world, imageStore);
   }

   public void executeCrabActivity(WorldModel world,
                                          ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Entity> crabTarget = world.findNearest(
              position, EntityKind.SGRASS);
      long nextPeriod = actionPeriod;

      if (crabTarget.isPresent())
      {
         Point tgtPos = crabTarget.get().position;

         if (moveToCrab(world, crabTarget.get(), scheduler))
         {
            Entity quake = world.createQuake(tgtPos,
                    imageStore.getImageList(QUAKE_KEY));

            world.addEntity(quake);
            nextPeriod += actionPeriod;
            quake.scheduleActions(scheduler, world, imageStore);
         }
      }

      scheduler.scheduleEvent(this,
              scheduler.createActivityAction(this, world, imageStore),
              nextPeriod);
   }

   public void executeQuakeActivity(WorldModel world,
                                           ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(this);
      world.removeEntity(this);
   }

   public void executeAtlantisActivity(WorldModel world,
                                              ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(this);
      world.removeEntity(this);
   }

   public void executeSgrassActivity(WorldModel world,
                                            ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Point> openPt = world.findOpenAround(position);

      if (openPt.isPresent())
      {
         Entity fish = world.createFish(FISH_ID_PREFIX + id,
                 openPt.get(), FISH_CORRUPT_MIN +
                         Functions.rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                 imageStore.getImageList(Functions.FISH_KEY));
         world.addEntity(fish);
         fish.scheduleActions(scheduler, world, imageStore);
      }

      scheduler.scheduleEvent(this,
              scheduler.createActivityAction(this, world, imageStore),
              actionPeriod);
   }

   public void scheduleActions(EventScheduler scheduler,
      WorldModel world, ImageStore imageStore)
   {
      switch (kind)
      {
      case OCTO_FULL:
         scheduler.scheduleEvent(this,
            scheduler.createActivityAction(this, world, imageStore),
            actionPeriod);
         scheduler.scheduleEvent(this, scheduler.createAnimationAction(this, 0),
                 getAnimationPeriod());
         break;

      case OCTO_NOT_FULL:
         scheduler.scheduleEvent(this,
            scheduler.createActivityAction(this, world, imageStore),
            actionPeriod);
         scheduler.scheduleEvent(this,
            scheduler.createAnimationAction(this, 0), getAnimationPeriod());
         break;

      case FISH:
         scheduler.scheduleEvent(this,
            scheduler.createActivityAction(this, world, imageStore),
            actionPeriod);
         break;

      case CRAB:
         scheduler.scheduleEvent(this,
            scheduler.createActivityAction(this, world, imageStore),
            actionPeriod);
         scheduler.scheduleEvent(this,
            scheduler.createAnimationAction(this, 0), getAnimationPeriod());
         break;

      case QUAKE:
         scheduler.scheduleEvent(this,
            scheduler.createActivityAction(this, world, imageStore),
            actionPeriod);
         scheduler.scheduleEvent(this,
            scheduler.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT),
                 getAnimationPeriod());
         break;

      case SGRASS:
         scheduler.scheduleEvent(this,
            scheduler.createActivityAction(this, world, imageStore),
            actionPeriod);
         break;
      case ATLANTIS:
         scheduler.scheduleEvent(this,
                    scheduler.createAnimationAction(this, ATLANTIS_ANIMATION_REPEAT_COUNT),
                 this.getAnimationPeriod());
            break;

      default:
      }
   }
}
