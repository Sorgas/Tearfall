package stonering.stage.pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import stonering.game.GameMvc;
import stonering.stage.SingleWindowStage;
import stonering.widget.ButtonMenu;
import stonering.util.saving.GameSaver;

/**
 * This menu appears, when no menu is shown and Q is pressed.
 * Pauses the game on showing.
 */
public class PauseMenu extends ButtonMenu {

    public PauseMenu() {
        super();
        createTable();
        addButtons();
        forbidEventPass = true;
    }

    private void createTable() {
        setDebug(true, true);
        setWidth(600);
        setHeight(400);
        table.defaults().center().top().height(50).width(600).padBottom(15);
    }

    private void addButtons() {
        createButton("Resume", Input.Keys.Q, () -> {
            GameMvc.model().setPaused(false);
            hide();
        }, true);
        createButton("Options", Input.Keys.O, () -> {
            GameMvc.view().addStage(new SingleWindowStage<>(new SettingsMenu(), true, false));
        }, true);
        createButton("Save", Input.Keys.S, this::saveGame, true);
        createButton("Save & Quit", Input.Keys.ESCAPE, () -> {
            saveGame();
            quitGame();
        }, true);
    }

    private void saveGame() {
        GameSaver.saveGame();
    }

    private void quitGame() {
        Gdx.app.exit();
    }

    @Override
    public boolean hide() {
        GameMvc.view().removeStage(getStage());
        return true;
    }
}
