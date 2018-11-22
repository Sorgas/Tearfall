package stonering.game.core.view.render.ui.menus.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.CameraInputHandler;
import stonering.game.core.controller.controllers.toolbar.DesignationsController;
import stonering.game.core.controller.inputProcessors.GameInputProcessor;
import stonering.game.core.model.GameCamera;
import stonering.game.core.model.LocalMap;
import stonering.game.core.view.render.ui.menus.Toolbar;
import stonering.global.utils.Position;
import stonering.utils.global.StaticSkin;

/**
 * Component for selecting places. Can be used many times.
 *
 * @author Alexander Kuzyakov on 12.07.2018.
 */
public class PlaceSelectComponent extends Label implements HideableComponent, MouseInvocable {
    private GameMvc gameMvc;
    private DesignationsController controller;
    private LocalMap localMap;
    private Toolbar toolbar;
    private GameCamera camera;
    private Position position;

    public PlaceSelectComponent(GameMvc gameMvc) {
        super("", StaticSkin.getSkin());
        this.gameMvc = gameMvc;
    }

    public void init() {
        camera = gameMvc.getModel().getCamera();
        localMap = gameMvc.getModel().getLocalMap();
        controller = gameMvc.getController().getDesignationsController();
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
    }

    @Override
    public boolean invoke(int keycode) {
        //TODO add reference to Key Settings
        switch (keycode) {
            case Input.Keys.E:
                handleConfirm(camera.getPosition());
                return true;
            case Input.Keys.Q:
                handleCancel();
                return true;
            case Input.Keys.W:
            case Input.Keys.A:
            case Input.Keys.S:
            case Input.Keys.D:
                updateCameraTile();
                position = camera.getPosition().clone();
                return false; // to not interrupt camera input
        }
        return false;
    }

    @Override
    public boolean invoke(int modelX, int modelY, int button, int action) {
        Position position = new Position(modelX, modelY, camera.getPosition().getZ());
        switch (action) {
            case GameInputProcessor.DOWN_CODE:
            case GameInputProcessor.UP_CODE: {
                handleConfirm(position);
            }
            break;
            case GameInputProcessor.MOVE_CODE: {
                updateCameraTile();
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
        finishHandling(eventPosition, eventPosition);
    }

    /**
     * Closes component or discards last position from select box.
     */
    private void handleCancel() {
        hide();
    }

    /**
     * Hides this and sets selected place to controller. Area size is 1.
     *
     * @param start
     * @param end
     */
    private void finishHandling(Position start, Position end) {
        hide();
        controller.setRectangle(start, end);
    }

    @Override
    public void show() {
        toolbar.addMenu(this);
    }

    @Override
    public void hide() {
        this.setText("");
        camera.resetSprite();
        toolbar.hideMenu(this);
    }

    private void updateCameraTile() {
        if (controller.getActiveDesignation().equals(DesignationTypeEnum.BUILD)) {
            if (!controller.getBuildingType().getCategory().equals("constructions")) {
                camera.updateStatus(isOnFloor() ? camera.GREEN_STATUS : camera.RED_STATUS);
            }
        }
    }

    private boolean isOnFloor() {
        return localMap.getBlockType(camera.getPosition()) == BlockTypesEnum.FLOOR.getCode();
    }
}
