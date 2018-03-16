package stonering.generators.worldgen;

import java.util.Random;

/**
 * Created by Alexander on 06.03.2017.
 *
 * Stores settings for world generation
 */
public class WorldGenConfig {
	private long seed;
	private int width;
	private int height;
	private Random random;

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
	private int seaLevel = 0;

	//for river worldgen
	private int riverDensity = 1000;

	//for temperature worldgen
	private float polarLineWidth = 0.04f;
	private float equatorLineWidth = 0.03f;
	private float maxTemperature = 35;
	private float minTemperature = -35;

	//rainfall
	private int minRainfall = 1;
	private int maxRainfall = 100;

	public WorldGenConfig(long seed, int width, int height) {
		this.seed = seed;
		this.width = width;
		this.height = height;
		random = new Random(seed);
		this.plateDensity = width * height / plateDensity;
	}

	public WorldGenConfig(int width, int height) {
		this(new Random().nextLong(), width, height);
	}

	public float getTopOffsetModifier() {
		return topOffsetModifier;
	}

	public void setTopOffsetModifier(float topOffsetModifier) {
		this.topOffsetModifier = topOffsetModifier;
	}

	public int getPlateDensity() {
		return plateDensity;
	}

	public void setPlateDensity(int plateDensity) {
		this.plateDensity = plateDensity;
	}

	public int getMountainsTopsDensity() {
		return mountainsTopsDensity;
	}

	public void setMountainsTopsDensity(int mountainsTopsDensity) {
		this.mountainsTopsDensity = mountainsTopsDensity;
	}

	public float getPlateSpeedToHeightModifier() {
		return PlateSpeedToHeightModifier;
	}

	public void setPlateSpeedToHeightModifier(float plateSpeedToHeightModifier) {
		PlateSpeedToHeightModifier = plateSpeedToHeightModifier;
	}

	public float getPlateSpeedToDepthModifier() {
		return plateSpeedToDepthModifier;
	}

	public void setPlateSpeedToDepthModifier(float plateSpeedToDepthModifier) {
		this.plateSpeedToDepthModifier = plateSpeedToDepthModifier;
	}

	public int getValleysTopsDensity() {
		return valleysTopsDensity;
	}

	public void setValleysTopsDensity(int valleysTopsDensity) {
		this.valleysTopsDensity = valleysTopsDensity;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
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

	public Random getRandom() {
		return random;
	}

	public int getMinPlateSpeed() {
		return minPlateSpeed;
	}

	public void setMinPlateSpeed(int minPlateSpeed) {
		this.minPlateSpeed = minPlateSpeed;
	}

	public int getMaxPlateSpeed() {
		return maxPlateSpeed;
	}

	public void setMaxPlateSpeed(int maxPlateSpeed) {
		this.maxPlateSpeed = maxPlateSpeed;
	}

	public int getSmoothIterations() {
		return smoothIterations;
	}

	public void setSmoothIterations(int smoothIterations) {
		this.smoothIterations = smoothIterations;
	}

	public int getSmoothRadius() {
		return smoothRadius;
	}

	public void setSmoothRadius(int smoothRadius) {
		this.smoothRadius = smoothRadius;
	}

	public int getHillDensity() {
		return hillDensity;
	}

	public void setHillDensity(int hillDensity) {
		this.hillDensity = hillDensity;
	}

	public float getWorldBorderDepth() {
		return worldBorderDepth;
	}

	public void setWorldBorderDepth(float worldBorderDepth) {
		this.worldBorderDepth = worldBorderDepth;
	}

	public float getCenterMargin() {
		return centerMargin;
	}

	public void setCenterMargin(float centerMargin) {
		this.centerMargin = centerMargin;
	}

	public int getSeaLevel() {
		return seaLevel;
	}

	public void setSeaLevel(int seaLevel) {
		this.seaLevel = seaLevel;
	}

	public float getHillMargin() {
		return hillMargin;
	}

	public void setHillMargin(float hillMargin) {
		this.hillMargin = hillMargin;
	}

	public int getRiverDensity() {
		return riverDensity;
	}

	public void setRiverDensity(int riverDensity) {
		this.riverDensity = riverDensity;
	}

	public float getPolarLineWidth() {
		return polarLineWidth;
	}

	public void setPolarLineWidth(float polarLineWidth) {
		this.polarLineWidth = polarLineWidth;
	}

	public float getEquatorLineWidth() {
		return equatorLineWidth;
	}

	public void setEquatorLineWidth(float equatorLineWidth) {
		this.equatorLineWidth = equatorLineWidth;
	}

	public float getMaxTemperature() {
		return maxTemperature;
	}

	public void setMaxTemperature(float maxTemperature) {
		this.maxTemperature = maxTemperature;
	}

	public float getMinTemperature() {
		return minTemperature;
	}

	public void setMinTemperature(float minTemperature) {
		this.minTemperature = minTemperature;
	}

	public int getMinRainfall() {
		return minRainfall;
	}

	public void setMinRainfall(int minRainfall) {
		this.minRainfall = minRainfall;
	}

	public int getMaxRainfall() {
		return maxRainfall;
	}

	public void setMaxRainfall(int maxRainfall) {
		this.maxRainfall = maxRainfall;
	}
}