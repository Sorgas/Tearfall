package stonering.game.core.view.render.ui.menus.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.GameMvc;
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

    private boolean singlePoint = false;
    private Position start = null; // never set if singlePoint is true.

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
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) { //if WASD pressed during dragging by mouse
                    Vector2 modelXY = gameMvc.getView().getWorldDrawer().translateScreenPositionToModel(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                    updateSelectBox(new Position(modelXY.x, modelXY.y, camera.getPosition().getZ()));
                } else {
                    updateSelectBox(camera.getPosition());
                }

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
                updateSelectBox(position);
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
        if (singlePoint) {
            finishHandling(eventPosition, eventPosition);
        } else {
            if (start == null) {                // box not started, start
                showSelectBox(eventPosition.clone());
            } else {                            // box started, finish
                hideSelectBox();
                finishHandling(start, eventPosition);
                start = null;
            }
        }
    }

    /**
     * Closes component or discards last position from select box.
     */
    private void handleCancel() {
        System.out.println("handling cancel");
        hideSelectBox();
        if (start != null) {
            start = null;
        } else {
            hide();
        }
    }

    /**
     * Sets frame start & end to camera, so frame will be drawn on screen.
     * Shows select box on one cell.
     *
     * @param eventPosition
     */
    private void showSelectBox(Position eventPosition) {
        localMap.normalizePosition(eventPosition);
        System.out.println("start = " + eventPosition);
        start = eventPosition;
        camera.setFrameStart(eventPosition.clone());
        camera.setFrameEnd(eventPosition.clone());
    }

    /**
     * Finishes place selecting process.
     */
    private void hideSelectBox() {
        if (start != null) {
            camera.resetSprite();
        }
    }

    /**
     * Updates end of select box.
     *
     * @param eventPosition
     */
    private void updateSelectBox(Position eventPosition) {
        localMap.normalizePosition(eventPosition);
        camera.setFrameEnd(eventPosition);
    }

    /**
     * After this controller should have coordinates of desired area.
     * Area size is 1 if singlePoint is true.
     *
     * @param start
     * @param end
     */
    private void finishHandling(Position start, Position end) {
        controller.setRectangle(start, end);
    }

    private boolean validatePlace() {
        // TODO add validation options, depending on building.
        int blocktype = localMap.getBlockType(camera.getPosition());
        return blocktype == BlockTypesEnum.FLOOR.getCode() || blocktype == BlockTypesEnum.SPACE.getCode();
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

    public PlaceSelectComponent setSinglePoint(boolean singlePoint) {
        this.singlePoint = singlePoint;
        return this;
    }
}
