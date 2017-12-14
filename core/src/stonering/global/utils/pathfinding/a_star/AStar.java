package stonering.global.utils.pathfinding.a_star;

import stonering.game.core.model.LocalMap;
import stonering.global.utils.Position;
import stonering.global.utils.pathfinding.jps.Node;
import stonering.objects.common.Path;

import java.util.Iterator;
import java.util.LinkedList;

public class AStar {
    private LocalMap localMap;
    private LinkedList<Node> nodes;
    private Position target;
    private Position start;

    public AStar(LocalMap localMap) {
        this.localMap = localMap;
    }

    public Path findPath(Position start, Position target) {
        this.start = start;
        this.target = target;
        Node startNode = new Node(start, null);
        nodes.add(startNode);
        while (true) {
            Node current = nodes.remove(0);
            if (current.getPosition().equals(target)) {
                return makePath(current);
            }
            addNeighbors(current);
        }
    }

    private void addNeighbors(Node node) {
        for (int z = -1; z < 2; z++) {
            for (int y = -1; y < 2; y++) {
                for (int x = -1; x < 2; x++) {
                    if (x != 0 && y != 0 && z != 0)
                        if (inMap(node.getX() + x, node.getY() + y, node.getZ() + z)
                                && localMap.isPassable(node.getX() + x, node.getY() + y, node.getZ() + z))
                            addNodeWithSort(new Node(new Position(node.getX() + x, node.getY() + y, node.getZ() + z), node));
                }
            }
        }
    }

    private void addNodeWithSort(Node node) {
        Iterator<Node> iterator = nodes.iterator();
        float nodeHeuristic = getHeurictic(node.getPosition());
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            if (getHeurictic(iterator.next().getPosition()) > nodeHeuristic) {
                nodes.add(i, node);
                break;
            }
        }
    }

    private Path makePath(Node node) {
        Path path = new Path();
        while (node.getParent() != null) {
            path.addPoint(node.getPosition());
            node = node.getParent();
        }
        return path;
    }

    private boolean inMap(int x, int y, int z) {
        return !(x < 0 || y < 0 || z < 0 ||
                localMap.getxSize() >= x ||
                localMap.getySize() >= y ||
                localMap.getzSize() >= z);
    }

    private float getHeurictic(Position cur) {
        return (float) Math.sqrt(Math.pow(cur.getX() - target.getX(), 2) +
                Math.pow(cur.getY() - target.getY(), 2) +
                Math.pow(cur.getZ() - target.getZ(), 2));
    }
}
