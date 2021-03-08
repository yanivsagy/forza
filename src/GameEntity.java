import processing.core.PImage;

import java.util.List;

public class GameEntity {

    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;


    public GameEntity(String id, Point position,
                  List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    protected String getID() {
        return id;
    }

    protected List<PImage> getImages() {
        return images;
    }

    protected Point getPosition() {
        return position;
    }

    protected void setPosition(Point p) {
        position = p;
    }

    public void nextImage()
    {
        imageIndex = (imageIndex + 1) % images.size();
    }

    public PImage getCurrentImage()
    {
        return images.get(this.imageIndex);
    }

}
