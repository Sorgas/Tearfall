package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
        addButton("W: wall", ConstructionsEnum.WALL, 'w');
        addButton("F: floor", ConstructionsEnum.FLOOR, 'f');
        addButton("R: ramp", ConstructionsEnum.RAMP, 'r');
        addButton("S: stairs", ConstructionsEnum.STAIRS, 's');
    }

    private void addButton(String text, ConstructionsEnum type, char hotKey) {
        super.createButton(text, hotKey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.setActiveDesignation(DesignationTypes.BUILD, type.getTitle());
                menuLevels.showMaterialSelect();
            }
        });
    }

    @Override
    public void reset() {
        controller.handleCancel();
    }
}
