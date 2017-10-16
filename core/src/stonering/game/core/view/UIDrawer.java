package stonering.game.core.view;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Alexander on 12.10.2017.
 */
public class UIDrawer {
    private Stage stage;
    private TileStatusBar tileStatusBar;

    public UIDrawer() {
        stage = new Stage();
        tileStatusBar = new TileStatusBar();
    }

    public Stage getStage() {
        return stage;
    }
}
