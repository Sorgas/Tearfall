package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.Input;
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
public class PlaceSelectComponent implements HideableComponent, Invokable {
    private GameMvc gameMvc;
    private boolean singlePoint;
    private boolean materialSelectNeeded;
    private GameCamera camera;
    private LocalMap localMap;
    private Toolbar toolbar;
    private DesignationsController controller;
    private Position start = null;

    public PlaceSelectComponent(GameMvc gameMvc, boolean singlePoint, boolean materialSelectNeeded) {
        this.gameMvc = gameMvc;
        this.singlePoint = singlePoint;
        this.materialSelectNeeded = materialSelectNeeded;
    }

    public void init() {
        camera = gameMvc.getModel().getCamera();
        localMap = gameMvc.getModel().getLocalMap();
        controller = gameMvc.getController().getDesignationsController();
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
    }

    @Override
    public boolean invoke(char c) {
        switch (c) {
            case (char) Input.Keys.ENTER:
                handleEnter();
                return true;
            case (char) Input.Keys.ESCAPE:
                handleEsc();
                return true;
        }
        return false;
    }

    private void handleEnter() {
        if (singlePoint) {
            finishHandling(camera.getPosition(), camera.getPosition());
        } else {
            if (start == null) {
                start = camera.getPosition();
            } else {
                finishHandling(start, camera.getPosition());
            }
        }
    }

    private void finishHandling(Position start, Position end) {
        controller.setRectangle(start, end);
        if(materialSelectNeeded) {
            new MaterialSelectList(gameMvc).show();
        }
    }

    private void handleEsc() {
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

    }

    @Override
    public void hide() {

    }
}
