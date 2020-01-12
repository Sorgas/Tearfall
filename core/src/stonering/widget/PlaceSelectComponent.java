package stonering.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import stonering.game.model.system.EntitySelectorSystem;
import stonering.util.validation.PositionValidator;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.local_map.LocalMap;
import stonering.stage.toolbar.menus.Toolbar;
import stonering.util.geometry.Position;

/**
 * Component for selecting places. Can be used many times.
 *
 * @author Alexander Kuzyakov on 12.07.2018.
 */
public class PlaceSelectComponent extends Actor implements Hideable, HintedActor {
    private PositionValidator positionValidator; // validates position on each move
    private EntitySelectorSystem selectorSystem;
    private EventListener eventListener; // fired on confirm
    private Toolbar toolbar;
    public Position position;
    private String hint;

    public PlaceSelectComponent(EventListener eventListener, PositionValidator positionValidator, String hint) {
        this.eventListener = eventListener;
        selectorSystem = GameMvc.instance().model().get(EntitySelectorSystem.class);
        toolbar = GameMvc.instance().view().mainUiStage.toolbar;
        this.positionValidator = positionValidator;
        this.hint = hint;
        createDefaultListener();
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.E:
                        if (validatePosition(selectorSystem.selector.position)) {
                            position = selectorSystem.selector.position.clone();
                            eventListener.handle(null);
                        }
                        return true;
                    case Input.Keys.Q:
                        hide();
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Validates position. returns true if validator was not set.
     */
    private boolean validatePosition(Position position) {
        return positionValidator == null ||
                positionValidator.validate(position);
    }

    @Override
    public void show() {
        toolbar.addMenu(this);
        selectorSystem.setPositionValidator(positionValidator);
        selectorSystem.validateAndUpdate();
    }

    @Override
    public void hide() {
        selectorSystem.setPositionValidator(null);
        selectorSystem.validateAndUpdate();
        toolbar.hideMenu(this);
    }

    public String getHint() {
        return "Select place for " + hint;
    }
}
