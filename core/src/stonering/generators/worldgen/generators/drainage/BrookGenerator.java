package stonering.generators.worldgen.generators.drainage;

import com.badlogic.gdx.math.Vector2;
import stonering.generators.worldgen.WorldGenContainer;
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
    private int width;
    private int height;
    private float seaLevel;
    private Vector2[][] slopes;

    public BrookGenerator(WorldGenContainer container) {
        super(container);
    }

    private void extractContainer(WorldGenContainer container) {
        width = container.getConfig().getWidth();
        height = container.getConfig().getHeight();
        seaLevel = container.getConfig().getSeaLevel();
        slopes = new Vector2[width][height];
    }

    public boolean execute() {
        System.out.println("generating brooks");
        extractContainer(container);
        countAngles();
        createBrookStartPositions().forEach(position -> {
            addBrookToContainer(runBrook(position));
        });
        return false;
    }

    private Brook runBrook(Position start) {
        Brook brook = new Brook();
        int x = start.getX();
        int y = start.getY();
        Vector2 curVector2 = slopes[x][y];

        while (container.inMap(x, y) &&
                curVector2 != null &&
                container.getElevation(x, y) > seaLevel &&
                container.getRiver(x, y) == null) {
//            brook.vectors.add(curVector2.cpy());

            Position position = new Position(x, y, 0);
            if(brook.positions.contains(position)) { //looped brook
                break;
            }
            brook.positions.add(position);
            System.out.println("brook point: " + x + " " + y);
            x += Math.round(curVector2.x);
            y += Math.round(curVector2.y);
            curVector2 = slopes[x][y];
        }
        return brook;
    }

    private void addBrookToContainer(Brook brook) {
        for (int i = 0; i < brook.positions.size(); i++) {
            Position position = brook.positions.get(i);
//            Vector2 vector2 = brook.vectors.get(i);
            container.setBrook(position.getX(), position.getY(), slopes[position.getX()][position.getY()].cpy());
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
