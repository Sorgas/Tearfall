package stonering.generators.worldgen.generators.elevation;

import com.badlogic.gdx.math.Vector2;
import stonering.generators.worldgen.generators.WorldGenerator;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.entity.world.Edge;
import stonering.entity.world.Mountain;
import stonering.util.geometry.Position;
import stonering.util.geometry.Vector;

import java.util.List;
import java.util.Random;

/**
 * @author Alexander Kuzyakov on 28.02.2017.
 */
public class MountainGenerator extends WorldGenerator {
    private Random random;
    int height;
    int width;
    private List<Edge> edges;
    private float plateSpeedToHeightModifier;
    private float topOffsetModifier;
    private int topsDensity;

    @Override
    public void set(WorldGenContainer container) {
        random = container.random;
//        edges = container.getEdges();
        width = container.config.width;
        height = container.config.height;
        topsDensity = container.config.mountainsTopsDensity;
        plateSpeedToHeightModifier = container.config.plateSpeedToHeightModifier;
        topOffsetModifier = container.config.topOffsetModifier;
    }

    @Override
    public void run() {
        System.out.println("generating mountains");
//		extractContainer();
//		for (Edge edge : edges) {
//			if (!edge.isWorldBorder()) {
//				configureEdge(edge);
//				createMountainHeights(edge);
//				mergeMountainChains(edge);
//				createMountainSlopes(edge);
//				applyOffsetVectors(edge);
//			}
//		}
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
                            edge.getMountains().get(i).getTop().z = (Math.round(height));
                        }
                    } else {
                        edge.getMountains().get(middle).getTop().z = (Math.round(edge.getPikeHeight()));
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
        int length = (int) Math.round(Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2)));
        int num = length / topsDensity;
        if (num > 0) {
            float xDensity = (pos2.x - pos1.x) / (float) num;
            float yDensity = (pos2.y - pos1.y) / (float) num;
            float x = pos1.x + xDensity / 2;
            float y = pos1.y + yDensity / 2;
            for (float i = 0; i < num; i++) {
                Position top = new Position(Math.round(x), Math.round(y), 0);
                double radians = Math.PI * i / num;
                top.z = ((int) Math.round(edge.getPikeHeight() * Math.sin(radians) + random.nextInt(3)));
                Vector offsetVector = new Vector(0, 0, 360 * random.nextFloat(), random.nextFloat() * edge.getPikeHeight());
                top.add(offsetVector);
                normalizePosition(top);

                Mountain mountain = new Mountain();
                mountain.setTop(top);
                mountain.setWidth(top.z);
                edge.addMountain(mountain);

//				Mountain foothill = new Mountain();
//				foothill.setWidth(top.z * 3);
//				top.setZ(top.z / 3);
//				foothill.setTop(top);
//				edge.addMountain(foothill);

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

                Vector edgeVector = new Vector(edge.getPoint1().x, edge.getPoint1().y, edge.getPoint2().x, edge.getPoint2().y);
                Vector edgeOffsetVector = edgeVector.getRightVector();
                edgeOffsetVector.setLength(topOffset);
//				if (!edgeVector.isAtRight(edge.getVectors().get(mainSpeed).getStartPoint())) {
//					edgeOffsetVector.rotate(180);
//				}
//				edge.setOffsetVector(edgeOffsetVector);
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
                Vector vector = new Vector(mountain.getTop().x, mountain.getTop().y, (float) slopeAngles[i], width);
                mountain.addCorner(vector.getEndPoint());
            }
        }
    }

    private void applyOffsetVectors(Edge edge) {
        Vector2 edgeOffsetVector = edge.getOffsetVector();
        if (edgeOffsetVector != null) {
            for (int i = 0; i < edge.getMountains().size(); i++) {
                Mountain mountain = edge.getMountains().get(i);
                Vector topOffsetVector = new Vector(0, 0, 0f, 0f);
                topOffsetVector.setAngle(edgeOffsetVector.angle());
                double radians = Math.PI * i / edge.getMountains().size();
                topOffsetVector.setLength(edgeOffsetVector.len() * Math.sin(radians));
                mountain.getTop().add(topOffsetVector);
            }
        }
    }

    private boolean isEdgeOnBorder(Edge edge) {
        return (isPointOnBorder(edge.getPoint1()) || isPointOnBorder(edge.getPoint2()));
    }

    private boolean isPointOnBorder(Position pos) {
        return (pos.x <= 0 || pos.x >= width || pos.y <= 0 || pos.y >= height);
    }

    private void normalizePosition(Position pos) {
        if (pos.x < 0) pos.x = (0);
        if (pos.x >= width) pos.x = (width - 1);
        if (pos.y < 0) pos.y = (0);
        if (pos.y >= height) pos.y = (height - 1);
    }
}