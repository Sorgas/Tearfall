package stonering.game.core.controller.controllers;

import com.badlogic.gdx.math.Vector2;
import stonering.game.core.GameMvc;
import stonering.game.core.view.GameView;
import stonering.game.core.view.render.ui.components.menus.util.Invokable;
import stonering.game.core.view.render.ui.components.menus.util.MouseInvocable;

import java.util.Arrays;

/**
 * @author Alexander on 06.09.2018.
 */
public class MouseController extends Controller {
    private GameView gameView;

    public MouseController(GameMvc gameMvc) {
        super(gameMvc);
    }

    @Override
    public void init() {
        gameView = gameMvc.getView();

    }

    public void handleEvent(int screenX, int screenY, int button, int action) {
        Invokable menu = gameView.getUiDrawer().getToolbar().getActiveMenu();
        if (Arrays.asList(menu.getClass().getInterfaces()).contains(MouseInvocable.class)) {
            Vector2 vector = gameView.getWorldDrawer().translateScreenPositionToModel(new Vector2(screenX, screenY));
            ((MouseInvocable) menu).invoke(Math.round(vector.x), Math.round(vector.y), button, action);
        }
    }


}
