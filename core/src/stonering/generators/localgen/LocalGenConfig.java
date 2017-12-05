package stonering.generators.localgen;

import stonering.global.utils.Position;

/**
 * Created by Alexander on 03.06.2017.
 *
 * Stores settings for local generation
 */
public class LocalGenConfig {
    private int worldToLocalElevationModifier = 10;
    private int areaSize = 192;
    private int areaHight = 400;
    private int localSeaLevel = 200;
    private int[] sublayerMaxCount = {5,5,6,6};
    private int[] sublayerMinThickness = {4,4,6,8};
    private int minCaveLayerHeight = 10;
    private int maxCaveLayerHeight = 20;

    private Position location;

    public int getWorldToLocalElevationModifier() {
        return worldToLocalElevationModifier;
    }

    public int getAreaSize() {
        return areaSize;
    }

    public int getAreaHight() {
        return areaHight;
    }

    public int getLocalSeaLevel() {
        return localSeaLevel;
    }

    public Position getLocation() {
        return location;
    }

    public void setLocation(Position location) {
        this.location = location;
    }

    public int[] getSublayerMaxCount() {
        return sublayerMaxCount;
    }

    public int[] getSublayerMinThickness() {
        return sublayerMinThickness;
    }

    public int getMinCaveLayerHeight() {
        return minCaveLayerHeight;
    }

    public int getMaxCaveLayerHeight() {
        return maxCaveLayerHeight;
    }
}
