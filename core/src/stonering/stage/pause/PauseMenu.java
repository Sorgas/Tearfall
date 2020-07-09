package stonering.stage.pause;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import stonering.game.GameMvc;
import stonering.stage.util.SingleActorShadedStage;
import stonering.widget.ButtonMenu;
import stonering.util.saving.GameSaver;

/**
 * This menu appears, when no menu is shown and Q is pressed.
 * Pauses the game on showing.
 */
public class PauseMenu extends ButtonMenu {

    public PauseMenu() {
        super(50);
        createTable();
        addButtons();
        forbidEventPass = true;
    }

    private void createTable() {
        setDebug(true, true);
        setWidth(600);
        setHeight(400);
        defaults().center().top().height(50).width(600).padBottom(15);
    }

    private void addButtons() {
        createButton("Resume", Input.Keys.Q, () -> {
            GameMvc.model().setPaused(false);
            GameMvc.view().removeStage(getStage());
        });
        createButton("Options", Input.Keys.O, () -> GameMvc.view().addStage(new SingleActorShadedStage<>(new SettingsMenu(), true)));
        createButton("Save", Input.Keys.S, this::saveGame);
        createButton("Save & Quit", Input.Keys.ESCAPE, () -> {
            saveGame();
            Gdx.app.exit();
        });
    }

    private void saveGame() {
        GameSaver.saveGame();
    }
}
