package stonering.generators.localgen;

/**
 * Created by Alexander on 03.06.2017.
 */
public class LocalGenConfig {
    private int worldToLocalElevationModifier = 8;
    private int areaSize = 400;
    private int areaHight = 400;
    private int localSeaLevel = 200;

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
}
