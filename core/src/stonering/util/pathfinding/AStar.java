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
        Set<Position> closedSet = new HashSet<>();
        PathFinishCondition finishCondition = new PathFinishCondition(targetPos, targetType);

        openSet.add(initialNode);
        while (openSet.size() > 0) {
            Node currentNode = openSet.poll(); //get element with the least sum of costs
            if (finishCondition.check(currentNode.position)) return currentNode; //check if path is complete
            int pathLength = currentNode.pathLength + 1;
            List<Node> nodes = getSuccessors(currentNode).collect(Collectors.toList());
            nodes = nodes.stream()
                    .filter(node -> !closedSet.contains(node.position)) // node already handled and closed
                    .peek(node -> node.pathLength = pathLength)
                    .peek(node -> node.parent = currentNode)
                    .peek(node -> node.heuristic = node.position.getDistance(targetPos))
                    .collect(Collectors.toList());
            nodes.forEach(newNode -> {
                Node oldNode = openSet.get(newNode);
                if ((oldNode == null ) || (oldNode.pathLength > newNode.pathLength)) { // if successor node is newly found, or has shorter path
                    openSet.add(newNode); // replace old node
                }
            });
            closedSet.add(currentNode.position);
        }
//        Logger.PATH.logDebug("No path found");
        return null;
    }

    /**
     * Gets tiles that can be stepped in from given tile.
     */
    private Stream<Node> getSuccessors(Node node) {
        return PositionUtil.all.stream()
                .map(delta -> Position.add(node.position, delta))
                .filter(localMap::inMap)
                .filter(pos -> localMap.passageMap.hasPathBetweenNeighbours(node.position, pos))
                .map(Node::new);
    }

    public static class NodeComparator implements Comparator<Node> {
        public int compare(Node node1, Node node2) {
            if(node1.position.equals(node2.position)) return 0;
            return node1.cost() < node2.cost() ? -1 : 1;
        }
    }
}
