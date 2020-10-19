package stonering.generators.localgen.generators;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import stonering.entity.world.World;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.localgen.LocalGenContainer;
import stonering.entity.world.WorldMap;
import stonering.util.geometry.Position;
import stonering.entity.environment.WaterSource;
import stonering.util.logging.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Generates rivers and brooks on local worldMap.
 * All incoming flows merge into flow on local map.
 * Border tiles in flows starts are water sources.
 *
 * @author Alexander Kuzyakov on 10.07.2017.
 */
public class LocalRiverGenerator extends LocalGenerator {
    private WorldMap worldMap;
    private LocalMap localMap;
    private Position location;

    private ArrayList<Inflow> inflows;
    private ArrayList<Flow> flows;

    private Inflow outflow;
    private transient MaterialMap materialMap;

    public LocalRiverGenerator(LocalGenContainer container) {
        super(container);
    }

    public void execute() {
        Logger.GENERATION.logDebug("generating rivers");
        extractContainer();
        lookupInflows(); // fill inflow collection
        makeOutFlow();
        makeFlows(); // fill flows collection
        flows.forEach(this::carveFlow);
    }

    private void extractContainer() {
        worldMap = container.model.get(World.class).worldMap;
        location = container.config.getLocation();
        inflows = new ArrayList<>();
        flows = new ArrayList<>();
        localMap = container.model.get(LocalMap.class);
        materialMap = MaterialMap.instance();
    }

    /**
     * Carves bed of current flow and its main inflow.
     */
    private void carveFlow(Flow flow) {
        LandscapeBrush brush = new LandscapeBrush(new ArrayList<>(Arrays.asList(5, 3, 2)), 1);
        carveWithBrush(flow.spline, brush);
    }

//    private void makeCentralLake() {
//        //TODO change shape from sphere to something natural.
//        int radius = 10;
//        int center = container.getConfig().getAreaSize() / 2;
//        int elevationInCenter = container.getRoundedHeightsMap()[center][center];
//        for (int x = -radius; x < radius; x++) {
//            for (int y = -radius; y < radius; y++) {
//                float rad = (float) Math.sqrt((x * x) + (y * y));
//                if (rad <= radius) {
//                    int elevation = container.getRoundedHeightsMap()[center + x][center + y];
//                    int sphereElevation = (int) (elevationInCenter + radius / 2 - Math.sqrt(Math.abs(-(x * x) - (y * y) + radius * radius)));
//                    updateLocalMapAndRoundedHeightMap(center + x, center + y, Math.min(sphereElevation, elevation));
//                }
//            }
//        }
//    }

    /**
     * Fills collections of inflows.
     */
    private void lookupInflows() {
        int cx = location.x;
        int cy = location.y;
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
        int cx = location.x;
        int cy = location.y;
        if (worldMap.getRiver(cx, cy) != null) {
            IntVector2 intVector = new IntVector2(worldMap.getRiver(cx, cy)); // pointing out
            this.outflow = createInflow(intVector.x, intVector.y, true); // localStart is end of flow
        } else if (worldMap.getBrook(cx, cy) != null) {
            IntVector2 intVector = new IntVector2(worldMap.getBrook(cx, cy)); // pointing out
            this.outflow = createInflow(intVector.x, intVector.y, false); // localStart is end of flow
        }
//        System.out.println("inflows: " + inflows.size());
    }

    private void makeFlows() {
        //TODO currently all flows have straight splines with common end point on the border of the map. This need to be changed to curved splines merging one to another.
        inflows.sort((o1, o2) -> Math.round((o1.waterAmount - o2.waterAmount))); // sort by size
        Position currentEnd = outflow != null ? outflow.localStart : new Position(localMap.xSize / 2, localMap.ySize / 2, 0);
        if (!inflows.isEmpty()) {
            for (Inflow inflow : inflows) {
                Flow flow = new Flow();
                flow.start = inflow.localStart;
                flow.end = currentEnd;
                flow.isRiver = inflow.isRiver;
                flow.waterAmount = inflow.waterAmount;
                createFlowSpline(flow);
                flows.add(flow);
//                System.out.println("added flow: " + flow.toString());
            }
        } else {
            Flow flow = new Flow();
            flow.start = new Position(localMap.xSize / 2, localMap.ySize / 2, 0);
            flow.end = currentEnd;
            flow.isRiver = outflow.isRiver;
            flow.waterAmount = outflow.waterAmount;
            createFlowSpline(flow);
            flows.add(flow);
//            System.out.println("added flow: " + flow.toString());
        }
    }

