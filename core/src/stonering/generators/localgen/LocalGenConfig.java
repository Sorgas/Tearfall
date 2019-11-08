package stonering.generators.localgen;

import stonering.util.geometry.Position;

/**
 * @author Alexander Kuzyakov on 03.06.2017.
 *
 * Stores settings for local generation
 */
public class LocalGenConfig {
    private Position location;

    public float seaLevel = 0.5f;
    public int worldToLocalElevationModifier = 100;
    public int airLayersAboveGround = 20;
    public int areaSize = 96;
    public int[] sublayerMaxCount = {5,5,6,6};
    public int[] sublayerMinThickness = {4,4,6,8};
    public int minCaveLayerHeight = 10;
    public int maxCaveLayerHeight = 20;

    public int getWorldToLocalElevationModifier() {
        return worldToLocalElevationModifier;
    }

    public int getAirLayersAboveGround() {
        return airLayersAboveGround;
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
