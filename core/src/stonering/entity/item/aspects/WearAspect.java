package stonering.entity.item.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.WearLayerEnum;
import stonering.enums.items.type.ItemType;

import java.util.List;

/**
 * {@link Item}s of {@link ItemType} with this aspect can be worn by units.
 *
 * //TODO handle left and right items and limbs.
 *
 * @author Alexander on 09.09.2019.
 */
public class WearAspect extends Aspect {
    public final String bodyTemplate; // creatures with this template can use item
    public final WearLayerEnum layer;
    public final List<String> additionalLimbs; // body parts, covered by item
    public final String slot; // 

    public WearAspect(Entity entity, List<String> arguments) {
        super(entity);
        bodyTemplate = arguments.get(0);
        slot = arguments.get(1);
        layer = WearLayerEnum.getByName(arguments.get(2));
        additionalLimbs = arguments.subList(3, arguments.size());
    }
}
