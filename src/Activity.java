public class Activity extends Action {

    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        super(entity, world, imageStore, 0);
    }

    protected void executeAction(EventScheduler scheduler) {
        if (getEntity() instanceof ActionEntity)
            ((ActionEntity)getEntity()).executeActivity(getWorld(), getImageStore(), scheduler);
    }
}
