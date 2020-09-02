package stonering.widget.lists;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import stonering.util.lang.StaticSkin;

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
            public boolean keyTyped(InputEvent event, char character) {
                event.stop();
                switch (character) {
                    case 'W':
                    case 'w': {
                        return handleNavigation(-1);
                    }
                    case 'S':
                    case 's': {
                        return handleNavigation(1);
                    }
                    case 'E':
                    case 'e': {
                        if (getList().getStage() != null) return selectListener == null || selectListener.handle(event);
                        showList();
                        return true;
                    }
                    case 'Q':
                    case 'q': {
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
