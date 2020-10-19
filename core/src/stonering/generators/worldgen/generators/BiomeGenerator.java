package stonering.generators.worldgen.generators;

import stonering.generators.worldgen.WorldGenContainer;

/**
 * Determines biomes marks for each cell of the world.
 * Should be used only for rendering world maps and not for flora/fauna areals determination.
 * Plants and animals should spread across different biomes.
 *
 * @author Alexander on 02.10.2018.
 */
public class BiomeGenerator extends WorldGenerator {
    private int width;
    private int height;
    private float seaLevel;

    @Override
    public void set(WorldGenContainer container) {
        width = container.config.width;
        height = container.config.height;
        seaLevel = container.config.seaLevel;
    }

    @Override
    public void run() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                markBiome(countBiome(x, y), x, y);
            }
        }
    }

    private int countBiome(int x, int y) {
        float temperature = (container.getSummerTemperature(x, y) + container.getWinterTemperature(x, y)) / 2f;
        if (container.getElevation(x, y) > seaLevel) {
            float rainfall = container.getRainfall(x, y) * (1 - container.getDrainage(x, y));
            if (temperature < -5) {
                return 3; // tundra
            } else if (temperature < 5) {
                return countBoreal(rainfall);
            } else if (temperature < 20) {
                return countTemperate(rainfall);
            } else {
                return countTropic(rainfall);
            }
        } else {
            if (temperature > 0) {
                return temperature > 15 ? 2 : 1; // warm, temperate sea
            } else {
                return 0; // cold sea
            }
        }
    }

    private void markBiome(int biome, int x, int y) {
        container.setBiome(x, y, biome);
    }

    private int countBoreal(float rainfall) {
        return rainfall < 17 ? 4 : 5; // grasslands, taiga
    }

    private int countTemperate(float rainfall) {
        if (rainfall < 17) {
            return 6; // steppe
        } else if (rainfall < 33) {
            return 7; // shrubland
        } else if (rainfall < 83) {
            return 8; // deciduous forest
        } else {
            return 9; //temp rainforest;
        }
    }


    private int countTropic(float rainfall) {
        if (rainfall < 17) {
            return 10; // desert
        } else if (rainfall < 33) {
            return 11; // savannah
        } else if (rainfall < 83) {
            return 12; // monsoon forest
        } else {
            return 13; // tropic rainforest
        }
    }
}
