package com.utils;

import java.io.Serializable;

/**
 * Class for storing in game coordinates
 * simply stores x, y, z int values
 */
public class Position implements Serializable{
    private int x;
    private int y;
    private int z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position addVector(Vector vector) {
    	Position endPoint = vector.getEndPoint();
    	int xOffset = endPoint.getX() - vector.getStartPoint().getX();
	    int yOffset = endPoint.getY() - vector.getStartPoint().getY();
	    return new Position(x + xOffset, y + yOffset, z);
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

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String toString() {
        return (new Integer(x).toString() + " " + new Integer(y).toString() + " " + new Integer(z).toString());
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

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		result = 31 * result + z;
		return result;
	}

	public Position clone() {
		return new Position(x,y,z);
	}
}
