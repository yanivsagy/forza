/*
Action: ideally what our various entities might do in our virutal world
 */

abstract class Action
{
   private final ActionEntity entity;
   private final WorldModel world;
   private final ImageStore imageStore;
   private final int repeatCount;

   public Action(ActionEntity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }

   protected ActionEntity getEntity() {
      return entity;
   }

   protected int getRepeatCount() {
      return repeatCount;
   }

   protected WorldModel getWorld() {
      return world;
   }

   protected ImageStore getImageStore() {
      return imageStore;
   }

   //   private void executeActivityAction(EventScheduler scheduler)
//   {
//      switch (entity.getKind())
//      {
//         case OCTO_FULL:
//            entity.executeOctoFullActivity(world,
//                    imageStore, scheduler);
//            break;
//
//         case OCTO_NOT_FULL:
//            entity.executeOctoNotFullActivity(world,
//                    imageStore, scheduler);
//            break;
//
//         case FISH:
//            entity.executeFishActivity(world, imageStore,
//                    scheduler);
//            break;
//
//         case CRAB:
//            entity.executeCrabActivity(world,
//                    imageStore, scheduler);
//            break;
//
//         case QUAKE:
//            entity.executeQuakeActivity(world, imageStore,
//                    scheduler);
//            break;
//
//         case SGRASS:
//            entity.executeSgrassActivity(world, imageStore,
//                    scheduler);
//            break;
//
//         case ATLANTIS:
//            entity.executeAtlantisActivity(world, imageStore,
//                    scheduler);
//            break;
//
//         default:
//            throw new UnsupportedOperationException(
//                    String.format("executeActivityAction not supported for %s",
//                            entity.getKind()));
//      }
//   }

   protected abstract void executeAction(EventScheduler scheduler);
//   {
//      switch (kind)
//      {
//         case ACTIVITY:
//            executeActivityAction(scheduler);
//            break;
//
//         case ANIMATION:
//            executeAnimationAction(scheduler);
//            break;
//      }
//   }

//   private void executeAnimationAction(EventScheduler scheduler)
//   {
//      entity.nextImage();
//
//      if (repeatCount != 1)
//      {
//         scheduler.scheduleEvent(entity,
//                 new Animation(entity,
//                         Math.max(repeatCount - 1, 0)),
//                 entity.getAnimationPeriod());
//      }
//   }

   public static void updateOnTime(EventScheduler scheduler, long time)
   {
      while (!scheduler.getEventQueue().isEmpty() &&
              scheduler.getEventQueue().peek().getTime() < time)
      {
         Event next = scheduler.getEventQueue().poll();

         scheduler.removePendingEvent(next);

         next.getAction().executeAction(scheduler);
      }
   }
}
