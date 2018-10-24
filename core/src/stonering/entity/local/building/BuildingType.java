package stonering.entity.local.building;

import com.badlogic.gdx.graphics.Color;
import stonering.entity.local.crafting.CommonComponentStep;

import java.util.ArrayList;

/**
 * Type of building.
 * Determines building process, usage, appearance.
 *
 * @author Alexander Kuzyakov
 * created on 28.06.2018
 */
public class BuildingType {
    private String building;
    private String title;
    private String description;
    private String category;
    private ArrayList<CommonComponentStep> components;
    private ArrayList<String> menuPath;
    private ArrayList<String> operations;

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

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public ArrayList<String> getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(ArrayList<String> menuPath) {
        this.menuPath = menuPath;
    }

    public ArrayList<String> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<String> operations) {
        this.operations = operations;
    }

    public ArrayList<CommonComponentStep> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<CommonComponentStep> components) {
        this.components = components;
    }
}
