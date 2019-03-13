package stonering.game.core.view.render.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.game.core.view.render.ui.menus.util.ButtonMenu;
import stonering.util.saving.GameSaver;


public class PauseMenu extends ButtonMenu {

    public PauseMenu() {
        super(false, true);
        createTable();
        addButtons();
    }

    private void createTable() {
        setDebug(true, true);
        setWidth(600);
        setHeight(400);
        defaults().center().top().height(50).width(600).padBottom(15);
    }

    private void addButtons() {
        createButton("Resume", Input.Keys.Q, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameMvc.getModel().setPaused(false);
                hide();
            }
        }, true);
        createButton("Options", Input.Keys.O, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        }, true);
        createButton("Save", Input.Keys.S, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveGame();
            }
        }, true);
        createButton("Save & Quit", Input.Keys.ESCAPE, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveGame();
                quitGame();
            }
        }, true);
    }

    private void saveGame() {
        GameSaver.saveGame();
    }

    private void quitGame() {
        Gdx.app.exit();
    }

    @Override
    public void reset() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
        gameMvc.getView().removeStage(getStage());
    }
}
