package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Generates pattern for pools and carves it on the surface.
 * Local area are filled with pools with constant density.
 *
 * @author Alexander Kuzyakov
 */
public class LocalSurfaceWaterPoolsGenerator extends LocalGenerator {
    private LocalMap localMap;

    public LocalSurfaceWaterPoolsGenerator(LocalGenContainer container) {
        super(container);
        localMap = container.model.get(LocalMap.class);
    }

    public void execute() {
        Logger.GENERATION.logDebug("generating pools");
        ArrayList<Pool> pools = determinePools(generateNoise());
        pools.stream().filter(pool -> pool.points.keySet().size() > 1).forEach(this::tryPlacePool);
    }

    private class Pool {
        HashMap<Point, Integer> points;
        HashSet<Point> borderPoints;

        Pool() {
            points = new HashMap<>();
            borderPoints = new HashSet<>();
        }

        void countBorderPoints() {
            borderPoints.clear();
            points.keySet().forEach(point -> {
                for (int dx = -1; dx < 2; dx++) {
                    for (int dy = -1; dy < 2; dy++) {
                        if (!(dx == 0 && dy == 0)) {
                            borderPoints.add(point);
                        }
                    }
                }
            });
            borderPoints.removeAll(points.keySet());
        }
    }

    private void tryPlacePool(Pool pool) {
        if (pool.points.keySet().size() > 0) {
            pool.countBorderPoints();
            int[][] heightMap = container.roundedHeightsMap;
            ArrayList<Point> points = new ArrayList<>(pool.points.keySet());
            int lowestPoint = heightMap[points.get(0).x][points.get(0).y];
            int highestPoint = lowestPoint;
            for (Point point : points) {
                int currentElevation = heightMap[point.x][point.y];
                lowestPoint = currentElevation < lowestPoint ? currentElevation : lowestPoint;
                highestPoint = currentElevation > highestPoint ? currentElevation : highestPoint;
            }
            MaterialMap materialMap = MaterialMap.instance();
            for (Point point : points) {
                for (int z = highestPoint; z >= lowestPoint; z--) {
                    localMap.blockType.setBlock(point.x, point.y, z, BlockTypeEnum.SPACE, materialMap.getId("air"));
                }
                heightMap[point.x][point.y] = lowestPoint;
            }

            fillWater(pool, lowestPoint);
        }
    }

    private void raiseBorders(Pool pool, int level) {
        pool.borderPoints.forEach(point -> {
//            map.setBlock(point.x, point.y, level, BlockTypesEnum.WALL.getCode(), container.get);
        });
    }

    private void fillWater(Pool pool, int level) {
        ArrayList<Point> points = new ArrayList<>(pool.points.keySet());
        for (Point point : points) {
            container.waterTiles.add(new Position(Math.round(point.x), Math.round(point.y), level));
//            map.setFlooding(point.x, point.y, level, 8);
        }
    }

    private ArrayList<Pool> determinePools(float[][] noise) {
        ArrayList<Pool> pools = new ArrayList<>();
        for (int x = 0; x < noise.length; x++) {
            for (int y = 0; y < noise[0].length; y++) {
                if (noise[x][y] != 0) {
                    pools.add(determinePool(noise, x, y));
                }
                noise[x][y] -= noise[x][y] < 0 ? noise[x][y] * 2 : 0; // [-2; 0]
            }
        }
        return pools;
    }

    private Pool determinePool(float[][] noise, int x, int y) {
        ArrayList<Point> openSet = new ArrayList<>();
        ArrayList<Point> closedSet = new ArrayList<>();
        Pool pool = new Pool();
        openSet.add(new Point(x, y));
        while (!openSet.isEmpty()) {
            Point current = openSet.remove(0);
            pool.points.put(current, (int) Math.ceil(noise[current.x][current.y]));
            noise[current.x][current.y] = 0;
            ArrayList<Point> neighbours = getNeighbours(current);
            for (int i = 0; i < neighbours.size(); i++) {
                Point point = neighbours.get(i);
                if (!(openSet.contains(point) || closedSet.contains(point)) && noise[point.x][point.y] != 0) { // pos is new
                    openSet.add(point);
                }
            }
        }
        return pool;
    }

    private ArrayList<Point> getNeighbours(Point point) {
        ArrayList<Point> points = new ArrayList<>();
        if (localMap.inMap(point.x + 1, point.y, 0)) {
            points.add(new Point(point.x + 1, point.y));
        }
        if (localMap.inMap(point.x - 1, point.y, 0)) {
            points.add(new Point(point.x - 1, point.y));
        }
        if (localMap.inMap(point.x, point.y + 1, 0)) {
            points.add(new Point(point.x, point.y + 1));
        }
        if (localMap.inMap(point.x, point.y - 1, 0)) {
            points.add(new Point(point.x, point.y - 1));
        }
        return points;
    }

    private float[][] generateNoise() {
        int sizeX = localMap.xSize;
        int sizeY = localMap.ySize;
        float[][] noise = new PerlinNoiseGenerator().generateOctavedSimplexNoise(sizeX, sizeY, 7, 0.5f, 0.065f);
        for (int x = 0; x < noise.length; x++) {
            for (int y = 0; y < noise[0].length; y++) {
                noise[x][y] = noise[x][y] < -0.7f ? noise[x][y] * 2 : 0; // [-2; 0]
            }
        }
        return noise;
    }

    private class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            return y == point.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    /**
     * Counts, how easy can water leave this area.
     * Steep slopes, low humidity, high temperature increase drainage.
     * e.g mountains and hills, equatorial areas far from rivers and seas tend to be dry.
     * Low lands (with no outgoing slopes), river valleys, coastal areas tend to be wet or swamped.
     * Swamps will be filled with pools, forests will have sparse pools, no pools in deserts and in high mountains.
     * @return amount of pols in this area.
     */
    private float countLocalDrainage() {
        //TODO
        return 0.5f;
    }
}
