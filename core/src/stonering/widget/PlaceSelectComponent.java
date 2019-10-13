package stonering.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import stonering.util.validation.PositionValidator;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.local_map.LocalMap;
import stonering.stage.toolbar.menus.Toolbar;
import stonering.util.geometry.Position;

/**
 * Component for selecting places. Can be used many times.
 *
 * @author Alexander Kuzyakov on 12.07.2018.
 */
public class PlaceSelectComponent extends Actor implements Hideable {
    private GameMvc gameMvc;
    private PositionValidator positionValidator; // validates position on each move
    private EntitySelector selector; // points to position
    private EventListener eventListener; // fired on confirm
    private Toolbar toolbar;
    public Position position;

    public PlaceSelectComponent(EventListener eventListener) {
        gameMvc = GameMvc.instance();
        this.eventListener = eventListener;
        selector = gameMvc.getModel().get(EntitySelector.class);
        toolbar = gameMvc.getView().mainUiStage.toolbar;
        createDefaultListener();
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.E:
                        if (validatePosition(selector.getPosition())) {
                            position = selector.getPosition().clone();
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
                positionValidator.validate(gameMvc.getModel().get(LocalMap.class), position);
    }

    @Override
    public void show() {
        toolbar.addMenu(this);
        selector.setPositionValidator(positionValidator);
        selector.updateStatusAndSprite();
    }

    @Override
    public void hide() {
        selector.setPositionValidator(null);
        selector.updateStatusAndSprite();
        toolbar.hideMenu(this);
    }

    public void setPositionValidator(PositionValidator positionValidator) {
        this.positionValidator = positionValidator;
    }
}
