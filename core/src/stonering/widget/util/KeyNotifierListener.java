package stonering.widget.util;

import java.util.Optional;
import java.util.function.Supplier;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Extension of {@link InputListener} that passes key events(up, down, typed) to some {@link Actor} defined in supplier.
 * Made for implementing {@link Stage} keyboardFocus logic on actor level. 
 * 
 * @author Alexander on 03.06.2020.
 */
public class KeyNotifierListener extends InputListener {
    public Supplier<Actor> eventTargetSupplier;

    public KeyNotifierListener(Supplier<Actor> eventTargetSupplier) {
        this.eventTargetSupplier = eventTargetSupplier;
    }

    public KeyNotifierListener(Actor actor) { // to use when actor never changes
        this(() -> actor);
    }
    
    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        return handle(event);
    }

    @Override
    public boolean keyUp(InputEvent event, int keycode) {
        return handle(event);
    }

    @Override
    public boolean keyTyped(InputEvent event, char character) {
        return handle(event);
    }
    
    private boolean handle(InputEvent event) {
        return Optional.ofNullable(eventTargetSupplier.get())
                .map(actor -> actor.notify(event, event.isCapture()))
                .orElse(false);
    }
}
