package stonering.enums.items.type.raw;

import stonering.entity.Aspect;
import stonering.entity.item.aspects.FuelAspect;
import stonering.entity.item.aspects.ValueAspect;
import stonering.entity.item.aspects.WearAspect;
import stonering.enums.items.type.ItemPartType;
import stonering.enums.items.type.ItemType;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Processes {@link RawItemType} into {@link ItemType} (creates type aspects).
 * In definition files, aspects configured as strings with format : "wear(humanoid,foot,medium)".
 * @author Alexander on 09.09.2019.
 */
public class RawItemTypeProcessor {

    public ItemType process(RawItemType rawType) {
        ItemType type = new ItemType(rawType);
//        createParts(type, rawType);
        fillAspects(type, rawType); // item aspects
        rawType.typeAspects.forEach(args -> type.addAspect(provideAspect(args, type))); // type aspects
        return type;
    }

    /**
     * Copies parts from raw object. If there is no parts, adds single default part.
     */
    private List<ItemPartType> createParts(ItemType type, RawItemType rawType) {
        List<ItemPartType> parts = new ArrayList<>();
        if (rawType.parts != null && !rawType.parts.isEmpty()) {
            parts.addAll(rawType.parts);
        } else {
            parts.add(new ItemPartType(type.name, true));
        }
        return parts;
    }

    /**
     * Translates raw lists of aspects to aspect names mapped to aspect parameters.
     */
    private void fillAspects(ItemType type, RawItemType rawType) {
        if (rawType.aspects == null || rawType.aspects.isEmpty()) return;
        for (String aspectString : rawType.aspects) {
            String [] aspectStringParts = aspectString.split("\\(");


        }
    }

    private Aspect createAspect(String aspectString, ItemType type) {
        String[] aspectParts = aspectString.split("\\(");
        String aspectName = aspectParts[0];
        String[] aspectParams = aspectParts[1].replace(")", "").split(",");

        switch (aspectName) {
            case "value": {
                return new ValueAspect(type, Float.parseFloat(aspectParams[0]));
            }
            case "fuel": {
                return new FuelAspect(type);
            }
            case "wear": {
                return new WearAspect(type, args);
            }
            default: {
                Logger.LOADING.logWarn("Item type aspect with name " + type.name);
                return null;

            }
        }
    }
}
