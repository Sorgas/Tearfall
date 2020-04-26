package stonering.stage;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import stonering.game.GameMvc;
import stonering.util.global.StaticSkin;

/**
 * Stage for displaying one ui window in the center of the screen. 
 * 
 * @author Alexander on 18.02.2020.
 */
public class SingleWindowStage<T extends Actor> extends UiStage {
    private T window;
    private Container<Container> shade;

    public SingleWindowStage(T window, boolean interceptInput, boolean shadeBackground) {
        super();
        this.window = window;
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
    }

    public void show() {
        GameMvc.view().addStage(this);
        GameMvc.controller().setSelectorEnabled(false);

    }

    public void hide() {
        GameMvc.view().removeStage(this);
        GameMvc.controller().setSelectorEnabled(true);
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("resize " + (shade != null) + " " + height);
        if(shade != null) shade.height(Math.max(800, height - 400));
        super.resize(width, height);
    }
}