    private void createFlowSpline(Flow flow) {
        //TODO add intermediate control points to form curved river beds
        Vector2 start = new Vector2(flow.start.x, flow.start.y);
        Vector2 end = new Vector2(flow.end.x, flow.end.y);
        CatmullRomSpline<Vector2> spline = new CatmullRomSpline<>();
        Vector2[] controlPoints = {start, start, end, end};
        spline.set(controlPoints, false);
        flow.spline = spline;
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
//        System.out.println("inflow check: " + rx + " " + ry + " " + inflow);
//        System.out.println("vector: " + Math.round(inflow.x) + " " + Math.round(inflow.y));
        IntVector2 intVector = new IntVector2(inflow);
        return intVector.x == -rx && intVector.y == -ry; // inflow vector points from observed position to current. [rx, ry] is offset from current position to position where inflow comes from.
    }

    private Inflow createInflow(int dx, int dy, boolean river) {
        Inflow inflow = new Inflow();
        inflow.isRiver = river;
        inflow.offset = new Position(dx, dy, 0);
        inflow.localStart = new Position((dx + 1) * ((localMap.xSize - 1) / 2), (dy + 1) * ((localMap.ySize - 1) / 2), 0);
        if (river) {
            inflow.waterAmount = worldMap.getRiver(location.x + dx, location.y + dy).len();
        } else {
            inflow.waterAmount = worldMap.getBrook(location.x + dx, location.y + dy).len();
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
//        System.out.println("new flow");
        Vector2 point = new Vector2();
        Vector2 start = spline.valueAt(point, 0).cpy();
        int currentElevation = getElevationInPoint(start);
        float step = 1f / localMap.xSize;
        for (float i = 0; i < 1; i += step) {
            spline.valueAt(point, i);
            if (localMap.inMap(point)) {
                currentElevation = Math.min(getElevationInPoint(point), currentElevation);
                applyBrush(brush, point, currentElevation, false);
            }
        }
        spline.valueAt(point, 0);
        applyBrush(brush, point, getElevationInPoint(point), true);
    }

    /**
     * Return current elevation in point
     *
     * @param vector
     * @return
     */
    private int getElevationInPoint(Vector2 vector) {
        int x = Math.round(vector.x);
        int y = Math.round(vector.y);
        if (localMap.inMap(x, y, 0)) {
            return container.roundedHeightsMap[x][y];
        }
        return -1;
    }

    /**
     * Applies ground removing brush in the given position.
     *
     * @param brush        brush to apply.
     * @param vector       carries x and y of desired point. Comes from spline.
     * @param maxElevation water can go only lower, max elevation ensures that.
     *                     Riverbed will carve hill, instead of climbing it.
     */
    private void applyBrush(LandscapeBrush brush, Vector2 vector, int maxElevation, boolean isStart) {
        int brushOffset = brush.depthPattern.length / 2;
        int cx = Math.round(vector.x) - brushOffset;
        int cy = Math.round(vector.y) - brushOffset;
        for (int x = cx; x < cx + brush.depthPattern.length; x++) {
            for (int y = cy; y < cy + brush.depthPattern.length; y++) {
                if (localMap.inMap(x, y, 0)) {
                    int elevation = Math.min(container.roundedHeightsMap[x][y], maxElevation);
                    updateLocalMapAndRoundedHeightMap(x, y, elevation, isStart && localMap.isBorder(x, y));
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
    private void updateLocalMapAndRoundedHeightMap(int x, int y, int elevation, boolean isWaterSource) {
        for (int z = elevation; z <= container.roundedHeightsMap[x][y]; z++) {
            localMap.blockType.setBlock(x, y, z, BlockTypeEnum.SPACE.CODE, materialMap.getId("air"));
            if (z <= elevation) {
//                localMap.setFlooding(x, y, z, 8);
                container.waterTiles.add(new Position(x,y,z));
                if (isWaterSource) {
                    Position position = new Position(x, y, z);
                    if (!container.waterSources.contains(position)) {
//                        System.out.println("water source: " + position);
                        WaterSource waterSource = new WaterSource(position, materialMap.getId("water"));
                        container.waterSources.add(position);
//                        localMap.getWaterSources().put(position, waterSource);
                    }
                }
            }
        }
    }

    /**
     * After updating, pattern is a grid of elevation above some level.
     */
    private class LandscapeBrush {
        ArrayList<Integer> layerRadiuses; // 0 is bottom
        int depth; // how many layers are below 0 and have water
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

        @Override
        public String toString() {
            return "Flow{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
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