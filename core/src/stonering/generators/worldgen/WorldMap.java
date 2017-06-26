package stonering.generators.worldgen;

import stonering.utils.Position;
import stonering.utils.Vector;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WorldMap implements Serializable {
    private int[][] elevation;
    private float[][] temperature;
    private Map<Position, Vector> rivers;
    private int width;
    private int height;

    public WorldMap(int xSize, int ySize) {
        this.width = xSize;
        this.height = ySize;
        elevation = new int[xSize][ySize];
        temperature = new float[xSize][ySize];
        rivers = new HashMap<>();
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

    public int getElevation(int x, int y) {
        return elevation[x][y];
    }

    public void setElevation(int x, int y, int val) {
        elevation[x][y] = val;
    }

    public float getTemperature(int x, int y) {
        return temperature[x][y];
    }

    public void setTemperature(int x, int y, float val) {
        temperature[x][y] = val;
    }

    public Map<Position, Vector> getRivers() {
        return rivers;
    }

    public void addRiverPoint(Vector vector) {
        rivers.put(new Position(vector.getX(), vector.getY(), 0), vector);
    }
}
