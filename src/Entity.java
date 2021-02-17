import java.util.List;
import java.util.Optional;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


abstract class Entity
{
   public static final String CRAB_KEY = "crab";
   public static final String CRAB_ID_SUFFIX = " -- crab";
   public static final int CRAB_PERIOD_SCALE = 4;
   public static final int CRAB_ANIMATION_MIN = 50;
   public static final int CRAB_ANIMATION_MAX = 150;
   public static final String QUAKE_KEY = "quake";
   public static final String FISH_ID_PREFIX = "fish -- ";
   public static final int FISH_CORRUPT_MIN = 20000;
   public static final int FISH_CORRUPT_MAX = 30000;
   public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
   public static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

   private final String id;
   private Point position;
   private final List<PImage> images;
   private int imageIndex;


   public Entity(String id, Point position,
      List<PImage> images)
   {
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
   }

//   public EntityKind getKind() {
//      return kind;
//   }

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
