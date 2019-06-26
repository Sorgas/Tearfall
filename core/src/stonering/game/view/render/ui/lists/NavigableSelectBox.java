package stonering.game.view.render.ui.lists;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import stonering.enums.ControlActionsEnum;
import stonering.util.global.StaticSkin;

/**
 * SelectBox, which can be observed with keys. Key set is configurable.
 * Each action should be specified with listener.
 *
 * @author Alexander on 27.11.2018.
 */
public class NavigableSelectBox<T> extends ListSelectBox<T> {

    public NavigableSelectBox() {
        super(StaticSkin.getSkin());
        createDefaultListener();
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                event.stop();
                switch (ControlActionsEnum.getAction(keycode)) {
                    case UP: {
                        return handleNavigation(-1);
                    }
                    case DOWN: {
                        return handleNavigation(1);
                    }
                }
                return false;
            }
        });
    }

    private boolean handleNavigation(int delta) {
        if (getList().getStage() == null) {
            showList();
        } else {
            navigate(delta);
        }
        return true;
    }

    public void navigate(int delta) {
        if (getItems().size != 0) {
            int newIndex = (getItems().size + getSelectedIndex() + delta) % getItems().size;
            setSelectedIndex(newIndex);
            getList().setSelectedIndex(newIndex);
        }
    }
}
