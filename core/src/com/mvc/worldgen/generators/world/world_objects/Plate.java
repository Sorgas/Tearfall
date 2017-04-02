package com.mvc.worldgen.generators.world.world_objects;

import com.utils.Position;
import com.utils.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 22.02.2017.
 */
public class Plate {
	private List<Edge> edges;
	private Position center;
	private Vector speedVector;

	public Plate(Position center) {
		this.center = center;
		edges = new ArrayList<Edge>();
		this.speedVector = new Vector(center.getX(), center.getY(), 0, 0);
	}

	public Position getCenter() {
		return center;
	}

	public void setCenter(Position center) {
		this.center = center;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void addEdge(Edge edge) {
		edges.add(edge);
	}

	public Vector getSpeedVector() {
		return speedVector;
	}

	public void setSpeedVector(Vector speedVector) {
		this.speedVector.setAngle(speedVector.getAngle());
		this.speedVector.setLength(speedVector.getLength());
	}

	public void setEdge(int index, Edge edge) {
		edges.set(index, edge);
	}
}