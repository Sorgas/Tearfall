package stonering.stage.entity_menu.building;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.widget.util.WrappedLabel;

/**
 * Section of menu. Has title. 
 *
 * @author Alexander on 25.04.2020
 */
public abstract class MenuSection extends Table {
    protected WrappedLabel header;
    public Runnable tabulation = () -> {};
    
    public MenuSection(String title, int columns) {
        add(header = new WrappedLabel(title)).height(100).colspan(columns).row();
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

    public MenuSection(String title) {
        this(title, 1);
    }
    
    public abstract String getHint();
}
