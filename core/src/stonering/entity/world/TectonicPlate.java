package stonering.entity.world;

import com.badlogic.gdx.math.Vector2;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents tectonic plate. Simply a polygon with speed.
 *
 * @author Alexander Kuzyakov on 22.02.2017.
 */
public class TectonicPlate {
	private List<Edge> edges;
	private Position center;
	private Vector2 speedVector;

	public TectonicPlate(Position center) {
		this.center = center;
		edges = new ArrayList<>();
		this.speedVector = new Vector2();
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

	public Vector2 getSpeedVector() {
		return speedVector;
	}

	public void setSpeedVector(Vector2 speedVector) {
		this.speedVector = speedVector;
	}

	public void setEdge(int index, Edge edge) {
		edges.set(index, edge);
	}
}