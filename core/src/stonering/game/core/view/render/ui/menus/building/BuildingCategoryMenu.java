package stonering.game.core.view.render.ui.menus.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.core.controller.controllers.designation.BuildingDesignationSequence;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.game.core.view.render.ui.menus.util.SubMenuMenu;

/**
 * Menu for one category of buildings.
 * Loads its children by menupath property of buiding types.
 *
 * @author Alexander Kuzyakov on 15.06.2018
 */
public class BuildingCategoryMenu extends SubMenuMenu {
    private final String category;

    private DesignationsController controller;

    public BuildingCategoryMenu(String category) {
        this.category = category;
        hideable = true;
    }

    @Override
    public void init() {
        addCategory();
        controller = gameMvc.getController().getDesignationsController();
        super.init();
    }

    /**
     * Creates all content from building category in this screen.
     */
    public void addCategory() {
        BuildingTypeMap.getInstance().getCategoryBuildings(category).forEach(buildingType ->
                addItem(buildingType.getTitle(),
                        new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
                                controller.setActiveDesignation(new BuildingDesignationSequence(buildingType));
                            }
                        },
                        buildingType.getMenuPath()));
    }

    @Override
    public void reset() {
        controller.handleCancel();
    }
}
