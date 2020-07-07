package stonering.stage.menu;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import stonering.TearFall;
import stonering.widget.ButtonMenu;

/**
 * First menu of the game.
 *
 * @author Alexander on 7/7/2020
 */
public class MainMenu extends ButtonMenu {

    public MainMenu(TearFall game) {
        table.defaults().height(50).width(300).pad(10, 0, 0, 0);
        pad(0, 10, 10, 10);
        createButton("C: Create world", Input.Keys.C, game::switchWorldGenMenu);
        createButton("E: Start game", Input.Keys.E, game::switchWorldsSelectMenu);
        if(worldExists()) createButton("L: Load game", Input.Keys.L, null);
        createButton("A: About", Input.Keys.A, null);
        createButton("Q: Quit", Input.Keys.Q, Gdx.app::exit);
    }

    private boolean worldExists() {
        File file = new File("saves");
        return file.exists() && file.listFiles() != null;
    }

    @Override
    public boolean hide() {
        return false;
    }
}
