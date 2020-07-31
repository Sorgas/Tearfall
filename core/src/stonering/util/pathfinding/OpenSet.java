package stonering.util.pathfinding;

import java.util.*;

/**
 * @author Alexander on 29.07.2020.
 */
public class OpenSet {
    private final TreeMap<Node, Node> treeMap; // sorts keys

    public OpenSet() {
        treeMap = new TreeMap<>(new AStar.NodeComparator());
    }

    public int size() {
        return treeMap.size();
    }

    public boolean isEmpty() {
        return treeMap.isEmpty();
    }

    public Node get(Node node) {
        return treeMap.get(node);
    }

    public boolean add(Node node) {
        treeMap.remove(node);
        treeMap.put(node, node);
        return true;
    }

    public Node poll() {
        return Optional.ofNullable(treeMap.pollFirstEntry())
                .map(Map.Entry::getValue)
                .orElse(null);
    }
}
