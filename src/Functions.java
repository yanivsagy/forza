import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import processing.core.PImage;
import processing.core.PApplet;

/*
Functions - everything our virtual world is doing right now - is this a good design?
 */

final class Functions
{
   public static final Random rand = new Random();

   public static final String OCTO_KEY = "octo";

   public static final String OBSTACLE_KEY = "obstacle";

   public static final String FISH_KEY = "fish";

   public static final String ATLANTIS_KEY = "atlantis";

   public static final String SGRASS_KEY = "seaGrass";

   public static final String QUAKE_ID = "quake";
   public static final int QUAKE_ACTION_PERIOD = 1100;
   public static final int QUAKE_ANIMATION_PERIOD = 100;

   public static final String BGND_KEY = "background";

   public static final int PROPERTY_KEY = 0;

   public static void drawViewport(WorldView view)
   {
      view.drawBackground();
      view.drawEntities();
   }
}
