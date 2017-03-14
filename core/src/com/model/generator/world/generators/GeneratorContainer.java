package com.model.generator.world.generators;

import com.model.generator.world.generators.drainage.OceanFiller;
import com.model.generator.world.generators.elevation.*;
import com.model.generator.world.map_objects.WorldGenContainer;

public class GeneratorContainer {
	private boolean rejected;
	int rejectCount;

	private WorldGenContainer worldGenContainer;
	private PlateGenerator plateGenerator;
	private MountainGenerator mountainGenerator;
	private ValleyGenerator valleyGenerator;
	private MountainRenderer mountainRenderer;
	private ValleyRenderer valleyRenderer;
	private HillGenerator hillGenerator;
	private HillRenderer hillRenderer;
	private OceanFiller oceanFiller;

	public GeneratorContainer() {

	}

	public void runContainer() {
		rejected = true;
		for (rejectCount = 0; rejected == true; rejectCount++) {
			worldGenContainer.reset();
			rejected = runGenerators();
		}
	}

	private boolean runGenerators() {
		if(plateGenerator != null)
		if (plateGenerator.execute()) return true;
		if (mountainGenerator.execute()) return true;
		if (valleyGenerator.execute()) return true;
		if (hillGenerator.execute()) return true;
		mountainRenderer.execute();
		valleyRenderer.execute();
		hillRenderer.execute();
		worldGenContainer.fillMap();
		oceanFiller.execute();
		return false;
	}

	public void setPlateGenerator(PlateGenerator plateGenerator) {
		this.plateGenerator = plateGenerator;
	}

	public void setMountainGenerator(MountainGenerator mountainGenerator) {
		this.mountainGenerator = mountainGenerator;
	}

	public void setValleyGenerator(ValleyGenerator valleyGenerator) {
		this.valleyGenerator = valleyGenerator;
	}

	public void setMountainRenderer(MountainRenderer mountainRenderer) {
		this.mountainRenderer = mountainRenderer;
	}

	public WorldGenContainer getWorldGenContainer() {
		return worldGenContainer;
	}

	public void setWorldGenContainer(WorldGenContainer worldGenContainer) {
		this.worldGenContainer = worldGenContainer;
	}

	public void setValleyRenderer(ValleyRenderer valleyRenderer) {
		this.valleyRenderer = valleyRenderer;
	}

	public void setHillGenerator(HillGenerator hillGenerator) {
		this.hillGenerator = hillGenerator;
	}

	public void setHillRenderer(HillRenderer hillRenderer) {
		this.hillRenderer = hillRenderer;
	}

	public void setOceanFiller(OceanFiller oceanFiller) {
		this.oceanFiller = oceanFiller;
	}
}