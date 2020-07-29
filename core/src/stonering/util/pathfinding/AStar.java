package stonering.util.pathfinding;

import stonering.entity.job.action.target.ActionTarget;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.ModelComponent;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.geometry.PositionUtil;
import stonering.util.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStar implements ModelComponent {
    private LocalMap localMap;

    /**
     * Returns the shortest Path from a start node to an end node according to
     * the A* heuristics (h must not overestimate). initialNode and last found node included.
     */
    public List<Position> makeShortestPath(Position initialPos, Position targetPos, ActionTargetTypeEnum targetType) {
        localMap = GameMvc.model().get(LocalMap.class);
        Node initialNode = new Node(initialPos);
        initialNode.pathLength = 0;
        initialNode.heuristic = initialPos.getDistance(targetPos);
        return Optional.ofNullable(search(initialNode, targetPos, targetType))
                .map(Node::getPath)
                .orElse(null);
    }

    public List<Position> makeShortestPath(Position initialPos, Position targetPos) {
        return makeShortestPath(initialPos, targetPos, ActionTargetTypeEnum.EXACT);
    }

    /**
     * @param initialNode start of the search
     * @param targetPos   end of the search
     * @param targetType  see {@link ActionTarget}
     * @return goal node to restore path from
     */
    private Node search(Node initialNode, Position targetPos, ActionTargetTypeEnum targetType) {
        
        OpenSet openSet = new OpenSet();
        Set<Node> closedSet = new HashSet<>();
        PathFinishCondition finishCondition = new PathFinishCondition(targetPos, targetType);
        System.out.println("finish condition " + finishCondition.acceptable);
        
        System.out.println("_add " + initialNode + " to open set");
        openSet.add(initialNode);
        while (openSet.size() > 0) {
            Node currentNode = openSet.poll(); //get element with the least sum of costs
            System.out.println("current is " + currentNode);
            if (finishCondition.check(currentNode.position)) return currentNode; //check if path is complete
            int pathLength = currentNode.pathLength + 1;
            getSuccessors(currentNode)
                    .filter(node -> !closedSet.contains(node)) // node already handled and closed
                    .peek(node -> node.pathLength = pathLength)
                    .peek(node -> node.parent = currentNode)
                    .peek(node -> node.heuristic = node.position.getDistance(targetPos))
                    .forEach(newNode -> {
                        if(newNode.toString().equals("[4 5 4]"))
                            System.out.println("found");
                        Node oldNode = openSet.get(newNode.position);
                        if (oldNode == null || oldNode.pathLength > newNode.pathLength) { // if successor node is newly found, or has shorter path
                            System.out.println("+add " + newNode + " to open set");
                            openSet.add(newNode); // replace old node
                        }
                    });
            System.out.println("-add " + currentNode + " to close set");
            closedSet.add(currentNode);
        }
        Logger.PATH.logDebug("No path found");
        return null;
    }

    /**
     * Gets tiles that can be stepped in from given tile.
     */
    private Stream<Node> getSuccessors(Node node) {
        final Position nodePos = node.position;
        return PositionUtil.all.stream()
                .map(delta -> Position.add(nodePos, delta))
                .filter(localMap::inMap)
                .filter(pos -> localMap.passageMap.hasPathBetweenNeighbours(nodePos, pos))
                .map(Node::new);
    }

    public static class NodeComparator implements Comparator<Node> {
        public int compare(Node node1, Node node2) {
            return Double.compare(node1.cost(), node2.cost());
        }
    }
}
