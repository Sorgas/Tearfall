package stonering.global.utils;


import stonering.util.global.Pair;

/**
 * @author Alexander Kuzyakov on 02.03.2018.
 */
public class FloatVector {
    private float x = 0;
    private float y = 0;
    private float endX = 0;
    private float endY = 0;
    // in degrees
    private double angle = 0;
    private double length = 0;

    public FloatVector(double angle, double length) {
        this.x = 0;
        this.y = 0;
        this.angle = angle;
        this.length = length;
    }

    public FloatVector(float x, float y, float x2, float y2) {
        this.x = x;
        this.y = y;
        this.endX = x2;
        this.endY = y2;
        double xProject = x2 - x;
        double yProject = y2 - y;
        this.length = Math.sqrt(xProject * xProject + yProject * yProject);
        if (xProject != 0) {
            this.angle = Math.toDegrees(Math.atan(yProject / xProject));
        } else {
            this.angle = 0;
        }
        if (xProject < 0) {
            rotate(180);
        }
    }

    public FloatVector sum(Vector vector2) {
        double xProject = length * Math.cos(Math.toRadians(angle));
        double yProject = length * Math.sin(Math.toRadians(angle));
        xProject += vector2.getLength() * Math.cos(Math.toRadians(vector2.getAngle()));
        yProject += vector2.getLength() * Math.sin(Math.toRadians(vector2.getAngle()));
        double sumAngle;
        if (xProject != 0) {
            sumAngle = (Math.toDegrees(Math.atan(yProject / xProject)) + 360) % 360;
            if (xProject < 0) {
                sumAngle = (sumAngle + 180) % 360;
            }
        } else {
            sumAngle = yProject > 0 ? 90 : 270;
        }
        double sumLength = Math.sqrt(Math.pow(xProject, 2) + Math.pow(yProject, 2));
        return new FloatVector(sumAngle, sumLength);
    }

    public Pair<Float, Float> getEndPoint() {
        return new Pair<>((float) (this.x + length * Math.cos(Math.toRadians(angle))),
                (float) (this.y + length * Math.sin(Math.toRadians(angle))));
    }

    /**
     * checks which half-plane from point1-point2 vector contains given point
     *
     * @return if
     */
    public boolean isAtRight(Position point) {
        return isAtRight(point.getX(), point.getY());
    }

    public boolean isAtRight(int x, int y) {
        Pair<Float, Float> endPoint = getEndPoint();
        float value = (endPoint.getKey() - this.x) * (y - this.y) -
                (endPoint.getValue() - this.y) * (x - this.x);
        return value >= 0;
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle % 360;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    @Override
    public String toString() {
        Position end = new Position((int) Math.round(x + length * Math.cos(Math.toRadians(angle))), (int) Math.round(y + length * Math.sin(Math.toRadians(angle))), 0);
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                ", angle=" + angle +
                ", length=" + length +
                ", end=" + end.toString() +
                '}';
    }

    public FloatVector getRightVector() {
        double rightAngle = (angle + 270) % 360;
        return new FloatVector(rightAngle, 1);
    }

    public Pair<Float, Float> getStartPoint() {
        return new Pair(x, y);
    }

    /**
     * counter-clockwise for positive argument
     *
     * @param angle rotation angle
     */
    public void rotate(float angle) {
        this.angle += angle;
        this.angle %= 360;
        if (this.angle < 0) {
            this.angle += 360;
        }
    }

    public FloatVector copy() {
        return new FloatVector(x, y, endX, endY);
    }

    public FloatVector moveToO() {
        return new FloatVector(0, 0, endX - x, endY - y);
    }

    public float getXProj() {
        return endX - x;
    }

    public float getYProj() {
        return endY - y;
    }
}
