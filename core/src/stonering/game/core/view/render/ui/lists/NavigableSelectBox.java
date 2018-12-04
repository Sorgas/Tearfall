package stonering.game.core.view.render.ui.lists;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import stonering.game.core.view.render.ui.menus.util.HideableComponent;
import stonering.game.core.view.render.ui.menus.util.Invokable;
import stonering.utils.global.StaticSkin;

/**
 * SelectBox, which can be observed with keys.
 *
 * @author Alexander on 27.11.2018.
 */
public class NavigableSelectBox<T> extends SelectBox<T> implements HideableComponent {
    //TODO add configurable controls.
    private EventListener hideListener;
    private EventListener selectListener;
    private EventListener showListener;

    public NavigableSelectBox() {
        super(StaticSkin.getSkin());
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                event.stop();
                switch (keycode) {
                    case Input.Keys.W:
                        up();
                        return true;
                    case Input.Keys.S:
                        down();
                        return true;
                    case Input.Keys.D:
                        select();
                        return true;
                    case Input.Keys.A:
                        hide();
                        return true;
                }
                return false;
            }
        });
    }

    public void up() {
        if (getSelectedIndex() > 0) {
            setSelectedIndex(getSelectedIndex() - 1);
        }
    }

    public void down() {
        if (getSelectedIndex() < getItems().size - 1) {
            setSelectedIndex(getSelectedIndex() + 1);
        }
    }

    public void select() {
        if(selectListener != null) {
            selectListener.handle(null);
        }
    }

    @Override
    public void show() {
        if(showListener != null) {
            showListener.handle(null);
        }
    }

    @Override
    public void hide() {
        if(hideListener != null) {
            hideListener.handle(null);
        }
    }

    public void setHideListener(EventListener hideListener) {
        this.hideListener = hideListener;
    }

    public void setSelectListener(EventListener selectListener) {
        this.selectListener = selectListener;
    }

    public void setShowListener(EventListener showListener) {
        this.showListener = showListener;
    }
}
