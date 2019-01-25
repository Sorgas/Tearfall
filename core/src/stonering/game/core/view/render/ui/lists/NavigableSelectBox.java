package stonering.game.core.view.render.ui.lists;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import stonering.util.global.StaticSkin;

/**
 * SelectBox, which can be observed with keys. Key set is configurable.
 * Each action should be specified with listener.
 *
 * @author Alexander on 27.11.2018.
 */
public class NavigableSelectBox<T> extends ListSelectBox<T> {
    private int upKey = Input.Keys.W;
    private int downKey = Input.Keys.S;

    public NavigableSelectBox() {
        super(StaticSkin.getSkin());
        createDefaultListener();
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                event.stop();
                if (keycode == upKey) {
                    navigate(-1);
                    showList();
                    return true;
                } else if (keycode == downKey) {
                    navigate(1);
                    showList();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public void navigate(int delta) {
        if (getItems().size != 0) {
            int newIndex = (getItems().size + getSelectedIndex() + delta) % getItems().size;
            setSelectedIndex(newIndex);
            getList().setSelectedIndex(newIndex);
        }
    }

    public void setUpKey(int upKey) {
        this.upKey = upKey;
    }

    public void setDownKey(int downKey) {
        this.downKey = downKey;
    }
}
