package stonering.util.pathfinding;

import stonering.entity.job.action.target.ActionTarget;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.ModelComponent;
import stonering.game.model.local_map.LocalMap;
import stonering.util.HashPriorityQueue;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import java.util.*;

public class AStar implements ModelComponent {
    private LocalMap localMap;
    private int maxSteps = 10000; // unlimited if negative

    /**
     * Returns the shortest Path from a start node to an end node according to
     * the A* heuristics (h must not overestimate). initialNode and last found node included.
     */
    public List<Position> makeShortestPath(Position initialPos, Position targetPos, ActionTargetTypeEnum targetType) {
        localMap = GameMvc.model().get(LocalMap.class);
        Node initialNode = new Node(initialPos, targetPos);
        Node pathNode = search(initialNode, targetPos, targetType); //perform search
        if (pathNode == null) return null;

        LinkedList<Position> path = new LinkedList<>();
        path.add(pathNode.position);
        while (pathNode.getParent() != null) {
            pathNode = pathNode.getParent();
            path.add(0, pathNode.position);
        }
        return path;
    }

    public List<Position> makeShortestPath(Position initialPos, Position targetPos) {
        return makeShortestPath(initialPos, targetPos, ActionTargetTypeEnum.EXACT);
    }

    /**
     * @param initialNode start of the search
     * @param targetPos   end of the search
     * @param targetType see {@link ActionTarget}
     * @return goal node to restore path from
     */
    private Node search(Node initialNode, Position targetPos, ActionTargetTypeEnum targetType) {
        HashPriorityQueue<Node, Node> openSet = new HashPriorityQueue(new NodeComparator());
        HashMap<Integer, Node> closedSet = new HashMap<>();
        PathFinishCondition finishCondition = new PathFinishCondition(targetPos, targetType);
        int numSearchSteps = 0; // current iteration of the search
        openSet.add(initialNode, initialNode);
        while (openSet.size() > 0 && (maxSteps < 0 || numSearchSteps < maxSteps)) {
            Node currentNode = openSet.poll(); //get element with the least sum of costs
            if(finishCondition.check(currentNode.position)) return currentNode; //check if path is complete
            ArrayList<Node> successorNodes = getSuccessors(currentNode, targetPos); //get successor nodes

            //process successor nodes
            for (Node successorNode : successorNodes) {
                if (closedSet.containsValue(successorNode)) continue; // node already closed, skip
                int pathLength = currentNode.getPathLength() + 1; //compute tentativeG

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
                successorNode.setParent(currentNode); //node was already discovered and this path is worse than the last one
                // if successorNode is already in data structure it has to be inserted again to regain the order
                if (inOpenSet) openSet.remove(successorNode, successorNode);
                successorNode.setPathLength(pathLength);
                openSet.add(successorNode, successorNode);
            }
            closedSet.put(currentNode.hashCode(), currentNode);
            numSearchSteps++;
        }
        Logger.PATH.logDebug("No path found");
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
                    if (localMap.passageMap.hasPathBetweenNeighbours(nodePos, newPos)) nodes.add(new Node(newPos, target));
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
