package stonering.generators.worldgen.generators.drainage;

import com.badlogic.gdx.math.Vector2;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.generators.worldgen.generators.AbstractGenerator;
import stonering.global.utils.Position;

import java.util.ArrayList;
import java.util.Random;

/**
 * Generates brooks
 *
 * @author Alexander Kuzyakov on 18.01.2018.
 */
public class BrookGenerator extends AbstractGenerator {
    private WorldMap map;
    private int width;
    private int height;
    private int[][] points;
    private Vector2[][] endPoints;
    private Vector2[][] brookVectors;

    public BrookGenerator(WorldGenContainer container) {
        super(container);
    }

    private void extractContainer(WorldGenContainer container) {
        width = container.getConfig().getWidth();
        height = container.getConfig().getHeight();
        endPoints = new Vector2[width][height];
        points = new int[width][height];
        map = container.getMap();
    }

    public boolean execute() {
        countAngles();
        createBrookStartPositions().forEach(position -> {
            runBrook(position);
        });
        return false;
    }

    private void runBrook(Position start) {
        int curX = start.getX();
        int curY = start.getY();
        while(map.getElevation(curX,curY) > 0 && map.getRiver(curX, curY) == null) {

        }
    }

    private void countAngles() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map.getElevation(x, y) > 0) {
                    Vector2 slope = countSlopeAngle(x, y);
                    endPoints[x][y] = new Vector2(Math.round(x + slope.x), Math.round(y + slope.y)); // ok
                }
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
     * Determines all positions to start brooks from.
     * Dry areas have lower density of brooks(and pools) but not rivers.
     *
     * @return
     */
    private ArrayList<Position> createBrookStartPositions() {
        Random random = new Random();
        ArrayList<Position> positions = new ArrayList<>();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.getElevation(x, y) > 0 && map.getRiver(x,y) == null) {
                    if (random.nextInt(100) > map.getRainfall(x, y)) {
                        positions.add(new Position(x, y, 0));
                    }
                }
            }
        }
        return null;
    }
}
