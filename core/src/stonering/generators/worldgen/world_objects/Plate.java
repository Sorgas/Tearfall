package stonering.generators.worldgen.world_objects;

import stonering.global.utils.Position;
import stonering.global.utils.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Kuzyakov on 22.02.2017.
 */
public class Plate {
	private List<Edge> edges;
	private Position center;
	private Vector speedVector;

	public Plate(Position center) {
		this.center = center;
		edges = new ArrayList<>();
		this.speedVector = new Vector(center.getX(), center.getY(), 0, 0);
	}

	public Position getCenter() {
		return center;
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
		this.speedVector = speedVector;
	}

	public void setEdge(int index, Edge edge) {
		edges.set(index, edge);
	}
}