package stonering.game.core.view.render.ui.lists;

import com.badlogic.gdx.Input;
import stonering.entity.local.crafting.CommonComponentStep;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.game.core.view.render.ui.menus.Toolbar;
import stonering.game.core.view.render.ui.menus.util.HideableComponent;
import stonering.game.core.view.render.ui.menus.util.Invokable;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.SimpleItemSelector;
import stonering.game.core.view.render.ui.util.ListItem;

import java.util.List;

/**
 * List that shows items, appropriate for building.
 * If no items found for building, shows shows indication line which cannot be selected.
 * Takes information from {@link DesignationsController}
 *
 * @author Alexander on 03.07.2018.
 */
public class MaterialSelectList extends ItemsCountList implements Invokable, HideableComponent {
    private GameMvc gameMvc;
    private boolean hideable;
    private DesignationsController controller;
    protected Toolbar toolbar;
    private boolean isEmpty = true;

    public MaterialSelectList(GameMvc gameMvc) {
        super();
        this.gameMvc = gameMvc;
        this.hideable = true;
    }

    public void init() {
        controller = gameMvc.getController().getDesignationsController();
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
    }

    @Override
    public boolean invoke(int keycode) {
        switch (keycode) {
            case Input.Keys.W:
                up();
                return true;
            case Input.Keys.S:
                down();
                return true;
            case Input.Keys.E:
                select();
                return true;
            case Input.Keys.Q:
                hide();
                return true;
        }
        return false;
    }

    /**
     * Confirms selected item to controller if list is not empty.
     */
    @Override
    public void select() {
        if (!isEmpty) {
            ListItem selected = getSelectedListItem();
            //TODO handle amount requirements more than 1
            controller.addItemSelector(new SimpleItemSelector(selected.getTitle(), selected.getMaterial(), 1));
        }
    }

    @Override
    public void close() {

    }

    /**
     * Fills this list for given crafting or building step.
     *
     * @param step
     */
    public void fillForCraftingStep(CommonComponentStep step) {
        clear();
        List<Item> items = gameMvc.getModel().getItemContainer().getAvailableMaterialsCraftingStep(step, controller.getStart());
        if (!items.isEmpty()) {
            addItems(items);
            isEmpty = false;
            addListener(event -> {
                if (getSelectedIndex() >= 0) {
                    System.out.println("selected material: ");
                    return true;
                } else {
                    return false;
                }
            });
            setSelectedIndex(-1); //change event is not fired without this.
            setSelectedIndex(0);
        } else {
            setItems(new ListItem("No items available.", null));
        }
    }

    @Override
    public void show() {
        toolbar.addMenu(this);
    }

    @Override
    public void hide() {
    }

    @Override
    public void clearItems() {
        super.clearItems();
        isEmpty = true;
    }
}
