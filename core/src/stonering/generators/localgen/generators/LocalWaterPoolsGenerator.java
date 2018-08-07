package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.LocalMap;
import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.localgen.LocalGenContainer;
import stonering.global.utils.Position;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Generates pattern for pools and carves it on the surface.
 *
 * @author Alexander Kuzyakov
 */
public class LocalWaterPoolsGenerator {
    private LocalGenContainer container;

    public LocalWaterPoolsGenerator(LocalGenContainer container) {
        this.container = container;
    }

    public void execute() {
        System.out.println("generating pools");
        ArrayList<Pool> pools = determinePools(generateNoise());
        pools.stream().filter(pool -> pool.points.keySet().size() > 1).forEach(pool -> {
//            System.out.println("placing pool, size: " + pool.points.keySet().size());
            tryPlacePool(pool);
        });
    }

    private class Pool {
        HashMap<Position, Integer> points;

        public Pool() {
            points = new HashMap<>();
        }
    }

    private void tryPlacePool(Pool pool) {
        if (pool.points.keySet().size() > 0) {
            int[][] heightMap = container.getRoundedHeightsMap();
            ArrayList<Position> positions = new ArrayList<>(pool.points.keySet());
            int lowestPoint = heightMap[positions.get(0).getX()][positions.get(0).getY()];
            int highestPoint = lowestPoint;
            for (Position position : positions) {
                int currentElevation = heightMap[position.getX()][position.getY()];
                lowestPoint = currentElevation < lowestPoint ? currentElevation : lowestPoint;
                highestPoint = currentElevation > highestPoint ? currentElevation : highestPoint;
            }
            LocalMap map = container.getLocalMap();
            MaterialMap materialMap = MaterialMap.getInstance();
            for (Position position : positions) {
                for (int z = highestPoint; z >= lowestPoint; z--) {
                    map.setBlock(position.getX(), position.getY(), z, BlockTypesEnum.SPACE, materialMap.getId("air"));
                }
                heightMap[position.getX()][position.getY()] = lowestPoint;
            }
            fillWater(pool, lowestPoint);
        }
    }

    private void fillWater(Pool pool, int level) {
        ArrayList<Position> positions = new ArrayList<>(pool.points.keySet());
        LocalMap map = container.getLocalMap();
        for (Position position : positions) {
            map.setFlooding(position.getX(), position.getY(), level, 8);
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
        ArrayList<Position> openSet = new ArrayList<>();
        ArrayList<Position> closedSet = new ArrayList<>();
        Pool pool = new Pool();
        openSet.add(new Position(x, y, 0));
        while (!openSet.isEmpty()) {
            Position current = openSet.remove(0);
            pool.points.put(current, (int) Math.ceil(noise[current.getX()][current.getY()]));
            noise[current.getX()][current.getY()] = 0;
            ArrayList<Position> neighbours = getNeighbours(current);
            for (int i = 0; i < neighbours.size(); i++) {
                Position position = neighbours.get(i);
                if (!(openSet.contains(position) || closedSet.contains(position)) && noise[position.getX()][position.getY()] != 0) { // pos is new
                    openSet.add(position);
                }
            }
        }
        return pool;
    }

    private ArrayList<Position> getNeighbours(Position position) {
        LocalMap localMap = container.getLocalMap();
        ArrayList<Position> positions = new ArrayList<>();
        if (localMap.inMap(position.getX() + 1, position.getY(), 0)) {
            positions.add(new Position(position.getX() + 1, position.getY(), 0));
        }
        if (localMap.inMap(position.getX() - 1, position.getY(), 0)) {
            positions.add(new Position(position.getX() - 1, position.getY(), 0));
        }
        if (localMap.inMap(position.getX(), position.getY() + 1, 0)) {
            positions.add(new Position(position.getX(), position.getY() + 1, 0));
        }
        if (localMap.inMap(position.getX(), position.getY() - 1, 0)) {
            positions.add(new Position(position.getX(), position.getY() - 1, 0));
        }
        return positions;
    }

    public static void main(String[] args) {
        LocalWaterPoolsGenerator generator = new LocalWaterPoolsGenerator(null);
        generator.generateNoise();
    }

    private float[][] generateNoise() {
        int sizeX = 200; //container.getLocalMap().getxSize();
        int sizeY = 200; //container.getLocalMap().getySize();
        float[][] noise = new PerlinNoiseGenerator().generateOctavedSimplexNoise(sizeX, sizeY, 7, 0.5f, 0.065f);
        for (int x = 0; x < noise.length; x++) {
            for (int y = 0; y < noise[0].length; y++) {
                noise[x][y] = noise[x][y] < -0.8 ? noise[x][y] * 2 : 0; // [-2; 0]
                System.out.print(noise[x][y] == 0 ? '_' : '#');
            }
            System.out.println();
        }
        return noise;
    }
}
