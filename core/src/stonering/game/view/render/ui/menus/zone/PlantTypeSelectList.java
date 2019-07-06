package stonering.game.view.render.ui.menus.zone;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import stonering.enums.ControlActionsEnum;
import stonering.enums.plants.PlantType;
import stonering.game.view.render.ui.lists.NavigableList;
import stonering.game.view.render.ui.menus.util.Highlightable;
import stonering.game.view.render.ui.menus.util.HintedActor;

import static stonering.enums.ControlActionsEnum.DELETE;

/**
 * List for selecting plant type in {@link FarmZoneMenu}
 *
 * @author Alexander on 06.07.2019.
 */
public class PlantTypeSelectList extends NavigableList<PlantType> implements HintedActor {
    private FarmZoneMenu menu;

    public PlantTypeSelectList(FarmZoneMenu menu) {
        this.menu = menu;
        createListeners();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        getHighlightHandler().handle();
    }

    private void createListeners() {
        setSelectListener(event -> {
            menu.select(getSelected());
            return true;
        });
        setHideListener(event -> {
            menu.close();
            return true;
        });
        setHighlightHandler(new Highlightable.CheckHighlightHandler() {
            @Override
            public void handle() {
                setColor(value ? Color.BLUE : Color.RED);
            }
        });
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (ControlActionsEnum.getAction(keycode) == DELETE) {
                    menu.deleteZone();
                    menu.close();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public String getHint() {
        return "WS: navigate, E: select, X: delete zone, Q: quit";
    }
}
