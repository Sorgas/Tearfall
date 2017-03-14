package com.model.generator.world.world_objects;

import com.model.utils.Position;
import com.model.utils.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alexander on 27.02.2017.
 */
public class Mountain {
	private Position top;
	private List<Position> corners;
	private Edge edge;
	private int minX;
	private int maxX;
	private int minY;
	private int maxY;
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
		if(pos.getX() > maxX || pos.getX() < minX) return false;
		if(pos.getY() > maxY || pos.getY() < minY) return false;
		return true;
	}

	private void countBounds() {
		minX = top.getX();
		maxX = top.getX();
		minY = top.getY();
		maxY = top.getY();
		for (Iterator<Position> iterator = corners.iterator(); iterator.hasNext();) {
			Position corner = iterator.next();
			if(corner.getX() < minX) {
				minX = corner.getX();
			} else if(corner.getX() > maxX) {
				maxX = corner.getX();
			}
			if(corner.getY() < minY) {
				minY = corner.getY();
			} else if(corner.getY() > maxY) {
				maxY = corner.getY();
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
}
