import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class People extends NonMovingObstacle implements Comparable<People> {

    public People (String id, Point position, List<PImage> images) {
        super(id, position, images);
    }

    public int compareTo(People other) {
        if (getPosition().y - other.getPosition().y > 0) return -1;
        else if(getPosition().y - other.getPosition().y < 0) return 1;
        else return 0;
    }
}
