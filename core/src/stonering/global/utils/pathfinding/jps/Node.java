package stonering.global.utils.pathfinding.jps;

import stonering.global.utils.Position;

public class Node {
    private Position position;
    private Node parent;

    public Node(Position position, Node parent) {
        this.position = position;
        this.parent = parent;
    }

    public Position getPosition() {
        return position;
    }

    public Node getParent() {
        return parent;
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public int getZ() {
        return position.getZ();
    }
}
