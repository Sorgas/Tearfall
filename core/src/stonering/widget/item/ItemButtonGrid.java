package stonering.widget.item;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import stonering.entity.item.Item;
import stonering.entity.item.ItemGroupingKey;
import stonering.game.model.system.item.ItemsStream;
import stonering.game.model.system.item.OnMapItemsStream;

/**
 * {@link ActorGrid} for item buttons. Fills unused cells with empty buttons.
 *
 * @author Alexander on 27.03.2020
 */
public class ItemButtonGrid extends ActorGrid<SingleItemSquareButton> {

    public ItemButtonGrid(int cellWidth) {
        super(cellWidth);
    }

    public void fill(Collection<Item> items) {
        Map<ItemGroupingKey, List<Item>> itemMap = new ItemsStream(items).groupByTypeAndMaterial();
//        itemMap.entrySet().forEach();
//        items.forEach(item -> {
//            super.addActorToGrid(new SingleItemSquareButton());
//                }
//        );
    }
}
