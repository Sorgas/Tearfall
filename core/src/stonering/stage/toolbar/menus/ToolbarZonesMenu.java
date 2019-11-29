package stonering.stage.toolbar.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.ZoneTypesEnum;
import stonering.game.GameMvc;
import stonering.game.controller.controllers.designation.ZoneDesignationSequence;
import stonering.game.controller.controllers.toolbar.DesignationsController;
import stonering.widget.ToolbarSubMenuMenu;

/**
 * Contains buttons for designating zones.
 *
 * @author Alexander on 04.03.2019.
 */
public class ToolbarZonesMenu extends ToolbarSubMenuMenu {

    public ToolbarZonesMenu(Toolbar toolbar) {
        super(toolbar);
    }

    @Override
    public void init() {
        createButtons();
        super.init();
    }

    private void createButtons() {
        for (ZoneTypesEnum type : ZoneTypesEnum.values()) {
            addItem(type.toString(), type.iconName, new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    DesignationsController designationsController = GameMvc.instance().getController().designationsController;
                    designationsController.setSequence(new ZoneDesignationSequence(type));
                    designationsController.startSequence();
                }
            }, null);
        }
        addItem("Update zone", "update_zone", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                DesignationsController designationsController = GameMvc.instance().getController().designationsController;
                designationsController.setSequence(new ZoneDesignationSequence());
                designationsController.startSequence();
            }
        }, null);
    }
}
