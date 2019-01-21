package stonering.game.core.controller.controllers.designation;

import com.badlogic.gdx.scenes.scene2d.Actor;
import stonering.game.core.GameMvc;

/**
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
     * Resets this sequence to initial state.
     */
    public abstract void reset();

    /**
     * Returns text fo toolbar
     */
    public abstract String getText();
}
