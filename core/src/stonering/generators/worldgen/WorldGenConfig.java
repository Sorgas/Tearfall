package stonering.generators.worldgen;

import java.util.Random;

/**
 * Stores settings for world generation.
 *
 * @author Alexander Kuzyakov on 06.03.2017.
 */
public class WorldGenConfig {
    public int seed;
    public int width;
    public int height;

    //for plate worldgen
    public int plateDensity = 2500;
    public int minPlateSpeed = 3;
    public int maxPlateSpeed = 6;
    public float centerMargin = 0.07f;

    // for mountain worldgen
    // minimal distance between tops
    public float plateSpeedToHeightModifier = 2.0f;
    public int mountainsTopsDensity = 10;
    public float topOffsetModifier = 2.5f;

    //for valleys worldgen
    public float plateSpeedToDepthModifier = 3.0f;
    public int valleysTopsDensity = 5;
    public float worldBorderDepth = -1.8f;

    //for valleys renderer
    public int smoothIterations = 0;
    public int smoothRadius = 1;

    //for hill worldgen
    public int hillDensity = 120;
    public float hillMargin = 0.08f;

    //for ocean filler
    public float seaLevel = 0.5f;

    //for river worldgen
    public int riverDensity = 1000;
    public float largeRiverStartLevel = 0.7f;

    //for temperature worldgen
    public float polarLineWidth = 0.04f;
    public float equatorLineWidth = 0.03f;
    public float maxTemperature = 35;
    public float minTemperature = -15;
    public float seasonalDeviation = 5;
    public float elevationInfluence = 4f;

    //rainfall x3 for sm/y
    public int minRainfall = 5; // deserts and glaciers
    public int maxRainfall = 75; // tropical forests

    public WorldGenConfig(int seed, int width, int height) {
        this.seed = seed;
        this.width = width;
        this.height = height;
        this.plateDensity = width * height / plateDensity;
    }

    public WorldGenConfig(int width, int height) {
        this(new Random().nextInt(), width, height);
    }
}