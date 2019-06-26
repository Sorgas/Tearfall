package stonering.game.view.render.ui.lists;

import stonering.entity.local.crafting.CommonComponent;
import stonering.game.GameMvc;
import stonering.game.controller.controllers.toolbar.DesignationsController;
import stonering.game.model.lists.ItemContainer;
import stonering.game.view.render.ui.menus.toolbar.Toolbar;
import stonering.game.view.render.ui.menus.util.HideableComponent;
import stonering.entity.local.item.Item;
import stonering.util.geometry.Position;

import java.util.List;

/**
 * List that shows item, appropriate for building.
 * If no item found for building, shows shows indication line which cannot be selected.
 * Takes information from {@link DesignationsController}
 *
 * @author Alexander on 03.07.2018.
 */
public class MaterialSelectList extends ItemsCountList implements HideableComponent {
    private GameMvc gameMvc;
    protected Toolbar toolbar;

    public MaterialSelectList() {
        super();
        gameMvc = GameMvc.instance();
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
    }

    /**
     * Fills this list for given crafting or building step.
     *
     * @param step
     */
    public void fillForCraftingStep(CommonComponent step, Position position) {
        clearItems();
        List<Item> items = gameMvc.getModel().get(ItemContainer.class).getAvailableMaterialsCraftingStep(step, position);
        if (!items.isEmpty()) {
            addItems(items);
            setSelectedIndex(-1); //change event is not fired without this.
            setSelectedIndex(0);
        } else {
            setItems(new ListItem("No item available.", null));
        }
    }

    @Override
    public void show() {
        toolbar.addMenu(this);
    }

    @Override
    public void hide() {
        toolbar.hideMenu(this);
    }
}