package stonering.util.pathfinding.a_star;

import stonering.game.GameMvc;
import stonering.game.model.system.ModelComponent;
import stonering.game.model.local_map.LocalMap;
import stonering.util.HashPriorityQueue;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class AStar implements ModelComponent {
    private LocalMap localMap;
    private int maxSteps = 10000; // unlimited if negative

    /**
     * Returns the shortest Path from a start node to an end node according to
     * the A* heuristics (h must not overestimate). initialNode and last found node included.
     */
    public List<Position> makeShortestPath(Position initialPos, Position targetPos, boolean exactTarget) {
        localMap = GameMvc.instance().getModel().get(LocalMap.class);
        Node initialNode = new Node(initialPos, targetPos);
        Node pathNode = search(initialNode, targetPos, exactTarget); //perform search
        if (pathNode == null) return null;

        LinkedList<Position> path = new LinkedList<>();
        path.add(pathNode.position);
        while (pathNode.getParent() != null) {
            pathNode = pathNode.getParent();
            path.add(0, pathNode.position);
        }
        return path;
    }

    /**
     * @param initialNode start of the search
     * @param targetPos   end of the search
     * @param exactTarget if false, target can be impassable, and path can end next to it.
     * @return goal node to restore path from
     */
    private Node search(Node initialNode, Position targetPos, boolean exactTarget) {
        Logger.PATH.logDebug("searching path from " + initialNode.position + " to " + targetPos);
        HashPriorityQueue<Node, Node> openSet = new HashPriorityQueue(new NodeComparator());
        HashMap<Integer, Node> closedSet = new HashMap<>();
        int numSearchSteps = 0; // current iteration of the search
        openSet.add(initialNode, initialNode);
        while (openSet.size() > 0 && (maxSteps < 0 || numSearchSteps < maxSteps)) {
            Node currentNode = openSet.poll(); //get element with the least sum of costs

            if (targetPos.equals(currentNode.position)) return currentNode; //check if path is complete

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
        final Position nodePos = node.position;
        Position offset = new Position(0, 0, 0);
        for (offset.z = -1; offset.z <= 1; offset.z++) {
            for (offset.y = -1; offset.y <= 1; offset.y++) {
                for (offset.x = -1; offset.x <= 1; offset.x++) {
                    if (offset.isZero()) continue; // skip same pos
                    Position newPos = Position.add(nodePos, offset);
                    if (!localMap.inMap(newPos)) continue; // skip out of map tile
                    if (localMap.passage.hasPathBetweenNeighbours(nodePos, newPos)) nodes.add(new Node(newPos, target));
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
}
