package stonering.enums.plants;

import java.util.ArrayList;

/**
 * Stores plant parameters
 */
public class PlantType {
    private String name;
    private String title;

    private String description;
    private int[] temperatureBounds;
    private int[] rainfallBounds;
    private ArrayList<String> waterSource;
    private ArrayList<String> lightNeed;
    private String soilType;
    private ArrayList<PlantLifeStage> lifeStages;
    private TreeType treeType;

    public PlantType() {
        temperatureBounds = new int[4];
        rainfallBounds = new int[2];
    }

    public static class PlantLifeStage {
        private String[] titlePrefixSuffix;
        private int stageLength;
        private ArrayList<String> harvestProducts;
        private ArrayList<String> cutProducts;
        private String materialName;
        private int[] atlasXY;
        private String color;

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
    }

    public boolean isTree() {
        return treeType != null;
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

    public TreeType getTreeType() {
        return treeType;
    }

    public void setTreeType(TreeType treeType) {
        this.treeType = treeType;
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
}
