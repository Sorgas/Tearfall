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
    private ArrayList<Inflow> inflows;
    private Inflow currentFlow; // direction of current flow
    private boolean currentFlowIsRiver;
    private Inflow mainInflow; // position on local map
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
        inflows = new ArrayList<>();
        localMap = container.getLocalMap();
        materialMap = MaterialMap.getInstance();
    }

    /**
     * Carves bed of current flow and its main inflow.
     */
    private void makeMainFlow() {
        LandscapeBrush brush = new LandscapeBrush(new ArrayList<>(Arrays.asList(5, 3, 2)), 1);

        Vector2 start = new Vector2(mainInflow.localStart.getX(), mainInflow.localStart.getY());
        Vector2 end = new Vector2(currentFlow.localStart.getX(), currentFlow.localStart.getY());
        Vector2 center = new Vector2(localMap.getxSize() / 2, localMap.getySize() / 2);

        Vector2[] vectors = {start, center, end};
        CatmullRomSpline<Vector2> spline = new CatmullRomSpline<>();
        spline.set(vectors, false);
        carveWithBrush(spline, brush);
    }

    private void makeRiverStart() {
        LandscapeBrush brush = new LandscapeBrush(new ArrayList<>(Arrays.asList(5, 3, 2)), 1);
        Vector2[] controlPoints = {new Vector2(localMap.getxSize() / 2, localMap.getySize() / 2),
                new Vector2(currentFlow.localStart.getX(), currentFlow.localStart.getY())};
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
        int cx = location.getX();
        int cy = location.getY();
        for (int dx = -1; dx < 2; dx++) { //offset x
            int x = cx + dx; //absolute x
            for (int dy = -1; dy < 2; dy++) { //offset y
                int y = cy + dy; // absolute y
                if ((x != cx || y != cy) && worldMap.inMap(x, y)) { //not current point and not out of borders
                    if (worldMap.getRiver(x, y) != null && isInflow(dx, dy, worldMap.getRiver(x, y))) {
                        inflows.add(createInflow(dx, dy, true));
                    }
                    if (worldMap.getBrook(x, y) != null && isInflow(dx, dy, worldMap.getBrook(x, y))) {
                        inflows.add(createInflow(dx, dy, false));
                    }
                }
            }
        }
        if (worldMap.getRiver(cx, cy) != null) {
            IntVector2 intVector = new IntVector2(worldMap.getRiver(cx, cy)); // pointing out
            this.currentFlow = createInflow(intVector.x, intVector.y, true); // localStart is end of flow
        } else if (worldMap.getBrook(cx, cy) != null) {
            IntVector2 intVector = new IntVector2(worldMap.getBrook(cx, cy)); // pointing out
            this.currentFlow = createInflow(intVector.x, intVector.y, false); // localStart is end of flow
        }
        System.out.println("inflows: " + inflows.size());
    }

    /**
     * Checks if given vector points to current position.
     *
     * @param rx
     * @param ry
     * @param inflow
     * @return
     */
    private boolean isInflow(int rx, int ry, Vector2 inflow) {
        System.out.println("inflow check: " + rx + " " + ry + " " + inflow);
        System.out.println("vector: " + Math.round(inflow.x) + " " + Math.round(inflow.y));
        IntVector2 intVector = new IntVector2(inflow);
        return intVector.x == -rx && intVector.y == -ry; // inflow vector points from observed position to current. [rx, ry] is offset from current position to position where inflow comes from.
    }

    private Inflow createInflow(int dx, int dy, boolean river) {
        Inflow inflow = new Inflow();
        inflow.isRiver = river;
        inflow.offset = new Position(dx, dy, 0);
        inflow.localStart = new Position((dx + 1) * (localMap.getxSize() / 2), (dy + 1) * (localMap.getySize() / 2), 0);
        if (river) {
            inflow.waterAmount = worldMap.getRiver(location.getX() + dx, location.getY() + dy).len();
        } else {
            inflow.waterAmount = worldMap.getBrook(location.getX() + dx, location.getY() + dy).len();
        }
        return inflow;
    }

    private void determineMailInflow() {
        inflows.forEach(inflow -> {
            if (mainInflow == null || mainInflow.waterAmount < inflow.waterAmount) {
                mainInflow = inflow;
            }
        });
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

    private class Inflow {
        boolean isRiver; // or brook
        Position offset;
        Position localStart;
        float waterAmount;
    }

    private class IntVector2 {
        int x;
        int y;

        public IntVector2(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Creates IntVector basing on vector angle.
         * All angles range divided into 8 sectors corresponding to 8 neighbours on square grid.
         * x and y become offset to neighbour pointed by vector.
         *
         * @param vector
         */
        public IntVector2(Vector2 vector) {
            float angleSector = (float) (vector.cpy().nor().angleRad() / Math.PI) / (1f / 16f); // number of 1/16 sectors in angle of inflow vector
            if (angleSector < 3 || angleSector >= 13) {
                x = 1;
            } else if (angleSector >= 5 && angleSector < 11) {
                x = -1;
            }
            if (angleSector >= 1 && angleSector < 7) {
                y = 1;
            } else if (angleSector >= 9 && angleSector < 15) {
                y = -1;
            }
        }
    }
}