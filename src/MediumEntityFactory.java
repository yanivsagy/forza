import processing.core.PImage;
import java.util.List;

public class MediumEntityFactory implements EntityFactory {
    public Entity createEntity(String id, Point pos, List<PImage> images) {
        if (id.equals("playerCarRight")) {
            return new PlayerCar(id, pos, images);
        }
        else if (id.endsWith("ComputerCarRight")) {
            return new ComputerCar(id, pos, images, 200, 200);
        }
        else if (id.equals("motorcycle")) {
            return new Motorcycle(id, pos, images, 200, 200);
        }
        else if (id.equals("barrel")) {
            return new Barrel(id, pos, images, 200, 200);
        }
        else if (id.equals("people")) {
            return new People(id, pos, images);
        }
        else if (id.equals("grass")) {
            return new Grass(id, pos, images);
        }
        else if (id.equals("roadstart")) {
            return new RoadStart(id, pos, images);
        }
        else {
            return new OilPuddle(id, pos, images);
        }
    }
}
