package stonering.game.core.view.render.ui.components.menus.building;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.local.building.BuildingType;
import stonering.enums.buildings.BuildingMap;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.DesignationsController;
import stonering.game.core.view.render.ui.components.menus.util.ButtonMenu;
import stonering.game.core.view.render.ui.components.menus.util.PlaceSelectComponent;

import java.util.HashMap;
import java.util.List;

/**
 * Menu for one category of buildings.
 * Loads its children by menupath property of buiding types.
 *
 * @author Alexander Kuzyakov on 15.06.2018
 */
public class BuildingCategoryMenu extends ButtonMenu {
    private final String category;

    private DesignationsController controller;
    private PlaceSelectComponent placeSelectComponent;

    protected HashMap<String, BuildingCategoryMenu> menus;

    public BuildingCategoryMenu(GameMvc gameMvc, String category) {
        super(gameMvc, true);
        this.category = category;
        hideable = true;
        menus = new HashMap<>();
        loadItems();
    }

    @Override
    public void init() {
        super.init();
        controller = gameMvc.getController().getDesignationsController();
        placeSelectComponent.init();
    }

    /**
     * Creates all buttons.
     * <p>
     * Creates {@link PlaceSelectComponent} (one for all constructions).
     */
    private void loadItems() {
        BuildingMap.getInstance().getCategoryBuildings(category).forEach(building -> {
            building.getMenuPath()
            addButton('Y' + ": " + building.getTitle(), building.getTitle(), 'y');
        });
        placeSelectComponent = new PlaceSelectComponent(gameMvc, false, true);
    }

    public void addItem(BuildingType type, List<String> steps) {
        if (!steps.isEmpty()) {
            String currentStep = steps.remove(0);
            if (!steps.isEmpty()) { // create submenu
                if (menus.keySet().contains(currentStep)) { //pass to submenu
                    menus.get(currentStep).addItem(type, steps);
                } else { //create submenu
                    menus.put(currentStep, new BuildingCategoryMenu(gameMvc, currentStep));
                    menus.get(currentStep).addItem(type, steps);
                }
            } else { //create button

            }
        }
    }

    private void addButton(String text, String constructionType, char hotKey) {
        super.createButton(text, Input.Keys.valueOf(Character.toString(hotKey).toUpperCase()), new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.setActiveDesignation(DesignationTypes.BUILD, constructionType);
                placeSelectComponent.show();
            }
        });
    }

    @Override
    public void reset() {
        controller.handleCancel();
    }
}
