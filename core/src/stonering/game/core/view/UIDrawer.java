package stonering.game.core.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.GameContainer;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 12.10.2017.
 */
public class UIDrawer {
    private Stage stage;
    private TileStatusBar tileStatusBar;
    private GameContainer container;
    private MaterialMap materialMap;

    public UIDrawer() {
        stage = new Stage();
        tileStatusBar = new TileStatusBar();
        stage.addActor(tileStatusBar);
        materialMap = new MaterialMap();
    }

    public void draw() {
        updateStatusBar();
        stage.draw();
    }

    public void setContainer(GameContainer container) {
        this.container = container;
    }

    private void updateStatusBar() {
        Position focus = container.getCamera().getPosition();
        tileStatusBar.setData(focus, materialMap.getMaterial(container.getLocalMap().getMaterial(focus.getX(), focus.getY(), focus.getZ())).getName());
    }
}
