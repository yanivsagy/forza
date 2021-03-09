import processing.core.PImage;

import java.util.List;

public class PlayerCar extends Entity {

    public static final String PLAYER_CAR_KEY = "playerCarRight";

    public PlayerCar(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}
