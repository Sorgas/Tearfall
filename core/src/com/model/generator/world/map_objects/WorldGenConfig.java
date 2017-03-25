package com.model.generator.world.map_objects;

import java.util.Random;

/**
 * Created by Alexander on 06.03.2017.
 */
public class WorldGenConfig {
	private long seed;
	private int width;
	private int height;
	private Random random;

	//for plate generator
	private int plateDensity = 1600;
	private int minPlateSpeed = 2;
	private int maxPlateSpeed = 5;
	private float centerMargin = 0.05f;

	// for mountain generator
	// minimal distance between tops
	private int mountainsTopsDensity = 3;
	private float PlateSpeedToHeightModifier = 2.2f;
	private float topOffsetModifier = 2f;

	//for valleys generator
	private int plateSpeedToDepthModifier = 2;
	private int valleysTopsDensity = 2;
	private int worldBorderDepth = -2;

	//for valleys renderer
	private int smoothIterations = 1;
	private int smoothRadius = 1;

	//for hill generator
	private int hillDensity = 120;
	private float hillMargin = 0.05f;

	//for ocean filler
	private int seaLevel = -1;

	//for river generator
	private int riverDensity = 1500;

	public WorldGenConfig(long seed, int width, int height) {
		System.out.println("seed: " + seed);
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

	public int getPlateSpeedToDepthModifier() {
		return plateSpeedToDepthModifier;
	}

	public void setPlateSpeedToDepthModifier(int plateSpeedToDepthModifier) {
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

	public int getWorldBorderDepth() {
		return worldBorderDepth;
	}

	public void setWorldBorderDepth(int worldBorderDepth) {
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
}