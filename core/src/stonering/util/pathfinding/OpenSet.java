package stonering.util.pathfinding;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import stonering.util.geometry.Position;

/**
 * @author Alexander on 29.07.2020.
 */
public class OpenSet {
    private final Map<Position, Node> map;
    private final TreeSet<Node> set;

    public OpenSet() {
        map = new HashMap<>();
        set = new TreeSet<>(new AStar.NodeComparator());
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean contains(Node node) {
        return map.containsKey(node.position);
    }

    public Node get(Position position) {
        return map.get(position);
    }

    public boolean add(Node node) {
        map.remove(node.position);
        set.remove(node);
        map.put(node.position, node);
        set.add(node);
        return true;
    }

    public Node poll() {
        Position key = set.pollFirst().position;
        Node node = map.get(key);
        map.remove(key);
        return node;
    }
}
