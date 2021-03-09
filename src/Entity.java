import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


abstract class Entity
{
   private final String id;
   private Point position;
   private List<PImage> images;
   private int imageIndex;


   public Entity(String id, Point position,
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

   public void setImages(List<PImage> images) {
      this.images = images;
   }

}
