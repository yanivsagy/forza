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

   protected abstract void executeAction(EventScheduler scheduler);

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
