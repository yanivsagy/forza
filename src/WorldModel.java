import processing.core.PImage;

import java.util.*;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{
   public static final Random rand = new Random();
   public static final int PROPERTY_KEY = 0;
   public static final int OCTO_NUM_PROPERTIES = 7;
   public static final int OCTO_ID = 1;
   public static final int OCTO_COL = 2;
   public static final int OCTO_ROW = 3;
   public static final int OCTO_LIMIT = 4;
   public static final int OCTO_ACTION_PERIOD = 5;
   public static final int OCTO_ANIMATION_PERIOD = 6;
   public static final int OBSTACLE_NUM_PROPERTIES = 4;
   public static final int OBSTACLE_ID = 1;
   public static final int OBSTACLE_COL = 2;
   public static final int OBSTACLE_ROW = 3;
   public static final int FISH_NUM_PROPERTIES = 5;
   public static final int FISH_ID = 1;
   public static final int FISH_COL = 2;
   public static final int FISH_ROW = 3;
   public static final int FISH_ACTION_PERIOD = 4;
   public static final int ATLANTIS_NUM_PROPERTIES = 4;
   public static final int ATLANTIS_ID = 1;
   public static final int ATLANTIS_COL = 2;
   public static final int ATLANTIS_ROW = 3;
   public static final int SGRASS_NUM_PROPERTIES = 5;
   public static final int SGRASS_ID = 1;
   public static final int SGRASS_COL = 2;
   public static final int SGRASS_ROW = 3;
   public static final int SGRASS_ACTION_PERIOD = 4;
   public static final int BGND_NUM_PROPERTIES = 4;
   public static final int BGND_ID = 1;
   public static final int BGND_COL = 2;
   public static final int BGND_ROW = 3;
   public static final int FISH_REACH = 1;

   public static final String PLAYER_CAR_RIGHT = "playerCarRight";
   public static final String PLAYER_CAR_LEFT = "playerCarLeft";
   public static final String PLAYER_CAR_DOWN = "playerCarDown";
   public static final String PLAYER_CAR_UP = "playerCarUp";
   public static final String MOTORCYCLE = "motorcycle";
   public static final String OIL_PUDDLE = "oilPuddle";
   public static final String BLACK_COMPUTER_CAR = "blackComputerCar";
   public static final String BLUE_COMPUTER_CAR = "blueComputerCar";
   public static final String GREEN_COMPUTER_CAR = "greenComputerCar";
   public static final String RED_COMPUTER_CAR = "redComputerCar";
   public static final String YELLOW_COMPUTER_CAR = "yellowComputerCar";
   public static final String FIRE = "fire";
   public static final String BARREL = "barrel";
   public static final String PEOPLE = "people";
   public static final String GRASS = "grass";

   private final int numRows;
   private final int numCols;
   private final Background[][] background;
   private final Entity[][] occupancy;
   private final Set<Entity> entities;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public int getNumRows() {
      return numRows;
   }

   public int getNumCols() {
      return numCols;
   }

   public Set<Entity> getEntities() {
      return entities;
   }

   public boolean withinBounds(Point pos)
   {
      return pos.y >= 0 && pos.y < numRows &&
              pos.x >= 0 && pos.x < numCols;
   }

   private Entity getOccupancyCell(Point pos)
   {
      return occupancy[pos.y][pos.x];
   }

   private void setOccupancyCell(Point pos, Entity entity)
   {
      occupancy[pos.y][pos.x] = entity;
   }

   private Background getBackgroundCell(Point pos)
   {
      return background[pos.y][pos.x];
   }

   private void setBackgroundCell(Point pos, Background background)
   {
      this.background[pos.y][pos.x] = background;
   }

   private void setBackground(Point pos, Background background)
   {
      if (withinBounds(pos))
      {
         setBackgroundCell(pos, background);
      }
   }

   public Optional<Entity> getOccupant(Point pos)
   {
      if (isOccupied(pos))
      {
         return Optional.of(getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   public boolean isOccupied(Point pos)
   {
      return withinBounds(pos) &&
              getOccupancyCell(pos) != null;
   }

   public void addEntity(Entity entity)
   {
      if (withinBounds(entity.getPosition()))
      {
         setOccupancyCell(entity.getPosition(), entity);
         entities.add(entity);
      }
   }

   private void tryAddEntity(Entity entity)
   {
      if (isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(entity);
   }

   private boolean parseBackground(String [] properties,
                                  WorldModel world, ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         world.setBackground(pt,
                 new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   private boolean parseOcto(String [] properties, WorldModel world,
                            ImageStore imageStore)
   {
      if (properties.length == OCTO_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[OCTO_COL]),
                 Integer.parseInt(properties[OCTO_ROW]));
         OctoNotFull octo = new OctoNotFull(properties[OCTO_ID],
                 Integer.parseInt(properties[OCTO_LIMIT]),
                 pt,
                 Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
                 Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]),
                 imageStore.getImageList(Octo.OCTO_KEY));
         world.tryAddEntity(octo);
      }

      return properties.length == OCTO_NUM_PROPERTIES;
   }

   private boolean parseObstacle(String [] properties, WorldModel world,
                                ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Obstacle obstacle = new Obstacle(properties[OBSTACLE_ID],
                 pt, imageStore.getImageList(Obstacle.OBSTACLE_KEY));
         world.tryAddEntity(obstacle);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   private boolean parseFish(String [] properties, WorldModel world,
                            ImageStore imageStore)
   {
      if (properties.length == FISH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
                 Integer.parseInt(properties[FISH_ROW]));
         Fish fish = new Fish(properties[FISH_ID],
                 pt, Integer.parseInt(properties[FISH_ACTION_PERIOD]),
                 imageStore.getImageList(Fish.FISH_KEY));
         world.tryAddEntity(fish);
      }

      return properties.length == FISH_NUM_PROPERTIES;
   }

   private boolean parseAtlantis(String [] properties, WorldModel world,
                                ImageStore imageStore)
   {
      if (properties.length == ATLANTIS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]),
                 Integer.parseInt(properties[ATLANTIS_ROW]));
         Atlantis atlantis = new Atlantis(properties[ATLANTIS_ID],
                 pt, imageStore.getImageList(Atlantis.ATLANTIS_KEY));
         world.tryAddEntity(atlantis);
      }

      return properties.length == ATLANTIS_NUM_PROPERTIES;
   }

   private boolean parseSgrass(String [] properties, WorldModel world,
                              ImageStore imageStore)
   {
      if (properties.length == SGRASS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
                 Integer.parseInt(properties[SGRASS_ROW]));
         Sgrass sgrass = new Sgrass(properties[SGRASS_ID],
                 pt,
                 Integer.parseInt(properties[SGRASS_ACTION_PERIOD]),
                 imageStore.getImageList(Sgrass.SGRASS_KEY));
         world.tryAddEntity(sgrass);
      }

      return properties.length == SGRASS_NUM_PROPERTIES;
   }

   private void parsePlayerCar(String [] properties, WorldModel world,
                                 ImageStore imageStore)
   {
      try {
            Point pt = new Point(Integer.parseInt(properties[2]),
                    Integer.parseInt(properties[3]));
            PlayerCar playerCar = new PlayerCar(properties[1],
                    pt, imageStore.getImageList(PLAYER_CAR_RIGHT));
            world.tryAddEntity(playerCar);
         }
      catch (Exception e) {
            System.out.println("Cannot make instance of PlayerCar: " + e.getMessage());
      }
   }

   private boolean parseComputerCar(String [] properties, WorldModel world,
                                 ImageStore imageStore)
   {
      try {
         Point pt = new Point(Integer.parseInt(properties[2]),
                 Integer.parseInt(properties[3]));
         ComputerCar computerCar = new ComputerCar(properties[1],
                 pt, imageStore.getImageList(properties[1]), 2, 2);
         world.tryAddEntity(computerCar);
         return true;
      }
      catch (Exception e) {
         return false;
      }

   }

