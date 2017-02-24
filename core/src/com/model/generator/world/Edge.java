package com.model.generator.world;

import com.model.utils.Position;

/**
 * Created by Alexander on 22.02.2017.
 */
public class Edge {
    private Position point1;
    private Position point2;

    public Edge(Position point1, Position point2) {
        this.point1 = point1;
        this.point2 = point2;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Edge edge = (Edge) o;

		if (point1 != null ? !point1.equals(edge.point1) : edge.point1 != null) return false;
		return point2 != null ? point2.equals(edge.point2) : edge.point2 == null;
	}

	@Override
	public int hashCode() {
		int result = point1 != null ? point1.hashCode() : 0;
		result = 31 * result + (point2 != null ? point2.hashCode() : 0);
		return result;
	}
}
