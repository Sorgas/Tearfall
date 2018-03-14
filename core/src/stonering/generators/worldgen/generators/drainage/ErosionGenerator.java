package stonering.generators.worldgen.generators.drainage;

import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.generators.worldgen.generators.AbstractGenerator;
import stonering.global.utils.Position;
import stonering.global.utils.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexander on 21.01.2018.
 */
public class ErosionGenerator extends AbstractGenerator {
    private Random random;
    private WorldMap map;
    private int width;
    private int height;
    private int riverCount;
    private int maxSteps = 200;
    private float dropCapacity = 1;
    private float dropPickupRadius = 1;
    private float dropInertia = 0.1f;
    private float elutionForce = 0.1f;
    private float erosion;
    private float deposition = 0.1f;
    private float evaporation = 0.02f;
    private float minElevationDelta = 0;
    private float maxCapacity = 1;
    private float[][] elevationBuffer;

    private ArrayList<Drop> drops;

    private class Drop {
        float x;
        float y;
        Vector speed;
        float capacity;
        float sediment;
        float water;

        public Drop(float x, float y) {
            this.x = x;
            this.y = y;
            water = 1;
            sediment = 0;
            speed = new Vector(0, 0, 0, 0);
        }
    }

    public ErosionGenerator(WorldGenContainer container) {
        super(container);
        extractContainer(container);
        drops = new ArrayList<>();
        elevationBuffer = new float[map.getWidth()][map.getHeight()];
    }

    private void extractContainer(WorldGenContainer container) {
        map = container.getMap();
        random = container.getConfig().getRandom();
        width = container.getConfig().getWidth();
        height = container.getConfig().getHeight();
    }

    public boolean execute() {
        putDrops();
        for (Drop drop : drops) {
            for (int i = 0; i < maxSteps; i++) {
                moveDrop(drop);
                if (!map.inMap(drop.x, drop.y)) {
                    break;
                }
            }
        }
//        applyElevationBuffer();
        return false;
    }

    private void putDrops() {
        Random random = new Random();
        while (true) {
            int x = random.nextInt(map.getWidth());
            int y = random.nextInt(map.getHeight());
            if (container.getElevation(x, y) > 0) {
                System.out.println("drop   " + x + " " + y);
                drops.add(new Drop(x, y));
                return;
            }
        }
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                if (container.getElevation(x, y) > 0) {
//                    drops.add(new Drop(x, y));
//                }
//            }
//        }
    }

    private void moveDrop(Drop drop) {
        //apply slope to speed
        Vector slopeVector = countSlopeVector(drop.x, drop.y);
        slopeVector.setLength((slopeVector.getLength() + 1) * (1 - dropInertia));
        Vector speedVector = drop.speed;
        speedVector.setLength(speedVector.getLength() * dropInertia);
//        System.out.println(slopeVector.toString());
//        System.out.println(speedVector.toString());
        speedVector = speedVector.sum(slopeVector);
        drop.speed = speedVector;

        float endX = drop.x + speedVector.getXProj();
        float endY = drop.y + speedVector.getYProj();

//        System.out.println(speedVector.toString());
        System.out.println(Math.round(drop.x) + " "
                + Math.round(drop.y) + " "
                + container.getElevation(Math.round(drop.x), Math.round(drop.y)) + " -> "
                + container.getElevation(Math.round(endX), Math.round(endY)));
        System.out.println(container.getElevation(Math.round(drop.x), Math.round(drop.y))
                - container.getElevation(Math.round(endX), Math.round(endY)));

//        float elevationDelta = container.getElevation(Math.round(drop.x), Math.round(drop.y)) -
//                container.getElevation(Math.round(endX), Math.round(endY)); // count el delta
//        drop.capacity = Math.max(elevationDelta, minElevationDelta)
//                * ((float) speedVector.getLength())
//                * drop.water
//                * maxCapacity; // change capacity
//        if (drop.capacity < drop.sediment) {
//            //depose
//            float sedChange = (drop.sediment - drop.capacity) * deposition;
//            drop.sediment -= sedChange;
////            elevationBuffer[Math.round(drop.x)][Math.round(drop.y)] += sedChange;
//            container.setElevation(Math.round(drop.x), Math.round(drop.y),
//                    container.getElevation(Math.round(drop.x), Math.round(drop.y)) + sedChange);
//        } else {
//            //erose
//            float sedChange = Math.min((drop.capacity - drop.sediment) * erosion, elevationDelta);
//            drop.sediment += sedChange;
//            decreaceElevationInRadius(Math.round(drop.x), Math.round(drop.y), sedChange);
//        }
//        drop.water = drop.water * (1 - evaporation); // decrease erosion ability
        drop.x = endX;
        drop.y = endY;
    }


    private float decreaceElevationInRadius(int dx, int dy, float force) {
        int flooredRadius = (int) Math.floor(dropPickupRadius);
        float total = 0;
        for (int x = dx - flooredRadius; x < dx + flooredRadius; x++) {
            for (int y = dy - flooredRadius; y < dy + flooredRadius; y++) {
                if (map.inMap(x, y)) {
                    float delta = force / (1 + countDistance(x, y, dx, dy));
                    total += delta;
//                    elevationBuffer[x][y] -= delta;
                    container.setElevation(x, y, container.getElevation(x, y) - delta);
                }
            }
        }
        return total;
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

    private Vector countSlopeVector(float cx, float cy) {
        float centerElevation = container.getElevation(Math.round(cx), Math.round(cy));
        float xProject = 0;
        float yProject = 0;
        for (int x = Math.round(cx) - 1; x <= cx + 1; x++) {
            for (int y = Math.round(cy) - 1; y <= cy + 1; y++) {
                if (map.inMap(x, y) && container.getElevation(x, y) < centerElevation) { // elevation decreases in this direction
                    float elevationDelta = centerElevation - container.getElevation(x, y);
                    xProject += (x - cx) * ((elevationDelta));
                    yProject += (y - cy) * ((elevationDelta));
                }
            }
        }
        Vector vector = new Vector(0, 0, xProject * 10, yProject * 10);
        return vector;
    }

    private int countAngleByOffsets(float xOffset, float yOffset) {
        double angle;
        if (xOffset != 0) {
            angle = (Math.toDegrees(Math.atan(yOffset / xOffset)));
            if (xOffset < 0) {
                angle += 180;
            }
        } else {
            angle = yOffset > 0 ? 90 : 270;
        }
        return (int) Math.round(angle + 360 % 360);
    }

    private void applyElevationBuffer() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                container.setElevation(x, y, container.getElevation(x, y) + elevationBuffer[x][y]);
            }
        }
    }

    private float countDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
