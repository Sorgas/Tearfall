package stonering.enums.items;

import com.badlogic.gdx.graphics.Color;
import stonering.entity.local.crafting.ItemPartCraftingStep;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Descriptor class of item. Stores all aspects, valid to the whole type of items, not for specific ones.
 * (e.g. not material, condition, ownership)
 */
public class ItemType {
    private String title;
    private float valueMod;
    private boolean isResource; // true if this item can be used for crafting as raw resource (its volume is counted).

    private WearItemType wear;
    private ToolItemType tool;
    private ContainerItemType container;

    private ArrayList<ItemPartCraftingStep> parts;

    private HashMap<String, ArrayList<Object>> aspects;
    private int atlasX;
    private int atlasY;
    private Color color;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public float getValueMod() {
        return valueMod;
    }

    public void setValueMod(float valueMod) {
        this.valueMod = valueMod;
    }

    public HashMap<String, ArrayList<Object>> getAspects() {
        return aspects;
    }

    public void setAspects(HashMap<String, ArrayList<Object>> aspects) {
        this.aspects = aspects;
    }

    public boolean isResource() {
        return isResource;
    }

    public void setResource(boolean resource) {
        isResource = resource;
    }

    public WearItemType getWear() {
        return wear;
    }

    public void setWear(WearItemType wear) {
        this.wear = wear;
    }

    public ToolItemType getTool() {
        return tool;
    }

    public void setTool(ToolItemType tool) {
        this.tool = tool;
    }

    public ContainerItemType getContainer() {
        return container;
    }

    public void setContainer(ContainerItemType container) {
        this.container = container;
    }

    public ArrayList<ItemPartCraftingStep> getParts() {
        return parts;
    }

    public void setParts(ArrayList<ItemPartCraftingStep> parts) {
        this.parts = parts;
    }
}
