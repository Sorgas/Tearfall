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
    private ArrayList<Inflow> inflows;
    private ArrayList<Flow> flows;
    private Position outflow; // outflow
    private MaterialMap materialMap;

    public LocalRiverGenerator(LocalGenContainer container) {
        this.container = container;
    }

    public void execute() {
        System.out.println("generating rivers");
        extractContainer();
        lookupInflows(); // fill inflow collection
        makeOutFlow();
        makeFlows(); // fill flows collection
        if (outflow != null) {
            System.out.println("main inflow");
            makeMainFlow();
        } else {
            System.out.println("river start");
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
        int startElevation = container.getRoundedHeightsMap()[mainInflow.localStart.getX()][mainInflow.localStart.getY()];
        int endElevation = container.getRoundedHeightsMap()[outflow.getX()][outflow.getY()];
        if (startElevation > endElevation) {
            //ok
        } else {

        }
        Vector2 start = new Vector2(mainInflow.localStart.getX(), mainInflow.localStart.getY());
        Vector2 end = new Vector2(outflow.getX(), outflow.getY());
        Vector2 center = new Vector2(localMap.getxSize() / 2, localMap.getySize() / 2);

        Vector2[] vectors = {start, start, center, end, end};
        CatmullRomSpline<Vector2> spline = new CatmullRomSpline<>(); // spline is d2 projection of river path (no z axis)
        spline.set(vectors, false);
        carveWithBrush(spline, brush);
    }

    private void carveFlow(Flow flow) {

    }

    private void addInflows() {
        inflows.forEach(inflow -> {
            //TODO find jointpoint to main flow, create brush, carve bed
        });
    }

    private void makeRiverStart() {
        LandscapeBrush brush = new LandscapeBrush(new ArrayList<>(Arrays.asList(5, 3, 2)), 1);
        Vector2[] controlPoints = {new Vector2(localMap.getxSize() / 2, localMap.getySize() / 2),
                new Vector2(outflow.localStart.getX(), outflow.localStart.getY())};
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

    /**
     * Fills collections of inflows.
     */
    private void lookupInflows() {
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
    }

    private void makeOutFlow() {
        int cx = location.getX();
        int cy = location.getY();
        if (worldMap.getRiver(cx, cy) != null) {
            IntVector2 intVector = new IntVector2(worldMap.getRiver(cx, cy)); // pointing out
            this.outflow = createInflow(intVector.x, intVector.y, true); // localStart is end of flow
        } else if (worldMap.getBrook(cx, cy) != null) {
            IntVector2 intVector = new IntVector2(worldMap.getBrook(cx, cy)); // pointing out
            this.outflow = createInflow(intVector.x, intVector.y, false); // localStart is end of flow
        }
        System.out.println("inflows: " + inflows.size());
    }

    private void makeFlows() {
        inflows.sort((o1, o2) -> Math.round((o1.waterAmount - o2.waterAmount)));
        Position currentEnd = outflow != null ? outflow : new Position(localMap.getxSize() / 2, localMap.getySize() / 2, 0);
        for (Inflow inflow : inflows) {
            Flow flow = new Flow();
            flow.start = inflow.localStart;
            flow.end = currentEnd;
            flow.isRiver = inflow.isRiver;
            flow.waterAmount = inflow.waterAmount;

            flow.spline = createFlowSpline();
        }
    }

    private CatmullRomSpline<Vector2> createFlowSpline() {

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

    /**
     * Applies brush along the spline.
     *
     * @param spline
     * @param brush
     */
    private void carveWithBrush(CatmullRomSpline<Vector2> spline, LandscapeBrush brush) {
        Vector2 point = new Vector2();
        Vector2 start = spline.valueAt(point, 0).cpy();
        Vector2 end = spline.valueAt(point, 1).cpy();
        int startElevation = container.getRoundedHeightsMap()[Math.round()][]
        float step = 1f / localMap.getxSize();
        for (float i = 0; i < 1; i += step) {
            spline.valueAt(point, i);
            applyBrush(brush, point, );
        }
    }

    /**
     * Applies ground removing brush in the given position.
     *
     * @param brush        brush to apply.
     * @param vector       carries x and y of desired point.
     * @param maxElevation water can go only lower, max elevation ensures that.
     *                     Riverbed will carve hill, instead of climbing it.
     */
    private void applyBrush(LandscapeBrush brush, Vector2 vector, int maxElevation) {
        int brushOffset = brush.depthPattern.length / 2;
        int cx = brushOffset - Math.round(vector.x);
        int cy = brushOffset - Math.round(vector.y);
        for (int x = cx; x < cx + brush.depthPattern.length; x++) {
            for (int y = cy; y < cy + brush.depthPattern.length; y++) {
                if (localMap.inMap(x, y, 0)) {
                    int elevation = Math.min(container.getRoundedHeightsMap()[x][y], maxElevation);
                    updateLocalMapAndRoundedHeightMap(x, y, elevation);
                }
            }
        }
    }

    /**
     * Changes elevation in specified point. Can only decrease elevation(thats enough for rivers).
     *
     * @param x
     * @param y
     * @param elevation
     */
    private void updateLocalMapAndRoundedHeightMap(int x, int y, int elevation) {
        for (int z = elevation; z <= container.getRoundedHeightsMap()[x][y]; z++) {
            localMap.setBlock(x, y, z, BlockTypesEnum.SPACE, materialMap.getId("air"));
            localMap.setFlooding(x, y, z, 8);
        }
    }

    /**
     * After updating, pattern is a grid of elevation above some level.
     */
    private class LandscapeBrush {
        ArrayList<Integer> layerRadiuses; // 0 is bottom
        int depth; // hao many layers are below 0
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
                        float distance = new Vector2(x - center, y - center).len();
                        for (int i = 0; i < layerRadiuses.size(); i++) {
                            if (distance < layerRadiuses.get(i)) {
                                depthPattern[x][y] = i + 1;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private class Flow {
        boolean isRiver;
        float waterAmount;
        Position start;
        Position end;
        CatmullRomSpline<Vector2> spline;
    }

    private class Inflow {
        boolean isRiver; // or brook
        Position offset; // offset on world map
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