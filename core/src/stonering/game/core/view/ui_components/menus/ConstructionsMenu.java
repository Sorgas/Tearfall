package stonering.game.core.view.ui_components.menus;

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
public class ConstructionsMenu extends SubMenuMenu {
    private DesignationsController controller;
    private static final String CATEGORY = "constructions";

    public ConstructionsMenu(GameMvc gameMvc) {
        super(gameMvc);
        hideable = true;
        crerateButtonsAndMenu();
    }

    @Override
    public void init() {
        super.init();
        controller = gameMvc.getController().getDesignationsController();
    }

    /**
     * Creates all buttons.
     * Creates {@link PlaceSelectMenu} (one for all constructions).
     */
    private void crerateButtonsAndMenu() {
        PlaceSelectMenu placeSelectMenu = new PlaceSelectMenu(gameMvc);
        BuildingMap.getInstance().getCategoryBuildings(CATEGORY).forEach(building -> {
            addButton(building.getHotKey().toUpperCase() + ": " + building.getTitle(), building.getTitle(), building.getHotKey().charAt(0));
            menus.put(building.getHotKey().charAt(0), placeSelectMenu);
        });
    }

    private void addButton(String text, String constructionType, char hotKey) {
        super.createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menus.get(hotKey).show();
                controller.setActiveDesignation(DesignationTypes.BUILD, constructionType);
            }
        });
    }

    @Override
    public void reset() {
        controller.handleCancel();
    }
}
