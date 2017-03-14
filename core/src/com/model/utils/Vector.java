package com.model.utils;

/**
 * Created by Alexander on 22.02.2017.
 */
public class Vector {
    private int x = 0;
    private int y = 0;
	private int endX = 0;
	private int endY = 0;
	// in degrees
    private double angle = 0;
    private double length = 0;

    public Vector(int x, int y, double angle, double length) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.length = length;
    }

	public Vector(int x, int y, int x2, int y2) {
		this.x = x;
		this.y = y;
		this.endX = x2;
		this.endY = y2;
		double xProect = x2 - x;
		double yProect = y2 - y;
		this.length = Math.sqrt(Math.pow(xProect, 2) + Math.pow(yProect, 2));
		this.angle = Math.toDegrees(Math.atan(yProect / xProect));
		if(xProect < 0) {
			rotate(180);
		}
    }

	public Vector sum(Vector vector2) {
        double xProect = length * Math.cos(Math.toRadians(angle));
        double yProect = length * Math.sin(Math.toRadians(angle));
        xProect += vector2.getLength() * Math.cos(Math.toRadians(vector2.getAngle()));
        yProect += vector2.getLength() * Math.sin(Math.toRadians(vector2.getAngle()));
        double sumAngle = Math.toDegrees(Math.atan(yProect / xProect));
        double sumLength = Math.sqrt(Math.pow(xProect,2) + Math.pow(yProect,2));
        return new Vector(x,y,sumAngle,sumLength);
    }

    public Position getEndPoint() {
    	return new Position((int) Math.round(x + length * Math.cos(Math.toRadians(angle))),
			    (int) Math.round(y + length * Math.sin(Math.toRadians(angle))),0);
    }

	/**
	 * checks which half-plane from point1-point2 vector contains given point
	 * @return if
	 */
	public boolean isAtRight(Position point) {
		return isAtRight(point.getX(), point.getY());
	}

	public boolean isAtRight(int x, int y) {
		Position endPoint = getEndPoint();
		float value = (endPoint.getX() - this.x) * (y - this.y) -
				(endPoint.getY() - this.y) * (x - this.x);
		return value >= 0;
	}

	public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
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
    	Position end = new Position((int) Math.round(x + length * Math.cos(Math.toRadians(angle))),(int) Math.round(y + length * Math.sin(Math.toRadians(angle))),0);
		return "Vector{" +
				"x=" + x +
				", y=" + y +
				", angle=" + angle +
				", length=" + length +
				", end=" + end.toString() +
				'}';
	}

	public Vector getRightVector() {
		double rightAngle = (angle + 270) % 360;
		return new Vector(x,y,rightAngle,1);
	}

	public Position getStartPoint() {
		return new Position(x,y,0);
	}

	/**
	 * counter-clockwise for positive argument
	 * @param angle rotation angle
	 */
	public void rotate(float angle) {
		this.angle += angle;
		this.angle %= 360;
		if(this.angle < 0) {
			this.angle += 360;
		}
	}
}