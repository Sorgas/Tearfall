package stonering.util.pathfinding.a_star;

import stonering.game.GameMvc;
import stonering.game.model.lists.ModelComponent;
import stonering.game.model.local_map.LocalMap;
import stonering.util.HashPriorityQueue;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class AStar implements ModelComponent, Initable {
    private LocalMap localMap;
    private int maxSteps = 10000; // unlimited if negative

    @Override
    public void init() {
        localMap = GameMvc.instance().getModel().get(LocalMap.class);
    }

    /**
     * Returns the shortest Path from a start node to an end node according to
     * the A* heuristics (h must not overestimate). initialNode and last found node included.
     */
    public List<Position> makeShortestPath(Position initialPos, Position targetPos, boolean exactTarget) {
        Node initialNode = new Node(initialPos, targetPos);
        //perform search and save the
        Node pathNode = search(initialNode, targetPos, exactTarget);
        if (pathNode == null) return null;

        //return shortest path according to AStar heuristics
        LinkedList<Position> path = new LinkedList<>();
        path.add(pathNode.getPosition());
        while (pathNode.getParent() != null) {
            pathNode = pathNode.getParent();
            path.add(0, pathNode.getPosition());
        }
        return path;
    }

    /**
     * @param initialNode start of the search
     * @param targetPos   end of the search
     * @return goal node from which you can reconstruct the path
     */
    private Node search(Node initialNode, Position targetPos, boolean exactTarget) {
        Logger.PATH.logDebug("searching path from " + initialNode.getPosition() + " to " + targetPos);
        HashPriorityQueue<Node, Node> openSet = new HashPriorityQueue(new NodeComparator());
        HashMap<Integer, Node> closedSet = new HashMap<>();

        // current iteration of the search
        int numSearchSteps = 0;

        openSet.add(initialNode, initialNode);
        while (openSet.size() > 0 && (maxSteps < 0 || numSearchSteps < maxSteps)) {
            //get element with the least sum of costs
            Node currentNode = openSet.poll();

            //check if path is complete
            if (exactTarget) {
                if (targetPos.equals(currentNode.getPosition())) return currentNode;
            } else {
                if (isAdjacent(targetPos, currentNode.getPosition())) return currentNode;
            }

            //get successor nodes
            ArrayList<Node> successorNodes = getSuccessors(currentNode, targetPos);

            //process successor nodes
            for (Node successorNode : successorNodes) {
                // node already closed, skip
                if (closedSet.containsValue(successorNode)) continue;

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

                    if (inOpenSet && pathLength >= successorNode.getPathLength()) continue;
                } else {
                    inOpenSet = false;
                }
                //node was already discovered and this path is worse than the last one
                successorNode.setParent(currentNode);
                // if successorNode is already in data structure it has to be inserted again to regain the order
                if (inOpenSet) openSet.remove(successorNode, successorNode);

                successorNode.setPathLength(pathLength);
                openSet.add(successorNode, successorNode);
            }
            closedSet.put(currentNode.hashCode(), currentNode);
            numSearchSteps += 1;
        }
        Logger.PATH.logDebug("no path found");
        return null;
    }

    /**
     * Gets tiles that can be stepped in from given tile.
     */
    private ArrayList<Node> getSuccessors(Node node, Position target) {
        ArrayList<Node> nodes = new ArrayList<>();
        final Position nodePos = node.getPosition();
        Position offset = new Position(0, 0, 0);
        for (offset.z = -1; offset.z <= 1; offset.z++) {
            for (offset.y = -1; offset.y <= 1; offset.y++) {
                for (offset.x = -1; offset.x <= 1; offset.x++) {
                    if (offset.isZero()) continue; // skip same pos
                    Position newPos = Position.add(nodePos, offset);
                    if (!localMap.inMap(newPos)) continue; // skip out of map tile
                    if (localMap.passage.hasPathBetween(nodePos, newPos)) nodes.add(new Node(newPos, target));
                }
            }
        }
        return nodes;
    }

    private boolean isAdjacent(Position pos1, Position pos2) {
        return Math.abs(pos1.getX() - pos2.getX()) < 2
                && Math.abs(pos1.getY() - pos2.getY()) < 2
                && Math.abs(pos1.getZ() - pos2.getZ()) < 2;
    }

    private static class NodeComparator implements Comparator<Node> {
        public int compare(Node node1, Node node2) {
            return Double.compare(node1.getCost(), node2.getCost());
        }
    }
}
