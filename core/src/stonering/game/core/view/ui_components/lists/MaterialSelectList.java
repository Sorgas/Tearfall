package stonering.game.core.view.ui_components.lists;

import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.DesignationsController;
import stonering.objects.local_actors.items.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * List that shows items, appropriate for building.
 * Takes information from {@link DesignationsController}
 *
 * @author Alexander on 03.07.2018.
 */
public class MaterialSelectList extends ItemsCountList {
    private GameMvc gameMvc;
    private DesignationsController controller;

    public MaterialSelectList(GameMvc gameMvc) {
        super(gameMvc, true);
        this.gameMvc = gameMvc;
    }

    public void init() {
        controller = gameMvc.getController().getDesignationsController();
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
