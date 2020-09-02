package stonering.stage.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;

import stonering.util.lang.Initable;

/**
 * Stage for displaying one ui actor. Made for menus.
 *
 * @author Alexander on 7/7/2020
 */
public class SingleActorStage<T extends Actor> extends UiStage implements Initable {
    public final Container<T> container;
    public final T actor;

    public SingleActorStage(T actor, boolean interceptInput) {
        this.actor = actor;
        this.interceptInput = interceptInput;
        addActor(container = new Container<>(actor));
        container.fill().setFillParent(true);
        setKeyboardFocus(actor);
    }

    @Override
    public void init() {
        if(actor instanceof Initable) ((Initable) actor).init();
    }
}
