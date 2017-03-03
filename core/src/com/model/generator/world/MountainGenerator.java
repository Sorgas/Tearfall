package com.model.generator.world;

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
	private int width;
	private int height;
	private int topsDensity = 6;
	private Random random;
	private List<Edge> edges;
	private int plateSpeedToHeightModifier = 6;

	public MountainGenerator(int width, int height, Random random, List<Edge> edges) {
		this.width = width;
		this.height = height;
		this.random = random;
		this.edges = edges;
		System.out.println("mountain generator created");
	}

	public void generateMountains() {
		for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext(); ) {
			Edge edge = iterator.next();
			edge.setWorldBorder(edge.getDynamics().size() != 2);
			edge.getMountains().clear();

			if (!edge.isWorldBorder()) {
				configureEdge(edge);
				createMountainHeights(edge);
				createMountains(edge);
				applyOffsetVectors(edge);
			}
		}
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
		if (edge.getDynamics().size() == 2) {
			if (edge.getDynamics().get(0) > 0 || edge.getDynamics().get(1) > 0) {
				createTops(edge);
				int mainSpeed = (edge.getDynamics().get(1) > edge.getDynamics().get(0)) ? 1 : 0;
				int secondSpeed = mainSpeed == 0 ? 1 : 0;

				int pikeHeight = edge.getDynamics().get(mainSpeed);
				pikeHeight += edge.getDynamics().get(secondSpeed) > 0 ? edge.getDynamics().get(secondSpeed) : 0;
				pikeHeight *= plateSpeedToHeightModifier;
				edge.setPikeHeight(pikeHeight);

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
				Position endPoint = edgeOffsetVector.getEndPoint();
				for (int i = 0; i < edge.getMountains().size(); i++) {
					Mountain mountain = edge.getMountains().get(i);
					int xRand = random.nextInt(2) - 1;
					int yRand = random.nextInt(2) - 1;
					Vector topOffsetVector = new Vector(edgeOffsetVector.getStartPoint().getX(), edgeOffsetVector.getStartPoint().getY(), endPoint.getX() + xRand, endPoint.getY() + yRand);
					if(Double.isNaN(topOffsetVector.getAngle())) {
						topOffsetVector.setAngle(edgeOffsetVector.getAngle());
					}
					double radians = Math.PI * i / edge.getMountains().size();
					topOffsetVector.setLength((topOffsetVector.getLength() * Math.sin(radians)));
					mountain.setTopOffset(topOffsetVector);
				}
			}
		}
	}

	private void createMountainHeights(Edge edge) {
		List<Mountain> mountains = edge.getMountains();
		if (mountains.size() > 0) {
			int bound = mountains.size() / 3;
			int pikeInd = mountains.size() / 3 + random.nextInt(bound > 0 ? bound : 1);
			if (pikeInd != 0) {
				for (int i = 0; i < pikeInd; i++) {
					Mountain mountain = mountains.get(i);
					int height = i * edge.getPikeHeight() / pikeInd;
					height += random.nextInt(5);
					mountain.getTop().setZ(height);
				}
				for (int i = pikeInd; i < mountains.size(); i++) {
					Mountain mountain = mountains.get(i);
					int height = (mountains.size() - i) * edge.getPikeHeight() / (mountains.size() - pikeInd);
					height += random.nextInt(5);
					mountain.getTop().setZ(height);
				}
			} else {
				int height = mountains.get(pikeInd).getTop().getZ();
				mountains.get(pikeInd).getTop().setZ(height + random.nextInt(5));
			}
		}
	}

	private void createMountains(Edge edge) {
		List<Mountain> mountains = edge.getMountains();
		for (Iterator<Mountain> iterator = mountains.iterator(); iterator.hasNext(); ) {
			Mountain mountain = iterator.next();
			int slopeCount = random.nextInt(2) + 4;
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
		List<Mountain> mountains = edge.getMountains();
		for (Iterator<Mountain> iterator = mountains.iterator(); iterator.hasNext(); ) {
			Mountain mountain = iterator.next();
			Vector vector = mountain.getTopOffset();
			if (vector != null) {
				mountain.setTop(mountain.getTop().addVector(vector));
			}
		}
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setTopsDensity(int topsDensity) {
		this.topsDensity = topsDensity;
	}

	public void setPlateSpeedToHeightModifier(int plateSpeedToHeightModifier) {
		this.plateSpeedToHeightModifier = plateSpeedToHeightModifier;
	}
}