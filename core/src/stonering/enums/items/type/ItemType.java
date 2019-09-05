package stonering.enums.items.type;

import stonering.entity.Entity;
import stonering.entity.item.aspects.FuelAspect;
import stonering.entity.item.aspects.ValueAspect;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Descriptor class of item. Stores all aspects, valid to the whole type of item, not for specific ones.
 * (e.g. not material, condition, ownership)
 */
public class ItemType extends Entity {
    public String name;                                // id
    public String title;                               // displayable name
    public String description;                         // displayable description

    public WearItemType wear;                          // is set if this item could be worn
    public ToolItemType tool;                          // is set if this item could be used as tool

    public ArrayList<ItemPartType> parts;              // defines parts of item. first one is main
    public Map<String, List<String>> aspects;          // other aspects, item aspects filled from this on generation.

    // render
    public int[] atlasXY;
    public String color;

    public ItemType(RawItemType rawItemType) {
        super();
        name = rawItemType.name;
        title = rawItemType.title.isEmpty() ? capitalize(rawItemType.name) : rawItemType.title;
        description = rawItemType.description;
        wear = rawItemType.wear;
        tool = rawItemType.tool;
        atlasXY = rawItemType.atlasXY;
        createParts(rawItemType.parts);
        fillAspects(rawItemType.aspects);
        createAspects(rawItemType.typeAspects);
    }

    /**
     * Copies parts from raw object. If there is no parts, add single default part.
     */
    private void createParts(List<ItemPartType> rawParts) {
        parts = new ArrayList<>();
        if (rawParts != null && !rawParts.isEmpty()) {
            parts.addAll(rawParts);
        } else {
            parts.add(new ItemPartType("name", true));
        }
    }

    /**
     * Translates raw lists of aspect parameters to map.
     */
    private void fillAspects(List<List<String>> rawAspects) {
        aspects = new HashMap<>();
        if (rawAspects == null || rawAspects.isEmpty()) return;
        for (List<String> rawAspect : rawAspects) {
            aspects.put(rawAspect.remove(0), rawAspect);
        }
    }

    /**
     * Creates constant aspects.
     */
    private void createAspects(List<List<String>> rawAspects) {
        for (List<String> aspectDescription : rawAspects) {
            if (aspectDescription.isEmpty()) Logger.LOADING.logWarn("Invalid aspect description for item " + name);
            switch (aspectDescription.get(0)) {
                case "value": {
                    addAspect(new ValueAspect(this, Float.valueOf(aspectDescription.get(1))));
                    continue;
                }
                case "fuel": {
                    addAspect(new FuelAspect(this));
                    continue;
                }
            }
        }
    }

    private String capitalize(String text) {
        return (text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase()).replace('_', ' ');
    }
}
