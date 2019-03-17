package stonering.game.core.view.render.ui.menus.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import stonering.game.core.GameMvc;
import stonering.game.core.model.EntitySelector;
import stonering.game.core.model.local_map.LocalMap;
import stonering.game.core.view.render.ui.menus.toolbar.Toolbar;
import stonering.util.geometry.Position;
import stonering.util.global.StaticSkin;
import stonering.util.global.TagLoggersEnum;

/**
 * Selects rectangle.
 *
 * @author Alexander on 22.11.2018.
 */
public class RectangleSelectComponent extends Label implements HideableComponent, MouseInvocable {
    private GameMvc gameMvc;
    private LocalMap localMap;
    private Toolbar toolbar;
    private EntitySelector selector;
    private EventListener listener; //finished rectangle confirmation handler

    public RectangleSelectComponent(EventListener listener) {
        super("rectangle", StaticSkin.getSkin());
        gameMvc = GameMvc.getInstance();
        this.listener = listener;
        selector = gameMvc.getModel().get(EntitySelector.class);
        localMap = gameMvc.getModel().get(LocalMap.class);
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
        createDefaultListener();
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                event.stop();
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
        });
    }

    @Override
    public boolean invoke(int keycode) {
        //TODO add reference to Key Settings
        TagLoggersEnum.UI.logWarn("Call to invoke() on RectangleSelectComponent.");
        return true;
    }

    @Override // TODO for mouse input
    public boolean invoke(int modelX, int modelY, int button, int action) {
//        Position position = new Position(modelX, modelY, selector.getPosition().getZ());
//        switch (action) {
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
        if (selector.getFrameStart() == null) {                                // box not started, start
            localMap.normalizePosition(eventPosition); // when mouse dragged out of map
            selector.setFrameStart(eventPosition.clone());
        } else {                                                               // box started, finish
            listener.handle(null);
            selector.setFrameStart(null); // ready for new rectangle after this.
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
