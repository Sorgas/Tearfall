package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.util.geometry.Position;
import stonering.widget.ToolbarSubMenuMenu;

/**
 * Component of toolbar.
 *
 * @author Alexander Kuzyakov on 19.12.2017.
 */
public class ParentMenu extends ToolbarSubMenuMenu {

    public ParentMenu(Toolbar toolbar) {
        super(toolbar);
        this.align(Align.bottom);
        createListener();
        createMenus();
    }

    private void createMenus() {
        addMenu(new PlantsMenu(toolbar), Input.Keys.P, "plants", "plants_menu");
        addMenu(new DiggingMenu(toolbar), Input.Keys.O, "digging", "digging_menu");
        addMenu(new ToolbarBuildingMenu(toolbar), Input.Keys.I, "building", "building_menu");
        addMenu(new ZonesMenu(toolbar), Input.Keys.U, "zones", "zones_menu");
    }

    private void createListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keycode) {
                    case Input.Keys.ESCAPE :
                        GameMvc.instance().getView().showPauseMenu();
                        return true;
                    case Input.Keys.E :
                        Position position = GameMvc.instance().model().get(EntitySelector.class).getPosition();
                        GameMvc.instance().getView().showEntityStage(position);
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Overridden to prevent closing.
     */
    @Override
    public void hide() {
    }
}
