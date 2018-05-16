package stonering.enums.plants;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

/**
 * Stores plant parameters
 */
public class PlantType {
    private String specimen;
    private String title;

    private String description;
    private int minTemperature;
    private int maxTemperature;
    private int minGrowingTemperature;
    private int maxGrowingTemperature;
    private int minRainfall;
    private int maxRainfall;
    private ArrayList<String> waterSource;
    private ArrayList<String> lightNeed;
    private String soilType;
    private String materialName;

    private int atlasX;
    private int atlasY;

    private String harvestProduct;
    private String cutProduct[];
    private Color color;

    private TreeType treeType;
    private int saplingTime;

    public class PlantLifeStage {
        private int stageLength;
        private String harvestProduct;
        private String cutProduct;
        private int atlasX;
        private int atlasY;
        private Color color;

        public int getStageLength() {
            return stageLength;
        }

        public void setStageLength(int stageLength) {
            this.stageLength = stageLength;
        }

        public String getHarvestProduct() {
            return harvestProduct;
        }

        public void setHarvestProduct(String harvestProduct) {
            this.harvestProduct = harvestProduct;
        }

        public String getCutProduct() {
            return cutProduct;
        }

        public void setCutProduct(String cutProduct) {
            this.cutProduct = cutProduct;
        }

        public int getAtlasX() {
            return atlasX;
        }

        public void setAtlasX(int atlasX) {
            this.atlasX = atlasX;
        }

        public int getAtlasY() {
            return atlasY;
        }

        public void setAtlasY(int atlasY) {
            this.atlasY = atlasY;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }

    public boolean isTree() {
        return treeType != null;
    }

    public String getSpecimen() {
        return specimen;
    }

    public void setSpecimen(String specimen) {
        this.specimen = specimen;
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
        return minTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public int getMinGrowingTemperature() {
        return minGrowingTemperature;
    }

    public void setMinGrowingTemperature(int minGrowingTemperature) {
        this.minGrowingTemperature = minGrowingTemperature;
    }

    public int getMaxGrowingTemperature() {
        return maxGrowingTemperature;
    }

    public void setMaxGrowingTemperature(int maxGrowingTemperature) {
        this.maxGrowingTemperature = maxGrowingTemperature;
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

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public TreeType getTreeType() {
        return treeType;
    }

    public void setTreeType(TreeType treeType) {
        this.treeType = treeType;
    }

    public int getSaplingTime() {
        return saplingTime;
    }

    public void setSaplingTime(int saplingTime) {
        this.saplingTime = saplingTime;
    }

    public int getAtlasX() {
        return atlasX;
    }

    public void setAtlasX(int atlasX) {
        this.atlasX = atlasX;
    }

    public int getAtlasY() {
        return atlasY;
    }

    public void setAtlasY(int atlasY) {
        this.atlasY = atlasY;
    }

    public String getHarvestProduct() {
        return harvestProduct;
    }

    public void setHarvestProduct(String harvestProduct) {
        this.harvestProduct = harvestProduct;
    }

    public String[] getCutProduct() {
        return cutProduct;
    }

    public void setCutProduct(String[] cutProduct) {
        this.cutProduct = cutProduct;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
