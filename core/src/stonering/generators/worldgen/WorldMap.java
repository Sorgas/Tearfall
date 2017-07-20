package stonering.generators.worldgen;

import stonering.utils.Position;
import stonering.utils.Vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldMap implements Serializable {
    private int[][] elevation;
    private float[][] temperature;
    private float[][] rainfall;
    private Map<Position, List<Vector>> rivers;
    private int width;
    private int height;

    public WorldMap(int xSize, int ySize) {
        this.width = xSize;
        this.height = ySize;
        elevation = new int[xSize][ySize];
        temperature = new float[xSize][ySize];
        rainfall = new float[xSize][ySize];
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

    public float getRainfall(int x, int y) {
        return rainfall[x][y];
    }

    public void setRainfall(int x, int y, float val) {
        rainfall[x][y] = val;
    }

    public Map<Position, List<Vector>> getRivers() {
        return rivers;
    }

    public void addRiverVector(Vector vector) {
        List<Vector> list = rivers.get(new Position(vector.getX(), vector.getY(),0));
        if(list != null) {
            list.add(vector);
        } else {
            list = new ArrayList<>();
            list.add(vector);
            rivers.put(new Position(vector.getX(), vector.getY(),0), list);
        }
    }
}
