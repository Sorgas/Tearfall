package stonering.widget.lists;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import stonering.enums.ControlActionsEnum;
import stonering.util.global.StaticSkin;

/**
 * SelectBox, which can be observed with keys and selected with keys.
 *
 * @author Alexander on 27.11.2018.
 */
public class NavigableSelectBox<T> extends ListSelectBox<T> {
    private EventListener selectListener;
    private EventListener cancelListener;

    public NavigableSelectBox() {
        super(StaticSkin.getSkin());
        createDefaultListener();
    }

    private void createDefaultListener() {
        addListener(new InputListener() { // handles key presses
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
                    case SELECT: {
                        if (getList().getStage() == null) {
                            showList();
                            return true;
                        }
                        return selectListener == null || selectListener.handle(event);
                    }
                    case CANCEL: {
                        return cancelListener == null || cancelListener.handle(event);
                    }
                }
                return false;
            }
        });
        getList().addListener(new ClickListener() { // tries to select item after clicking
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectListener != null) selectListener.handle(event);
            }
        });
    }

    /**
     * Navigates on open list or shows hidden list.
     */
    private boolean handleNavigation(int delta) {
        if (getList().getStage() != null) {
            navigate(delta);
        } else {
            showList();
        }
        return true;
    }

    /**
     * Navigates list by delta positions.
     */
    public void navigate(int delta) {
        if (getItems().size == 0) return;
        int newIndex = (getItems().size + getSelectedIndex() + delta) % getItems().size;
        setSelectedIndex(newIndex);
        getList().setSelectedIndex(newIndex);
    }

    public void setSelectListener(EventListener selectListener) {
        this.selectListener = selectListener;
    }

    public void setCancelListener(EventListener cancelListener) {
        this.cancelListener = cancelListener;
    }
}
