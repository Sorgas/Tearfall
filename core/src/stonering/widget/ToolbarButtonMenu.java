package stonering.widget;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import stonering.game.GameMvc;
import stonering.enums.images.DrawableMap;
import stonering.stage.toolbar.menus.Toolbar;

/**
 * Abstract menu for {@link Toolbar}. Can add and remove itself in toolbar.
 *
 * @author Alexander Kuzyakov on 27.12.2017.
 */
public abstract class ToolbarButtonMenu extends ButtonMenu implements Highlightable {
    protected Toolbar toolbar;
    private HighlightHandler handler;

    public ToolbarButtonMenu(Toolbar toolbar) {
        this.toolbar = toolbar;
        defaults().size(120, 30).pad(5).padBottom(10);
        bottom();
        createHighlightHandler();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateHighlighting(GameMvc.view().toolbarStage.toolbar.menusGroup.getChildren().peek() == this);
    }

    private void createHighlightHandler() {
        handler = new CheckHighlightHandler(this) {

            @Override
            public void handle(boolean value) {
                Drawable drawable = DrawableMap.REGION.getDrawable("toolbar_menu" + (value ? ":focused" : ""));
                drawable.setMinWidth(getWidth());
                drawable.setMinHeight(getHeight());
                setBackground(drawable);
            }
        };
    }

    @Override
    public void show() {
        toolbar.addMenu(this);
    }

    @Override
    public void hide() {
        toolbar.removeMenu(this);
    }

    @Override
    public Highlightable.HighlightHandler getHighlightHandler() {
        return handler;
    }
}