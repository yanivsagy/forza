/*
Action: ideally what our various entities might do in our virutal world
 */

final class Action
{
   public ActionKind kind;
   public Entity entity;
   public WorldModel world;
   public ImageStore imageStore;
   public int repeatCount;

   public Action(ActionKind kind, Entity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.kind = kind;
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }

   public void executeActivityAction(EventScheduler scheduler)
   {
      switch (entity.kind)
      {
         case OCTO_FULL:
            entity.executeOctoFullActivity(world,
                    imageStore, scheduler);
            break;

         case OCTO_NOT_FULL:
            entity.executeOctoNotFullActivity(world,
                    imageStore, scheduler);
            break;

         case FISH:
            entity.executeFishActivity(world, imageStore,
                    scheduler);
            break;

         case CRAB:
            entity.executeCrabActivity(world,
                    imageStore, scheduler);
            break;

         case QUAKE:
            entity.executeQuakeActivity(world, imageStore,
                    scheduler);
            break;

         case SGRASS:
            entity.executeSgrassActivity(world, imageStore,
                    scheduler);
            break;

         case ATLANTIS:
            entity.executeAtlantisActivity(world, imageStore,
                    scheduler);
            break;

         default:
            throw new UnsupportedOperationException(
                    String.format("executeActivityAction not supported for %s",
                            entity.kind));
      }
   }

   public void executeAction(EventScheduler scheduler)
   {
      switch (kind)
      {
         case ACTIVITY:
            executeActivityAction(scheduler);
            break;

         case ANIMATION:
            executeAnimationAction(scheduler);
            break;
      }
   }

   public void executeAnimationAction(EventScheduler scheduler)
   {
      entity.nextImage();

      if (repeatCount != 1)
      {
         scheduler.scheduleEvent(entity,
                 scheduler.createAnimationAction(entity,
                         Math.max(repeatCount - 1, 0)),
                 entity.getAnimationPeriod());
      }
   }

   public static void updateOnTime(EventScheduler scheduler, long time)
   {
      while (!scheduler.eventQueue.isEmpty() &&
              scheduler.eventQueue.peek().time < time)
      {
         Event next = scheduler.eventQueue.poll();

         scheduler.removePendingEvent(next);

         next.action.executeAction(scheduler);
      }
   }
}
