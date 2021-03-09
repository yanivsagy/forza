import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.stream.Collectors;

import processing.core.*;

/*
VirtualWorld is our main wrapper
It keeps track of data necessary to use Processing for drawing but also keeps track of the necessary
components to make our world run (eventScheduler), the data in our world (WorldModel) and our
current view (think virtual camera) into that world (WorldView)
 */

public final class VirtualWorld
   extends PApplet
{
   public static final int TIMER_ACTION_PERIOD = 100;

   public static final int VIEW_WIDTH = 640;
   public static final int VIEW_HEIGHT = 480;
   public static final int TILE_WIDTH = 32;
   public static final int TILE_HEIGHT = 32;
   public static final int WORLD_WIDTH_SCALE = 2;
   public static final int WORLD_HEIGHT_SCALE = 2;

   public static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   public static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   public static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   public static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   public static final String IMAGE_LIST_FILE_NAME = "imagelist";
   public static final String DEFAULT_IMAGE_NAME = "background_default";
   public static final int DEFAULT_IMAGE_COLOR = 0x808080;

   public static final String LOAD_FILE_NAME = "world.sav";

   public static final String FAST_FLAG = "-fast";
   public static final String FASTER_FLAG = "-faster";
   public static final String FASTEST_FLAG = "-fastest";
   public static final double FAST_SCALE = 0.5;
   public static final double FASTER_SCALE = 0.25;
   public static final double FASTEST_SCALE = 0.10;

   public static final String PLAYER_CAR_RIGHT = "playerCarRight";
   public static final String PLAYER_CAR_LEFT = "playerCarLeft";
   public static final String PLAYER_CAR_DOWN = "playerCarDown";
   public static final String PLAYER_CAR_UP = "playerCarUp";

   public static double timeScale = 1.0;

   private ImageStore imageStore;
   private WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;
   private Entity p1;

   private long next_time;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         Action.updateOnTime(this.scheduler, time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      view.drawViewport();
   }

   public void keyPressed()
   {
      p1 = world.getEntities().stream()
              .filter(p -> p.getID().equals("playerCarRight"))
              .collect(Collectors.toList()).get(0);

      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               p1.setImages(imageStore.getImageList(PLAYER_CAR_UP));
               break;
            case DOWN:
               dy = 1;
               p1.setImages(imageStore.getImageList(PLAYER_CAR_DOWN));
               break;
            case LEFT:
               dx = -1;
               p1.setImages(imageStore.getImageList(PLAYER_CAR_LEFT));
               break;
            case RIGHT:
               dx = 1;
               p1.setImages(imageStore.getImageList(PLAYER_CAR_RIGHT));
               break;
         }
         view.shiftView(dx, dy);



         if (!(p1.getPosition().x + dx > 39) && !(p1.getPosition().x + dx < 0)
                 && !(p1.getPosition().y + dy < 0) && !(p1.getPosition().y + dy > 29)
                  && !(world.isOccupied(new Point(p1.getPosition().x + dx, p1.getPosition().y + dy))))
         {
            p1.setPosition(new Point(p1.getPosition().x + dx, p1.getPosition().y + dy));
//            world.rotateImage(p1, p1.getPosition(), imageStore);
            world.moveEntity(p1, p1.getPosition());
         }
      }
   }

   public void mousePressed() {
      Motorcycle motor = new Motorcycle("1", new Point(mouseX / 32, mouseY / 32),
              imageStore.getImageList(WorldModel.MOTORCYCLE), 0, 0);
      world.addEntity(motor);
      OilPuddle oil1 = new OilPuddle("2",
              new Point(mouseX / 32, mouseY / 32 + 1), imageStore.getImageList(WorldModel.OIL_PUDDLE));
      world.addEntity(oil1);
      OilPuddle oil2 = new OilPuddle("3",
              new Point(mouseX / 32, mouseY / 32 - 1), imageStore.getImageList(WorldModel.OIL_PUDDLE));
      world.addEntity(oil2);
   }

   private static Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   private static PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private static void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private static void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         world.load(in, world, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   private static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getEntities())
      {
         //Only start actions for entities that include action (not those with just animations)
         if (entity instanceof ActionEntity)
               ((ActionEntity)entity).scheduleActions(scheduler, world, imageStore);
      }
   }

   private static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
