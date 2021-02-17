public class Animation extends Action {

    public Animation(Entity entity, int repeatCount) {
        super(entity, null, null, repeatCount);
    }

    protected void executeAction(EventScheduler scheduler)
    {
        getEntity().nextImage();

        if (getRepeatCount() != 1)
        {
            scheduler.scheduleEvent(getEntity(),
                    new Animation(getEntity(),
                            Math.max(getRepeatCount() - 1, 0)),
                    ((AnimatedEntity) getEntity()).getAnimationPeriod());
        }
    }
}
