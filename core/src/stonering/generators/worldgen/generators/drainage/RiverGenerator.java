package stonering.generators.worldgen.generators.drainage;

import com.badlogic.gdx.math.Vector2;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.generators.worldgen.generators.AbstractGenerator;
import stonering.global.utils.Position;

import java.util.*;

/**
 * Created by Alexander on 14.03.2017.
 * <p>
 * Generates river.
 * For rivers additional elevation map (smoothed) is used. The map of slope vectors are based on it.
 * Rivers start from points close to mountains pikes, distributed with interspaces.
 * River has its own vector, which is modified by slope vectors on every passing cell.
 */
public class RiverGenerator extends AbstractGenerator {
    private Random random;
    private WorldMap map;
    private int width;
    private int height;
    private int riverCount;
    private Vector2[][] slopeInclination;
    private Vector2[][] inflows;
    private Vector2[][] riverVectors;
    private float[][] waterAmount;
    private List<Position> cells;
    private float seaLevel;

    public RiverGenerator(WorldGenContainer container) {
        super(container);
    }

    private void extractContainer(WorldGenContainer container) {
        random = container.getConfig().getRandom();
        width = container.getConfig().getWidth();
        height = container.getConfig().getHeight();
        riverCount = (int) (width * height * container.getLandPart() / container.getConfig().getRiverDensity());
        System.out.println(container.getLandPart() + "  " + riverCount);
        slopeInclination = new Vector2[width][height];
        inflows = new Vector2[width][height];
        riverVectors = new Vector2[width][height];
        waterAmount = new float[width][height];
        cells = new ArrayList<>();
        seaLevel = container.getConfig().getSeaLevel();
    }

    @Override
    public boolean execute() {
        extractContainer(container);
        System.out.println("generating rivers");
        map = container.getMap();
        countAngles();
        runWater();
        return false;
    }

