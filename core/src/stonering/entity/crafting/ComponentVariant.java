package stonering.entity.crafting;

import stonering.entity.building.Blueprint;

/**
 * Variant for {@link BuildingComponent} of {@link Blueprint}.
 *
 * @author Alexander on 08.03.2019.
 */
public class ComponentVariant {
    public final String itemType;
    public final String tag;
    public final int amount;
    public final int[] atlasXY;

    public ComponentVariant(String itemType, String tag, int amount, int[] atlasXY) {
        this.itemType = itemType;
        this.tag = tag;
        this.amount = amount;
        this.atlasXY = atlasXY;
    }
}
