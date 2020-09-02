package stonering.stage.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;

import stonering.game.GameMvc;
import stonering.util.lang.Initable;
import stonering.util.lang.StaticSkin;

/**
 * Stage for displaying one actor and shaded rectangle behind it.
 * This stage pauses game and disables {@link stonering.game.model.entity_selector.EntitySelector} on show.
 * Guideline for windows is 900 px height.
 *
 * @author Alexander on 18.02.2020.
 */
public class SingleActorShadedStage<T extends Actor> extends SingleActorStage<T> implements Initable {
    private Container<Container<Actor>> shade;
    private boolean wasPaused;

    public SingleActorShadedStage(T actor, boolean interceptInput) {
        super(actor, interceptInput);
        Container<Actor> inner = new Container<>();
        inner.setBackground(StaticSkin.generator.generate(StaticSkin.shade));
        shade = new Container<>(inner).fillX();
        shade.setFillParent(true);
        getRoot().addActorAt(0, shade);
    }

    @Override
    public void init() {
        super.init();
        GameMvc.controller().setSelectorEnabled(false);
        wasPaused = GameMvc.model().gameTime.paused; // used for unpausing when menu is closed
        GameMvc.model().gameTime.setPaused(true);
        GameMvc.controller().pauseInputAdapter.enabled = false;
    }

    @Override
    public void dispose() {
        GameMvc.controller().setSelectorEnabled(true);
        GameMvc.model().gameTime.setPaused(wasPaused);
        GameMvc.controller().pauseInputAdapter.enabled = true;
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (shade != null) shade.height(Math.max(900, height - 400));
    }
}
