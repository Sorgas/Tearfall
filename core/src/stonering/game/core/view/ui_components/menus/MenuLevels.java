package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import stonering.game.core.GameMvc;

/**
 * @author Alexander on 17.06.2018.
 */
public class MenuLevels extends HorizontalGroup {
    private GameMvc gameMvc;
    private Toolbar toolbar;

    public MenuLevels(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
        toolbar = new Toolbar(gameMvc);
        addActor(toolbar);
        toolbar.init();
        toolbar.show();
    }


}
