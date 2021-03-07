import java.util.List;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.HashMap;

class AStarPathingStrategy
        implements PathingStrategy
{

    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<>();
        HashMap<String, Point> openList = new HashMap<>();
        HashMap<String, Point> closedList = new HashMap<>();
        PriorityQueue<Point> queue = new PriorityQueue<>((pt1, pt2) -> {
            if (pt1.getF() - pt2.getF() > 0) return 1;
            else if (pt1.getF() - pt2.getF() < 0) return -1;
            return 0;
        });

        if (withinReach.test(start, end)) return path;

        while (!withinReach.test(start, end)) {
            if (openList.get(start.toString()) == null) openList.put(start.toString(), start);

            Point currNode = start;
            List<Point> adjacentNodes = potentialNeighbors.apply(currNode)
                    .filter(canPassThrough) //filters out obstacles/out of bounds nodes
                    .filter(pt -> closedList.get(pt.toString()) == null)    //filters out nodes in closed list
                    .map(pt -> {
                        String point = pt.toString();

                        if (openList.get(point) == null) {
                            openList.put(point, pt);
                            openList.get(point).setH(Math.abs(pt.x - end.x) + Math.abs(pt.y - end.y));
                            openList.get(point).setPriorNode(currNode);
                        }

                        if (currNode.x != openList.get(point).x && currNode.y != openList.get(point).y) {
                            double newG = openList.get(currNode.toString()).getG() + 1.4;
                            if (openList.get(point).getG() == 0
                                    || openList.get(point).getG() > newG) {
                                openList.get(point).setG(newG);
                            }
                        }
                        else {
                            double newG = openList.get(currNode.toString()).getG() + 1.0;
                            if (openList.get(point).getG() == 0
                                    || openList.get(point).getG() > newG) {
                                openList.get(point).setG(newG);
                            }
                        }

                        double g = openList.get(point).getG();
                        double h = openList.get(point).getH();
                        openList.get(point).setF(g + h);

                        return openList.get(point);
                    })
                    .collect(Collectors.toList());

            closedList.put(currNode.toString(), currNode);
            openList.remove(currNode.toString());

            queue.removeIf(pt -> closedList.get(pt.toString()) != null);
            queue.addAll(adjacentNodes);

            start = queue.poll();

            if (openList.size() == 0) break;

        }

        if (openList.size() == 0) {
            return path;
        }

        path.add(0, start);

        if (openList.get(start.toString()).getPriorNode().getPriorNode() != null) {
            Point node = openList.get(start.toString()).getPriorNode();
            path.add(0, node);
            while (closedList.get(node.toString()).getPriorNode().getPriorNode() != null) {
                node = closedList.get(node.toString()).getPriorNode();
                path.add(0, node);
            }
        }

        return path;
    }
}

