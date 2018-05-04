package stonering.generators.worldgen.generators.drainage;

import com.badlogic.gdx.math.Vector2;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.AbstractGenerator;
import stonering.global.utils.Position;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Alexander on 21.01.2018.
 * <p>
 * Generates erosion effect on worldMap.
 */
public class ErosionGenerator extends AbstractGenerator {
    private Random random;
    private WorldGenContainer container;
    private int width;
    private int height;
    private int maxSteps = 200;
    private float dropCapacity = 6;
    private float dropPickupRadius = 1;
    private float dropInertia = 0.5f;
    private float erosion = 0.1f;
    private float deposition = 0.1f;
    private float evaporation = 0.015f;
    private float minElevationDelta = 0;
    private float[][] elevationBuffer;

    private ArrayList<Drop> drops;

    /**
     * Represents waterflow. Has direction. Can carry sediment, evaporates over time.
     */
    private class Drop {
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

    public ErosionGenerator(WorldGenContainer container) {
        super(container);
        extractContainer(container);
        drops = new ArrayList<>();
        elevationBuffer = new float[container.getConfig().getWidth()][container.getConfig().getHeight()];
    }

    private void extractContainer(WorldGenContainer container) {
        this.container = container;
        random = container.getConfig().getRandom();
        width = container.getConfig().getWidth();
        height = container.getConfig().getHeight();
    }

    public boolean execute() {
        System.out.println("generating erosion");
        putDrops();
        runDrops();
        return false;
    }

    /**
     * creates drops on every point of map above sea level
     */
    private void putDrops() {
        if (false) {
            Position maxElevation = findMaxElevation();
            drops.add(new Drop(maxElevation.getX(), maxElevation.getY()));
        } else {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (container.getElevation(x, y) > 0) {
                        drops.add(new Drop(x, y));
                    }
                }
            }
        }
    }

    /**
     * cycles through drops collection and moves them
     */
    private void runDrops() {
        for (Drop drop : drops) {
            for (int i = 0; i < maxSteps; i++) {
                moveDrop(drop);
                if (!container.inMap(drop.x, drop.y)) {
                    break;
                }
            }
        }
    }

    private Position findMaxElevation() {
        int maxX = 0;
        int maxY = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (container.getElevation(x, y) > container.getElevation(maxX, maxY)) {
                    maxX = x;
                    maxY = y;
                }
            }
        }
        return new Position(maxX, maxY, 0);
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
        float elevationDelta = container.getElevation(Math.round(endX), Math.round(endY)) - container.getElevation(Math.round(drop.x), Math.round(drop.y));

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
                erose(Math.round(drop.x), Math.round(drop.y), sedChange);
            }
        }

        // update water
        drop.water = drop.water * (1 - evaporation);
        drop.x = endX;
        drop.y = endY;
    }

    private void erose(int x, int y, float amount) {
        container.setElevation(x, y, container.getElevation(x, y) - amount);
    }

    private void depose(int x, int y, float amount) {
        container.setElevation(x, y, container.getElevation(x, y) + amount);
    }

    private float decreaceElevationInRadius(int dx, int dy, float force) {
        int flooredRadius = (int) Math.floor(dropPickupRadius);
        float total = 0;
        for (int x = dx - flooredRadius; x < dx + flooredRadius; x++) {
            for (int y = dy - flooredRadius; y < dy + flooredRadius; y++) {
                if (container.inMap(x, y)) {
                    float delta = force / (1 + countDistance(x, y, dx, dy));
                    total += delta;
                    container.setElevation(x, y, container.getElevation(x, y) - delta);
                }
            }
        }
        return total;
    }

    /**
     * counts normalized vector of a slope
     *
     * @param cx slope center x
     * @param cy slope center y
     * @return vector, pointing downhill
     */
    private Vector2 countSlopeVector(float cx, float cy) {
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

}