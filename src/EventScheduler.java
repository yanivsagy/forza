import java.util.*;

/*
EventScheduler: ideally our way of controlling what happens in our virtual world
 */

final class EventScheduler
{
   private final PriorityQueue<Event> eventQueue;
   private final Map<Entity, List<Event>> pendingEvents;
   private final double timeScale;

   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }

   public PriorityQueue<Event> getEventQueue() {
      return eventQueue;
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

//   public Action createAnimationAction(Entity entity, int repeatCount)
//   {
//      return new Action(ActionKind.ANIMATION, entity, null, null, repeatCount);
//   }

//   public Action createActivityAction(Entity entity, WorldModel world,
//                                             ImageStore imageStore)
//   {
//      return new Action(ActionKind.ACTIVITY, entity, world, imageStore, 0);
//   }

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
      List<Event> pending = pendingEvents.get(event.getEntity());

      if (pending != null)
      {
         pending.remove(event);
      }
   }
}
