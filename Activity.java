public class Activity extends Action {

    public Activity(ActionEntity entity, WorldModel world, ImageStore imageStore) {
        super(entity, world, imageStore, 0);
    }

    protected void executeAction(EventScheduler scheduler) {
            getEntity().executeActivity(getWorld(), getImageStore(), scheduler);
    }
}
