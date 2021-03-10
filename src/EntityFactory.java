import processing.core.PImage;

import java.util.List;

public interface EntityFactory {
    public Entity createEntity(String id, Point pos, List<PImage> images);
}