    private void runWater() {
        //count water amount
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (container.getElevation(x, y) * 3f > seaLevel) {
                    float amount = getWaterAmount(x, y);
                    Vector2 current = new Vector2(x, y);
                    //run flow
                    do {
                        waterAmount[(int) current.x][(int) current.y] += amount;
                        current.add(slopeInclination[x][y]);
                    } while (container.inMap(current.x, current.y));
                }
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (container.getElevation(x, y) * 3f > seaLevel) {
                    inflows[x][y] = lookupMainInflow(x, y);
                }
            }
        }
        // create river vectors
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (container.getElevation(x, y) * 3f > seaLevel) {
//                    if (inflows[x][y] != null) {
                        riverVectors[x][y] = slopeInclination[x][y].cpy();
//                        riverVectors[x][y].setLength(waterAmount[x][y]);
                        map.setRiver(x, y, riverVectors[x][y]);
//                    } else {
//
//                    }
                }
            }
        }
    }

    private float getWaterAmount(int x, int y) {
        return 0.01f;
//        return container.getRainfall(x, y) / 10000f;
    }

    private void markMainReavers() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

            }
        }
    }

    private Vector2 lookupMainInflow(int cx, int cy) {
        int curX = -2;
        int curY = -2;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int x = cx + dx;
                int y = cy + dy;
                if (inMap(x, y)                                                         // not out of map
                        && Math.round(slopeInclination[x][y].x) == -dx
                        && Math.round(slopeInclination[x][y].y) == -dy) {                  // is income to current cell
                    if (curX < -1                                                     // inflow not found yet
                            || waterAmount[x][y] > waterAmount[curX][curY]) {               // inflow has more water than previous
                        // set new main inflow
                        curX = x;
                        curY = y;
                    }
                }
            }
        }
        return curX >= -1 ? new Vector2(cx - curX, cy - curY) : null;
    }

    private void countRiverStart() {
        TreeSet<Position> sortedCells = new TreeSet<>(new ElevationComparator());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                sortedCells.add(new Position(x, y, Math.round(waterAmount[x][y])));
            }
        }
        int count = 0;
        cells.clear();
        for (Iterator<Position> iterator = sortedCells.iterator(); count < riverCount && iterator.hasNext(); ) {
            Position riverStart = iterator.next();
            boolean rejected = false;
            for (Iterator<Position> cellsIterator = cells.iterator(); cellsIterator.hasNext(); ) {
                if (isNear(riverStart, cellsIterator.next(), 18)) {
                    rejected = true;
                    break;
                }
            }
            if (!rejected) {
                cells.add(riverStart);
                count++;
            }
        }
    }

    private void runRiver(int x, int y, int maxLength, int branchingDepth) {
//        int i = 0;
//        float seaLevel = container.getConfig().getSeaLevel() - 1;
//        int savedAngle = 0;
//        if (!inMap(x, y)) return;
//        Vector riverVector = new Vector(x, y, container.getSlopeAngles(x, y), 2.0f);
//        int turningCounter = 0;
//        while (i < maxLength && container.getElevation(x, y) > seaLevel && inMap(x, y)) {
//
//            float curElevation = waterAmount[x][y]; // getting elevation in current point
//
//            if (turningCounter == 0) {  // starting river turn
//                turningCounter = random.nextInt(14);
//            }
//
//            Vector slopeVector = new Vector(0, 0, container.getSlopeAngles(x, y), 1); // getting slope vector
//            riverVector = riverVector.sum(slopeVector); // applying slope to river
//            riverVector.setLength(riverVector.getLength() / 2); // decreasing river speed
//            if (turningCounter != 0) { // turning river
//                int mod = Math.round(Math.copySign(1, turningCounter - 7));
//                riverVector.setAngle((riverVector.getAngle() + 15 * mod + 360) % 360);
//                turningCounter--;
//            }
//            // converting river angle to x45, and saving difference
//            riverVector.setAngle((riverVector.getAngle() + savedAngle + 360) % 360);
//            int targetAngle = ((int) ((riverVector.getAngle() + 22.5f + 360) % 360) / 45);
//            targetAngle *= 45;
//            savedAngle = (int) ((riverVector.getAngle() - targetAngle + 360) % 360);
//
//            //branching river
//            if (i > 6 && branchingDepth > 0) {
//                if (random.nextInt(100) < 15) {
//                    targetAngle = (targetAngle + 45) % 360;
//                    savedAngle = 0;
//                    int branchAngle = (targetAngle - 90 + 360) % 360;
//                    int bx = x + getXProject(branchAngle);
//                    int by = y + getYProject(branchAngle);
//                    riverVector.setLength(2);
//                    branchingDepth--;
//                    runRiver(bx, by, maxLength, branchingDepth);
//                }
//            }
//
//            riverVector.setAngle(targetAngle);
//            map.addRiverVector(new Vector(x, y, x + getXProject(targetAngle), y + getYProject(targetAngle)));  // set river in current point
//            x += getXProject(targetAngle); // getting next river point
//            y += getYProject(targetAngle);
//            if (!inMap(x, y) || map.getRivers().containsKey(new Position(x, y, 0)) || (waterAmount[x][y] - curElevation > 0.3f)) {
//                break;
//            }
//            i++;
//        }
    }

    private boolean isNear(Position pos1, Position pos2, float limit) {
        double distance = countDistance(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY());
        return distance < limit;
    }

    private int getXProject(float angle) {
        if ((angle < 62.5) || (angle > 292.5)) return 1;
        if ((angle > 112.5) && (angle < 247.5)) return -1;
        return 0;
    }

    private int getYProject(float angle) {
        if (angle > 22.5 && angle < 137.5) return 1;
        if (angle > 202.5 && angle < 337.5) return -1;
        return 0;
    }

    private boolean inMap(int x, int y) {
        if (x < 0 || x >= width) return false;
        if (y < 0 || y >= height) return false;
        return true;
    }

    private void countAngles() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                slopeInclination[x][y] = countSlopeAngle(x, y);
            }
        }
    }

    private Vector2 countSlopeAngle(float cx, float cy) {
        float centerElevation = container.getElevation(Math.round(cx), Math.round(cy));
        Vector2 vector = new Vector2();
        for (int x = Math.round(cx) - 1; x <= cx + 1; x++) {
            for (int y = Math.round(cy) - 1; y <= cy + 1; y++) {
                if (container.inMap(x, y)) { // elevation decreases in this direction
                    float elevationDelta = centerElevation - container.getElevation(x, y);
                    vector.add((x - cx) * ((elevationDelta)), (y - cy) * ((elevationDelta)));
                }
            }
        }
        return vector.nor();
    }

    private float countDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private float countMiddleElevation(int x, int y, int radius) {
        int minX = x - radius;
        int maxX = x + radius + 1;
        int minY = y - radius;
        int maxY = y + radius + 1;
        float sum = 0;
        if (minX < 0) minX = 0;
        if (maxX > width) maxX = width;
        if (minY < 0) minY = 0;
        if (maxY > height) maxY = height;
        for (int i = minX; i < maxX; i++) {
            for (int j = minY; j < maxY; j++) {
                sum += waterAmount[i][j];
            }
        }
        return sum / ((maxX - minX) * (maxY - minY));
    }

    private class ElevationComparator implements Comparator<Position> {
        @Override
        public int compare(Position o1, Position o2) {
            int value = 0;
            if (o1 != null && o2 != null) {
                value = o2.getZ() - o1.getZ();
                if (value == 0) {
                    value = 1;
                }
            }
            return value;
        }
    }
}