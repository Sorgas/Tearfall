package stonering.generators.worldgen.generators.elevation;

import stonering.generators.worldgen.WorldGenConfig;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.AbstractGenerator;
import stonering.generators.worldgen.world_objects.Edge;
import stonering.generators.worldgen.world_objects.Mountain;
import stonering.global.utils.Position;
import stonering.global.utils.Vector;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexander on 03.03.2017.
 *
 * Generates deep cavities for oceans and seas. valleys are generated where plates move away from another
 */
public class ValleyGenerator extends AbstractGenerator {
	private Random random;
	private List<Edge> edges;
	private float plateSpeedToDepthModifier;
	private float topOffsetModifier;
	private int topsDensity;
	private float worldBorderDepth;

	public ValleyGenerator(WorldGenContainer container) {
		super(container);
	}

	private void extractContainer() {
		WorldGenConfig config = container.getConfig();
		random = config.getRandom();
		plateSpeedToDepthModifier = config.getPlateSpeedToDepthModifier();
		topOffsetModifier = config.getTopOffsetModifier();
		topsDensity = config.getValleysTopsDensity();
		worldBorderDepth = config.getWorldBorderDepth();
		edges = container.getEdges();
	}

	@Override
	public boolean execute() {
		System.out.println("generating valleys");
		extractContainer();
		for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext(); ) {
			Edge edge = iterator.next();
			edge.getValleys().clear();
			configureEdge(edge);
			createValleyDepths(edge);
			applyOffsetVectors(edge);
			createValley(edge);
		}
		return false;
	}

	private void createTops(Edge edge) {
		Position pos1 = edge.getPoint1().clone();
		Position pos2 = edge.getPoint2().clone();
		int length = (int) Math.round(Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2) + Math.pow(pos1.getY() - pos2.getY(), 2)));
		float extentX = (pos1.getX() - pos2.getX()) * 0.5f;
		float extentY = (pos1.getY() - pos2.getY()) * 0.5f;
		pos1.setX((int) (pos1.getX() + extentX));
		pos1.setY((int) (pos1.getY() + extentY));
		pos2.setX((int) (pos2.getX() - extentX));
		pos2.setY((int) (pos2.getY() - extentY));
		int num = length / topsDensity;
		if (num > 0) {
			float xDensity = (pos2.getX() - pos1.getX()) / (float) num;
			float yDensity = (pos2.getY() - pos1.getY()) / (float) num;
			int xStart = pos1.getX();
			int yStart = pos1.getY();
			float x = xStart + xDensity / 2;
			float y = yStart + yDensity / 2;
			for (int i = 0; i < num; i++) {
				Mountain valley = new Mountain();
				valley.setTop(new Position(Math.round(x), Math.round(y), 0));
				edge.addValley(valley);
				x += xDensity;
				y += yDensity;
			}
		}
	}

	private void configureEdge(Edge edge) {
		if (edge.isWorldBorder()) {
			createTops(edge);
			float maxDepth = worldBorderDepth;
			maxDepth *= plateSpeedToDepthModifier;
			edge.setPikeHeight(maxDepth);
		} else {
			if (edge.getDynamics().get(0) < 0 && edge.getDynamics().get(1) < 0) { //if both plates move from the edge
				createTops(edge);
				int speed1 = Math.min(edge.getDynamics().get(0), edge.getDynamics().get(1));
				int speed2 = Math.max(edge.getDynamics().get(0), edge.getDynamics().get(1));


				int maxDepth = speed1 + speed2;
				maxDepth *= plateSpeedToDepthModifier;
				edge.setPikeHeight(maxDepth);

				int topOffset = speed1;
				topOffset -= speed2;

				Vector edgeVector = new Vector(edge.getPoint1().getX(), edge.getPoint1().getY(), edge.getPoint2().getX(), edge.getPoint2().getY());
				edge.setOffsetVector(new Vector(edgeVector.getX(), edgeVector.getY(), edgeVector.getAngle() + 180 * Math.signum(speed1 - speed2), topOffset));
			}
		}
	}

	private void createValleyDepths(Edge edge) {
		List<Mountain> valleys = edge.getValleys();
		for (int i = 0; i < valleys.size(); i++) {
			double radians = Math.PI * i / edge.getValleys().size();
			valleys.get(i).getTop().setZ((int) Math.round((edge.getPikeHeight() + 8) * Math.sin(radians) - random.nextInt(3) - 11));
		}
	}

	private void applyOffsetVectors(Edge edge) {
		Vector edgeOffsetVector = edge.getOffsetVector();
		if (edgeOffsetVector != null) {
			Position endPoint = edgeOffsetVector.getEndPoint();
			for (int i = 0; i < edge.getValleys().size(); i++) {
				Mountain valley = edge.getValleys().get(i);
				int xRand = random.nextInt(2) - 1;
				int yRand = random.nextInt(2) - 1;
				Vector topOffsetVector = new Vector(edgeOffsetVector.getStartPoint().getX(), edgeOffsetVector.getStartPoint().getY(), endPoint.getX() + xRand, endPoint.getY() + yRand);
				if (Double.isNaN(topOffsetVector.getAngle())) {
					topOffsetVector.setAngle(edgeOffsetVector.getAngle());
				}
				double radians = Math.PI * i / edge.getValleys().size();
				topOffsetVector.setLength((topOffsetVector.getLength() * Math.sin(radians) * topOffsetModifier));
				valley.setTop(valley.getTop().addVector(topOffsetVector));
			}
		}
	}

	private void createValley(Edge edge) {
		float radiusModifier = 1;
		if (edge.isWorldBorder()) {
			radiusModifier = 3;
		}
		List<Mountain> valleys = edge.getValleys();
		for (Iterator<Mountain> iterator = valleys.iterator(); iterator.hasNext(); ) {
			Mountain valley = iterator.next();
			int slopeCount = random.nextInt(2) + 6;
			int[] slopeAngles = new int[slopeCount];
			int spinAngle = random.nextInt(360);
			for (int i = 0; i < slopeCount; i++) {
				slopeAngles[i] = random.nextInt(30) - 15 + 360 / slopeCount * i;
				slopeAngles[i] += spinAngle;
				slopeAngles[i] %= 360;
			}
			for (int i = 0; i < slopeCount; i++) {
				int radius = valley.getTop().getZ() * 2;
				int offset = radius / 2 > 0 ? random.nextInt(radius / 2) : 1;
				Vector vector = new Vector(valley.getTop().getX(), valley.getTop().getY(), (float) slopeAngles[i], radius * radiusModifier + offset);
				valley.addCorner(vector.getEndPoint());
			}
		}
	}
}
