package com.menu.worldgen.generators.world.generators.elevation;

import com.menu.worldgen.generators.world.generators.AbstractGenerator;
import com.menu.worldgen.generators.world.WorldGenContainer;
import com.menu.worldgen.generators.world.world_objects.Edge;
import com.menu.worldgen.generators.world.world_objects.Mountain;
import com.utils.Position;
import com.utils.Vector;

import java.util.List;
import java.util.Random;

/**
 * Created by Alexander on 28.02.2017.
 */
public class MountainGenerator extends AbstractGenerator {
	private Random random;
	int height;
	int width;
	private List<Edge> edges;
	private float plateSpeedToHeightModifier;
	private float topOffsetModifier;
	private int topsDensity;

	public MountainGenerator(WorldGenContainer container) {
		super(container);
	}

	private void extractContainer() {
		random = container.getConfig().getRandom();
		edges = container.getEdges();
		width = container.getConfig().getWidth();
		height = container.getConfig().getHeight();
		topsDensity = container.getConfig().getMountainsTopsDensity();
		plateSpeedToHeightModifier = container.getConfig().getPlateSpeedToHeightModifier();
		topOffsetModifier = container.getConfig().getTopOffsetModifier();
	}

	@Override
	public boolean execute() {
		System.out.println("generating mountains");
		extractContainer();
		for (Edge edge : edges) {
			if (!edge.isWorldBorder()) {
				configureEdge(edge);
				createMountainHeights(edge);
				mergeMountainChains(edge);
				createMountainSlopes(edge);
				applyOffsetVectors(edge);
			}
		}
		return false;
	}

	private void mergeMountainChains(Edge edge) {
		if (edge.getPikeHeight() > 0 && edge.getMountains().size() > 0) {
			for (Edge edge1 : edges) {
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

	private void createTops(Edge edge) {
		Position pos1 = edge.getPoint1();
		Position pos2 = edge.getPoint2();
		int length = (int) Math.round(Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow(pos1.getY() - pos2.getY(), 2)));
		int num = length / topsDensity;
		if (num > 0) {
			float xDensity = (pos2.getX() - pos1.getX()) / (float) num;
			float yDensity = (pos2.getY() - pos1.getY()) / (float) num;
			float x = pos1.getX() + xDensity / 2;
			float y = pos1.getY() + yDensity / 2;
			for (float i = 0; i < num; i++) {
				Position top = new Position(Math.round(x), Math.round(y), 0);
				double radians = Math.PI * i / num;
				top.setZ((int) Math.round(edge.getPikeHeight() * Math.sin(radians) + random.nextInt(3)));
				Vector offsetVector = new Vector(0, 0, 360 * random.nextFloat(), random.nextFloat() * edge.getPikeHeight());
				top = top.addVector(offsetVector);
				normalizePosition(top);

				Mountain mountain = new Mountain();
				mountain.setTop(top);
				mountain.setWidth(top.getZ());
				edge.addMountain(mountain);

				Mountain foothill = new Mountain();
				foothill.setWidth(top.getZ() * 3);
				top.setZ(top.getZ() / 3);
				foothill.setTop(top);
				edge.addMountain(foothill);

				x += xDensity;
				y += yDensity;
			}
		}
	}

	private void configureEdge(Edge edge) {
		if (edge.getDynamics().size() == 2 && !isEdgeOnBorder(edge)) {
			if (edge.getDynamics().get(0) > 0 || edge.getDynamics().get(1) > 0) {
				int mainSpeed = (edge.getDynamics().get(1) > edge.getDynamics().get(0)) ? 1 : 0;
				int secondSpeed = (mainSpeed + 1) % 2;

				float maxHeigth = edge.getDynamics().get(mainSpeed);
				maxHeigth += edge.getDynamics().get(secondSpeed) > 0 ? edge.getDynamics().get(secondSpeed) : 0;
				maxHeigth *= plateSpeedToHeightModifier;
				edge.setPikeHeight(maxHeigth);

				int topOffset = edge.getDynamics().get(mainSpeed);
				if (edge.getDynamics().get(secondSpeed) > 0) topOffset -= edge.getDynamics().get(secondSpeed);
				topOffset *= topOffsetModifier;

				Vector edgeVector = new Vector(edge.getPoint1().getX(), edge.getPoint1().getY(), edge.getPoint2().getX(), edge.getPoint2().getY());
				Vector edgeOffsetVector = edgeVector.getRightVector();
				edgeOffsetVector.setLength(topOffset);
				if (!edgeVector.isAtRight(edge.getVectors().get(mainSpeed).getStartPoint())) {
					edgeOffsetVector.rotate(180);
				}
				edge.setOffsetVector(edgeOffsetVector);
				createTops(edge);
			}
		}
	}

	private void createMountainHeights(Edge edge) {
		List<Mountain> mountains = edge.getMountains();
		if (mountains.size() > 0) {
			for (int i = 0; i < mountains.size(); i++) {

			}
		}
	}

	private void createMountainSlopes(Edge edge) {
		for (Mountain mountain : edge.getMountains()) {
			int slopeCount = (int) (3 + random.nextInt(2) + mountain.getWidth() * 2 * Math.PI / 15);
			int[] slopeAngles = new int[slopeCount];
			int spinAngle = random.nextInt(360);
			for (int i = 0; i < slopeCount; i++) {
				slopeAngles[i] = random.nextInt(30) + 360 / slopeCount * i;
				slopeAngles[i] += spinAngle;
				slopeAngles[i] %= 360;
			}
			for (int i = 0; i < slopeCount; i++) {
				float width = mountain.getWidth() + random.nextInt(3);
				Vector vector = new Vector(mountain.getTop().getX(), mountain.getTop().getY(), (float) slopeAngles[i], width);
				mountain.addCorner(vector.getEndPoint());
			}
		}
	}

	private void applyOffsetVectors(Edge edge) {
		Vector edgeOffsetVector = edge.getOffsetVector();
		if (edgeOffsetVector != null) {
			for (int i = 0; i < edge.getMountains().size(); i++) {
				Mountain mountain = edge.getMountains().get(i);
				Vector topOffsetVector = new Vector(0, 0, 0f, 0f);
				topOffsetVector.setAngle(edgeOffsetVector.getAngle());
				double radians = Math.PI * i / edge.getMountains().size();
				topOffsetVector.setLength(edgeOffsetVector.getLength() * Math.sin(radians));
				mountain.setTop(mountain.getTop().addVector(topOffsetVector));
			}
		}
	}

	private boolean isEdgeOnBorder(Edge edge) {
		return (isPointOnBorder(edge.getPoint1()) || isPointOnBorder(edge.getPoint2()));
	}

	private boolean isPointOnBorder(Position pos) {
		return (pos.getX() <= 0 || pos.getX() >= width || pos.getY() <= 0 || pos.getY() >= height);
	}

	private void normalizePosition(Position pos) {
		if (pos.getX() < 0) pos.setX(0);
		if (pos.getX() >= width) pos.setX(width - 1);
		if (pos.getY() < 0) pos.setY(0);
		if (pos.getY() >= height) pos.setY(height - 1);
	}
}