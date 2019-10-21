package stonering.util.geometry;

import java.io.Serializable;

/**
 * @author Alexander Kuzyakov on 22.02.2017.
 * <p>
 * Utility class of geometrical vector
 */
public class Vector implements Serializable {
    private float x = 0;
    private float y = 0;
    private float endX = 0;
    private float endY = 0;
    // in degrees
    private double angle = 0;
    private double length = 0;

    public Vector(float x, float y, double angle, double length) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.length = length;
        countEndPoint();
    }

    public Vector(float x, float y, float x2, float y2) {
        this.x = x;
        this.y = y;
        this.endX = x2;
        this.endY = y2;
        countLengthAndAngle();
    }

    private void countLengthAndAngle() {
        length = Math.sqrt(Math.pow(endX - x, 2) + Math.pow(endY - y, 2));
        if (x != endX) {
            angle = Math.toDegrees(Math.atan((endY - y) / (endX - x)));
        } else {
            angle = 0;
        }
        if (angle < 0) {
            rotate(180);
        }
    }

    private void countEndPoint() {
        endX = (float) (x + length * Math.cos(Math.toRadians(angle)));
        endY = (float) (y + length * Math.sin(Math.toRadians(angle)));
    }

    public Vector sum(Vector vector2) {
        float ex = endX + vector2.getXProj();
        float ey = endY + vector2.getYProj();
        return new Vector(x, y, ex, ey);
    }

    public Position getEndPoint() {
        return new Position((int) Math.round(x + length * Math.cos(Math.toRadians(angle))), (int) Math.round(y + length * Math.sin(Math.toRadians(angle))), 0);
    }

    /**
     * checks which half-plane from point1-point2 vector contains given point
     *
     * @return if
     */
    public boolean isAtRight(Position point) {
        return isAtRight(point.x, point.y);
    }

    public boolean isAtRight(int x, int y) {
        Position endPoint = getEndPoint();
        float value = (endPoint.x - this.x) * (y - this.y) -
                (endPoint.y - this.y) * (x - this.x);
        return value >= 0;
    }

    public Vector getRightVector() {
        double rightAngle = (angle + 270) % 360;
        return new Vector(x, y, rightAngle, 1);
    }

    /**
     * counter-clockwise for positive argument
     *
     * @param angle rotation angle
     */
    public void rotate(float angle) {
        this.angle += angle + 360;
        this.angle %= 360;
        countEndPoint();
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                ", angle=" + angle +
                ", length=" + length +
                ", endX=" + endX +
                ", endY=" + endY +
                '}';
    }

    public Position getStartPoint() {
        return new Position(Math.round(x), Math.round(y), 0);
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        countLengthAndAngle();
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        countLengthAndAngle();
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle % 360;
        countEndPoint();
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
        countEndPoint();
    }

    public float getXProj() {
        return endX - x;
    }

    public float getYProj() {
        return endY - y;
    }
}