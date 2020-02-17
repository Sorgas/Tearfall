package stonering.widget.item;

import stonering.enums.items.type.ItemType;

/**
 * @author Alexander on 17.02.2020.
 */
public class StackedItemSquareButton {
    public final ItemType type;
    public int number;

    public StackedItemSquareButton(ItemType type) {
        this.type = type;
    }
}
