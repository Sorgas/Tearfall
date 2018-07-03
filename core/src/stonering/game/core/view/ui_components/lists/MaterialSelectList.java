package stonering.game.core.view.ui_components.lists;

import stonering.game.core.GameMvc;

import java.util.HashMap;

/**
 * List that shows items, appropriate for building.
 *
 * @author Alexander on 03.07.2018.
 */
public class MaterialSelectList extends StringIntegerList {
    private GameMvc gameMvc;

    public MaterialSelectList(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
    }

    public void init() {

    }

    public void refill(String buildingTitle) {
        clear();
        HashMap<String, Integer> materials = gameMvc.getModel().getItemContainer().getAvailableMaterialsForBuilding(buildingTitle);
        addItems(materials);
        addListener(event -> {
            if (getSelectedIndex() >= 0) {
                gameMvc.getController().getDesignationsController().setMaterial(getSelectedString());
                System.out.println("selected material: " + getSelectedString());
                return true;
            } else {
                return false;
            }
        });
        if(getItems().size > 0) {
            setSelectedIndex(-1); //change event is not fired without this.
            setSelectedIndex(0);
        }
    }
}
