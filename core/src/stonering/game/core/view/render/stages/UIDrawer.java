package stonering.game.core.view.render.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.GameMvc;
import stonering.game.core.model.EntitySelector;
import stonering.game.core.model.GameModel;
import stonering.game.core.model.MainGameModel;
import stonering.game.core.model.local_map.LocalMap;
import stonering.game.core.view.render.ui.TileStatusBar;
import stonering.game.core.view.render.ui.menus.Toolbar;
import stonering.util.geometry.Position;

/**
 * @author Alexander Kuzyakov on 12.10.2017.
 */
public class UIDrawer extends Stage {
    private GameModel gameModel;
    private MaterialMap materialMap;

    private Toolbar toolbar;
    private TileStatusBar tileStatusBar;

    public UIDrawer() {
        super();
        materialMap = MaterialMap.getInstance();
        toolbar = new Toolbar();
    }

    public void init() {
        this.setDebugAll(true);
        tileStatusBar = new TileStatusBar();
        this.addActor(new Container(tileStatusBar).bottom().left().pad(10));
        gameModel = GameMvc.getInstance().getModel();
        toolbar.init();

        VerticalGroup rightTools = new VerticalGroup();
        rightTools.addActor(toolbar);
        Container container = new Container(rightTools).bottom().right().pad(10);
        container.setFillParent(true);
        addActor(container);
        addActor(toolbar);
        setDebugAll(true);
        setKeyboardFocus(toolbar);
    }

    public void draw() {
        updateStatusBar();
        super.draw();
    }

    private void updateStatusBar() {
        Position focus = gameModel.get(EntitySelector.class).getPosition();
        Material material = materialMap.getMaterial(gameModel.get(LocalMap.class).getMaterial(focus));
        tileStatusBar.setData(focus,
                material != null ? material.getName() : "",
                gameModel.get(LocalMap.class).getPassageMap().getArea().getValue(focus),
                gameModel.get(LocalMap.class).getFlooding(focus));
    }

    public void resize(int width, int height) {
        this.getViewport().update(width, height, true);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbarLabelText(String text) {
        toolbar.setText(text);
    }
}
