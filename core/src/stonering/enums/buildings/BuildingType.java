package stonering.enums.buildings;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

/**
 * @author Alexander Kuzyakov
 * created on 28.06.2018
 */
public class BuildingType {
    private String title;
    private String description;
    private ArrayList<String> items;
    private ArrayList<String> materials;
    private int amount;
    private String category;
    private String hotKey;

    private int atlasX;
    private int atlasY;
    private Color color;

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

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public ArrayList<String> getMaterials() {
        return materials;
    }

    public void setMaterials(ArrayList<String> materials) {
        this.materials = materials;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getHotKey() {
        return hotKey;
    }

    public void setHotKey(String hotKey) {
        this.hotKey = hotKey;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
