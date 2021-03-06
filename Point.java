final class Point
{
   public final int x;
   public final int y;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

   public boolean adjacent(Point other)
   {
      return (this.x == other.x && Math.abs(this.y - other.y) == 1) ||
              (this.y == other.y && Math.abs(this.x - other.x) == 1);
   }

   public int distanceSquared(Point p2)
   {
      int deltaX = x - p2.x;
      int deltaY = y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }
}
