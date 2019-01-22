package stonering.game.core.view.render.ui.menus.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import stonering.entity.local.building.validators.PositionValidator;
import stonering.game.core.GameMvc;
import stonering.game.core.model.EntitySelector;
import stonering.game.core.view.render.ui.menus.Toolbar;
import stonering.util.geometry.Position;
import stonering.util.global.StaticSkin;

/**
 * Component for selecting places. Can be used many times.
 *
 * @author Alexander Kuzyakov on 12.07.2018.
 */
public class PlaceSelectComponent extends Label implements HideableComponent {
    private GameMvc gameMvc;
    private PositionValidator positionValidator; // validates position on each move
    private EntitySelector selector; // points to position
    private EventListener eventListener; // fired on confirm
    private Toolbar toolbar;
    private Position position;

    private String defaultText;
    private String warningText;

    public PlaceSelectComponent(EventListener eventListener) {
        super("", StaticSkin.getSkin());
        this.gameMvc = GameMvc.getInstance();
        this.eventListener = eventListener;
    }

    public void init() {
        selector = gameMvc.getModel().getCamera();
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
        createDefaultListener();
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            //TODO !!! probably 1 event delay to EntitySelector here
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                boolean isValid = validatePosition();
                setText(isValid ? defaultText : warningText);
                switch (keycode) {
                    case Input.Keys.E:
                        if (isValid) {
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

    private boolean validatePosition() {
        if (positionValidator == null) return true; // no validation
        return positionValidator.validate(gameMvc.getModel().getLocalMap(), selector.getPosition());
    }

    /**
     * Adds this to toolbar and sets position validator to selector.
     */
    @Override
    public void show() {
        toolbar.addMenu(this);
        selector.setPositionValidator(positionValidator);
        selector.updateStatus();
    }

    @Override
    public void hide() {
        selector.setPositionValidator(null);
        toolbar.hideMenu(this);
        selector.updateStatus();
    }

    public void setPositionValidator(PositionValidator positionValidator) {
        this.positionValidator = positionValidator;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
        super.setText(defaultText);
    }

    public void setWarningText(String warningText) {
        this.warningText = warningText;
    }

    public Position getPosition() {
        return position;
    }
}
