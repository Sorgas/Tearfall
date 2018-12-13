package stonering.menu.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.TearFall;

import java.io.File;

/**
 * Main menu screen of the game.
 *
 * @author Alexander Kuzyakov on 02.04.2017.
 */
public class MainScreen implements Screen {
    private TearFall game;
    private Stage stage;

    private TextButton createWorldButton;  // available always
    private TextButton startGameButton;    // available when world with no settlements present
    private TextButton loadGameButton;     // available when savegame exists
    private TextButton aboutButton;        // available always
    private TextButton quitButton;         // available always


    public MainScreen(TearFall game) {
        this.game = game;
    }

    @Override
    public void show() {
        init();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    /**
     * Creates stage for this screen
     */
    public void init() {
        stage = new Stage();
        stage.setDebugAll(true);
        stage.addActor(createTable());
        stage.addListener(createKeyListener());
    }

    private Table createTable() {
        Table menuTable = new Table();
        menuTable.defaults().height(30).width(300).pad(10, 0, 0, 0);
        menuTable.pad(10);
        menuTable.left().bottom();
        menuTable.setFillParent(true);

        createWorldButton = new TextButton("C: Create world", game.getSkin());
        createWorldButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchWorldGenMenu();
            }
        });
        menuTable.add(createWorldButton);
        menuTable.row();

        if (worldExist()) {
            startGameButton = new TextButton("E: Start game", game.getSkin());
            startGameButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.switchWorldsSelectMenu();
                }
            });
            menuTable.add(startGameButton);
            menuTable.row();

            loadGameButton = new TextButton("L: Load game", game.getSkin());
            menuTable.add(loadGameButton);
            menuTable.row();
        }

        aboutButton = new TextButton("A: About", game.getSkin());
        menuTable.add(aboutButton);
        menuTable.row();

        quitButton = new TextButton("Q: Quit", game.getSkin());
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        menuTable.add(quitButton);
        return menuTable;
    }

    private InputListener createKeyListener() {
        return new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.E : {
                        startGameButton.toggle();
                        return true;
                    }
                    case Input.Keys.Q : {
                        quitButton.toggle();
                        return true;
                    }
                    case Input.Keys.L : {
                        loadGameButton.toggle();
                        return true;
                    }
                    case Input.Keys.C : {
                        createWorldButton.toggle();
                        return true;
                    }
                    case Input.Keys.A : {
                        aboutButton.toggle();
                        return true;
                    }
                }
                return false;
            }
        };
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

    public void reset() {
        stage.dispose();
        init();
    }

    private boolean worldExist() {
        File file = new File("saves");
        return file.exists() && file.listFiles() != null;
    }
}
