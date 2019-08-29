package stonering.game.view.render.ui.menus.util;

import stonering.game.GameMvc;
import stonering.game.view.render.ui.images.DrawableMap;
import stonering.game.view.render.ui.menus.toolbar.Toolbar;

/**
 * Abstract menu for toolbar. Removes itself from {@link Toolbar} on hide.
 * Menus don't have their controllers, all behavior logic is written in their buttons.
 * Keys sets of menus should not overlap.
 *
 * @author Alexander Kuzyakov on 27.12.2017.
 */
public abstract class ToolbarButtonMenu extends ButtonMenu implements Highlightable {
    private HighlightHandler handler;

    public ToolbarButtonMenu() {
        defaults().size(120, 30).padBottom(10).padRight(10);
        bottom();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateHighlighting(false); // TODO
    }

    private void createHighlightHandler() {
        handler = new CheckHighlightHandler(this) {

            @Override
            public void handle(boolean value) {
                setBackground(DrawableMap.instance().getDrawable("toolbar_menu" + (value ? ":focused" : "")));
            }
        };
    }

    @Override
    public void show() {
        GameMvc.instance().getView().getUiDrawer().getToolbar().addMenu(this);
    }

    @Override
    public void hide() {
        GameMvc.instance().getView().getUiDrawer().getToolbar().hideMenu(this);
    }

    @Override
    public Highlightable.HighlightHandler getHighlightHandler() {
        return handler;
    }
}