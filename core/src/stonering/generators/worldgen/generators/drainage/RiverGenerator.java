package stonering.generators.worldgen.generators.drainage;

import com.badlogic.gdx.math.Vector2;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.WorldGenerator;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Alexander Kuzyakov on 14.03.2017.
 * <p>
 * Generates rivers. Marks lakes if river stucks.
 * Rivers start from distributed point in high areas.
 */
public class RiverGenerator extends WorldGenerator {
    private Random random;
    private int width;
    private int height;
    private Vector2[][] slopeInclination;
    private Vector2[][] endPoints;
    private Vector2[][] inflows;
    private Vector2[][] riverVectors;
    private float[][] waterAmount;
    private float seaLevel;
    private float riverStartLevel;

    @Override
    public void set(WorldGenContainer container) {
        random = container.random;
        width = container.config.width;
        height = container.config.height;
        slopeInclination = new Vector2[width][height];
        endPoints = new Vector2[width][height];
        inflows = new Vector2[width][height];
        riverVectors = new Vector2[width][height];
        waterAmount = new float[width][height];
        seaLevel = container.config.seaLevel;
        riverStartLevel = container.config.largeRiverStartLevel;
    }

    @Override
    public void run() {
        System.out.println("generating rivers");
        countAngles();
        countWaterAmount();
        countRiverVectors();
        elevationStartPoints().forEach(this::runRiverFromStart);
    }

    private void countAngles() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2 slope = countSlopeAngle(x, y);
                slopeInclination[x][y] = slope;
                endPoints[x][y] = new Vector2(Math.round(x + slope.x), Math.round(y + slope.y)); // ok
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

    /**
     * Creates map of amounts of water passing through each cell;
     * Initial amount of water is based on rainfall;
     */
    private void countWaterAmount() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (container.getElevation(x, y) > seaLevel) {
                    float amount = container.getRainfall(x, y) / 10000f;
                    Vector2 current = new Vector2(x, y);
                    //run flow to modify water amount in tiles
                    do {
                        waterAmount[(int) current.x][(int) current.y] += amount;
                        current.add(slopeInclination[x][y]);
                    } while (container.inMap(current.x, current.y));
                }
            }
        }
    }

    private void countRiverVectors() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (container.getElevation(x, y) > seaLevel) {
                    riverVectors[x][y] = slopeInclination[x][y].cpy();
                    riverVectors[x][y].setLength(waterAmount[x][y]);
                }
            }
        }
    }

    /**
     * Creates collection of positions to start rivers from.
     *
     * @return
     */
    private ArrayList<Vector2> elevationStartPoints() {
        ArrayList<Vector2> starts = new ArrayList<>();
        ArrayList<Vector2> potentialStarts = new ArrayList<>();
        for (int x = 0; x < width; x++) { // select all points above riverStartLevel
            for (int y = 0; y < height; y++) {
                if (container.getElevation(x, y) > riverStartLevel) {
                    potentialStarts.add(new Vector2(x, y));
                }
            }
        }
        while (!potentialStarts.isEmpty()) { // select 1 random start and remove all near it
            Vector2 start = potentialStarts.get(random.nextInt(potentialStarts.size()));
            starts.add(start);
            potentialStarts.removeIf(qwer -> qwer.cpy().sub(start).len() < 7);
        }
        return starts;
    }

    private void runRiverFromStart(Vector2 start) {
        ArrayList<Vector2> river = new ArrayList<>();
        int length = 0;
        while (length < 100) {
            int x = Math.round(start.x);
            int y = Math.round(start.y);
            if(!container.inMap(x,y)) {
                break;
            }
            if(river.contains(riverVectors[x][y])) { //loop, add lake
                container.getLakes().add(new Position(x,y,0));
                break;
            }
            if (container.getElevation(x, y) <= seaLevel) { //sea reached
                break;
            }
            river.add(riverVectors[x][y]);
            container.setRiver(x, y, riverVectors[x][y]);
            start = endPoints[x][y];
            length++;
        }
    }

    private void countInflows() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (container.getElevation(x, y) > seaLevel) {
                    inflows[x][y] = lookupMainInflow(x, y);
                }
            }
        }
    }

    private ArrayList<Vector2> lookupInflows(int cx, int cy) {
        ArrayList<Vector2> inflowsArray = new ArrayList<>();
        for (int x = cx - 1; x <= cx + 1; x++) {
            for (int y = cy - 1; y <= cy + 1; y++) {
                if ((x != cx || y != cx) // not center
                        && container.inMap(x, y)   // not out of map
                        && endPoints[x][y].x == cx
                        && endPoints[x][y].y == cy)  //is inflow
                {
                    inflowsArray.add(new Vector2(x, y));
                }
            }
        }
        return inflowsArray;
    }

    private Vector2 lookupMainInflow(int cx, int cy) {
        int maxX = -2;
        int maxY = -2;
        for (int x = cx - 1; x <= cx + 1; x++) {
            for (int y = cy - 1; y <= cy + 1; y++) {
                if ((x != cx || y != cx) // not center
                        && container.inMap(x, y)   // not out of map
                        && endPoints[x][y].x == cx
                        && endPoints[x][y].y == cy  //is inflow
                        && (maxX < -1 || waterAmount[maxX][maxY] < waterAmount[x][y])) { // is first or greater inflow
                    maxX = x;
                    maxY = y;
                }
            }
        }
        return maxX >= -1 ? new Vector2(maxX - cx, maxY - cy) : null;
    }

    private ArrayList<Vector2> findRiversOuts() {
        ArrayList<Vector2> outs = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (riverVectors[x][y] != null) {
                    if (container.getElevation(Math.round(endPoints[x][y].x), Math.round(endPoints[x][y].y)) < seaLevel) { // river goes to sea
                        outs.add(new Vector2(x, y));
                    }
                }
            }
        }
        return outs;
    }

    private void runRiverFromEnd(Vector2 end) {
        ArrayList<Vector2> lookupList = new ArrayList<>();
        ArrayList<Vector2> riverPoints = new ArrayList<>();
        lookupList.add(end);
        while (lookupList.size() > 0) {
            Vector2 point = lookupList.remove(0);
            if (!riverPoints.contains(point)) {
                riverPoints.add(point);
                int x = Math.round(point.x);
                int y = Math.round(point.y);
                container.setRiver(x, y, riverVectors[x][y]);
                lookupList.addAll(lookupInflows(x, y));
            }
        }
    }

    private boolean hasNearSea(int cx, int cy) {
        for (int x = cx - 1; x <= cx + 1; x++) {
            for (int y = cy - 1; y <= cy + 1; y++) {
                if (container.inMap(x, y)
                        && (x != cx || y != cy)
                        && container.getElevation(x, y) < seaLevel) {
                    return true;
                }
            }
        }
        return false;
    }
}