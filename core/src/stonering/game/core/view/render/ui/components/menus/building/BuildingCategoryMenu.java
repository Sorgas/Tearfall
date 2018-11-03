package stonering.game.core.view.render.ui.components.menus.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.HotkeySequence;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.designations.DesignationTypes;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.game.core.view.render.ui.components.menus.util.PlaceSelectComponent;
import stonering.game.core.view.render.ui.components.menus.util.SubMenuMenu;

/**
 * Menu for one category of buildings.
 * Loads its children by menupath property of buiding types.
 *
 * @author Alexander Kuzyakov on 15.06.2018
 */
public class BuildingCategoryMenu extends SubMenuMenu {
    private final String category;

    private DesignationsController controller;
    private HotkeySequence sequence;


    public BuildingCategoryMenu(GameMvc gameMvc, String category) {
        super(gameMvc);
        this.category = category;
        hideable = true;
        sequence = new HotkeySequence();
    }

    @Override
    public void init() {
        addCategory();
        controller = gameMvc.getController().getDesignationsController();
        super.init();
    }

    /**
     * Creates all content from building category in this menu.
     *
     * Creates {@link PlaceSelectComponent} (one for all constructions).
     */
    public void addCategory() {
        BuildingTypeMap.getInstance().getCategoryBuildings(category).forEach(building ->
                addItem(building.getTitle(), new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        controller.setActiveDesignation(DesignationTypes.BUILD, building);
                    }
                }, building.getMenuPath())
        );
    }

    @Override
    public void reset() {
        controller.handleCancel();
    }
}
