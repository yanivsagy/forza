import processing.core.PImage;

import java.util.List;

public abstract class GameActionEntity extends GameEntity {

    private final int actionPeriod;

    public GameActionEntity(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    protected abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

    protected int getActionPeriod() {
        return actionPeriod;
    }
}
