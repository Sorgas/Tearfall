package stonering.util.geometry;

import com.badlogic.gdx.math.Vector3;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Class for storing in game coordinates, stores x, y, z integer values.
 * Has method for taking values from {@link Vector3}, does ceiling of vector components.
 */
public class Position implements Serializable, Cloneable {
    public int x;
    public int y;
    public int z;

    public Position() {
        this(0, 0, 0);
    }

    public Position(int x, int y, int z) {
        set(x, y, z);
    }

    public Position(float x, float y, float z) {
        this.x = Math.round(x);
        this.y = Math.round(y);
        this.z = Math.round(z);
    }

    public Position(Vector3 vector) {
        this.x = Math.round(vector.x);
        this.y = Math.round(vector.y);
        this.z = Math.round(vector.z);
    }

    public static Position add(Position pos1, Position pos2) {
        return new Position(pos1.x + pos2.x, pos1.y + pos2.y, pos1.z + pos2.z);
    }

    public static Position add(Position pos1, int x, int y, int z) {
        return new Position(pos1.x + x, pos1.y + y, pos1.z + z);
    }

    public static Position sub(Position pos1, Position pos2) {
        return new Position(pos1.x - pos2.x, pos1.y - pos2.y, pos1.z - pos2.z);
    }

    public static Position sub(Position pos1, int x, int y, int z) {
        return new Position(pos1.x - x, pos1.y - y, pos1.z - z);
    }


    public void add(Vector vector) {
        x += vector.getEndPoint().x - vector.getStartPoint().y;
        y += vector.getEndPoint().y - vector.getStartPoint().y;
    }

    public float getDistance(Position pos) {
        return getDistance(pos.x, pos.y, pos.z);
    }

    /**
     * Real distance.
     */
    public float getDistance(int x, int y, int z) {
        return (float) Math.sqrt(Math.pow((float) (this.x - x), 2) +
                Math.pow((float) (this.y - y), 2) +
                Math.pow((float) (this.z - z), 2));
    }

    /**
     * For using in comparators. Coordinates should be positive.
     */
    public int fastDistance(Position p) {
        return Math.abs(x - p.x) + Math.abs(y - p.y) + Math.abs(z - p.z);
    }

    public boolean isNeighbour(Position position) {
        Position result = sub(this, position);
        return result.x > -2 && result.x < 2 &&
                result.y > -2 && result.y < 2 &&
                result.z > -2 && result.z < 2;
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public Position set(Position position) {
        return set(position.x, position.y, position.z);
    }

    public  Position set(Vector3 vector) {
        return set(Math.round(vector.x), Math.round(vector.y), Math.round(vector.z));
    }

    public Position set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Position add(IntVector2 vector) {
        return add(vector.x, vector.y, 0);
    }

    public Position add(int dx, int dy, int dz) {
        x += dx;
        y += dy;
        z += dz;
        return this;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        if (y != position.y) return false;
        return z == position.z;
    }

    public boolean equals(Vector3 vector) {
        return x == vector.x && y == vector.y && z == vector.z;
    }

    public boolean equals(int x, int y, int z) {
        return x == this.x && y == this.y && z == this.z;
    }

    public Position clone() {
        return new Position(x, y, z);
    }

    public Vector3 toVector3() {
        return new Vector3(x, y, z);
    }

    public String toString() {
        return ("[" + x + " " + y + " " + z + "]");
    }

    public Stream<Position> neighbourStream(Collection<Position> deltas) {
        return deltas.stream().map(delta -> Position.add(this, delta));
    }
}
