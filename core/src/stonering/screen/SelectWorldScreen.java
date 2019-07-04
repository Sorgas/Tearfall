package stonering.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.TearFall;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import stonering.entity.World;
import stonering.game.view.render.ui.lists.NavigableList;
import stonering.screen.ui_components.MiniMap;
import stonering.screen.ui_components.WorldListItem;
import stonering.screen.util.WorldSaver;
import stonering.util.global.StaticSkin;

import java.io.File;

/**
 * Screen for selecting world file for game start.
 *
 * @author Alexander Kuzyakov on 14.04.2017.
 */
public class SelectWorldScreen extends SimpleScreen {
    private World world;
    private TearFall game;
    private Stage stage;
    private NavigableList<WorldListItem> worldList;
    private MiniMap minimap;

    private TextButton proceedButton;
    private TextButton backButton;

    public SelectWorldScreen(TearFall game) {
        this.game = game;
    }

    /**
     * Creates stage.
     */
    public void init() {
        stage = new Stage();
        stage.setDebugAll(true);
        Container container = createContainer();
        container.setActor(createTable());
        stage.addActor(container);
        stage.addListener(createKeyListener());
    }

    /**
     * Creates container that fills whole screen.
     */
    private Container createContainer() {
        Container container = new Container();
        container.setFillParent(true);
        container.left().bottom();
        return container;
    }

    /**
     * Creates table
     *
     * @return
     */
    private Table createTable() {
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.defaults().fill().expandY().space(10);
        rootTable.pad(10).align(Align.bottomLeft);
        rootTable.add(createMenuTable());
        rootTable.add(createMinimap()).expandX();
        return rootTable;
    }

    private InputListener createKeyListener() {
        return new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                switch (keycode) {
                    case Input.Keys.E: {
                        if (proceedButton != null) proceedButton.toggle();
                        return true;
                    }
                    case Input.Keys.Q: {
                        if (backButton != null) backButton.toggle();
                        return true;
                    }
                    case Input.Keys.W: {
                        worldList.up();
                        return true;
                    }
                    case Input.Keys.S: {
                        worldList.down();
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public Array<WorldListItem> getWorldListItems() {
        File root = new File("saves");
        Array<WorldListItem> list = new Array<>();
        if (root.exists()) {
            for (File file : root.listFiles()) {
                if (file.listFiles().length > 0) {
                    list.add(new WorldListItem(file.getName(), file));
                }
            }
        }
        return list;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
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

    private Table createMenuTable() {
        Table menuTable = new Table();
        menuTable.defaults().prefHeight(30).prefWidth(300).padBottom(10).minWidth(300);
        menuTable.align(Align.bottomLeft);
        menuTable.add(new Label("Select world:", StaticSkin.getSkin())).row();
        menuTable.add(createWorldList()).row();
        menuTable.add().expandY().row();
        proceedButton = new TextButton("E: Proceed", StaticSkin.getSkin());
        menuTable.add(proceedButton).row();
        if (worldList.getItems().size > 0) {
            proceedButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.switchLocationSelectMenu(getWorld());
                }
            });
        } else {
            //TODO dim button
        }
        backButton = new TextButton("Back", StaticSkin.getSkin());
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchMainMenu();
            }
        });
        menuTable.add(backButton).pad(0);
        return menuTable;
    }

    private Table createMinimap() {
        minimap = new MiniMap(new Texture("sprites/map_tiles.png"));
        WorldListItem item = worldList.getSelected();
        if (item != null) {
            World world = new WorldSaver().loadWorld(item.getTitle());
            setWorld(world);
            minimap.setWorld(world);
        }
        return minimap;
    }

    private List<WorldListItem> createWorldList() {
        worldList = new NavigableList<>();
        worldList.setItems(getWorldListItems());
        worldList.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                World world = new WorldSaver().loadWorld(((List<WorldListItem>) actor).getSelected().getTitle());
                setWorld(world);
                minimap.setWorld(world);
            }
        });
        if (worldList.getItems().size > 0) {
            worldList.setSelected(worldList.getItems().get(0));
        }
        return worldList;
    }

    public Stage getStage() {
        return stage;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}