package stonering.stage.toolbar;

import stonering.stage.UiStage;
import stonering.widget.TileStatusBar;

/**
 * Contains toolbar and status bar.
 *
 * @author Alexander Kuzyakov on 12.10.2017.
 */
public class ToolbarStage extends UiStage {
    public Toolbar toolbar;
    private TileStatusBar tileStatusBar;

    public ToolbarStage() {
        super();
        addActor(toolbar = new Toolbar());
        addActor(tileStatusBar = new TileStatusBar());
        setKeyboardFocus(toolbar);
        interceptInput = false;
    }

    public void draw() {
        if(tileStatusBar != null) tileStatusBar.update();
        super.draw();
    }
}
