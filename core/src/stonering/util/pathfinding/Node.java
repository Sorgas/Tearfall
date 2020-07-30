package stonering.util.pathfinding;

import java.util.LinkedList;
import java.util.List;

import stonering.util.geometry.Position;

/**
 * Node of pathfinding tree.
 * 
 * @author Alexander Kuzyakov on 12.02.2018.
 */
public class Node {
    public Position position;
    public Node parent;
    public int pathLength;
    public float heuristic;
    
    public Node(Position position) {
        this.position = position;
    }

    public float cost() {
        return pathLength + heuristic;
    }

    public List<Position> getPath() {
        LinkedList<Position> path = new LinkedList<>();
        path.add(position);
        Node current = this;
        while (current != null) {
            path.add(0, current.position);
            current = current.parent;
        }
        return path;
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return position.equals(((Node) o).position);
    }

    @Override
    public String toString() {
        return position.toString();
    }
}