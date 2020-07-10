package stonering.generators.worldgen;

import com.badlogic.gdx.math.Vector2;
import stonering.entity.world.*;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Contains intermediate and complete results of world generation.
 *
 * @author Alexander Kuzyakov on 05.03.2017.
 */
public class WorldGenContainer {
    public final Random random;
    public final WorldGenConfig config;
    private World world;
    private int width;
    private int height;

    private ArrayList<TectonicPlate> tectonicPlates;
    public float[][] elevation;
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
        random = new Random(config.seed);
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
        lakes = new HashSet<>();
    }

    public boolean inMap(int x, int y) {
        return (x >= 0 && y >= 0 && x < width && y < height);
    }

    public boolean inMap(float x, float y) {
        return world.getWorldMap().inMap(x, y);
    }

    public float getSlopeAngles(int x, int y) {
        return inMap(x, y) ? slopeAngles[x][y] : 0;
    }

    public List<TectonicPlate> getTectonicPlates() {
        return tectonicPlates;
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
        setElevation(position.x, position.y, value);
    }

    public float getElevation(int x, int y) {
        return inMap(x, y) ? elevation[x][y] : 0;
    }

    public float getElevation(Position position) {
        return getElevation(position.x, position.y);
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
    
    public HashSet<Position> getLakes() {
        return lakes;
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

    public World getWorld() {
        return world;
    }
}