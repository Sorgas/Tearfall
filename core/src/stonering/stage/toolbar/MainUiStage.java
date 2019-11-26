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

/**
 * Contains toolbar and status bar.
 *
 * @author Alexander Kuzyakov on 12.10.2017.
 */
public class MainUiStage extends UiStage {
    public Toolbar toolbar;
    private TileStatusBar tileStatusBar;

    public MainUiStage() {
        super();
        addActor(toolbar = new Toolbar());
        addActor(tileStatusBar = new TileStatusBar());
        setKeyboardFocus(toolbar);
        interceptInput = false;
        setDebugAll(true);
    }

    public void draw() {
        updateStatusBar();
        super.draw();
    }

    private void updateStatusBar() {
        GameModel gameModel = GameMvc.instance().model();
        Position focus = gameModel.get(EntitySelector.class).position;
        Material material = MaterialMap.instance().getMaterial(gameModel.get(LocalMap.class).getMaterial(focus));
        tileStatusBar.setData(focus,
                material != null ? material.getName() : "",
                gameModel.get(LocalMap.class).passageMap != null ? gameModel.get(LocalMap.class).passageMap.area.get(focus) : 0,
                gameModel.get(LocalMap.class).getBlockType(focus));
    }
}
