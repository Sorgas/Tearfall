package stonering.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.local_map.LocalMap;
import stonering.stage.toolbar.menus.Toolbar;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;
import stonering.util.global.StaticSkin;

/**
 * Label that is shown in toolbae and can select rectangle. Accepts E/Q keys input like menu.
 * Selected zone can have multiple z-levels.
 * TODO remove invoke methods, keeping mouse input.
 *
 * @author Alexander on 22.11.2018.
 */
public class RectangleSelectComponent extends Label implements Hideable, MouseInvocable {
    private LocalMap localMap;
    private Toolbar toolbar;
    private EntitySelector selector;
    private EventListener startListener; //started rectangle confirmation handler
    private EventListener finishListener; //finished rectangle confirmation handler

    /**
     * @param finishListener is called when selection is complete.
     */
    public RectangleSelectComponent(EventListener startListener, EventListener finishListener) {
        super("rectangle", StaticSkin.getSkin());
        this.finishListener = finishListener;
        this.startListener = startListener;
        GameMvc gameMvc = GameMvc.instance();
        selector = gameMvc.model().get(EntitySelector.class);
        localMap = gameMvc.model().get(LocalMap.class);
        toolbar = gameMvc.view().mainUiStage.toolbar;
        createDefaultListener();
    }

    /**
     * Used for confirming and cancelling selection.
     */
    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                event.stop();
                switch (keycode) {
                    case Input.Keys.E:
                        handleConfirm(selector.position);
                        return true;
                    case Input.Keys.Q:
                        handleCancel();
                        return true;
                }
                return false;
            }
        });
    }

    @Override // TODO for mouse input
    public boolean invoke(int modelX, int modelY, int button, int action) {
//        Position position = new Position(modelX, modelY, selector.getPosition().getZ());
//        switch (name) {
//            case GameInputProcessor.DOWN_CODE:
//            case GameInputProcessor.UP_CODE: {
//                handleConfirm(position);
//                return true;
//            }
//        }
        return false;
    }

    /**
     * Finishes handling or adds position to select box
     */
    private void handleConfirm(Position eventPosition) {
        Logger.UI.logDebug("confirming " + eventPosition + "in RectangleSelectComponent");
        localMap.normalizePosition(eventPosition);             // when mouse dragged out of map
        if (selector.getFrameStart() == null) {                // box not started, start
            selector.setFrameStart(eventPosition.clone());
            if (startListener != null) startListener.handle(null);
        } else {                                               // box started, finish
            if (finishListener != null) finishListener.handle(null);
            selector.setFrameStart(null);                      // ready for new rectangle after this.
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
