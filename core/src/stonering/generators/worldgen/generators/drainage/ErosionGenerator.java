package stonering.generators.worldgen.generators.drainage;

import com.badlogic.gdx.math.Vector2;

import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.WorldGenerator;
import stonering.util.geometry.Int2dBounds;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates erosion effect on worldMap.
 *
 * @author Alexander Kuzyakov on 21.01.2018.
 */
public class ErosionGenerator extends WorldGenerator {
    private int width;
    private int height;
    private int expandedWidth;
    private int expandedHeight;
    private int maxSteps = 200;
    private float dropCapacity = 6;
    private float dropPickupRadius = 1;
    private float dropInertia = 0.5f;
    private float erosion = 0.1f;
    private float deposition = 0.1f;
    private float evaporation = 0.015f;
    private float minElevationDelta = 0;
    private float[][] elevation;
    private final int expansionWidth = 10;
    private Int2dBounds bounds;
    private Int2dBounds expandedBounds;

    private List<Drop> drops = new ArrayList<>();

    @Override
    public void set(WorldGenContainer container) {
        width = container.config.width;
        height = container.config.height;
        bounds = new Int2dBounds(0, 0, width - 1, height - 1);
    }

    @Override
    public void run() {
        System.out.println("generating erosion");
        expandMap();
        putDrops();
        runDrops();
        reduceMap();
    }

    private void expandMap() {
        expandedWidth = width + expansionWidth * 2;
        expandedHeight = height + expansionWidth * 2;
        expandedBounds = new Int2dBounds(0, 0, expandedWidth - 1, expandedHeight - 1);
        elevation = new float[expandedWidth][expandedWidth];
        bounds.iterate((x, y) -> elevation[x + expansionWidth][y + expansionWidth] = container.elevation[x][y]);
    }

    private void reduceMap() {
        bounds.iterate((x, y) -> container.elevation[x][y] = elevation[x + expansionWidth][y + expansionWidth]);
    }

    /**
     * creates drops on every point of map above sea level
     */
    private void putDrops() {
        expandedBounds.iterate((x, y) -> {
            if (elevation[x][y] > 0) {
                drops.add(new Drop(x, y));
            }
        });
    }

    /**
     * cycles through drops collection and moves them
     */
    private void runDrops() {
        for (Drop drop : drops) {
            for (int i = 0; i < maxSteps; i++) {
                moveDrop(drop);
                if (outOfMap(drop.x, drop.y)) break;
            }
        }
    }

    /**
     * main method of simulation. simulates 1 step.
     *
     * @param drop drop to simulate
     */
    private void moveDrop(Drop drop) {
        Vector2 slope = countSlopeVector(drop.x, drop.y);

        //apply slope to direction and normalize
        drop.direction = applyInertia(drop.direction, slope).nor();

        //count new drop position
        float endX = drop.x + drop.direction.x;
        float endY = drop.y + drop.direction.y;

        // count elevation delta. positive for moving uphill
        float elevationDelta = elevation[Math.round(endX)][Math.round(endY)] - elevation[Math.round(drop.x)][Math.round(drop.y)];

        // update velocity
        float velocityEstimation = (float) (Math.pow(drop.velocity, 2) - elevationDelta);
        drop.velocity = (float) Math.sqrt((velocityEstimation >= 0) ? velocityEstimation : 0);

        if (elevationDelta > 0) { //uphill, fill pit
            float sedChange = Math.min(drop.sediment, elevationDelta);
            depose(Math.round(drop.x), Math.round(drop.y), sedChange);
            drop.sediment -= sedChange;
        } else { //downhill, regular behavior
            // change capacity
            float capacity = (Math.max(-elevationDelta, minElevationDelta) * drop.velocity * drop.water * dropCapacity);
            if (drop.sediment > capacity) {
                //reduce sediment and depose
                float sedChange = (drop.sediment - capacity) * deposition;
                drop.sediment -= sedChange;
                depose(Math.round(drop.x), Math.round(drop.y), sedChange);
            } else {
                //gain sediment and erose
                float sedChange = Math.min((capacity - drop.sediment) * erosion, -elevationDelta);
                drop.sediment += sedChange;
                erode(Math.round(drop.x), Math.round(drop.y), sedChange);
            }
        }

        // update water
        drop.water = drop.water * (1 - evaporation);
        drop.x = endX;
        drop.y = endY;
    }

    private void erode(int x, int y, float amount) {
        elevation[x][y] = elevation[x][y] - amount;
    }

    private void depose(int x, int y, float amount) {
        elevation[x][y] = elevation[x][y] + amount;
    }

    /**
     * counts normalized vector of a slope
     *
     * @param cx slope center x
     * @param cy slope center y
     * @return vector, pointing downhill
     */
    private Vector2 countSlopeVector(float cx, float cy) {
        float centerElevation = elevation[Math.round(cx)][Math.round(cy)];
        Vector2 vector = new Vector2();
        for (int x = Math.round(cx) - 1; x <= cx + 1; x++) {
            for (int y = Math.round(cy) - 1; y <= cy + 1; y++) {
                if (outOfMap(x, y)) { // elevation decreases in this direction
                    float elevationDelta = centerElevation - elevation[x][y];
                    vector.add((x - cx) * ((elevationDelta)), (y - cy) * ((elevationDelta)));
                }
            }
        }
        return vector.nor();
    }

    /**
     * applyes slope vector to direction vector, taking inertia in account
     *
     * @param speed direction of drop
     * @param slope vector of slope inclination
     * @return new direction vector
     */
    private Vector2 applyInertia(Vector2 speed, Vector2 slope) {
        speed.scl(dropInertia);
        slope.scl(1 - dropInertia);
        return speed.add(slope);
    }

    private boolean outOfMap(float x, float y) {
        return x < 0 || y < 0 || x >= expandedWidth || y >= expandedHeight;
    }

    /**
     * Represents unit of waterflow. Has direction. Can carry sediment, evaporates over time.
     */
    private static class Drop {
        float x;
        float y;
        Vector2 direction;
        float velocity;
        float sediment;
        float water;

        public Drop(float x, float y) {
            this.x = x;
            this.y = y;
            water = 1;
            sediment = 0;
            direction = new Vector2(0, 0);
            velocity = 0;
        }

    }
}
