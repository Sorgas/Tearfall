package stonering.generators.localgen.generators;

import stonering.game.core.model.LocalMap;
import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.localgen.LocalGenContainer;
import stonering.global.utils.Position;

import java.util.ArrayList;
import java.util.HashMap;

/**
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
            System.out.println("placing pool, size: " + pool.points.keySet().size());
        });
    }

    private class Pool {
        HashMap<Position, Integer> points;

        public Pool() {
            points = new HashMap<>();
        }
    }

    private void tryPlacePool(Pool pool) {
        int lowestPoint = -1;
        pool.points.keySet().forEach(position -> {

        });
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
