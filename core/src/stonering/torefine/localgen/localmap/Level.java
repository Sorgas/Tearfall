package stonering.torefine.localgen.localmap;

/**
 * Created by Alexander on 16.02.2017.
 */
public class Level {
    private MapCell[][] level;
    private int xSize;
    private int ySize;

    public Level(int xSize, int ySize)  {
        this.level = new MapCell[xSize][ySize];
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public MapCell[][] getLevel() {
        return level;
    }

    public void setLevel(MapCell[][] level) {
        this.level = level;
    }

    public int getxSize() {
        return xSize;
    }

    public void setxSize(int xSize) {
        this.xSize = xSize;
    }

    public int getySize() {
        return ySize;
    }

    public void setySize(int ySize) {
        this.ySize = ySize;
    }

    public MapCell getTile(int x, int y) {
        return level[x][y];
    }
}
