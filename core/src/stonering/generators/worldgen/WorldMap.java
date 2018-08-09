package stonering.generators.worldgen;

import com.badlogic.gdx.math.Vector2;
import stonering.generators.worldgen.world_objects.Plate;
import stonering.global.utils.Position;
import stonering.global.utils.Vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldMap implements Serializable {
    private float[][] elevation;
    private float[][] summerTemperature;
    private float[][] winterTemperature;
    private float[][] rainfall;
    private Vector2[][] rivers;
    private Vector2[][] brooks;
    private Vector2[][] debug;
    private int width;
    private int height;
    private long seed;
    private ArrayList<Plate> plates;

    public WorldMap(int xSize, int ySize) {
        this.width = xSize;
        this.height = ySize;
        elevation = new float[xSize][ySize];
        summerTemperature = new float[xSize][ySize];
        winterTemperature = new float[xSize][ySize];
        rainfall = new float[xSize][ySize];
        rivers = new Vector2[xSize][ySize];
        brooks = new Vector2[xSize][ySize];
        debug = new Vector2[xSize][ySize];
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getElevation(int x, int y) {
        return elevation[x][y];
    }

    public void setElevation(int x, int y, float val) {
        elevation[x][y] = val;
    }

    public float getSummerTemperature(int x, int y) {
        return summerTemperature[x][y];
    }

    public void setSummerTemperature(int x, int y, float val) {
        summerTemperature[x][y] = val;
    }

    public float getWinterTemperature(int x, int y) {
        return winterTemperature[x][y];
    }

    public void setWinterTemperature(int x, int y, float val) {
        winterTemperature[x][y] = val;
    }

    public float getRainfall(int x, int y) {
        return rainfall[x][y];
    }

    public void setRainfall(int x, int y, float val) {
        rainfall[x][y] = val;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public boolean inMap(int x, int y) {
        return (x >= 0 && y >= 0 && x < width && y < height);
    }

    public boolean inMap(float x, float y) {
        return (x >= 0 && y >= 0 && x < width && y < height);
    }

    public ArrayList<Plate> getPlates() {
        return plates;
    }

    public void setPlates(ArrayList<Plate> plates) {
        this.plates = plates;
    }

    public Vector2 getRiver(int x, int y) {
        return rivers[x][y];
    }

    public void setRiver(int x, int y, Vector2 river) {
        this.rivers[x][y] = river;
    }

    public Vector2 getBrook(int x, int y) {
        return brooks[x][y];
    }

    public void setBrook(int x, int y, Vector2 brook) {
        this.brooks[x][y] = brook;
    }

    public Vector2 getDebug(int x, int y) {
        return debug[x][y];
    }

    public void setDebug(int x, int y, Vector2 river) {
        this.debug[x][y] = river;
    }
}
