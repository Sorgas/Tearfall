package stonering.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
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
import stonering.screen.ui_components.MiniMap;
import stonering.screen.ui_components.WorldListItem;
import stonering.screen.util.WorldCellInfo;

import java.io.File;

/**
 * Screen for selecting location for settlement on game start.
 * //TODO fix too fast input.
 *
 * @author Alexander Kuzyakov on 14.04.2017.
 */
public class SelectLocationMenu implements Screen {

    private TearFall game;
    private Stage stage;
    private MiniMap minimap;
    private World world;
    private Label worldInfoLabel;
    private WorldCellInfo worldCellInfo;

    public SelectLocationMenu(TearFall game) {
        this.game = game;
        worldCellInfo = new WorldCellInfo();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        checkInput();
        writeWorldInfoToLabel();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
        if (stage != null) stage.dispose();
        stage = new Stage();
        stage.setDebugAll(true);
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.defaults().fill().expandY().space(10);
        rootTable.pad(10).align(Align.bottomLeft);
        rootTable.add(createMenuTable());
        rootTable.add(createMinimap()).expandX();
        stage.addActor(rootTable);
    }

    private Table createMinimap() {
        minimap = new MiniMap(new Texture("sprites/map_tiles.png"));
        minimap.setWorld(world);
        System.out.println(getWorld());
        return minimap;
    }

    private void checkInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            minimap.moveFocus(1, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            minimap.moveFocus(-1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            minimap.moveFocus(0, 1);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            minimap.moveFocus(0, -1);
        }
    }

    private void writeWorldInfoToLabel() {
        if (getWorld() != null) {
            int x = minimap.getFocus().getX();
            int y = minimap.getFocus().getY();
            worldInfoLabel.setText(worldCellInfo.getCellInfo(x, y,
                    Math.round(world.getWorldMap().getElevation(x, y)),
                    world.getWorldMap().getSummerTemperature(x, y),
                    world.getWorldMap().getRainfall(x, y)));
        }
    }

    private Table createMenuTable() {
        Table menuTable = new Table();
        menuTable.defaults().prefHeight(30).prefWidth(300).padBottom(10).minWidth(300);
        menuTable.align(Align.bottomLeft);

        menuTable.add(new Label("Select location", game.getSkin())).row();

        worldInfoLabel = new Label("", game.getSkin());
        menuTable.add(worldInfoLabel);
        menuTable.row();

        menuTable.add().expandY();
        menuTable.row();

        TextButton proceedButton = new TextButton("Proceed", game.getSkin());
        proceedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchPrepareExpeditionMenu(world, minimap.getFocus());
            }
        });
        menuTable.add(proceedButton);
        menuTable.row();

        TextButton backButton = new TextButton("Back", game.getSkin());
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchWorldsSelectMenu();
            }
        });
        menuTable.add(backButton).colspan(2).pad(0);

        return menuTable;
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
}