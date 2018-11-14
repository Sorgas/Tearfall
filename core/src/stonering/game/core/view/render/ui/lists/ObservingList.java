package stonering.game.core.view.render.ui.lists;

import stonering.game.core.GameMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shows items, divided to categories.
 *
 * @author Alexander on 11.11.2018.
 */
public class ObservingList extends NavigableList {
    private Map<String, List<String>> content; // categorized content

    public ObservingList(GameMvc gameMvc) {
        super(gameMvc, true);
        content = new HashMap<>();
    }

    @Override
    public void select() {

    }
}
