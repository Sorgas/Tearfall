package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.buildings.BuildingMap;
import stonering.enums.constructions.ConstructionsEnum;
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

    public ConstructionsMenu(GameMvc gameMvc) {
        super(gameMvc, 1);
        hideable = true;
        initMenu();
    }

    @Override
    public void init() {
        super.init();
        controller = gameMvc.getController().getDesignationsController();
    }

    private void initMenu() {
        BuildingMap buildingMap = BuildingMap.getInstance();
        buildingMap.getCategoryBuildings(CATEGORY).forEach(building ->
            addButton(building.getHotKey().toUpperCase() + ": " + building.getTitle(), building.getTitle(), building.getHotKey().charAt(0)));
    }

    private void addButton(String text, String constructionType, char hotKey) {
        super.createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.setActiveDesignation(DesignationTypes.BUILD, constructionType);
                menuLevels.showMaterialSelect(constructionType);
            }
        });
    }

    @Override
    public void reset() {
        controller.handleCancel();
    }
}
