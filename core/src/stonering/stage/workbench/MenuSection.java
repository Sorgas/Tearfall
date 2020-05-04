package stonering.stage.workbench;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.widget.util.WrappedLabel;

/**
 * Section of menu.
 *
 * @author Alexander on 25.04.2020
 */
public class MenuSection extends Table {
    protected WrappedLabel header;

    public MenuSection(String title) {
        add(header = new WrappedLabel(title)).height(100).growX().row();
        MenuSection section = this;
        align(Align.topLeft);
        header.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getStage().setKeyboardFocus(section);
                return true;
            }
        });
    }
}
