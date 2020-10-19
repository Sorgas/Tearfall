package stonering.generators.worldgen.generators.elevation;

import com.badlogic.gdx.math.Vector2;
import stonering.generators.worldgen.WorldGenConfig;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.WorldGenerator;
import stonering.entity.world.Edge;
import stonering.entity.world.Mountain;
import stonering.util.geometry.Position;
import stonering.util.geometry.Vector;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author Alexander Kuzyakov on 03.03.2017.
 * <p>
 * Generates deep cavities for oceans and seas. valleys are generated where plates move away from another
 */
public class ValleyGenerator extends WorldGenerator {
    private Random random;
    private List<Edge> edges;
    private float plateSpeedToDepthModifier;
    private float topOffsetModifier;
    private int topsDensity;
    private float worldBorderDepth;

    @Override
    public void set(WorldGenContainer container) {
        random = container.random;
        plateSpeedToDepthModifier = config.plateSpeedToDepthModifier;
        topOffsetModifier = config.topOffsetModifier;
        topsDensity = config.valleysTopsDensity;
        worldBorderDepth = config.worldBorderDepth;
//        edges = container.getEdges();
    }

    @Override
    public void run() {
        System.out.println("generating valleys");
        for (Edge edge : edges) {
            edge.getValleys().clear();
            configureEdge(edge);
            createValleyDepths(edge);
            applyOffsetVectors(edge);
            createValley(edge);
        }
    }

    private void createTops(Edge edge) {
        Position pos1 = edge.getPoint1().clone();
        Position pos2 = edge.getPoint2().clone();
        int length = (int) Math.round(Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2)));
        float extentX = (pos1.x - pos2.x) * 0.5f;
        float extentY = (pos1.y - pos2.y) * 0.5f;
        pos1.x = (int) (pos1.x + extentX);
        pos1.y = (int) (pos1.y + extentY);
        pos2.x = (int) (pos2.x - extentX);
        pos2.y = (int) (pos2.y - extentY);
        int num = length / topsDensity;
        if (num > 0) {
            float xDensity = (pos2.x - pos1.x) / (float) num;
            float yDensity = (pos2.y - pos1.y) / (float) num;
            int xStart = pos1.x;
            int yStart = pos1.y;
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

                Vector edgeVector = new Vector(edge.getPoint1().x, edge.getPoint1().y, edge.getPoint2().x, edge.getPoint2().y);
                edge.setOffsetVector(new Vector2((float) (edgeVector.getAngle() + 180 * Math.signum(speed1 - speed2) - edgeVector.getX()), topOffset - edgeVector.getY()));
            }
        }
    }

    private void createValleyDepths(Edge edge) {
        List<Mountain> valleys = edge.getValleys();
        for (int i = 0; i < valleys.size(); i++) {
            double radians = Math.PI * i / edge.getValleys().size();
            valleys.get(i).getTop().z = (int) Math.round((edge.getPikeHeight() + 8) * Math.sin(radians) - random.nextInt(3) - 11);
        }
    }

    private void applyOffsetVectors(Edge edge) {
//        Vector2 edgeOffsetVector = edge.getOffsetVector();
//        if (edgeOffsetVector != null) {
//            Position endPoint = edgeOffsetVector.getEndPoint();
//            for (int i = 0; i < edge.getValleys().size(); i++) {
//                Mountain valley = edge.getValleys().get(i);
//                int xRand = random.nextInt(2) - 1;
//                int yRand = random.nextInt(2) - 1;
//                Vector topOffsetVector = new Vector(edgeOffsetVector.getStartPoint().x, edgeOffsetVector.getStartPoint().y, endPoint.x + xRand, endPoint.y + yRand);
//                if (Double.isNaN(topOffsetVector.getAngle())) {
//                    topOffsetVector.setAngle(edgeOffsetVector.getAngle());
//                }
//                double radians = Math.PI * i / edge.getValleys().size();
//                topOffsetVector.setLength((topOffsetVector.getLength() * Math.sin(radians) * topOffsetModifier));
//                valley.setTop(valley.getTop().add(topOffsetVector));
//            }
//        }
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
                int radius = valley.getTop().z * 2;
                int offset = radius / 2 > 0 ? random.nextInt(radius / 2) : 1;
                Vector vector = new Vector(valley.getTop().x, valley.getTop().y, (float) slopeAngles[i], radius * radiusModifier + offset);
                valley.addCorner(vector.getEndPoint());
            }
        }
    }
}
