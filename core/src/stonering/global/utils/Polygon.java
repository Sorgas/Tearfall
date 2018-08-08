package stonering.global.utils;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Class for polygon.
 *
 * @author Alexander Kuzyakov
 */
public class Polygon {
    private ArrayList<Vector2> points;
    private ArrayList<Vector2> sides;

    public ArrayList<Vector2> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<Vector2> points) {
        this.points = points;
    }

    public ArrayList<Vector2> getSides() {
        return sides;
    }

    public void setSides(ArrayList<Vector2> sides) {
        this.sides = sides;
    }
}
