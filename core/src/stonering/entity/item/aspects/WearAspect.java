package stonering.entity.item.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.WearLayers;
import stonering.enums.items.type.ItemType;

import java.util.List;

/**
 * {@link Item}s of {@link ItemType} with this aspect can be worn by units.
 * //TODO handle left and right items and limbs.
 *
 * @author Alexander on 09.09.2019.
 */
public class WearAspect extends Aspect {
    public final String bodyTemplate; // creatures with this template can use item
    public final WearLayers layer;
    public final List<String> additionalLimbs; // body parts, covered by item

    public WearAspect(Entity entity, List<String> arguments) {
        super(entity);
        bodyTemplate = arguments.get(0);
        layer = WearLayers.getByName(arguments.get(1));
        additionalLimbs = arguments.subList(2, arguments.size());
    }
}
