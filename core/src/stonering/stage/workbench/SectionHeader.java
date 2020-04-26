package stonering.stage.workbench;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Header of menu sections. Focuses its stage on click.
 *
 * @author Alexander on 25.04.2020
 */
public class SectionHeader extends Table {

    public SectionHeader(Actor section) {
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getStage().setKeyboardFocus(section);
                return true;
            }
        });
    }
}
