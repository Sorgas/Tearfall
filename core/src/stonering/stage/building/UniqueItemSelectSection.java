package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
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
public class UniqueItemSelectSection extends ItemSelectSection {
    private ItemSelectGrid grid;

    public UniqueItemSelectSection(Ingredient ingredient, String title) {
        super(title);
        List<Item> items = new ItemsStream().filterByTypes(ingredient.itemTypes).filterHasTag(ingredient.tag).toList();
        grid = new ItemSelectGrid(5, items.size() / 5 + 1);
        Drawable background = DrawableMap.getTextureDrawable("ui/item_slot.png");
        for (Item item : items) {
            grid.addActorToGrid(new SingleItemSquareButton(item, background));
        }
    }

    @Override
    public ItemSelector getItemSelector() {
        // create multyitem selector
        return null;
    }

    @Override
    protected void setAllEnabled(boolean enable) {

    }

    @Override
    public boolean isAtLeastOneSelected() {
        return false;
    }
}