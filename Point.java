final class Point
{
   public final int x;
   public final int y;
   private double g;
   private double h;
   private double f;
   private Point priorNode;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
      this.g = 0;
      this.h = 0;
      this.f = 0;
      this.priorNode = null;
   }

   public double getG() {
      return this.g;
   }

   public void setG(double n) {
      this.g = n;
   }

   public double getH() {
      return this.h;
   }

   public void setH(double n) {
      this.h = n;
   }

   public double getF() {
      return this.f;
   }

   public void setF(double n) {
      this.f = n;
   }

   public Point getPriorNode() {
      return this.priorNode;
   }

   public void setPriorNode(Point p) {
      this.priorNode = p;
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

   public boolean adjacent(Point p)
   {
      return (x == p.x && Math.abs(y - p.y) == 1) ||
              (y == p.y && Math.abs(x - p.x) == 1);
   }

   public int distanceSquared(Point p2)
   {
      int deltaX = x - p2.x;
      int deltaY = y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }
}
