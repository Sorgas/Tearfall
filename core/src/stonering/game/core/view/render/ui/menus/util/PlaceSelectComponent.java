package stonering.game.core.view.render.ui.menus.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import stonering.entity.local.building.validators.PositionValidator;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.game.core.model.EntitySelector;
import stonering.game.core.view.render.ui.menus.Toolbar;
import stonering.global.utils.Position;
import stonering.util.global.StaticSkin;
import stonering.util.global.TagLoggersEnum;

/**
 * Component for selecting places. Can be used many times.
 *
 * @author Alexander Kuzyakov on 12.07.2018.
 */
public class PlaceSelectComponent extends Label implements HideableComponent {
    private GameMvc gameMvc;
    private DesignationsController controller;
    private Toolbar toolbar;
    private EntitySelector selector;
    private PositionValidator positionValidator;
    private String defaultText;
    private String warningText;

    public PlaceSelectComponent(GameMvc gameMvc) {
        super("", StaticSkin.getSkin());
        this.gameMvc = gameMvc;
    }

    public void init() {
        selector = gameMvc.getModel().getCamera();
        controller = gameMvc.getController().getDesignationsController();
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
        createDefaultListener();
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                TagLoggersEnum.UI.logDebug("handling " + Input.Keys.toString(keycode) + " in PlaceSelectComponent");
                event.stop();
                switch (keycode) {
                    case Input.Keys.E:
                        handleConfirm(selector.getPosition().clone());
                        return true;
                    case Input.Keys.Q:
                        handleCancel();
                        return true;
                    case Input.Keys.W:
                    case Input.Keys.A:
                    case Input.Keys.S:
                    case Input.Keys.D:
                        setText(defaultText);
                        return false; // to interrupt navigation input.
                }
                return false;
            }
        });
    }

    /**
     * Finishes handling or adds position to select box
     *
     * @param eventPosition
     */
    private void handleConfirm(Position eventPosition) {
        TagLoggersEnum.UI.logDebug("confirming place selection");
        if(selector.getStatus() == selector.GREEN_STATUS) {
            hide();
            controller.setRectangle(eventPosition, eventPosition);
        } else {
            super.setText(warningText);
        }
    }

    /**
     * Closes component or discards last position from select box.
     */
    private void handleCancel() {
        hide();
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
}
