package stonering.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.TearFall;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.entity.world.World;
import stonering.screen.util.SimpleScreen;
import stonering.util.geometry.Position;
import stonering.util.lang.StaticSkin;

/**
 * ButtonMenu for choosing settlers and resources for game start.
 * //TODO
 *
 * @author Alexander Kuzyakov on 14.04.2017.
 */
public class PrepareExpeditionMenu extends SimpleScreen {
    private TearFall game;
    private Stage stage;
    private World world;
    private Position location;

    private TextButton proceedButton;
    private TextButton backButton;

    public PrepareExpeditionMenu(TearFall game) {
        this.game = game;
    }

    private void init() {
        stage.setDebugAll(true);
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.defaults().fill().expandY().space(10);
        rootTable.pad(10).align(Align.bottomLeft);
        rootTable.add(createMenuTable());
        stage.addActor(rootTable);
        stage.addListener(createKeyListener());
    }

    private Table createMenuTable() {
        Table menuTable = new Table();
        menuTable.defaults().prefHeight(30).prefWidth(300).padBottom(10).minWidth(300);
        menuTable.align(Align.bottomLeft);
        menuTable.add(new Label("Prepare to advance", StaticSkin.getSkin())).row();
        menuTable.add().expandY().row();
        proceedButton = new TextButton("E: Start", StaticSkin.getSkin());
        proceedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchToLocalGen(getWorld(), getLocation());
            }
        });
        menuTable.add(proceedButton).row();
        backButton = new TextButton("Q: Back", StaticSkin.getSkin());
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchLocationSelectMenu(getWorld());
            }
        });
        menuTable.add(backButton).colspan(2).pad(0);
        return menuTable;
    }

    private InputListener createKeyListener() {
        return new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.E: {
                        proceedButton.toggle();
                        return true;
                    }
                    case Input.Keys.Q: {
                        backButton.toggle();
                        return true;
                    }
                }
                return false;
            }
        };
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage = new Stage();
        init();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.dispose();
        stage = new Stage();
        init();
        Gdx.input.setInputProcessor(stage);
    }

    public Stage getStage() {
        return stage;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public void setLocation(Position location) {
        this.location = location;
    }

    public Position getLocation() {
        return location;
    }
}