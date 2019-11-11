package stonering.game.controller.controllers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.screen.GameView;

/**
 * Passes input events to {@link GameView} stages in a sequence they are rendered, until event is handled.
 *
 * @author Alexander
 */
public class StageInputAdapter extends InputAdapter {
    private GameView gameView;

    public StageInputAdapter(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public boolean keyDown(int keycode) {
        for (int i = gameView.stageList.size() - 1; i >= 0; i--) {
            if(gameView.stageList.get(i).keyDown(keycode)) return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        for (int i = gameView.stageList.size() - 1; i >= 0; i--) {
            if(gameView.stageList.get(i).keyTyped(character)) return true;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Stage stage = gameView.getActiveStage();
        if(stage != null) return stage.touchDown(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Stage stage = gameView.getActiveStage();
        if(stage != null) return stage.touchUp(screenX, screenY, pointer, button);
        return false;
    }
}
