package stonering.game.view.render.stages;

import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.render.ui.TileStatusBar;
import stonering.game.view.render.ui.menus.toolbar.Toolbar;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;

/**
 * Contains toolbar and status bar.
 *
 * @author Alexander Kuzyakov on 12.10.2017.
 */
public class MainUiStage extends UiStage implements Initable {
    private Toolbar toolbar;
    private TileStatusBar tileStatusBar;

    public MainUiStage() {
        super();
        toolbar = new Toolbar();
        tileStatusBar = new TileStatusBar();
        addActor(toolbar);
        addActor(tileStatusBar);
        setKeyboardFocus(toolbar);
        setDebugAll(true);
    }

    public void init() {
        toolbar.init();
    }

    public void draw() {
        updateStatusBar();
        super.draw();
    }

    private void updateStatusBar() {
        GameModel gameModel = GameMvc.instance().getModel();
        Position focus = gameModel.get(EntitySelector.class).getPosition();
        Material material = MaterialMap.instance().getMaterial(gameModel.get(LocalMap.class).getMaterial(focus));
        tileStatusBar.setData(focus,
                material != null ? material.getName() : "",
                gameModel.get(LocalMap.class).getPassage().getArea().getValue(focus),
                gameModel.get(LocalMap.class).getBlockType(focus));
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbarLabelText(String text) {
        toolbar.setText(text);
    }
}
