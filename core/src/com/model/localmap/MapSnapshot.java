package com.model.localmap;

import com.model.utils.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * represents state of LocalMap for rendering purposes
 */
public class MapSnapshot {
    private List<Level> levels;
    private int layerCount;
    private int xSize;
    private int ySize;
    private Position camera;

    public MapSnapshot(int xSize, int ySize) {
        this.levels = new ArrayList<Level>();
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public List<Level> getSnapshot() {
        return levels;
    }

    public void setSnapshot(List<Level> snapshot) {
        this.levels = snapshot;
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

    /**
     * camera is Position object used to centering picture in screen
     *
     * @return camera
     */
    public Position getCamera() {
        return camera;
    }

    /**
     * camera is Position object used to centering picture in screeт
     *
     * @param camera camera
     */
    public void setCamera(Position camera) {
        this.camera = camera;
    }

    public void addLevel(Level level) {
        levels.add(level);
        layerCount++;
    }

    public int getLayerCount() {
        return layerCount;
    }

    public Level getLevel(int i) {
        return levels.get(i);
    }
}