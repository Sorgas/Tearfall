package stonering.entity.world;

import stonering.util.geometry.Plane;
import stonering.util.geometry.Position;
import stonering.util.geometry.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents part of a mountain (triangle with elevation).
 *
 * @author Alexander Kuzyakov on 03.03.2017.
 */
public class Slope {
    private List<Vector> vectors;
    private Plane plane;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;

    public Slope(Position pos1, Position pos2, Position pos3) {
        vectors = new ArrayList<>();
        plane = new Plane(pos1, pos2, pos3);
        minX = pos1.x;
        minY = pos1.y;
        maxX = pos1.x;
        maxY = pos1.y;

        List<Position> list = new ArrayList<>();
        list.add(pos1);
        list.add(pos2);
        list.add(pos3);
        defineBounds(list);
        createVectors(list);
    }

    private void defineBounds(List<Position> list) {
        for (Position pos : list) {
            if (pos.x < minX) {
                minX = pos.x;
            } else if (pos.x > maxX) {
                maxX = pos.x;
            }
            if (pos.y < minY) {
                minY = pos.y;
            } else if (pos.y > maxY) {
                maxY = pos.y;
            }
        }
    }

    private void createVectors(List<Position> list) {
        Position center = new Position((maxX + minX) / 2, (maxY + minY) / 2, 0);
        Position prevPos = list.get(list.size() - 1);
        for (Iterator<Position> iterator = list.iterator(); iterator.hasNext(); ) {
            Position pos = iterator.next();
            Vector vector = new Vector(prevPos.x, prevPos.y, pos.x, pos.y);
            prevPos = pos;
            vectors.add(vector);
        }
    }

    public boolean isInside(Position pos) {
        return isInside(pos.x, pos.y);
    }

    public boolean isInside(int x, int y) {
        for (Vector vector: vectors) {
            if (!vector.isAtRight(x, y)) {
                return false;
            }
        }
        return true;
    }

    public float getAltitude(Position pos) {
        return plane.getZ(pos.x, pos.y);
    }

    public float getAltitude(int x, int y) {
        return plane.getZ(x, y);
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    @Override
    public String toString() {
        return "Slope{" +
                ", minX=" + minX +
                ", minY=" + minY +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                '}';
    }
}