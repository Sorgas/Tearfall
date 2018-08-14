package stonering.generators.localgen.generators;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.sun.org.apache.bcel.internal.generic.LAND;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.LocalMap;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.global.utils.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Generates rivers and brooks on local worldMap.
 * All incoming flows merge into flow on local map.
 *
 * @author Alexander Kuzyakov on 10.07.2017.
 */
public class LocalRiverGenerator {
    private LocalGenContainer container;
    private WorldMap worldMap;
    private LocalMap localMap;
    private Position location;
    private ArrayList<Position> incomingRivers; // absolute coords on world map
    private ArrayList<Position> incomingBrooks; // absolute coords on world map
    private Vector2 currentFlow;
    private boolean currentFlowIsRiver;
    private Position mainInflow; // absolute coords on world map
    private MaterialMap materialMap;

    public LocalRiverGenerator(LocalGenContainer container) {
        this.container = container;
    }

    public void execute() {
        System.out.println("generating rivers");
        extractContainer();
//        System.out.println("locations with inflows: " + countLocationsWithInflows());
        lookupFlows(location.getX(), location.getY());
        determineMailInflow();
        if (mainInflow != null) {
            makeMainFlow();
        } else {
            makeRiverStart();
            makeCentralLake();
        }
    }

    private void extractContainer() {
        worldMap = container.getWorldMap();
        localMap = container.getLocalMap();
        location = container.getConfig().getLocation();
        incomingBrooks = new ArrayList<>();
        incomingRivers = new ArrayList<>();
        localMap = container.getLocalMap();
        materialMap = MaterialMap.getInstance();
    }

    /**
     * Carves bed of current flow and its main inflow.
     */
    private void makeMainFlow() {
        Position startPos = getPositionOnBorderByWorldCoords(mainInflow);
        Vector2 start = new Vector2(startPos.getX(), startPos.getY());

        Vector2 end = currentFlow.cpy().nor();
        Position endPos = getPositionOnBorderByWorldCoords(new Position(Math.round(end.x), Math.round(end.y), 0));
        end = new Vector2(endPos.getX(), endPos.getY());

        Vector2[] vectors = {start, end};
        Bezier<Vector2> bezier = new Bezier<>();
        bezier.set(vectors);

        Vector2 vector2 = new Vector2();
        for (int i = 0; i < 100; i++) {
            System.out.println(bezier.valueAt(vector2, i / 100f));
        }
    }

    private void makeRiverStart() {
        LandscapeBrush brush = new LandscapeBrush();
        brush.depth = 1;
        brush.layerRadiuses = new ArrayList<>(Arrays.asList(5, 3, 2));
        brush.updatePattern();
        currentFlow.nor();
        Vector2[] controlPoints = {new Vector2(localMap.getxSize() / 2, localMap.getySize() / 2), new Vector2(Math.round(currentFlow.x), Math.round(currentFlow.y))};
        CatmullRomSpline<Vector2> spline = new CatmullRomSpline<>(controlPoints, false);
        for (float i = 0; i < 1; i += 0.1f) {
            Vector2 point = new Vector2();
            spline.valueAt(point, i);
            applyBrush(brush, point);
        }
    }

    private void applyBrush(LandscapeBrush brush, Vector2 vector) {
        int cx = Math.round(vector.x);
        int cy = Math.round(vector.y);
        for (int x = 0; x < brush.depthPattern.length; x++) {
            for (int y = 0; y < brush.depthPattern.length; y++) {
                updateLocalMapAndRoundedHeightMap(x,y,);
            }
        }
    }

