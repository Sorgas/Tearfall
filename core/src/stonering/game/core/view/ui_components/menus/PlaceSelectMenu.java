package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.GameMvc;
import stonering.game.core.model.GameCamera;
import stonering.game.core.model.LocalMap;

/**
 * @author Alexander on 12.07.2018.
 */
public class PlaceSelectMenu extends ButtonMenu {
    private GameCamera camera;
    private LocalMap localMap;

    public PlaceSelectMenu(GameMvc gameMvc) {
        super(gameMvc);
    }

    /**
     * Showing this menu means activating for selecting place.
     */
    @Override
    public void show() {
        super.show();
    }

    @Override
    public void init() {
        super.init();
        camera = gameMvc.getModel().getCamera();
        localMap = gameMvc.getModel().getLocalMap();
    }

    private void makeButtons() {
        super.createButton("enter", (char) Input.Keys.ENTER, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("psm enter");
            }
        });
        super.createButton("esc", (char) Input.Keys.ESCAPE, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                camera.resetSprite();
                hide();
            }
        });
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
        if(validatePlace()) {
            camera.setValidSprite();
        } else {
            camera.setInvalidSprite();
        }
    }

    @Override
    public void reset() {

    }
}
