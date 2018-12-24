package stonering.entity.local.building;

import com.badlogic.gdx.graphics.Color;
import stonering.entity.local.crafting.CommonComponentStep;

import java.util.ArrayList;
import java.util.List;

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
    private List<CommonComponentStep> components;
    private List<String> menuPath;
    private List<String> recipes;

    private boolean container;

    private int atlasX;
    private int atlasY;
    private Color color;

    public BuildingType() {
        components = new ArrayList<>();
        menuPath = new ArrayList<>();
        recipes = new ArrayList<>();
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

    public List<String> getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(List<String> menuPath) {
        this.menuPath = menuPath;
    }

    public List<String> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<String> recipes) {
        this.recipes = recipes;
    }

    public List<CommonComponentStep> getComponents() {
        return components;
    }

    public void setComponents(List<CommonComponentStep> components) {
        this.components = components;
    }
}
