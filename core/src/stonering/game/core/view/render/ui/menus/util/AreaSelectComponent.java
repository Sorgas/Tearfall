package stonering.game.core.view.render.ui.menus.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.game.core.controller.inputProcessors.GameInputProcessor;
import stonering.game.core.model.EntitySelector;
import stonering.game.core.model.LocalMap;
import stonering.game.core.view.render.ui.menus.Toolbar;
import stonering.global.utils.Position;
import stonering.utils.global.StaticSkin;

/**
 * Selects rectangle.
 *
 * @author Alexander on 22.11.2018.
 */
public class AreaSelectComponent extends Label implements HideableComponent, MouseInvocable {
    private GameMvc gameMvc;
    private DesignationsController controller;
    private LocalMap localMap;
    private Toolbar toolbar;
    private EntitySelector selector;

    public AreaSelectComponent(GameMvc gameMvc) {
        super("", StaticSkin.getSkin());
        this.gameMvc = gameMvc;
    }

    public void init() {
        selector = gameMvc.getModel().getCamera();
        localMap = gameMvc.getModel().getLocalMap();
        controller = gameMvc.getController().getDesignationsController();
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
    }

    @Override
    public boolean invoke(int keycode) {
        //TODO add reference to Key Settings
        switch (keycode) {
            case Input.Keys.E:
                handleConfirm(selector.getPosition());
                return true;
            case Input.Keys.Q:
                handleCancel();
                return true;
        }
        return false;
    }

    @Override
    public boolean invoke(int modelX, int modelY, int button, int action) {
        Position position = new Position(modelX, modelY, selector.getPosition().getZ());
        switch (action) {
            case GameInputProcessor.DOWN_CODE:
            case GameInputProcessor.UP_CODE: {
                handleConfirm(position);
                return true;
            }
        }
        return false;
    }

    /**
     * Finishes handling or adds position to select box
     *
     * @param eventPosition
     */
    private void handleConfirm(Position eventPosition) {
        if (selector.getFrameStart() == null) {                                // box not started, start
            localMap.normalizePosition(eventPosition);
            selector.setFrameStart(eventPosition.clone());
        } else {                                                               // box started, finish
            controller.setRectangle(selector.getFrameStart(), eventPosition);
            selector.setFrameStart(null);
        }
    }

    /**
     * Closes component or discards last position from select box.
     */
    private void handleCancel() {
        if (selector.getFrameStart() != null) {
            selector.setFrameStart(null);
        } else {
            hide();
        }
    }

    @Override
    public void show() {
        toolbar.addMenu(this);
    }

    @Override
    public void hide() {
        toolbar.hideMenu(this);
    }
}
