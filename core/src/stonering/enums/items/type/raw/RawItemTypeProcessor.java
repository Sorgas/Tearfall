package stonering.enums.items.type.raw;

import stonering.entity.Aspect;
import stonering.entity.item.aspects.FuelAspect;
import stonering.entity.item.aspects.ValueAspect;
import stonering.entity.item.aspects.WearAspect;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.ItemTypeMap;
import stonering.util.logging.Logger;
import stonering.util.lang.Pair;

import java.util.Arrays;
import java.util.List;

/**
 * Processes {@link RawItemType} into {@link ItemType} (creates type aspects).
 * In definition files, aspects configured as strings with format : "wear(humanoid,foot,medium)".
 * Item types can extends each other, overriding field values.
 *
 * @author Alexander on 09.09.2019.
 */
public class RawItemTypeProcessor {

    public ItemType process(RawItemType rawType) {
        return addAspectsFromRawType(new ItemType(rawType), rawType);
    }

    public ItemType processExtendedType(RawItemType rawType, String namePrefix) {
        ItemType baseType = ItemTypeMap.instance().getItemType(rawType.baseItem); // get base type
        return addAspectsFromRawType(new ItemType(baseType, rawType, namePrefix), rawType);
    }

    private ItemType addAspectsFromRawType(ItemType type, RawItemType rawType) {
        rawType.typeAspects.forEach(s -> type.add(createAspect(s, type))); // create type aspects
        rawType.aspects.stream().map(this::parseAspectString).forEach(pair -> type.itemAspects.put(pair.key, pair.value));
        return type;
    }

    private Aspect createAspect(String aspectString, ItemType type) {
        Pair<String, List<String>> pair = parseAspectString(aspectString);
        switch (pair.key) {
            case "value": {
                return new ValueAspect(type, Float.parseFloat(pair.value.get(0)));
            }
            case "fuel": {
                return new FuelAspect(type);
            }
            case "wear": {
                return new WearAspect(type, pair.value);
            }
            default:
                return Logger.LOADING.logWarn("Item type aspect with name " + type.name, null);
        }
    }

    private Pair<String, List<String>> parseAspectString(String aspectString) {
        String[] aspectParts = aspectString.replace(")", "").split("\\(");
        return new Pair<>(aspectParts[0], aspectParts.length > 1 ? Arrays.asList(aspectParts[1].split(",")) : null);
    }
}
