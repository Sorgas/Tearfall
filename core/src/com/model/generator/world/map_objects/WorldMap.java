package com.model.generator.world.map_objects;

import com.model.generator.world.world_objects.WorldCell;

public class WorldMap {
    private WorldCell[][] map;
    private int width;
    private int height;

    public WorldMap(int xSize, int ySize) {
        this.width = xSize;
        this.height = ySize;
        map = new WorldCell[xSize][ySize];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = new WorldCell();
            }
        }
    }

    public WorldCell[][] getMap() {
        return map;
    }

    public void setMap(WorldCell[][] map) {
        this.map = map;
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

    public WorldCell getCell(int x, int y) {
        return map[x][y];
    }
}