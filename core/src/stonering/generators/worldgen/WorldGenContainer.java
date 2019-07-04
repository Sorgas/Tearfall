package stonering.generators.worldgen;

import com.badlogic.gdx.math.Vector2;
import stonering.entity.*;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Contains intermediate and complete results of world generation.
 *
 * @author Alexander Kuzyakov on 05.03.2017.
 */
public class WorldGenContainer {
    private WorldGenConfig config;
    private World world;
    private int width;
    private int height;

    private ArrayList<TectonicPlate> tectonicPlates;
    private List<Edge> edges;
    private List<Mountain> mountains;
    private List<Mountain> valleys;
    private List<Mountain> hills;

    private float[][] elevation;
    private float[][] drainage;
    private float[][] slopeAngles;
    private float[][] summerTemperature;
    private float[][] winterTemperature;
    private float[][] rainfall;
    private float[][] debug;
    private int[][] biome;
    private Vector2[][] rivers;
    private Vector2[][] brooks;
    private HashSet<Position> lakes;

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
        WorldMap map = world.getWorldMap();
        map.setTectonicPlates(tectonicPlates);
        float maxElevation = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map.setElevation(x, y, elevation[x][y]);
                map.setSummerTemperature(x, y, Math.round(summerTemperature[x][y]));
                map.setWinterTemperature(x, y, Math.round(winterTemperature[x][y]));
                map.setRainfall(x, y, rainfall[x][y]);
                map.setRiver(x, y, rivers[x][y]);
                map.setBrook(x, y, brooks[x][y]);
                map.setDrainage(x, y, drainage[x][y]);
                map.setBiome(x, y, biome[x][y]);
                if (elevation[x][y] > maxElevation) {
                    maxElevation = elevation[x][y];
                }
            }
        }
        map.getLakes().addAll(lakes);
    }

    public void reset() {
        world = new World(width, height);
        elevation = new float[width][height];
        drainage = new float[width][height];
        slopeAngles = new float[width][height];
        summerTemperature = new float[width][height];
        winterTemperature = new float[width][height];
        rainfall = new float[width][height];
        debug = new float[width][height];
        biome = new int[width][height];
        rivers = new Vector2[width][height];
        brooks = new Vector2[width][height];
        tectonicPlates = new ArrayList<>();
        edges = new ArrayList<>();
        mountains = new ArrayList<>();
        valleys = new ArrayList<>();
        hills = new ArrayList<>();
        lakes = new HashSet<>();
    }

    public boolean inMap(int x, int y) {
        return (x >= 0 && y >= 0 && x < width && y < height);
    }

    public boolean inMap(float x, float y) {
        return world.getWorldMap().inMap(x, y);
    }

    public void setSlopeAngles(int x, int y, float value) {
        if (inMap(x, y)) {
            slopeAngles[x][y] = value;
        }
    }

    public float getSlopeAngles(int x, int y) {
        return inMap(x, y) ? slopeAngles[x][y] : 0;
    }

    public List<TectonicPlate> getTectonicPlates() {
        return tectonicPlates;
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
        return world.getWorldMap();
    }

    public void setElevation(int x, int y, float value) {
        if (inMap(x, y)) {
            elevation[x][y] = value;
        }
    }

    public void setElevation(Position position, float value) {
        setElevation(position.getX(), position.getY(), value);
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

    public HashSet<Position> getLakes() {
        return lakes;
    }

    public void setLakes(HashSet<Position> lakes) {
        this.lakes = lakes;
    }

    public void setDrainage(int x, int y, float value) {
        if (inMap(x, y)) {
            drainage[x][y] = value;
        }
    }

    public float getDrainage(int x, int y) {
        return inMap(x, y) ? drainage[x][y] : 0;
    }

    public void setBiome(int x, int y, int value) {
        if (inMap(x, y)) {
            biome[x][y] = value;
        }
    }

    public int getBiome(int x, int y) {
        return inMap(x, y) ? biome[x][y] : 0;
    }

    public World getWorld() {
        return world;
    }
}