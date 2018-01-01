package stonering.global.utils.pathfinding.a_star;

import javafx.geometry.Pos;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.LocalMap;
import stonering.global.utils.Position;
import stonering.global.utils.pathfinding.NoPathException;
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
        nodes = new LinkedList<>();
    }

    public Path findPath(Position start, Position target) throws NoPathException {
        this.start = start;
        this.target = target;
        Node startNode = new Node(start, null);
        nodes.add(startNode);
        while (true) {
            if (nodes.isEmpty()) {
                throw new NoPathException();
            }
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
                    if (x != 0 || y != 0 || z != 0) {
                        if (inMap(node.getX() + x, node.getY() + y, node.getZ() + z)) {
                            Position newPos = new Position(node.getX() + x, node.getY() + y, node.getZ() + z);
                            if (hasPathBetween(node.getPosition(), newPos)) {
                                addNodeWithSort(new Node(newPos, node));
                                System.out.println(nodes.size());
                            }
                        }
                    }
                }
            }
        }
    }

    // cells should be adjacent
    private boolean hasPathBetween(Position pos1, Position pos2) {
        boolean passable1 = BlockTypesEnum.getType(localMap.getBlockType(pos1)).getPassing() == 2;
        boolean passable2 = BlockTypesEnum.getType(localMap.getBlockType(pos2)).getPassing() == 2;
        boolean sameLevel = pos1.getZ() == pos2.getZ();
        boolean lowRamp = BlockTypesEnum.getType(localMap.getBlockType(lowerOf(pos1, pos2))) == BlockTypesEnum.RAMP;
        return (passable1 && passable2 && (sameLevel || lowRamp));
//        return (BlockTypesEnum.getType(localMap.getBlockType(pos1)).getPassing() == 2 &&
//                BlockTypesEnum.getType(localMap.getBlockType(pos2)).getPassing() == 2) &&
//                (pos1.getZ() == pos2.getZ() ||
//                        BlockTypesEnum.getType(localMap.getBlockType(lowerOf(pos1, pos2))) == BlockTypesEnum.RAMP);
    }

    private void addNodeWithSort(Node node) {
        Iterator<Node> iterator = nodes.iterator();
        float nodeHeuristic = getHeurictic(node.getPosition());
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            if (getHeurictic(iterator.next().getPosition()) > nodeHeuristic) {
                nodes.add(i, node);
                return;
            }
        }
        nodes.add(node);
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
                x >= localMap.getxSize() ||
                y >= localMap.getySize() ||
                z >= localMap.getzSize());
    }

    private float getHeurictic(Position cur) {
        return (float) Math.sqrt(Math.pow(cur.getX() - target.getX(), 2) +
                Math.pow(cur.getY() - target.getY(), 2) +
                Math.pow(cur.getZ() - target.getZ(), 2));
    }

    private Position lowerOf(Position pos1, Position pos2) {
        return pos1.getZ() < pos2.getZ() ? pos1 : pos2;
    }
}
