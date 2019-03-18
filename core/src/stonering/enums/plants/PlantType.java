package stonering.enums.plants;

import stonering.util.global.Initable;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores plant parameters
 */
public class PlantType implements Initable {
    private String name;
    private String title;

    private String description;
    private int[] temperatureBounds;
    private int[] rainfallBounds;
    private ArrayList<String> waterSource;
    private ArrayList<String> lightNeed;
    private String soilType;
    private ArrayList<PlantLifeStage> lifeStages;
    private ArrayList<String> placingTags;
    private List<Integer> plantingStart;

    public PlantType() {
        temperatureBounds = new int[4];
        rainfallBounds = new int[2];
        plantingStart = new ArrayList<>();
    }

    /**
     * Recounts life stages bounds.
     */
    @Override
    public void init() {
        int totalAge = 0;
        for (PlantLifeStage lifeStage : lifeStages) {
            totalAge += lifeStage.stageLength;
            lifeStage.stageEnd = totalAge;
        }
    }

    public int getMaxAge() {
        return lifeStages.get(lifeStages.size() - 1).getStageEnd();
    }

    public static class PlantLifeStage {
        private String[] titlePrefixSuffix;
        private int stageLength;
        private int stageEnd;
        private ArrayList<String> harvestProducts;
        private ArrayList<String> cutProducts;
        private String materialName;
        private int[] atlasXY;
        private String color;
        private TreeType treeType;

        public int getStageLength() {
            return stageLength;
        }

        public void setStageLength(int stageLength) {
            this.stageLength = stageLength;
        }

        public ArrayList<String> getHarvestProducts() {
            return harvestProducts;
        }

        public void setHarvestProducts(ArrayList<String> harvestProducts) {
            this.harvestProducts = harvestProducts;
        }

        public ArrayList<String> getCutProducts() {
            return cutProducts;
        }

        public void setCutProducts(ArrayList<String> cutProducts) {
            this.cutProducts = cutProducts;
        }

        public int[] getAtlasXY() {
            return atlasXY;
        }

        public void setAtlasXY(int[] atlasXY) {
            this.atlasXY = atlasXY;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getMaterialName() {
            return materialName;
        }

        public void setMaterialName(String materialName) {
            this.materialName = materialName;
        }

        public String[] getTitlePrefixSuffix() {
            return titlePrefixSuffix;
        }

        public void setTitlePrefixSuffix(String[] titlePrefixSuffix) {
            this.titlePrefixSuffix = titlePrefixSuffix;
        }

        public TreeType getTreeType() {
            return treeType;
        }

        public void setTreeType(TreeType treeType) {
            this.treeType = treeType;
        }

        public int getStageEnd() {
            return stageEnd;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinTemperature() {
        return temperatureBounds[0];
    }

    public void setMinTemperature(int minTemperature) {
        temperatureBounds[0] = minTemperature;
    }

    public int getMaxTemperature() {
        return temperatureBounds[1];
    }

    public void setMaxTemperature(int maxTemperature) {
        temperatureBounds[1]= maxTemperature;
    }

    public int getMinGrowingTemperature() {
        return temperatureBounds[2];
    }

    public void setMinGrowingTemperature(int minGrowingTemperature) {
        temperatureBounds[2]= minGrowingTemperature;
    }

    public int getMaxGrowingTemperature() {
        return temperatureBounds[3];
    }

    public void setMaxGrowingTemperature(int maxGrowingTemperature) {
        temperatureBounds[3] = maxGrowingTemperature;
    }

    public int getMinRainfall() {
        return rainfallBounds[0];
    }

    public void setMinRainfall(int minRainfall) {
        rainfallBounds[0] = minRainfall;
    }

    public int getMaxRainfall() {
        return rainfallBounds[1];
    }

    public void setMaxRainfall(int maxRainfall) {
        rainfallBounds[1] = maxRainfall;
    }

    public ArrayList<String> getWaterSource() {
        return waterSource;
    }

    public void setWaterSource(ArrayList<String> waterSource) {
        this.waterSource = waterSource;
    }

    public ArrayList<String> getLightNeed() {
        return lightNeed;
    }

    public void setLightNeed(ArrayList<String> lightNeed) {
        this.lightNeed = lightNeed;
    }

    public String getSoilType() {
        return soilType;
    }

    public void setSoilType(String soilTypes) {
        this.soilType = soilTypes;
    }

    public ArrayList<PlantLifeStage> getLifeStages() {
        return lifeStages;
    }

    public void setLifeStages(ArrayList<PlantLifeStage> lifeStages) {
        this.lifeStages = lifeStages;
    }

    public int[] getTemperatureBounds() {
        return temperatureBounds;
    }

    public void setTemperatureBounds(int[] temperatureBounds) {
        this.temperatureBounds = temperatureBounds;
    }

    public int[] getRainfallBounds() {
        return rainfallBounds;
    }

    public void setRainfallBounds(int[] rainfallBounds) {
        this.rainfallBounds = rainfallBounds;
    }

    public boolean isTree() {
        return lifeStages.get(0).treeType != null;
    }

    public ArrayList<String> getPlacingTags() {
        return placingTags;
    }

    public void setPlacingTags(ArrayList<String> placingTags) {
        this.placingTags = placingTags;
    }

    public List<Integer> getPlantingStart() {
        return plantingStart;
    }

    public void setPlantingStart(List<Integer> plantingStart) {
        this.plantingStart = plantingStart;
    }
}
