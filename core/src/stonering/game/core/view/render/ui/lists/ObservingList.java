package stonering.game.core.view.render.ui.lists;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.util.Invokable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shows items, divided to categories. Categories can be collapsed.
 *
 * @author Alexander on 11.11.2018.
 */
public class ObservingList extends Table implements Invokable {
    private GameMvc gameMvc;
    private Map<String, List<String>> content; // categorized content

    public ObservingList() {
        gameMvc = GameMvc.getInstance();
        content = new HashMap<>();
    }

    public void select() {

    }

    @Override
    public boolean invoke(int keycode) {
        switch (keycode) {

        }
        return false;
    }
}
