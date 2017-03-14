package com.model.generator.world.generators.elevation;

import com.model.generator.world.map_objects.WorldGenContainer;
import com.model.generator.world.world_objects.Edge;
import com.model.generator.world.world_objects.Mountain;
import com.model.utils.Position;
import com.model.utils.Vector;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexander on 28.02.2017.
 */
public class MountainGenerator {
	private final WorldGenContainer container;
	private Random random;
	private List<Edge> edges;
	private float plateSpeedToHeightModifier;
	private float topOffsetModifier;
	private int topsDensity;

	public MountainGenerator(WorldGenContainer container) {
		this.container = container;
	}

	public boolean execute() {
		extractContainer();
		for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext(); ) {
			Edge edge = iterator.next();
			edge.getMountains().clear();
			if (!edge.isWorldBorder()) {
				configureEdge(edge);
				createMountainHeights(edge);
				mergeMountainChains(edge);
				createMountains(edge);
				applyOffsetVectors(edge);
			}
		}
		return false;
	}

	private void mergeMountainChains(Edge edge) {
		if (edge.getPikeHeight() > 0 && edge.getMountains().size() > 0) {
			for (Iterator<Edge> edgeIterator = edges.iterator(); edgeIterator.hasNext(); ) {
				Edge edge1 = edgeIterator.next();
				if (edge1.getPikeHeight() > 0 && isNeighborEdges(edge, edge1)) {
					Position pos = getCommonPoint(edge, edge1);
					int start = 0;
					int step = 1;
					int middle = edge.getMountains().size() / 2;
					float middleHeight = (edge.getPikeHeight() + edge1.getPikeHeight()) / 2;
					if (pos.equals(edge.getPoint2())) {
						start = edge.getMountains().size() - 1;
						step = -1;
					}
					if (middle != 0) {
						for (int i = start; i != middle; i += step) {
							float height = middleHeight + (edge.getPikeHeight() - middleHeight) / middle * (i - start) * step;
							height += random.nextInt(6) - 3;
							edge.getMountains().get(i).getTop().setZ(Math.round(height));
						}
					} else {
						edge.getMountains().get(middle).getTop().setZ(Math.round(edge.getPikeHeight()));
					}
				}
			}
		}
	}

	private boolean isNeighborEdges(Edge edge, Edge edge1) {
		boolean value = false;
		if (edge.getPoint1().equals(edge1.getPoint1()) || edge.getPoint1().equals(edge1.getPoint2())) {
			value = true;
		}
		if (edge.getPoint2().equals(edge1.getPoint1()) || edge.getPoint2().equals(edge1.getPoint2())) {
			value = true;
		}
		return value;
	}

	private Position getCommonPoint(Edge edge, Edge edge1) {
		Position pos = null;
		if (edge.getPoint1().equals(edge1.getPoint1()) || edge.getPoint1().equals(edge1.getPoint2())) {
			pos = edge.getPoint1().clone();
		}
		if (edge.getPoint2().equals(edge1.getPoint1()) || edge.getPoint2().equals(edge1.getPoint2())) {
			pos = edge.getPoint2().clone();
		}
		return pos;
	}

	private void extractContainer() {
		random = container.getConfig().getRandom();
		edges = container.getEdges();
		topsDensity = container.getConfig().getMountainsTopsDensity();
		plateSpeedToHeightModifier = container.getConfig().getPlateSpeedToHeightModifier();
		topOffsetModifier = container.getConfig().getTopOffsetModifier();
	}

	private void createTops(Edge edge) {
		Position pos1 = edge.getPoint1();
		Position pos2 = edge.getPoint2();
		int length = (int) Math.round(Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow(pos1.getY() - pos2.getY(), 2)));
		int num = length / topsDensity;
		if (num > 0) {
			float xDensity = (pos2.getX() - pos1.getX()) / (float) num;
			float yDensity = (pos2.getY() - pos1.getY()) / (float) num;
			int xStart = pos1.getX();
			int yStart = pos1.getY();
			float x = xStart + xDensity / 2;
			float y = yStart + yDensity / 2;
			for (int i = 0; i < num; i++) {
				Mountain mountain = new Mountain();
				mountain.setTop(new Position(Math.round(x), Math.round(y), 0));
				edge.addMountain(mountain);
				x += xDensity;
				y += yDensity;
			}
		}
	}

	private void configureEdge(Edge edge) {
		if (edge.getDynamics().size() == 2 && !isEdgeOnBorder(edge)) {
			if (edge.getDynamics().get(0) > 0 || edge.getDynamics().get(1) > 0) {
				createTops(edge);
				int mainSpeed = (edge.getDynamics().get(1) > edge.getDynamics().get(0)) ? 1 : 0;
				int secondSpeed = (mainSpeed + 1) % 2;

				float maxHeigth = edge.getDynamics().get(mainSpeed);
				maxHeigth += edge.getDynamics().get(secondSpeed) > 0 ? edge.getDynamics().get(secondSpeed) : 0;
				maxHeigth *= plateSpeedToHeightModifier;
				edge.setPikeHeight(maxHeigth);

				int topOffset = edge.getDynamics().get(mainSpeed);
				if (edge.getDynamics().get(secondSpeed) > 0) {
					topOffset -= edge.getDynamics().get(secondSpeed);
				}

				Vector edgeVector = new Vector(edge.getPoint1().getX(), edge.getPoint1().getY(), edge.getPoint2().getX(), edge.getPoint2().getY());
				Vector edgeOffsetVector = edgeVector.getRightVector();
				edgeOffsetVector.setLength(topOffset);
				if (!edgeVector.isAtRight(edge.getVectors().get(mainSpeed).getStartPoint())) {
					edgeOffsetVector.rotate(180);
				}
				edge.setOffsetVector(edgeOffsetVector);
			}
		}
	}

	private boolean isEdgeOnBorder(Edge edge) {
		boolean value = false;
		int x1 = edge.getPoint1().getX();
		int y1 = edge.getPoint1().getY();
		int x2 = edge.getPoint2().getX();
		int y2 = edge.getPoint2().getY();
		int width = container.getConfig().getWidth();
		int height = container.getConfig().getHeight();
		if(x1 == 0 || x1 == width || x2 == 0 || x2 == width) {
			value = true;
		}
		if(y1 == 0 || y1 == height || y2 == 0 || y2 == height) {
			value = true;
		}
		return value;
	}

	private void createMountainHeights(Edge edge) {
		List<Mountain> mountains = edge.getMountains();
		if (mountains.size() > 0) {
			for (int i = 0; i < mountains.size(); i++) {
				double radians = Math.PI * i / edge.getMountains().size();
				mountains.get(i).getTop().setZ((int) Math.round(edge.getPikeHeight() * Math.sin(radians) + random.nextInt(3)));
			}
		}
	}

	private void createMountains(Edge edge) {
		List<Mountain> mountains = edge.getMountains();
		for (Iterator<Mountain> iterator = mountains.iterator(); iterator.hasNext(); ) {
			Mountain mountain = iterator.next();
			int slopeCount = random.nextInt(2) + 6 + mountain.getTop().getZ() / 39;
			int[] slopeAngles = new int[slopeCount];
			int spinAngle = random.nextInt(360);
			for (int i = 0; i < slopeCount; i++) {
				slopeAngles[i] = random.nextInt(30) - 15 + 360 / slopeCount * i;
				slopeAngles[i] += spinAngle;
				slopeAngles[i] %= 360;
			}
			for (int i = 0; i < slopeCount; i++) {
				int height = mountain.getTop().getZ();
				int offset = height / 2 > 0 ? random.nextInt(height / 2) : 1;
				Vector vector = new Vector(mountain.getTop().getX(), mountain.getTop().getY(), (float) slopeAngles[i], height + offset);
				mountain.addCorner(vector.getEndPoint());
			}
		}
	}

	private void applyOffsetVectors(Edge edge) {
		Vector edgeOffsetVector = edge.getOffsetVector();
		if (edgeOffsetVector != null) {
			Position endPoint = edgeOffsetVector.getEndPoint();
			for (int i = 0; i < edge.getMountains().size(); i++) {
				Mountain mountain = edge.getMountains().get(i);
				int xRand = random.nextInt(2) - 1;
				int yRand = random.nextInt(2) - 1;
				Vector topOffsetVector = new Vector(edgeOffsetVector.getStartPoint().getX(), edgeOffsetVector.getStartPoint().getY(), endPoint.getX() + xRand, endPoint.getY() + yRand);
				if (Double.isNaN(topOffsetVector.getAngle())) {
					topOffsetVector.setAngle(edgeOffsetVector.getAngle());
				}
				double radians = Math.PI * i / edge.getMountains().size();
				topOffsetVector.setLength((topOffsetVector.getLength() * Math.sin(radians) * topOffsetModifier));
				mountain.setTop(mountain.getTop().addVector(topOffsetVector));
			}
		}
	}
}