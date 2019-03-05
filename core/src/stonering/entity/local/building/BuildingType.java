package stonering.entity.local.building;

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
    private List<CommonComponentStep> components;
    private List<String> menuPath;
    private List<String> recipes;
    private List<List<String>> aspects;
    private List<String> parts;

    private int[] atlasXY;
    private String color;

    public BuildingType() {
        components = new ArrayList<>();
        menuPath = new ArrayList<>();
        recipes = new ArrayList<>();
        aspects = new ArrayList<>();
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

    public List<List<String>> getAspects() {
        return aspects;
    }

    public void setAspects(List<List<String>> aspects) {
        this.aspects = aspects;
    }

    public List<String> getParts() {
        return parts;
    }

    public void setParts(List<String> parts) {
        this.parts = parts;
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
}
