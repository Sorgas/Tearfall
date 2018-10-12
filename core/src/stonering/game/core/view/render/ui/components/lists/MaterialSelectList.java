package stonering.game.core.view.render.ui.components.lists;

import com.badlogic.gdx.Input;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.DesignationsController;
import stonering.game.core.view.render.ui.components.menus.Invokable;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.SimpleItemSelector;

import java.util.List;

/**
 * List that shows items, appropriate for building.
 * Takes information from {@link DesignationsController}
 *
 * @author Alexander on 03.07.2018.
 */
public class MaterialSelectList extends ItemsCountList implements Invokable {
    private DesignationsController controller;

    public MaterialSelectList(GameMvc gameMvc) {
        super(gameMvc, true);
    }

    public void init() {
        super.init();
        controller = gameMvc.getController().getDesignationsController();
    }

    @Override
    public boolean invoke(int keycode) {
        switch (keycode) {
            case Input.Keys.X:
                up();
                return true;
            case Input.Keys.Z:
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

    @Override
    public void select() {
        super.select();
        ListItem selected = getSelectedListItem();
        //TODO handle amount requirements more than 1
        controller.addItemSelector(new SimpleItemSelector(selected.title, selected.material, 1));
        controller.finishTaskBuilding();
    }

    public void refill() {
        clear();
        List<Item> items = gameMvc.getModel().getItemContainer().getAvailableMaterialsForBuilding(controller.getBuilding(), controller.getStart());
        addItems(items);
        addListener(event -> {
            if (getSelectedIndex() >= 0) {
                System.out.println("selected material: ");
                return true;
            } else {
                return false;
            }
        });
        if (getItems().size > 0) {
            setSelectedIndex(-1); //change event is not fired without this.
            setSelectedIndex(0);
        }
    }
}
