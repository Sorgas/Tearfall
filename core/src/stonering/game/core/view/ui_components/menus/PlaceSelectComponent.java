package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.GameMvc;
import stonering.game.core.controller.controllers.DesignationsController;
import stonering.game.core.model.GameCamera;
import stonering.game.core.model.LocalMap;
import stonering.game.core.view.ui_components.lists.MaterialSelectList;
import stonering.global.utils.Position;

/**
 * Component for selecting places.
 *
 * @author Alexander on 12.07.2018.
 */
public class PlaceSelectComponent extends Actor implements HideableComponent, Invokable {
    private GameMvc gameMvc;
    private DesignationsController controller;
    private LocalMap localMap;
    private Toolbar toolbar;
    private GameCamera camera;

    private Position start = null; // never set if singlePoint is true.
    private MaterialSelectList materialSelectList;
    private boolean materialSelectNeeded;
    private boolean singlePoint;

    public PlaceSelectComponent(GameMvc gameMvc, boolean singlePoint, boolean materialSelectNeeded) {
        this.gameMvc = gameMvc;
        this.singlePoint = singlePoint;
        this.materialSelectNeeded = materialSelectNeeded;
        materialSelectList = new MaterialSelectList(gameMvc);
    }

    public void init() {
        camera = gameMvc.getModel().getCamera();
        localMap = gameMvc.getModel().getLocalMap();
        controller = gameMvc.getController().getDesignationsController();
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
        materialSelectList.init();
    }

    @Override
    public boolean invoke(int keycode) {
        switch (keycode) {
            case Input.Keys.E:
                handleConfirm();
                return true;
            case Input.Keys.Q:
                handleCancel();
                return true;
        }
        return false;
    }

    private void handleConfirm() {
        System.out.println("handling confirm");
        if (singlePoint) {
            finishHandling(camera.getPosition(), camera.getPosition());
        } else {
            if (start == null) { // box not started, start
                start = camera.getPosition();
                camera.setFrameStart(camera.getPosition().clone());
            } else { // box started, finish
                finishHandling(start, camera.getPosition());
                camera.setFrameStart(null);
            }
        }
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
        if (materialSelectNeeded) {
            materialSelectList.refill();
            materialSelectList.show();
        } else {
            controller.finishTaskBuilding();
        }
    }

    private void handleCancel() {
        System.out.println("handling cancel");
        if (start != null) {
            start = null;
        } else {
            reset();
            hide();
        }
    }

    private boolean validatePlace() {
        // TODO add validation options, depending on building.
        int blocktype = localMap.getBlockType(camera.getPosition());
        return blocktype == BlockTypesEnum.FLOOR.getCode() || blocktype == BlockTypesEnum.SPACE.getCode();
    }

    /**
     * For indicating whether placing is valid or not before enter is pressed.
     */
    private void updateCameraSprine() {
        if (validatePlace()) {
            camera.setValidSprite();
        } else {
            camera.setInvalidSprite();
        }
    }

    public void reset() {
        camera.resetSprite();
    }

    @Override
    public void show() {
        toolbar.addMenu(this);
    }

    @Override
    public void hide() {
        reset();
        toolbar.hideMenu(this);
    }
}
