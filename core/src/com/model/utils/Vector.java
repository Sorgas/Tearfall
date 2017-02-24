package com.model.utils;

/**
 * Created by Alexander on 22.02.2017.
 */
public class Vector {
    private int x = 0;
    private int y = 0;
    // in degrees
    private float angle = 0;
    private float length = 0;

    public Vector(int x, int y, float angle, float length) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.length = length;
    }

    public Vector sum(Vector vector2) {
        double xProect = length * Math.cos(Math.toRadians(angle));
        double yProect = length * Math.sin(Math.toRadians(angle));
        xProect += vector2.getLength() * Math.cos(Math.toRadians(vector2.getAngle()));
        yProect += vector2.getLength() * Math.sin(Math.toRadians(vector2.getAngle()));
        float sumAngle = (float) Math.atan(yProect / xProect);
        float sumLength = (float) Math.sqrt(Math.pow(xProect,2) + Math.pow(yProect,2));
        return new Vector(x,y,sumAngle,sumLength);
    }

    public Position getEndPoint() {
    	return new Position((int) (x + length * Math.cos(Math.toRadians(angle))), (int) (y + length * Math.sin(Math.toRadians(angle))),0);
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

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

	@Override
	public String toString() {
    	Position end = new Position((int) (x + length * Math.cos(Math.toRadians(angle))), (int) (y + length * Math.sin(Math.toRadians(angle))),0);
		return "Vector{" +
				"x=" + x +
				", y=" + y +
				", angle=" + angle +
				", length=" + length +
				", end=" + end.toString() +
				'}';
	}
}
