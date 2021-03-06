import java.util.List;
import processing.core.PImage;

final class Background
{
   public static final String BGND_KEY = "background";
   private String id;
   private List<PImage> images;
   private int imageIndex;

   public Background(String id, List<PImage> images)
   {
      this.id = id;
      this.images = images;
   }

   public PImage getCurrentImage()
   {
      return images.get(this.imageIndex);
   }
}
