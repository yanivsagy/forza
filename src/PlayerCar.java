import processing.core.PImage;

import java.util.List;

public class PlayerCar extends Entity {

    public PlayerCar(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }

    public void move(int colDelta, int rowDelta) {
        this.setPosition(new Point(colDelta, rowDelta));
    }
}
