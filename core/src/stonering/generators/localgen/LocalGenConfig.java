package stonering.generators.localgen;

import stonering.util.geometry.Position;

/**
 * @author Alexander Kuzyakov on 03.06.2017.
 *
 * Stores settings for local generation
 */
public class LocalGenConfig {
    private Position location;

    private float seaLevel = 0.5f;
    private int worldToLocalElevationModifier = 100;
    private int airLayersAboveGround = 20;
    private int areaSize = 96;
    private int[] sublayerMaxCount = {5,5,6,6};
    private int[] sublayerMinThickness = {4,4,6,8};
    private int minCaveLayerHeight = 10;
    private int maxCaveLayerHeight = 20;

    public int getWorldToLocalElevationModifier() {
        return worldToLocalElevationModifier;
    }

    public int getAirLayersAboveGround() {
        return airLayersAboveGround;
    }

    public int getAreaSize() {
        return areaSize;
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

    public float getSeaLevel() {
        return seaLevel;
    }
}
