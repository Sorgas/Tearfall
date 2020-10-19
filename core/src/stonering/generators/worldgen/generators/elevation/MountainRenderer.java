package stonering.generators.worldgen.generators.elevation;

import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.WorldGenerator;
import stonering.entity.world.Edge;
import stonering.entity.world.Mountain;
import stonering.entity.world.Slope;
import stonering.util.geometry.Position;

/**
 * @author Alexander Kuzyakov on 03.03.2017.
 */
public class MountainRenderer extends WorldGenerator {
    private int width;
    private int height;
    private float[][] elevation;
    private float[][] smoothElevation;

    @Override
    public void set(WorldGenContainer container) {
        this.width = container.config.width;
        this.height = container.config.height;
        elevation = new float[width][height];
        smoothElevation = new float[width][height];
    }

    @Override
    public void run() {
        System.out.println("rendering mountains");
//        for (Edge edge : container.getEdges()) {
//            for (Mountain mountain : edge.getMountains()) {
//                renderMountain(mountain);
//            }
//        }
        smoothMountains(0);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (smoothElevation[x][y] > elevation[x][y]) {
                    elevation[x][y] = smoothElevation[x][y];
                }
                container.setElevation(x, y, container.getElevation(x, y) + elevation[x][y]);
            }
        }
    }

    private void renderMountain(Mountain mountain) {
        Position prevCorner = mountain.getCorners().get(mountain.getCorners().size() - 1);
        for (Position corner : mountain.getCorners()) {
            Slope slope = new Slope(mountain.getTop(), prevCorner, corner);
            prevCorner = corner;
            renderSlope(slope);
        }
    }

    private void renderSlope(Slope slope) {
        int minX = Math.max(slope.getMinX(), 0);
        int minY = Math.max(slope.getMinY(), 0);
        int maxX = Math.min(slope.getMaxX(), width);
        int maxY = Math.min(slope.getMaxY(), height);
        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                if (slope.isInside(x, y)) {
                    float z = slope.getAltitude(x, y);
                    if (elevation[x][y] < z) {
                        elevation[x][y] = z;
                    }
                }
            }
        }
    }

    private void smoothMountains(int iterations) {
        for (int i = 0; i < iterations; i++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    smoothElevation[x][y] = countMiddleElevation(x, y, 10);
                }
            }
        }
    }

    private float countMiddleElevation(int x, int y, int radius) {
        if ((x - radius < 0) || (x + radius >= width) || (y - radius < 0) || (y + radius >= height)) {
            return elevation[x][y];
        }
        float sum = 0;
        for (int i = x - radius; i < x + radius; i++) {
            for (int j = y - radius; j < y + radius; j++) {
                sum += elevation[i][j];
            }
        }
        return sum / (float) (Math.pow(2 * radius + 1, 2));
    }
}