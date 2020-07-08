package stonering.widget;

import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.stage.toolbar.Toolbar;

/**
 * Abstract menu for {@link Toolbar}. Can add and remove itself in toolbar.
 * TODO add Q button
 *  
 * @author Alexander Kuzyakov on 27.12.2017.
 */
public abstract class ToolbarButtonMenu extends ButtonMenu  {
    protected Toolbar toolbar;

    public ToolbarButtonMenu(Toolbar toolbar) {
        super();
        this.toolbar = toolbar;
        defaults().size(120, 30).pad(5).padBottom(10);
        bottom();
    }

    @Override
    public void hide() {
        if(!GameMvc.model().get(EntitySelectorSystem.class).cancelSelection()) toolbar.removeMenu(this);
    }
}