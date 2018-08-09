package stonering.generators.worldgen;

import com.badlogic.gdx.math.Vector2;
import stonering.generators.worldgen.world_objects.Edge;
import stonering.generators.worldgen.world_objects.Mountain;
import stonering.generators.worldgen.world_objects.Plate;
import stonering.global.utils.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Kuzyakov on 05.03.2017.
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

    private float[][] elevation;
    private float[][] slopeAngles;
    private float[][] summerTemperature;
    private float[][] winterTemperature;
    private float[][] rainfall;
    private float[][] debug;
    private Vector2[][] rivers;
    private Vector2[][] brooks;
    private ArrayList<Vector2> lakes;

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
                map.setElevation(x, y, elevation[x][y]);
                map.setSummerTemperature(x, y, Math.round(summerTemperature[x][y]));
                map.setWinterTemperature(x, y, Math.round(winterTemperature[x][y]));
                map.setRainfall(x, y, rainfall[x][y]);
                map.setRiver(x,y, rivers[x][y]);
                map.setBrook(x,y, brooks[x][y]);
                if(elevation[x][y] > maxElevation) {
                    maxElevation = elevation[x][y];
                }
            }
        }
    }

    public void reset() {
        map = new WorldMap(width, height);
        map.setSeed(config.getSeed());
        elevation = new float[width][height];
        slopeAngles = new float[width][height];
        summerTemperature = new float[width][height];
        winterTemperature = new float[width][height];
        rainfall = new float[width][height];
        debug = new float[width][height];
        rivers = new Vector2[width][height];
        brooks = new Vector2[width][height];
        plates = new ArrayList<>();
        edges = new ArrayList<>();
        mountains = new ArrayList<>();
        valleys = new ArrayList<>();
        hills = new ArrayList<>();
        lakes = new ArrayList<>();
    }

    public boolean inMap(int x, int y) {
        return (x >= 0 && y >= 0 && x < width && y < height);
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

    public float getLandPart() {
        return landPart;
    }

    public void setLandPart(float landPart) {
        this.landPart = landPart;
    }

    public void setRiver(int x, int y, Vector2 value) {
        if (inMap(x, y)) {
            rivers[x][y] = value;
        }
    }

    public Vector2 getRiver(int x, int y) {
        return inMap(x, y) ? rivers[x][y] : null;
    }

    public void setBrook(int x, int y, Vector2 value) {
        if (inMap(x, y)) {
            brooks[x][y] = value;
        }
    }

    public Vector2 getBrook(int x, int y) {
        return inMap(x, y) ? brooks[x][y] : null;
    }

    public Vector2[][] getRivers() {
        return rivers;
    }

    public ArrayList<Vector2> getLakes() {
        return lakes;
    }

    public void setLakes(ArrayList<Vector2> lakes) {
        this.lakes = lakes;
    }
}