package stonering.generators.worldgen.generators.drainage;

import com.badlogic.gdx.math.Vector2;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.WorldGenerator;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.Random;

/**
 * Generates brooks.
 * //TODO add water amount to brooks. see {@link RiverGenerator}.
 *
 * @author Alexander Kuzyakov on 18.01.2018.
 */
public class BrookGenerator extends WorldGenerator {
    private int width;
    private int height;
    private float seaLevel;
    private Vector2[][] slopes;

    @Override
    public void set(WorldGenContainer container) {
        width = container.config.width;
        height = container.config.height;
        seaLevel = container.config.seaLevel;
        slopes = new Vector2[width][height];
    }

    @Override
    public void run() {
        System.out.println("generating brooks");
        countAngles();
        createBrookStartPositions().forEach(position -> {
            addBrookToContainer(runBrook(position));
        });
    }

    private Brook runBrook(Position start) {
        Brook brook = new Brook();
        int x = start.x;
        int y = start.y;
        Vector2 curVector2 = slopes[x][y];

        while (container.inMap(x, y) &&
                curVector2 != null &&
                container.getElevation(x, y) > seaLevel &&
                container.getRiver(x, y) == null) {
            Position position = new Position(x, y, 0);
            if(brook.positions.contains(position)) { //looped brook
                break;
            }
            brook.positions.add(position);
            x += Math.round(curVector2.x);
            y += Math.round(curVector2.y);
            if(!container.inMap(x,y)) {
                break;
            }
            curVector2 = slopes[x][y];
        }
        return brook;
    }

    private void addBrookToContainer(Brook brook) {
        for (int i = 0; i < brook.positions.size(); i++) {
            Position position = brook.positions.get(i);
//            Vector2 vector2 = brook.vectors.get(i);
            container.setBrook(position.x, position.y, slopes[position.x][position.y].cpy());
        }
    }

    private void countAngles() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (container.getElevation(x, y) > seaLevel) {
                    slopes[x][y] = countSlopeAngle(x, y);
                }
            }
        }
    }

    private Vector2 countSlopeAngle(int cx, int cy) {
        float centerElevation = container.getElevation(cx, cy);
        Vector2 vector = new Vector2();
        for (int x = cx - 1; x <= cx + 1; x++) {
            for (int y = cy - 1; y <= cy + 1; y++) {
                if (container.inMap(x, y) && !(x == cx && y == cy)) { // elevation decreases in this direction
                    float elevationDelta = centerElevation - container.getElevation(x, y);
                    vector.add((x - cx) * elevationDelta, (y - cy) * elevationDelta);
                }
            }
        }
        return vector.nor();
    }

    /**
     * Determines all vectors to start brooks from.
     * Dry areas have lower density of brooks(and pools) but not rivers.
     *
     * @return
     */
    private ArrayList<Position> createBrookStartPositions() {
        Random random = new Random();
        ArrayList<Position> positions = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (container.getElevation(x, y) > seaLevel && container.getRiver(x, y) == null) {
                    if (random.nextInt(120) < container.getRainfall(x, y)) {
                        positions.add(new Position(x, y, 0));
                    }
                }
            }
        }
        return positions;
    }

    private class Brook {
//        ArrayList<Vector2> vectors;
        ArrayList<Position> positions;

        public Brook() {
//            vectors = new ArrayList<>();
            positions = new ArrayList<>();
        }
    }
}
