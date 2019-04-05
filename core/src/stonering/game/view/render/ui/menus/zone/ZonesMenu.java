package stonering.game.view.render.ui.menus.zone;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.ZoneTypesEnum;
import stonering.game.GameMvc;
import stonering.game.controller.controllers.designation.ZoneDesignationSequence;
import stonering.game.controller.controllers.toolbar.DesignationsController;
import stonering.game.view.render.ui.menus.util.SubMenuMenu;

/**
 * Contains buttons for designating zones.
 *
 * @author Alexander on 04.03.2019.
 */
public class ZonesMenu extends SubMenuMenu {
    private DesignationsController designationsController;

    @Override
    public void init() {
        createButtons();
        designationsController = GameMvc.instance().getController().getDesignationsController();
        super.init();
    }

    private void createButtons() {
        for (ZoneTypesEnum type : ZoneTypesEnum.values()) {
            addItem(type.toString(), new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    designationsController.setActiveDesignation(new ZoneDesignationSequence(type));
                    designationsController.startSequence();
                }
            }, null);
        }
        addItem("Update zone", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                designationsController.setActiveDesignation(new ZoneDesignationSequence());
                designationsController.startSequence();
            }
        }, null);
    }
}
