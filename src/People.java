import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class People extends NonMovingObstacle {

    public People (String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}
