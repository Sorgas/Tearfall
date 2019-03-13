package stonering.game.core.controller.controllers.designation;

import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.toolbar.Toolbar;

/**
 * Designation sequences used to add actors to {@link Toolbar}
 * in order to maintain designating player's order.
 * (like selecting place for building and then selecting items for building parts).
 *
 * @author Alexander on 21.01.2019.
 */
public abstract class DesignationSequence {
    protected GameMvc gameMvc;

    public DesignationSequence() {
        gameMvc = GameMvc.getInstance();
    }

    /**
     * Starts designation sequence showing first actor.
     */
    public abstract void start();

    /**
     * Hides all actors of this sequence.
     */
    public abstract void end();

    /**
     * Resets this sequence to state as it is just started.
     */
    public abstract void reset();

    /**
     * Returns text for toolbar
     */
    public abstract String getText();
}
