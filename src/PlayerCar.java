import processing.core.PImage;

import java.util.List;

public class PlayerCar extends GameAnimatedEntity {

    public PlayerCar(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
    }
}
