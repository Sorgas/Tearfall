package stonering.game.core.view.render.ui.menus.zone;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.game.core.controller.controllers.designation.ZoneDesignationSequence;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.game.core.view.render.ui.menus.util.SubMenuMenu;

/**
 * Contains buttons for designating zones.
 *
 * @author Alexander on 04.03.2019.
 */
public class ZonesMenu extends SubMenuMenu {
    private DesignationsController designationsController;

    private void createButtons() {
        addItem("farm", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                designationsController.setActiveDesignation(new ZoneDesignationSequence());
            }
        }, null);
    }
}
