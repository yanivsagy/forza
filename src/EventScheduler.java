import java.util.*;

/*
EventScheduler: ideally our way of controlling what happens in our virtual world
 */

final class EventScheduler
{
   public PriorityQueue<Event> eventQueue;
   public Map<Entity, List<Event>> pendingEvents;
   public double timeScale;

   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }

   public void scheduleEvent(Entity entity, Action action, long afterPeriod)
   {
      long time = System.currentTimeMillis() +
              (long)(afterPeriod * timeScale);
      Event event = new Event(action, time, entity);

      eventQueue.add(event);

      // update list of pending events for the given entity
      List<Event> pending = pendingEvents.getOrDefault(entity,
              new LinkedList<>());
      pending.add(event);
      pendingEvents.put(entity, pending);
   }

   public Action createAnimationAction(Entity entity, int repeatCount)
   {
      return new Action(ActionKind.ANIMATION, entity, null, null, repeatCount);
   }

   public Action createActivityAction(Entity entity, WorldModel world,
                                             ImageStore imageStore)
   {
      return new Action(ActionKind.ACTIVITY, entity, world, imageStore, 0);
   }

   public void scheduleActions(Entity entity,
                                      WorldModel world, ImageStore imageStore)
   {
      switch (entity.kind)
      {
         case OCTO_FULL:
            scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
            scheduleEvent(entity, createAnimationAction(entity, 0),
                    entity.getAnimationPeriod());
            break;

         case OCTO_NOT_FULL:
            scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
            scheduleEvent(entity,
                    createAnimationAction(entity, 0), entity.getAnimationPeriod());
            break;

         case FISH:
            scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
            break;

         case CRAB:
            scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
            scheduleEvent(entity,
                    createAnimationAction(entity, 0), entity.getAnimationPeriod());
            break;

         case QUAKE:
            scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
            scheduleEvent(entity,
                    createAnimationAction(entity, Functions.QUAKE_ANIMATION_REPEAT_COUNT),
                    entity.getAnimationPeriod());
            break;

         case SGRASS:
            scheduleEvent(entity,
                    createActivityAction(entity, world, imageStore),
                    entity.actionPeriod);
            break;
         case ATLANTIS:
            scheduleEvent(entity,
                    createAnimationAction(entity, Functions.ATLANTIS_ANIMATION_REPEAT_COUNT),
                    entity.getAnimationPeriod());
            break;

         default:
      }
   }

   public void unscheduleAllEvents(Entity entity)
   {
      List<Event> pending = pendingEvents.remove(entity);

      if (pending != null)
      {
         for (Event event : pending)
         {
            eventQueue.remove(event);
         }
      }
   }

   public void removePendingEvent(Event event)
   {
      List<Event> pending = pendingEvents.get(event.entity);

      if (pending != null)
      {
         pending.remove(event);
      }
   }
}
