package com.model.localmap;

/**
 * Class for storing local map
 */
public class LocalMap {
    private MapCell[][][] localMap;
    private int xSize;
    private int ySize;
    private int zSize;

    /**
     * Constructor, initializes array with given size
     *
     * @param xSize
     * @param ySize
     * @param zSize
     */
    public LocalMap(int xSize, int ySize, int zSize) {
        localMap = new MapCell[xSize][ySize][zSize];
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    /**
     * getter for one cell, specified by coordinates
     *
     * @param x
     * @param y
     * @param z
     * @return Cell
     */
    public MapCell getCell(int x, int y, int z) {
        return localMap[x][y][z];
    }

    /**
     * setter of one cell, osition should be given in cell object
     *
     * @param cell Cell to set
     */
    public void setCell(MapCell cell) {
        Position pos = cell.getPosition();
        localMap[pos.getX()][pos.getY()][pos.getZ()] = cell;
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }

    public int getzSize() {
        return zSize;
    }

    /**
     * returns one z-level array, with !COPIED! cells
     *
     * @param zLevel z-level from bottom
     * @return
     */
    public Level getLevel(int zLevel) {
        MapCell[][] array = new MapCell[xSize][ySize];
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                array[x][y] = new MapCell(localMap[x][y][zLevel]);
            }
        }
        Level level = new Level(xSize, ySize);
        level.setLevel(array);
        return level;
    }

    public MapCell getCell(Position pos) {
        return localMap[pos.getX()][pos.getY()][pos.getZ()];
    }
}