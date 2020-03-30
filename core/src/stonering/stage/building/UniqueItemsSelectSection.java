package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import stonering.entity.item.Item;
import stonering.enums.images.DrawableMap;
import stonering.enums.items.recipe.Ingredient;
import stonering.game.model.system.item.ItemsStream;
import stonering.widget.item.ItemSelectGrid;
import stonering.widget.item.SingleItemSquareButton;

import java.util.List;

/**
 * Section for selecting items like tools, anvils and sawblades for building.
 *
 * @author Alexander on 27.03.2020
 */
public class UniqueItemsSelectSection extends Container<ItemSelectGrid> {
    private ItemSelectGrid grid;

    public UniqueItemsSelectSection(Ingredient ingredient) {
        List<Item> items = new ItemsStream().filterByTypes(ingredient.itemTypes).filterHasTag(ingredient.tag).toList();
        grid = new ItemSelectGrid(5, items.size() / 5 + 1);
        Drawable background = DrawableMap.getTextureDrawable("ui/item_slot.png");
        for (Item item : items) {
            grid.addActorToGrid(new SingleItemSquareButton(item, background));
        }
    }
}
