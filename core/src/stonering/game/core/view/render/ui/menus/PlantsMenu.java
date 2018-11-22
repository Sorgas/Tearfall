package stonering.game.core.view.render.ui.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.game.core.view.render.ui.menus.util.AreaSelectComponent;
import stonering.game.core.view.render.ui.menus.util.PlaceSelectComponent;
import stonering.game.core.view.render.ui.menus.util.SubMenuMenu;

/**
 * Menu with orders related to plants
 *
 * @author Alexander Kuzyakov on 28.05.2018.
 */
public class PlantsMenu extends SubMenuMenu {
    private DesignationsController controller;
    private AreaSelectComponent areaSelectComponent;

    public PlantsMenu(GameMvc gameMvc) {
        super(gameMvc);
        hideable = true;
        initMenu();
    }

    @Override
    public void init() {
        super.init();
        controller = gameMvc.getController().getDesignationsController();
        areaSelectComponent.init();
    }

    @Override
    public void reset() {
        controller.handleCancel();
    }

    private void initMenu() {
        addButtonToTable("P: chop trees", DesignationTypeEnum.CHOP, Input.Keys.P);
        addButtonToTable("O: harvest", DesignationTypeEnum.HARVEST, Input.Keys.O);
        addButtonToTable("I: cut", DesignationTypeEnum.CUT, Input.Keys.I);
        addButtonToTable("U: clear", DesignationTypeEnum.NONE, Input.Keys.U);

        areaSelectComponent = new AreaSelectComponent(gameMvc);
    }

    private void addButtonToTable(String text, DesignationTypeEnum type, int hotKey) {
        createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.setActiveDesignation(type, null);
            }
        }, true);
    }
}
