package stonering.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.TearFall;
import stonering.screen.util.SimpleScreen;
import stonering.stage.util.UiStage;
import stonering.util.lang.StaticSkin;

import java.io.File;

/**
 * Main screen of the game.
 *
 * @author Alexander Kuzyakov on 02.04.2017.
 */
public class MainMenuScreen extends SimpleScreen {
    private TearFall game;
    private UiStage stage;

    private TextButton createWorldButton;  // available always
    private TextButton startGameButton;    // available when world with no settlements present
    private TextButton loadGameButton;     // available when savegame exists
    private TextButton aboutButton;        // available always
    private TextButton quitButton;         // available always

    public MainMenuScreen(TearFall game) {
        this.game = game;
        stage = new UiStage();
        stage.setDebugAll(true);
        Container container = new Container<>(createTable());
        container.left().bottom().setFillParent(true);
        stage.addActor(container);
        stage.addListener(createKeyListener());
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    /**
     * Creates menu table
     */
    private Table createTable() {
        Table menuTable = new Table();
        menuTable.defaults().height(30).width(300).pad(10, 0, 0, 0);
        menuTable.pad(0, 10, 10, 10);
        menuTable.add(createWorldButton = new TextButton("C: Create world", StaticSkin.getSkin())).row();
        createWorldButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchWorldGenMenu();
            }
        });

        if (worldExist()) {
            menuTable.add(startGameButton = new TextButton("E: Start game", StaticSkin.getSkin())).row();
            startGameButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.switchWorldsSelectMenu();
                }
            });
            menuTable.add(loadGameButton = new TextButton("L: Load game", StaticSkin.getSkin())).row();
        }
        menuTable.add(aboutButton = new TextButton("A: About", StaticSkin.getSkin())).row();
        menuTable.add(quitButton = new TextButton("Q: Quit", StaticSkin.getSkin()));
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        return menuTable;
    }

    private InputListener createKeyListener() {
        return new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.E: {
                        if (startGameButton != null) startGameButton.toggle();
                        return true;
                    }
                    case Input.Keys.Q: {
                        quitButton.toggle();
                        return true;
                    }
                    case Input.Keys.L: {
                        loadGameButton.toggle();
                        return true;
                    }
                    case Input.Keys.C: {
                        createWorldButton.toggle();
                        return true;
                    }
                    case Input.Keys.A: {
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
        super.resize(width, height);
        stage.resize(width, height);
    }

    private boolean worldExist() {
        File file = new File("saves");
        return file.exists() && file.listFiles() != null;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
}
