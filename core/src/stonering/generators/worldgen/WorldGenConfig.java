package stonering.generators.worldgen;

import java.util.Random;

/**
 * Stores settings for world generation.
 *
 * @author Alexander Kuzyakov on 06.03.2017.
 */
public class WorldGenConfig {
    public final long seed;
    public int width;
    public int height;

    //for plate worldgen
    private int plateDensity = 2500;
    private int minPlateSpeed = 3;
    private int maxPlateSpeed = 6;
    private float centerMargin = 0.07f;

    // for mountain worldgen
    // minimal distance between tops
    private float PlateSpeedToHeightModifier = 2.0f;
    private int mountainsTopsDensity = 10;
    private float topOffsetModifier = 2.5f;

    //for valleys worldgen
    private float plateSpeedToDepthModifier = 3.0f;
    private int valleysTopsDensity = 5;
    private float worldBorderDepth = -1.8f;

    //for valleys renderer
    private int smoothIterations = 0;
    private int smoothRadius = 1;

    //for hill worldgen
    private int hillDensity = 120;
    private float hillMargin = 0.08f;

    //for ocean filler
    private float seaLevel = 0.5f;

    //for river worldgen
    private int riverDensity = 1000;
    private float largeRiverStartLevel = 0.7f;

    //for temperature worldgen
    private float polarLineWidth = 0.04f;
    private float equatorLineWidth = 0.03f;
    private float maxTemperature = 35;
    private float minTemperature = -15;
    private float seasonalDeviation = 5;
    private float elevationInfluence = 4f;

    //rainfall x3 for sm/y
    private int minRainfall = 5; // deserts and glaciers
    private int maxRainfall = 75; // tropical forests

    public WorldGenConfig(long seed, int width, int height) {
        this.seed = seed;
        this.width = width;
        this.height = height;
        this.plateDensity = width * height / plateDensity;
    }

    public WorldGenConfig(int width, int height) {
        this(new Random().nextLong(), width, height);
    }

    public float getTopOffsetModifier() {
        return topOffsetModifier;
    }

    public int getPlateDensity() {
        return plateDensity;
    }

    public int getMountainsTopsDensity() {
        return mountainsTopsDensity;
    }

    public float getPlateSpeedToHeightModifier() {
        return PlateSpeedToHeightModifier;
    }

    public float getPlateSpeedToDepthModifier() {
        return plateSpeedToDepthModifier;
    }

    public int getValleysTopsDensity() {
        return valleysTopsDensity;
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

    public int getMinPlateSpeed() {
        return minPlateSpeed;
    }

    public int getMaxPlateSpeed() {
        return maxPlateSpeed;
    }

    public int getSmoothIterations() {
        return smoothIterations;
    }

    public int getSmoothRadius() {
        return smoothRadius;
    }

    public int getHillDensity() {
        return hillDensity;
    }

    public float getWorldBorderDepth() {
        return worldBorderDepth;
    }

    public float getCenterMargin() {
        return centerMargin;
    }

    public float getSeaLevel() {
        return seaLevel;
    }

    public float getHillMargin() {
        return hillMargin;
    }

    public float getPolarLineWidth() {
        return polarLineWidth;
    }

    public float getEquatorLineWidth() {
        return equatorLineWidth;
    }

    public float getMaxTemperature() {
        return maxTemperature;
    }

    public float getMinTemperature() {
        return minTemperature;
    }

    public int getMinRainfall() {
        return minRainfall;
    }

    public int getMaxRainfall() {
        return maxRainfall;
    }

    public float getLargeRiverStartLevel() {
        return largeRiverStartLevel;
    }

    public float getElevationInfluence() {
        return elevationInfluence;
    }

    public float getSeasonalDeviation() {
        return seasonalDeviation;
    }
}