    private void makeCentralLake() {
        //TODO change form from sphere to something natural.
        int radius = 10;
        int center = container.getConfig().getAreaSize() / 2;
        int elevationInCenter = container.getRoundedHeightsMap()[center][center];
        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                float rad = (float) Math.sqrt((x * x) + (y * y));
                if (rad <= radius) {
                    int elevation = container.getRoundedHeightsMap()[center + x][center + y];
                    int sphereElevation = (int) (elevationInCenter + radius / 2 - Math.sqrt(Math.abs(-(x * x) - (y * y) + radius * radius)));
                    updateLocalMapAndRoundedHeightMap(center + x, center + y, Math.min(sphereElevation, elevation));
                }
            }
        }
    }

    private void updateLocalMapAndRoundedHeightMap(int x, int y, int elevation) {
        for (int z = elevation; z <= container.getRoundedHeightsMap()[x][y]; z++) {
            localMap.setBlock(x, y, z, BlockTypesEnum.SPACE, materialMap.getId("air"));
            localMap.setFlooding(x, y, z, 8);
        }
    }

    private void placeRiver(int dx, int dy) {
        System.out.println("placing river");
        int x = localMap.getxSize() / 2;
        int y = localMap.getySize() / 2;
        while (localMap.inMap(x, y, 0)) {
            localMap.setBlock(x, y, 0, BlockTypesEnum.SPACE, 1);
            x += dx;
            y += dy;
        }
    }

    private Position getPositionOnBorderByWorldCoords(Position position) {
        int dx = location.getX() - position.getX();
        int dy = location.getY() - position.getY();
        int x = 0;
        int y = 0;
        if (dx != 0) {
            x = dx > 0 ? 1 : -1;
        }
        if (dy != 0) {
            y = dy > 0 ? 1 : -1;
        }
        return new Position(x, y, 0);
    }

    /**
     * Fills collections of inflows and sets current flow.
     */
    private void lookupFlows(int cx, int cy) {
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                int x = cx + dx;
                int y = cy + dy;
                if ((dx != 0 || dy != 0) && worldMap.inMap(x, y)) {
                    if (worldMap.getRiver(x, y) != null && isInflow(dx, dy, worldMap.getRiver(x, y).cpy())) {
                        incomingRivers.add(new Position(x, y, 0));
                    }
                    if (worldMap.getBrook(x, y) != null && isInflow(dx, dy, worldMap.getBrook(x, y).cpy())) {
                        incomingBrooks.add(new Position(x, y, 0));
                    }
                }
            }
        }
        currentFlow = worldMap.getRiver(cx, cy);
        if (currentFlow != null) {
            currentFlowIsRiver = true;
        } else {
            currentFlow = worldMap.getBrook(cx, cy);
            currentFlowIsRiver = currentFlow == null;
        }
        System.out.println("incoming rivers: " + incomingRivers.size());
        System.out.println("incoming brooks: " + incomingBrooks.size());
        System.out.println("currentFlow: " + currentFlow.toString());
        System.out.println("current flow is river: " + currentFlowIsRiver);
    }

    /**
     * Checks if given vector points to current position.
     *
     * @param relativeX
     * @param relativeY
     * @param vector
     * @return
     */
    private boolean isInflow(int relativeX, int relativeY, Vector2 vector) {
        System.out.println("inflow check: " + relativeX + " " + relativeY + " " + vector);
        System.out.println("vector: " + Math.round(vector.x) + " " + Math.round(vector.y));
        vector.nor();
        return Math.round(vector.x) + relativeX == 0 && Math.round(vector.y) + relativeY == 0; // vector points to current location
    }

    private void determineMailInflow() {
        if (currentFlowIsRiver || !incomingRivers.isEmpty()) {
            // if current flow is river, only river inflow can be main.
            // if current flow is brook and river inflows present, one of them should be main as well.
            incomingRivers.forEach(position -> {
                if (mainInflow == null || // first found inflow will go
                        worldMap.getRiver(position.getX(), position.getY()).len() > worldMap.getRiver(mainInflow.getX(), mainInflow.getY()).len()) { // choose inflow with greater amount of water
                    mainInflow = position;
                }
            });
        } else { // no rivers, look for biggest brook
            incomingBrooks.forEach(position -> {
                if (mainInflow == null || // first found inflow will go
                        worldMap.getBrook(position.getX(), position.getY()).len() > worldMap.getBrook(mainInflow.getX(), mainInflow.getY()).len()) { // choose inflow with greater amount of water
                    mainInflow = position;
                }
            });
        }
    }

    /**
     * for debug
     *
     * @return
     */
    private int countLocationsWithInflows() {
        int counter = 0;
        for (int x = 0; x < worldMap.getWidth(); x++) {
            for (int y = 0; y < worldMap.getHeight(); y++) {
                if (worldMap.getElevation(x, y) > container.getConfig().getSeaLevel() && checkInflows(x, y)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    private boolean checkInflows(int cx, int cy) {
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                int x = cx + dx;
                int y = cy + dy;
                if ((dx != 0 || dy != 0) && worldMap.inMap(x, y)) {
                    if (worldMap.getRiver(x, y) != null && isInflow(dx, dy, worldMap.getRiver(x, y).cpy())) {
                        return true;
                    }
                    if (worldMap.getBrook(x, y) != null && isInflow(dx, dy, worldMap.getBrook(x, y).cpy())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private class LandscapeBrush {
        ArrayList<Integer> layerRadiuses;
        int depth;
        int[][] depthPattern;

        public void updatePattern() {
            if (layerRadiuses != null) {
                int patternWidth = Collections.max(layerRadiuses);
                int center = patternWidth / 2;
                depthPattern = new int[patternWidth][patternWidth];
                for (int x = 0; x < patternWidth; x++) {
                    for (int y = 0; y < patternWidth; y++) {
                        Vector2 vector = new Vector2(x - center, y - center);
                        for (int i = 0; i < layerRadiuses.size(); i++) {
                            if(vector.len() < layerRadiuses.get(i)) {
                                depthPattern[x][y] = i + 1;
                            }
                        }
                    }
                }
            }
        }
    }
}