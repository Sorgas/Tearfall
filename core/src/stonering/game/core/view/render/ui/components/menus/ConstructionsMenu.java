package stonering.game.core.view.render.ui.components.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.buildings.BuildingMap;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.DesignationsController;

/**
 * ButtonMenu with buttons
 *
 * @author Alexander Kuzyakov on 15.06.2018
 */
public class ConstructionsMenu extends ButtonMenu {
    private DesignationsController controller;
    private static final String CATEGORY = "constructions";
    private PlaceSelectComponent placeSelectComponent;

    public ConstructionsMenu(GameMvc gameMvc) {
        super(gameMvc, true);
        hideable = true;
        crerateButtonsAndMenu();
    }

    @Override
    public void init() {
        super.init();
        controller = gameMvc.getController().getDesignationsController();
        placeSelectComponent.init();
    }

    /**
     * Creates all buttons.
     * Creates {@link PlaceSelectComponent} (one for all constructions).
     */
    private void crerateButtonsAndMenu() {
        BuildingMap.getInstance().getCategoryBuildings(CATEGORY).forEach(building -> {
            addButton(building.getHotKey().toUpperCase() + ": " + building.getTitle(), building.getTitle(), building.getHotKey().charAt(0));
        });
        placeSelectComponent = new PlaceSelectComponent(gameMvc, false, true);
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
