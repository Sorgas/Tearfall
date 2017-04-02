package com.mvc.worldgen.generators.world.world_objects;

import com.utils.Plane;
import com.utils.Position;
import com.utils.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alexander on 03.03.2017.
 */
public class Slope {
	private List<Vector> vectors;
	private Plane plane;
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;

	public Slope(Position pos1, Position pos2, Position pos3) {
		vectors = new ArrayList<>();
		plane = new Plane(pos1, pos2, pos3);
		minX = pos1.getX();
		minY = pos1.getY();
		maxX = pos1.getX();
		maxY = pos1.getY();

		List<Position> list = new ArrayList<>();
		list.add(pos1);
		list.add(pos2);
		list.add(pos3);
		defineBounds(list);
		createVectors(list);
	}

	private void defineBounds(List<Position> list) {
		for (int i = 0; i < list.size(); i++) {
			Position pos = list.get(i);
			if (pos.getX() < minX) {
				minX = pos.getX();
			} else if (pos.getX() > maxX) {
				maxX = pos.getX();
			}
			if (pos.getY() < minY) {
				minY = pos.getY();
			} else if (pos.getY() > maxY) {
				maxY = pos.getY();
			}
		}
	}

	private void createVectors(List<Position> list) {
		Position center = new Position((maxX + minX) / 2, (maxY + minY) / 2, 0);
		Position prevPos = list.get(list.size() - 1);
		for (Iterator<Position> iterator = list.iterator(); iterator.hasNext(); ) {
			Position pos = iterator.next();
			Vector vector = new Vector(prevPos.getX(), prevPos.getY(), pos.getX(), pos.getY());
			prevPos = pos;
			vectors.add(vector);
		}
	}

	public boolean isInside(Position pos) {
		return isInside(pos.getX(), pos.getY());
	}

	public boolean isInside(int x, int y) {
		boolean inside = true;
		for (Iterator<Vector> iterator = vectors.iterator(); iterator.hasNext() && inside; ) {
			if (!iterator.next().isAtRight(x,y)) {
				inside = false;
			}
		}
		return inside;
	}

	public float getAltitude(Position pos) {
		return plane.getZ(pos.getX(), pos.getY());
	}

	public float getAltitude(int x, int y) {
		return plane.getZ(x, y);
	}

	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	@Override
	public String toString() {
		return "Slope{" +
				", minX=" + minX +
				", minY=" + minY +
				", maxX=" + maxX +
				", maxY=" + maxY +
				'}';
	}
}