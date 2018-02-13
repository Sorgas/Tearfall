package stonering.global.utils.pathfinding.a_star;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.LocalMap;
import stonering.global.utils.HashPriorityQueue;
import stonering.global.utils.Position;

import java.util.*;

public class AStar {
    private LocalMap localMap;
    private LinkedList<Node> nodes;
    private Position target;

    public AStar(LocalMap localMap) {
        this.localMap = localMap;
        nodes = new LinkedList<>();
    }

    // The maximum number of completed nodes. After that number the algorithm returns null.
    // If negative, the search will run until the goal node is found.
    private int maxSteps = 10000;

    public Node bestNodeAfterSearch;

    /**
     * Returns the shortest Path from a start node to an end node according to
     * the A* heuristics (h must not overestimate). initialNode and last found node included.
     */
    public ArrayList<Position> makeShortestPath(Position initialPos, Position targetPos) {
        Node initialNode = new Node(initialPos, targetPos);
        //perform search and save the
        Node endNode = search(initialNode, targetPos);
        if (endNode == null)
            return null;
        //return shortest path according to AStar heuristics
        ArrayList<Node> nodePath = AStar.makePath(endNode);
        ArrayList<Position> path = new ArrayList<>();
        nodePath.forEach(node -> path.add(node.getPosition()));
        return path;
    }

    /**
     * @param initialNode start of the search
     * @param targetPos    end of the search
     * @return goal node from which you can reconstruct the path
     */
    private Node search(Node initialNode, Position targetPos) {
        HashPriorityQueue<Node, Node> openSet = new HashPriorityQueue(new NodeComparator());
        HashMap<Integer, Node> closedSet = new HashMap<>();

        // current iteration of the search
        int numSearchSteps = 0;

        openSet.add(initialNode, initialNode);
        while (openSet.size() > 0 && (maxSteps < 0 || numSearchSteps < maxSteps)) {
            //get element with the least sum of costs
            Node currentNode = openSet.poll();

            //path is complete
            if (targetPos.equals(currentNode.getPosition())) {
                this.bestNodeAfterSearch = currentNode;
                return currentNode;
            }

            //get successor nodes
            ArrayList<Node> successorNodes = getSuccessors(currentNode, targetPos);

            //process successor nodes
            for (Node successorNode : successorNodes) {
                // node already closed, skip
                if (closedSet.containsValue(successorNode)) {
                    continue;
                }

                //compute tentativeG
                int pathLength = currentNode.getPathLength() + 1;

                /* Special rule for nodes that are generated within other nodes:
                 * We need to ensure that we use the node and
                 * its g value from the openSet if its already discovered
                 */
                Node discSuccessorNode = openSet.get(successorNode);
                boolean inOpenSet;
                if (discSuccessorNode != null) {
                    successorNode = discSuccessorNode;
                    inOpenSet = true;

                    if (inOpenSet && pathLength >= successorNode.getPathLength())
                        continue;

                } else {
                    inOpenSet = false;
                }
                //node was already discovered and this path is worse than the last one
                successorNode.setParent(currentNode);
                if (inOpenSet) {
                    // if successorNode is already in data structure it has to be inserted again to regain the order
                    openSet.remove(successorNode, successorNode);
                }
                successorNode.setPathLength(pathLength);
                openSet.add(successorNode, successorNode);
            }
            closedSet.put(currentNode.hashCode(), currentNode);
            numSearchSteps += 1;
        }

        closedSet.values().forEach((node -> {
            if(bestNodeAfterSearch == null || bestNodeAfterSearch.getPathLength() < node.getPathLength()) {
                bestNodeAfterSearch = node;
            }
        }));
        return bestNodeAfterSearch;
    }

    /**
     * returns path from the earliest ancestor to the node in the argument
     * if the parents are set via AStar search, it will return the path found.
     * This is the shortest shortest path, if the heuristic h does not overestimate
     * the true remaining costs
     *
     * @param node node from which the parents are to be found. Parents of the node should
     *             have been properly set in preprocessing (f.e. AStar.search)
     * @return path to the node in the argument
     */
    private static ArrayList<Node> makePath(Node node) {
        ArrayList<Node> path = new ArrayList<Node>();
        path.add(node);
        Node currentNode = node;
        while (currentNode.getParent() != null) {
            Node parent = currentNode.getParent();
            path.add(0, parent);
            currentNode = parent;
        }
        return path;
    }

    private ArrayList<Node> getSuccessors(Node node, Position target) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (int z = -1; z < 2; z++) {
            for (int y = -1; y < 2; y++) {
                for (int x = -1; x < 2; x++) {
                    if (x != 0 || y != 0 || z != 0) {
                        if (inMap(node.getPosition().getX() + x, node.getPosition().getY() + y, node.getPosition().getZ() + z)) {
                            Position newPos = new Position(node.getPosition().getX() + x, node.getPosition().getY() + y, node.getPosition().getZ() + z);
                            if (hasPathBetween(node.getPosition(), newPos)) {
                                nodes.add(new Node(newPos, target));
                            }
                        }
                    }
                }
            }
        }
        return nodes;
    }

    private static class NodeComparator implements Comparator<Node> {
        public int compare(Node node1, Node node2) {
            return Double.compare(node1.getCost(), node2.getCost());
        }
    }

    private boolean hasPathBetween(Position pos1, Position pos2) {
        boolean passable1 = BlockTypesEnum.getType(localMap.getBlockType(pos1)).getPassing() == 2;
        boolean passable2 = BlockTypesEnum.getType(localMap.getBlockType(pos2)).getPassing() == 2;
        boolean sameLevel = pos1.getZ() == pos2.getZ();
        boolean lowRamp = BlockTypesEnum.getType(localMap.getBlockType(lowerOf(pos1, pos2))) == BlockTypesEnum.RAMP;
        return (passable1 && passable2 && (sameLevel || lowRamp));
    }

    private boolean inMap(int x, int y, int z) {
        return !(x < 0 || y < 0 || z < 0 ||
                x >= localMap.getxSize() ||
                y >= localMap.getySize() ||
                z >= localMap.getzSize());
    }

    private Position lowerOf(Position pos1, Position pos2) {
        return pos1.getZ() < pos2.getZ() ? pos1 : pos2;
    }
}
