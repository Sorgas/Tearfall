package stonering.menu.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.TearFall;

import java.io.File;

/**
 * Main menu of the game.
 *
 * @author Alexander Kuzyakov on 02.04.2017.
 */
public class MainMenu implements Screen {
    private TearFall game;
    private Stage stage;

    public MainMenu(TearFall game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        init();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        reset();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    public void init() {
        stage = new Stage();
        stage.setDebugAll(true);
        stage.addActor(createTable());
    }

    public void reset() {
        stage.dispose();
        init();
    }

    private Table createTable() {
        Table menuTable = new Table();
        menuTable.defaults().height(30).width(300).pad(10, 0, 0, 0);
        menuTable.pad(10);
        menuTable.left().bottom();
        menuTable.setFillParent(true);

        TextButton newWorldButton = new TextButton("Create world", game.getSkin());
        newWorldButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchWorldGenMenu();
            }
        });
        menuTable.add(newWorldButton);
        menuTable.row();

        if (worldExist()) {
            TextButton startGameButton = new TextButton("Start game", game.getSkin());
            startGameButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.switchWorldsSelectMenu();
                }
            });
            menuTable.add(startGameButton);
            menuTable.row();

            menuTable.add(new TextButton("Load game", game.getSkin()));
            menuTable.row();
        }

        menuTable.add(new TextButton("About", game.getSkin()));
        menuTable.row();

        TextButton quitButton = new TextButton("Quit", game.getSkin());
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        menuTable.add(quitButton);

        return menuTable;
    }

    private boolean worldExist() {
        File file = new File("saves");
        return file.exists() && file.listFiles() != null;
    }
}
