package stonering.enums.items.type.raw;

import stonering.entity.Aspect;
import stonering.entity.item.aspects.FuelAspect;
import stonering.entity.item.aspects.ItemContainerAspect;
import stonering.entity.item.aspects.ValueAspect;
import stonering.entity.item.aspects.WearAspect;
import stonering.enums.items.type.ItemPartType;
import stonering.enums.items.type.ItemType;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Processes {@link RawItemType} into {@link ItemType} (creates type Aspect).
 * @author Alexander on 09.09.2019.
 */
public class RawItemTypeProcessor {

    public ItemType process(RawItemType rawType) {
        ItemType type = new ItemType(rawType);
        createParts(type, rawType);
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
        for (List<String> rawAspect : rawType.aspects) {
            type.aspects.put(rawAspect.remove(0), rawAspect);
        }
    }

    private Aspect provideAspect(List<String> aspectDescription, ItemType type) {
        if (aspectDescription.isEmpty()) {
            Logger.LOADING.logWarn("Invalid type aspect description for item type" + type.name);
            return null;
        }
        List<String> args = aspectDescription.subList(1, aspectDescription.size());
        switch (aspectDescription.get(0)) {
            case "value": {
                return new ValueAspect(type, Float.valueOf(aspectDescription.get(1)));
            }
            case "fuel": {
                return new FuelAspect(type);
            }
            case "wear": {
                return new WearAspect(type, args);
            }
        }
        return null;
    }
}
