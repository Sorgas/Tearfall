package stonering.util.pathfinding.a_star;

import stonering.util.geometry.Position;

/**
 * @author Alexander Kuzyakov on 12.02.2018.
 */
public class Node {
    public Position position;
    private Node parent;
    private int pathLength;
    private float heuristic;

    public Node(Position position, Position target) {
        this.position = position;
        heuristic = countHeuristic(target);
    }

    /**
     * heuristic function
     *
     * @param target position to count to
     * @return distance between positions
     */
    private float countHeuristic(Position target) {
        return (float) Math.sqrt(Math.pow(position.x - target.x, 2) +
                Math.pow(position.y - target.y, 2) +
                Math.pow(position.z - target.z, 2));
    }

    public float getCost() {
        return pathLength + heuristic;
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getPathLength() {
        return pathLength;
    }

    public void setPathLength(int pathLength) {
        this.pathLength = pathLength;
    }

    public float getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(float heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return position.equals(((Node) o).position);
    }
}