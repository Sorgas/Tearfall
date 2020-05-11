package stonering.stage;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import stonering.game.GameMvc;
import stonering.util.global.Initable;
import stonering.util.global.StaticSkin;

/**
 * Stage for displaying one ui window in the center of the screen.
 * This stage pauses game and disables {@link stonering.game.model.entity_selector.EntitySelector} on show.
 * Guideline for windows is 900 px height.
 *
 * @author Alexander on 18.02.2020.
 */
public class SingleWindowStage<T extends Actor> extends UiStage {
    private Container<Container> shade;
    private boolean wasPaused;

    public SingleWindowStage(T window, boolean interceptInput, boolean shadeBackground) {
        super();
        this.interceptInput = interceptInput;
        if(shadeBackground) {
            Container inner = new Container();
            inner.setBackground(StaticSkin.generator.generate(StaticSkin.shade));
            shade = new Container<>(inner);
            shade.setFillParent(true);
            shade.fillX();
            addActor(shade);
        }
        Container<T> container = new Container<>();
        container.setActor(window);
        container.setFillParent(true);
        addActor(container);
        if(window instanceof Initable) ((Initable) window).init();
    }

    public void show() {
        GameMvc.view().addStage(this);
        GameMvc.controller().setSelectorEnabled(false);
        wasPaused = GameMvc.model().paused; // used for unpausing when menu is closed
        GameMvc.model().setPaused(true);
        GameMvc.controller().pauseInputAdapter.enabled = false;
    }

    public void hide() {
        GameMvc.view().removeStage(this);
        GameMvc.controller().setSelectorEnabled(true);
        GameMvc.model().setPaused(wasPaused);
        GameMvc.controller().pauseInputAdapter.enabled = true;
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resize " + (shade != null) + " " + height);
        if(shade != null) shade.height(Math.max(900, height - 400));
        super.resize(width, height);
    }
}
