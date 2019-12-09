package stonering.entity.world;

import stonering.util.geometry.Position;
import stonering.util.geometry.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 *
 * @author Alexander Kuzyakov on 27.02.2017.
 */
public class Mountain {
	private Position top;
	private List<Position> corners;
	private Edge edge;
	private int minX;
	private int maxX;
	private int minY;
	private int maxY;
	private float width;
	private Vector topOffset;

	public Mountain() {
		corners = new ArrayList<>();
	}

	public int[][] getHeightArray(Position point) {
		countBounds();
		for(int i = 1; i < corners.size(); i++) {

		}
		return null;
	}

	private boolean isInMountain(Position pos) {
		return true;
	}

	private boolean isInBounds(Position pos) {
		if(pos.x > maxX || pos.x < minX) return false;
		if(pos.y > maxY || pos.y < minY) return false;
		return true;
	}

	private void countBounds() {
		minX = top.x;
		maxX = top.x;
		minY = top.y;
		maxY = top.y;
		for (Iterator<Position> iterator = corners.iterator(); iterator.hasNext();) {
			Position corner = iterator.next();
			if(corner.x < minX) {
				minX = corner.x;
			} else if(corner.x > maxX) {
				maxX = corner.x;
			}
			if(corner.y < minY) {
				minY = corner.y;
			} else if(corner.y > maxY) {
				maxY = corner.y;
			}
		}
	}

	public Position getTop() {
		return top;
	}

	public void setTop(Position top) {
		this.top = top;
		countBounds();
	}

	public List<Position> getCorners() {
		return corners;
	}

	public void addCorner(Position corner) {
		this.corners.add(corner);
		countBounds();
	}

	public Edge getEdge() {
		return edge;
	}

	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	public int getMinX() {
		return minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public Vector getTopOffset() {
		return topOffset;
	}

	public void setTopOffset(Vector topOffset) {
		this.topOffset = topOffset;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
}
