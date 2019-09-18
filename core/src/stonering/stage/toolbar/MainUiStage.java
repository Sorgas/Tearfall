package stonering.stage.toolbar;

import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.stage.UiStage;
import stonering.widget.TileStatusBar;
import stonering.stage.toolbar.menus.Toolbar;
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
        addActor(toolbar = new Toolbar());
        addActor(tileStatusBar = new TileStatusBar());
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
                gameModel.get(LocalMap.class).passage != null ? gameModel.get(LocalMap.class).passage.getArea().getValue(focus) : 0,
                gameModel.get(LocalMap.class).getBlockType(focus));
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbarLabelText(String text) {
        toolbar.setText(text);
    }
}
