package stonering.generators.worldgen.generators.drainage;

import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.generators.worldgen.generators.AbstractGenerator;
import stonering.global.utils.Position;
import stonering.global.utils.Vector;

import java.util.ArrayList;
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
    private float dropInertia = 0.2f;
    private float elutionForce = 0.1f;
    private float erosion;
    private float deposition = 0.1f;
    private float evaporation = 0.02f;
    private float minElevationDelta = 0;
    private float maxCapacity = 1;

    private ArrayList<Drop> drops;

    private class Drop {
        Position position;
        Vector speed;
        float capacity;
        float sediment;
        float water;

        public Drop(Position position) {
            this.position = position;
            water = 1;
            sediment = 0;
            speed = new Vector(0, 0, 0, 0);
        }
    }

    public ErosionGenerator(WorldGenContainer container) {
        super(container);
        drops = new ArrayList<>();
    }

    public boolean execute() {
        extractContainer(container);
        putDrops();
        for (int i = 0; i < maxSteps; i++) {
            drops.forEach((drop) -> moveDrop(drop));
        }
        return false;
    }

    private void extractContainer(WorldGenContainer container) {
        map = container.getMap();
        random = container.getConfig().getRandom();
        width = container.getConfig().getWidth();
        height = container.getConfig().getHeight();
    }

    private void putDrops() {
//        Random random = new Random();
//        while (true) {
//            int x = random.nextInt(map.getWidth());
//            int y = random.nextInt(map.getHeight());
//            if (container.getElevation(x, y) > 0) {
//                System.out.println("drop   " + x + " " + y);
//                drops.add(new Drop(new Position(x, y, 0)));
//                return;
//            }
//        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (container.getElevation(x, y) > 0) {
                    drops.add(new Drop(new Position(x, y, 0)));
                }
            }
        }
    }

    private void moveDrop(Drop drop) {
        Position pos = drop.position; // current position
        //apply slope to speed
        Vector slopeVector = countSlopeVector(pos.getX(), pos.getY());
        slopeVector.setLength(slopeVector.getLength() * (1 - dropInertia));
        Vector speedVector = drop.speed;
        speedVector.setLength(speedVector.getLength() * dropInertia);
        speedVector = speedVector.sum(slopeVector);

        Position end = new Position(pos.getX() + speedVector.getXProj(), pos.getY() + speedVector.getYProj(), 0);
        float elevationDelta = container.getElevation(pos.getX(), pos.getY()) - container.getElevation(end.getX(), end.getY()); // count el delta. pos for down
        drop.capacity = Math.max(elevationDelta, minElevationDelta) * ((float) speedVector.getLength()) * drop.water * maxCapacity; // change capacity
        if (drop.capacity < drop.sediment) {
            //depose
            float sedChange = (drop.sediment - drop.capacity) * deposition;
            drop.sediment -= sedChange;
            container.setElevation(pos.getX(), pos.getY(), container.getElevation(pos.getX(), pos.getY()) + sedChange);
        } else {
            //erose
            float sedChange = Math.min((drop.capacity - drop.sediment) * erosion, elevationDelta);
            drop.sediment += sedChange;
            decreaceElevationInRadius(pos.getX(), pos.getY(), sedChange);
        }
        drop.water = drop.water * (1 - evaporation); // decrease erosion ability
    }


    private float decreaceElevationInRadius(int dx, int dy, float force) {
        int flooredRadius = (int) Math.floor(dropPickupRadius);
        float total = 0;
        for (int x = dx - flooredRadius; x < dx + flooredRadius; x++) {
            for (int y = dy - flooredRadius; y < dy + flooredRadius; y++) {
                float delta = force / (1 + countDistance(x, y, dx, dy));
                total += delta;
                container.setElevation(x, y, container.getElevation(x, y) - delta);
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

    private Vector countSlopeVector(int cx, int cy) {
        float centerElevation = container.getElevation(cx, cy);
        float xProject = 0;
        float yProject = 0;
        for (int x = cx - 1; x <= cx + 1; x++) {
            for (int y = cy - 1; y <= cy + 1; y++) {
                if (map.inMap(x, y) && container.getElevation(x, y) < centerElevation) { // elevation decreases in this direction
                    float elevation = container.getElevation(x, y);
                    xProject += (x - cx) * (centerElevation - elevation);
                    yProject += (y - cy) * (centerElevation - elevation);
                }
            }
        }
        return new Vector(0, 0, xProject, yProject);
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

    private float countDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }


}
