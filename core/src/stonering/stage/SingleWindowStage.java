package stonering.stage;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import stonering.game.GameMvc;

/**
 * Stage for displaying one ui window in the center of the screen. 
 * 
 * @author Alexander on 18.02.2020.
 */
public class SingleWindowStage<T extends Window> extends UiStage {
    private T window;

    public SingleWindowStage(T window, boolean interceptInput) {
        super();
        this.window = window;
        this.interceptInput = interceptInput;
        Container<T> container = new Container<>();
        container.setActor(window);
        container.setFillParent(true);
        addActor(container);
    }

    public void show() {
        GameMvc.view().addStage(this);
    }

    public void hide() {
        GameMvc.view().removeStage(this);
    }
}
