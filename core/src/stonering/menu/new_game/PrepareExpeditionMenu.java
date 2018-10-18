package stonering.menu.new_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.TearFall;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import stonering.entity.world.World;
import stonering.menu.ui_components.WorldListItem;
import stonering.global.utils.Position;

import java.io.File;

/**
 * ButtonMenu for choosing settlers and resources for game start.
 * //TODO
 *
 * @author Alexander Kuzyakov on 14.04.2017.
 */
public class PrepareExpeditionMenu implements Screen {
    private TearFall game;
    private Stage stage;
    private World world;
    private Position location;

    public PrepareExpeditionMenu(TearFall game) {
        this.game = game;
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

    public Array<WorldListItem> getWorldListItems() {
        File root = new File("saves");
        Array<WorldListItem> list = new Array<>();
        for (File file : root.listFiles()) {
            list.add(new WorldListItem(file.getName(), file));
        }
        return list;
    }

    private void init() {
        stage.setDebugAll(true);
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.defaults().fill().expandY().space(10);
        rootTable.pad(10).align(Align.bottomLeft);
        rootTable.add(createMenuTable());
        stage.addActor(rootTable);
    }

    private Table createMenuTable() {
        Table menuTable = new Table();
        menuTable.defaults().prefHeight(30).prefWidth(300).padBottom(10).minWidth(300);
        menuTable.align(Align.bottomLeft);

        menuTable.add(new Label("Prepare to advance", game.getSkin())).row();

        menuTable.add().expandY();
        menuTable.row();

        TextButton proceedButton = new TextButton("Start", game.getSkin());
        proceedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchToLocalGen(getWorld(), getLocation());
            }
        });
        menuTable.add(proceedButton);
        menuTable.row();

        TextButton backButton = new TextButton("Back", game.getSkin());
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchLocationSelectMenu(getWorld());
            }
        });
        menuTable.add(backButton).colspan(2).pad(0);

        return menuTable;
    }

    public void checkInput() {}

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