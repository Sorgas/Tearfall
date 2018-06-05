package stonering.menu.new_game.select_world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.TearFall;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import stonering.menu.mvc_interfaces.GameModel;
import stonering.generators.worldgen.WorldMap;
import stonering.menu.ui_components.MiniMap;
import stonering.menu.ui_components.WorldListItem;
import stonering.menu.utils.WorldSaver;

import java.io.File;

/**
 * Created by Alexander on 14.04.2017.
 */
public class SelectWorldMenu implements Screen {
    private WorldMap world;
    private Table table;
    private TearFall game;
    private Stage stage;
    private List<WorldListItem> worldList;
    private MiniMap minimap;

    public SelectWorldMenu(TearFall game) {
        this.game = game;
    }

    public void init() {
        stage.setDebugAll(true);
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.defaults().fill().expandY().space(10);
        rootTable.pad(10).align(Align.bottomLeft);
        rootTable.add(createMenuTable());
        rootTable.add(createMinimap()).expandX();
        stage.addActor(rootTable);
    }

    public Array<WorldListItem> getWorldListItems() {
        File root = new File("saves");
        Array<WorldListItem> list = new Array<>();
        if (root.exists()) {
            for (File file : root.listFiles()) {
                list.add(new WorldListItem(file.getName(), file));
            }
        }
        return list;
    }

    public void checkInput() {

    }

    public Stage getStage() {
        return stage;
    }

    public void setWorld(WorldMap world) {
        this.world = world;
    }

    public WorldMap getWorld() {
        return world;
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
        checkInput();
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

    private Table createMenuTable() {
        Table menuTable = new Table();
        menuTable.defaults().prefHeight(30).prefWidth(300).padBottom(10).minWidth(300);
        menuTable.align(Align.bottomLeft);

        menuTable.add(new Label("Select world:", game.getSkin()));
        menuTable.row();

        menuTable.add(createWorldList());
        menuTable.row();

        menuTable.add().expandY();
        menuTable.row();

        TextButton proceedButton = new TextButton("Proceed", game.getSkin());
        proceedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchLocationSelectMenu(getWorld());
            }
        });
        menuTable.add(proceedButton);
        menuTable.row();

        TextButton backButton = new TextButton("Back", game.getSkin());
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchMainMenu();
            }
        });
        menuTable.add(backButton).colspan(2).pad(0);

        return menuTable;
    }

    private Table createMinimap() {
        minimap = new MiniMap(new Texture("sprites/map_tiles.png"));
        WorldListItem item = worldList.getSelected();
        if (item != null) {
            WorldMap map = new WorldSaver().loadWorld(item.getTitle());
            setWorld(map);
            minimap.setMap(map);
        }
        return minimap;
    }

    private List<WorldListItem> createWorldList() {
        worldList = new List<>(game.getSkin());
        worldList.setItems(getWorldListItems());
        worldList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                WorldMap world = new WorldSaver().loadWorld(((List<WorldListItem>) actor).getSelected().getTitle());
                setWorld(world);
                minimap.setMap(world);
            }
        });
        if (worldList.getItems().size > 0) {
            worldList.setSelected(worldList.getItems().get(0));
        }
        return worldList;
    }
}