//   private boolean parseBarrel(String [] properties, WorldModel world,
//                                 ImageStore imageStore)
//   {
//      try {
//         Point pt = new Point(Integer.parseInt(properties[2]),
//                 Integer.parseInt(properties[3]));
//         Barrel barrel = new Barrel(properties[0],
//                 pt, imageStore.getImageList(BARREL), 0, 0);
//         return true;
//      }
//      catch (Exception e) {
//         return false;
//      }
//   }
//
   private void parsePeople(String [] properties, WorldModel world,
                               ImageStore imageStore)
   {
      try {
         Point pt = new Point(Integer.parseInt(properties[2]),
                 Integer.parseInt(properties[3]));
         People people = new People(properties[1],
                 pt, imageStore.getImageList(PEOPLE));
         tryAddEntity(people);
      }
      catch (Exception e) {
         System.out.println("Cannot make instance of PlayerCar: " + e.getMessage());
      }

   }

   private void parseGrass(String [] properties, WorldModel world,
                            ImageStore imageStore)
   {
      try {
         Point pt = new Point(Integer.parseInt(properties[2]),
                 Integer.parseInt(properties[3]));
         Grass grass = new Grass(properties[0],
                 pt, imageStore.getImageList(GRASS));
         tryAddEntity(grass);
      }
      catch (Exception e) {
         System.out.println("Cannot make instance of PlayerCar: " + e.getMessage());
      }

   }

   private boolean processLine(String line, WorldModel world,
                                     ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[WorldModel.PROPERTY_KEY])
         {
            case Background.BGND_KEY:
               if (properties[1].equals(PEOPLE)) {
                  parsePeople(properties, world, imageStore);
               }
               else if (properties[1].equals(GRASS)) {
                  parseGrass(properties, world, imageStore);
               }
               return parseBackground(properties, world, imageStore);
            case Octo.OCTO_KEY:
               return parseOcto(properties, world, imageStore);
            case Obstacle.OBSTACLE_KEY:
               return parseObstacle(properties, world, imageStore);
            case Fish.FISH_KEY:
               return parseFish(properties, world, imageStore);
            case Atlantis.ATLANTIS_KEY:
               return parseAtlantis(properties, world, imageStore);
            case Sgrass.SGRASS_KEY:
               return parseSgrass(properties, world, imageStore);
            case PlayerCar.PLAYER_CAR_KEY:
               if (properties[1].equals(PLAYER_CAR_RIGHT)) {
                  parsePlayerCar(properties, world, imageStore);
               }
            case ComputerCar.COMPUTER_CAR_KEY:
               if (properties[1].substring(properties[1].length() - 11).equals("ComputerCar")) {
                  parseComputerCar(properties, world, imageStore);
               }

//            case BLACK_COMPUTER_CAR:
//            case BLUE_COMPUTER_CAR:
//            case GREEN_COMPUTER_CAR:
//            case YELLOW_COMPUTER_CAR:
//            case RED_COMPUTER_CAR:
//               return parseComputerCar(properties, world, imageStore);
//            case BARREL:
//               return parseBarrel(properties, world, imageStore);
//            case PEOPLE:
//               return parsePeople(properties, world, imageStore);
         }
      }

      return false;
   }

   public void load(Scanner in, WorldModel world, ImageStore imageStore)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), world, imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }
   }

   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (withinBounds(pos) && !pos.equals(oldPos))
      {
         setOccupancyCell(oldPos, null);
         removeEntityAt(pos);
         setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   public void removeEntity(Entity entity)
   {
      removeEntityAt(entity.getPosition());
   }

   public void removeEntityAt(Point pos)
   {
      if (withinBounds(pos)
              && getOccupancyCell(pos) != null)
      {
         Entity entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         entities.remove(entity);
         setOccupancyCell(pos, null);
      }
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (withinBounds(pos))
      {
         return Optional.of(getBackgroundCell(pos).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }

   private Optional<Entity> nearestEntity(List<Entity> entities,
                                                Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = nearest.getPosition().distanceSquared(pos);

         for (Entity other : entities)
         {
            int otherDistance = other.getPosition().distanceSquared(pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

   public Optional<Entity> findNearest(Point pos, Class kind)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : entities)
      {
         if (kind.isInstance(entity))
         {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }

   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -FISH_REACH; dy <= FISH_REACH; dy++)
      {
         for (int dx = -FISH_REACH; dx <= FISH_REACH; dx++)
         {
            Point newPt = new Point(pos.x + dx, pos.y + dy);
            if (withinBounds(newPt) &&
                    !isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }
}
