package stonering.generators.localgen.generators;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
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
    private ArrayList<Position> incomingRivers; // positions on local map
    private ArrayList<Position> incomingBrooks; // positions on local map
    private Vector2 currentFlow; // direction of current flow
    private boolean currentFlowIsRiver;
    private Position mainInflow; // position on local map
    private MaterialMap materialMap;

    public LocalRiverGenerator(LocalGenContainer container) {
        this.container = container;
    }

    public void execute() {
        System.out.println("generating rivers");
        extractContainer();
        lookupFlows();
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
        LandscapeBrush brush = new LandscapeBrush(new ArrayList<>(Arrays.asList(5, 3, 2)), 1);
        Position startPos = getPositionOnBorderByWorldCoords(mainInflow);
        Vector2 start = new Vector2(startPos.getX(), startPos.getY());

        Vector2 end = currentFlow.cpy().nor();
        Position endPos = getPositionOnBorderByWorldCoords(new Position(Math.round(end.x), Math.round(end.y), 0));
        end = new Vector2(endPos.getX(), endPos.getY());

        Vector2 center = new Vector2(localMap.getxSize() / 2, localMap.getySize() / 2);

        Vector2[] vectors = {start, center, end};
        CatmullRomSpline<Vector2> spline = new CatmullRomSpline<>();
        spline.set(vectors, false);
        carveWithBrush(spline, brush);
    }

    private void makeRiverStart() {
        LandscapeBrush brush = new LandscapeBrush(new ArrayList<>(Arrays.asList(5, 3, 2)), 1);
        currentFlow.nor();
        Vector2[] controlPoints = {new Vector2(localMap.getxSize() / 2, localMap.getySize() / 2), new Vector2(Math.round(currentFlow.x), Math.round(currentFlow.y))};
        CatmullRomSpline<Vector2> spline = new CatmullRomSpline<>(controlPoints, false);
        carveWithBrush(spline, brush);
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
    private void lookupFlows() {
        int commonOffset = -1;
        int cx = location.getX();
        int cy = location.getY();
        for (int x = cx - 1; x < cx + 2; x++) {
            for (int y = cy - 1; y < cy + 2; y++) {
                if ((x != cx || y != cy) && worldMap.inMap(x, y)) {
                    if (worldMap.getRiver(x, y) != null) {
                        Vector2 river = worldMap.getRiver(x,y).cpy().nor();
                        if(river.x)
                    }
                }
            }
        }
//        for (int dx = 0; dx < 3; dx++) {
//            int x = dx + commonOffset + cx;
//            for (int dy = 0; dy < 3; dy++) {
//                int y = dy + commonOffset + cy;
//
//            }
//        }
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

    private void carveWithBrush(CatmullRomSpline<Vector2> spline, LandscapeBrush brush) {
        for (float i = 0; i < 1; i += 0.1f) {
            Vector2 point = new Vector2();
            spline.valueAt(point, i);
            applyBrush(brush, point);
        }
    }

    private void applyBrush(LandscapeBrush brush, Vector2 vector) {
        int brushOffset = brush.depthPattern.length / 2;
        int cx = brushOffset - Math.round(vector.x);
        int cy = brushOffset - Math.round(vector.y);
        for (int x = 0; x < brush.depthPattern.length; x++) {
            for (int y = 0; y < brush.depthPattern.length; y++) {
                int mx = cx - brushOffset + x;
                int my = cy - brushOffset + y;
                if (localMap.inMap(mx, my, 0)) {
                    updateLocalMapAndRoundedHeightMap(mx, my, container.getRoundedHeightsMap()[mx][my]);
                }
            }
        }
    }

    private class LandscapeBrush {
        ArrayList<Integer> layerRadiuses;
        int depth;
        int[][] depthPattern;

        public LandscapeBrush(ArrayList<Integer> layerRadiuses, int depth) {
            this.layerRadiuses = layerRadiuses;
            this.depth = depth;
            updatePattern();
        }

        private void updatePattern() {
            if (layerRadiuses != null) {
                int patternWidth = Collections.max(layerRadiuses);
                int center = patternWidth / 2;
                depthPattern = new int[patternWidth][patternWidth];
                for (int x = 0; x < patternWidth; x++) {
                    for (int y = 0; y < patternWidth; y++) {
                        Vector2 vector = new Vector2(x - center, y - center);
                        for (int i = 0; i < layerRadiuses.size(); i++) {
                            if (vector.len() < layerRadiuses.get(i)) {
                                depthPattern[x][y] = i + 1;
                            }
                        }
                    }
                }
            }
        }
    }
}