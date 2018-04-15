package stonering.generators.worldgen;

import stonering.generators.worldgen.world_objects.Edge;
import stonering.generators.worldgen.world_objects.Mountain;
import stonering.generators.worldgen.world_objects.Plate;
import stonering.global.utils.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 05.03.2017.
 * <p>
 * Contains intermediate results of world generation
 */
public class WorldGenContainer {
    private WorldGenConfig config;
    private int width;
    private int height;
    private WorldMap map;

    private ArrayList<Plate> plates;
    private List<Edge> edges;
    private List<Mountain> mountains;
    private List<Mountain> valleys;
    private List<Mountain> hills;
    private List<Position> lakes;

    private float[][] elevation;
    private float[][] slopeAngles;
    private float[][] summerTemperature;
    private float[][] winterTemperature;
    private float[][] rainfall;
    private float[][] debug;

    private float landPart;

    public WorldGenContainer(WorldGenConfig config) {
        this.width = config.getWidth();
        this.height = config.getHeight();
        this.config = config;
        reset();
    }

    /**
     * flushes collections from container to map
     */
    public void fillMap() {
        map.setPlates(plates);
        float maxElevation = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map.setElevation(x, y, elevation[x][y] * 3f);
                map.setSummerTemperature(x, y, Math.round(summerTemperature[x][y]));
                map.setWinterTemperature(x, y, Math.round(winterTemperature[x][y]));
                map.setRainfall(x, y, rainfall[x][y]);
                if(elevation[x][y] > maxElevation) {
                    maxElevation = elevation[x][y];
                }
            }
        }
        System.out.println("max: " + maxElevation);
    }

    public void reset() {
        elevation = new float[width][height];
        slopeAngles = new float[width][height];
        summerTemperature = new float[width][height];
        winterTemperature = new float[width][height];
        rainfall = new float[width][height];
        debug = new float[width][height];
        map = new WorldMap(width, height);
        map.setSeed(config.getSeed());
        plates = new ArrayList<>();
        edges = new ArrayList<>();
        mountains = new ArrayList<>();
        valleys = new ArrayList<>();
        hills = new ArrayList<>();
        lakes = new ArrayList<>();
    }

    public boolean inMap(int x, int y) {
        return map.inMap(x, y);
    }

    public boolean inMap(float x, float y) {
        return map.inMap(x, y);
    }

    public void setSlopeAngles(int x, int y, float value) {
        if (inMap(x, y)) {
            slopeAngles[x][y] = value;
        }
    }

    public float getSlopeAngles(int x, int y) {
        return inMap(x, y) ? slopeAngles[x][y] : 0;
    }

    public List<Plate> getPlates() {
        return plates;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Mountain> getHills() {
        return hills;
    }

    public List<Mountain> getMountains() {
        return mountains;
    }

    public WorldGenConfig getConfig() {
        return config;
    }

    public WorldMap getMap() {
        return map;
    }

    public void setElevation(int x, int y, float value) {
        if (inMap(x, y)) {
            elevation[x][y] = value;
        }
    }

    public float getElevation(int x, int y) {
        return inMap(x, y) ? elevation[x][y] : 0;
    }

    public float getElevation(Position position) {
        return getElevation(position.getX(), position.getY());
    }

    public void setDebug(int x, int y, float value) {
        if (inMap(x, y)) {
            debug[x][y] = value;
        }
    }

    public void setSummerTemperature(int x, int y, float value) {
        if (inMap(x, y)) {
            summerTemperature[x][y] = value;
        }
    }

    public float getSummerTemperature(int x, int y) {
        return inMap(x, y) ? summerTemperature[x][y] : 0;
    }

    public void setWinterTemperature(int x, int y, float value) {
        if (inMap(x, y)) {
            winterTemperature[x][y] = value;
        }
    }

    public float getWinterTemperature(int x, int y) {
        return inMap(x, y) ? winterTemperature[x][y] : 0;
    }

    public void setRainfall(int x, int y, float value) {
        if (inMap(x, y)) {
            rainfall[x][y] = value;
        }
    }

    public float getRainfall(int x, int y) {
        return inMap(x, y) ? rainfall[x][y] : 0;
    }

    public List<Position> getLakes() {
        return lakes;
    }

    public float getLandPart() {
        return landPart;
    }

    public void setLandPart(float landPart) {
        this.landPart = landPart;
    }
}