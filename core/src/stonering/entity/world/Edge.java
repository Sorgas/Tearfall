package stonering.entity.world;

import com.badlogic.gdx.math.Vector2;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Side of a {@link TectonicPlate}.
 *
 * @author Alexander Kuzyakov on 22.02.2017.
 */
public class Edge {
    private Position point1;
    private Position point2;
    private List<Vector2> vectors;
    private List<Integer> dynamics;
    private List<Mountain> mountains;
    private List<Mountain> foothills;
    private List<Mountain> valleys;
    private float pikeHeight;
    private Vector2 offsetVector;
    private boolean isWorldBorder;

    public Edge(Position point1, Position point2) {
        this.point1 = point1;
        this.point2 = point2;
        vectors = new ArrayList<>();
        dynamics = new ArrayList<>();
        mountains = new ArrayList<>();
        valleys = new ArrayList<>();
        isWorldBorder = false;
    }

    public Position getPoint1() {
        return point1;
    }

    public void setPoint1(Position point1) {
        this.point1 = point1;
    }

    public Position getPoint2() {
        return point2;
    }

    public void setPoint2(Position point2) {
        this.point2 = point2;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "point1=" + point1.toString() +
                ", point2=" + point2.toString() +
                '}';
    }

    public List<Vector2> getVectors() {
        return vectors;
    }

    /**
     * add vector projection to edge
     *
     * @param vector non-projected vector
     */
    public void addVector(Vector2 vector) {
        // positive is enclosing
//        dynamics.add(getDistance(new Position(vector.x, vector.x, 0)) - getDistance(vector.getEndPoint()));
        vectors.add(vector);
        if (dynamics.size() > 1) {
            isWorldBorder = false;
        } else {
            isWorldBorder = true;
        }
    }

    private int getDistance(Position pos) {
        int value = Math.abs((point2.y - point1.y) * pos.x -
                (point2.x - point1.x) * pos.y +
                point2.x * point1.y - point2.y * point1.x);
        value = (int) Math.round((float) value / Math.sqrt(Math.pow(point2.y - point1.y, 2) +
                Math.pow(point2.x - point1.x, 2)));
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (point1 == null && edge.point1 == null && point2 == null && edge.point2 == null) return true;
        if (point1 == null && edge.point1 == null && point2.equals(edge.point2)) return true;
        if (point2 == null && edge.point2 == null && point1.equals(edge.point1)) return true;
        if (point1 == null && edge.point2 == null && point2.equals(edge.point1)) return true;
        if (point2 == null && edge.point1 == null && point1.equals(edge.point2)) return true;
        if (point2.equals(edge.point1) && point1.equals(edge.point2)) return true;
        if (point1.equals(edge.point1) && point2.equals(edge.point2)) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int result = point1 != null ? point1.hashCode() : 0;
        result = 31 * result + (point2 != null ? point2.hashCode() : 0);
        return result;
    }

    public List<Mountain> getMountains() {
        return mountains;
    }

    public void addMountain(Mountain mountain) {
        mountains.add(mountain);
    }

    public List<Mountain> getFoothills() {
        return foothills;
    }

    public void addFoothill(Mountain foothill) {
        foothills.add(foothill);
    }

    public List<Integer> getDynamics() {
        return dynamics;
    }

    public float getPikeHeight() {
        return pikeHeight;
    }

    public void setPikeHeight(float pikeHeight) {
        this.pikeHeight = pikeHeight;
    }

    public Vector2 getOffsetVector() {
        return offsetVector;
    }

    public void setOffsetVector(Vector2 offsetVector) {
        this.offsetVector = offsetVector;
    }

    public boolean isWorldBorder() {
        return isWorldBorder;
    }

    public void setWorldBorder(boolean worldBorder) {
        isWorldBorder = worldBorder;
    }

    public List<Mountain> getValleys() {
        return valleys;
    }

    public void addValley(Mountain valley) {
        valleys.add(valley);
    }
}