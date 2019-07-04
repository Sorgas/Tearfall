package stonering.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.TearFall;
import stonering.entity.World;
import stonering.generators.worldgen.WorldGenConfig;
import stonering.generators.worldgen.WorldGeneratorContainer;
import stonering.screen.ui_components.MiniMap;
import stonering.screen.util.WorldCellInfo;
import stonering.screen.util.WorldSaver;
import stonering.util.global.StaticSkin;

import java.util.Random;

/**
 * @author Alexander Kuzyakov on 06.03.2017.
 */
public class WorldGenScreen implements Screen {
    private WorldGeneratorContainer worldGeneratorContainer;
    private World world;
    private long seed; // gets updated from ui
    private int worldSize = 100; // changed from ui
    private TearFall game;
    private Random random;
    private Stage stage;
    private MiniMap minimap;
    private WorldCellInfo worldCellInfo;

    private TextField seedField;

    private Label worldInfoLabel;

    public WorldGenScreen(TearFall game) {
        this.game = game;
        worldCellInfo = new WorldCellInfo();
        random = new Random();
        seed = random.nextLong();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        checkInput();
        updateWorldInfoToLabel();
        stage.setDebugAll(true);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) stage.dispose();
        stage = new Stage();
        init();
        Gdx.input.setInputProcessor(stage);
    }

    private void init() {
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.defaults().fill().expandY().space(10);
        rootTable.pad(10);
        rootTable.add(createMenuTable());
        rootTable.add(createMinimap()).expandX();
        stage.addActor(rootTable);
    }

    private Table createMenuTable() {
        Table menuTable = new Table();
        menuTable.defaults().prefHeight(30).padBottom(10);
        menuTable.columnDefaults(0).prefWidth(260).fillX();
        menuTable.columnDefaults(1).prefWidth(30).padLeft(10);
        menuTable.align(Align.bottomLeft);

        menuTable.add(new Label("Seed: ", StaticSkin.getSkin()));
        menuTable.row();

        seedField = new TextField(Long.toString(seed), StaticSkin.getSkin());
        menuTable.add(seedField);

        TextButton randButton = new TextButton("R", StaticSkin.getSkin());
        randButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                randomizeSeed();
                seedField.setText(Long.toString(seed));
            }
        });
        menuTable.add(randButton);
        menuTable.row();

        menuTable.add(new Label("World size: ", StaticSkin.getSkin()));
        menuTable.row();

        Slider worldSizeSlider = new Slider(100, 500, 100, false, StaticSkin.getSkin());
        worldSizeSlider.setValue(worldSize);
        Label worldSizeLabel = new Label(Integer.toString(worldSize), StaticSkin.getSkin());

        worldSizeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int size = Math.round(((Slider) actor).getValue());
                setWorldSize(size);
                worldSizeLabel.setText(Integer.toString(size));
            }
        });

        menuTable.add(worldSizeSlider);
        menuTable.add(worldSizeLabel);
        menuTable.row();

        worldInfoLabel = new Label("", StaticSkin.getSkin());
        menuTable.add(worldInfoLabel);
        menuTable.row();

        menuTable.add().expandY();
        menuTable.row();

        TextButton generateButton = new TextButton("Generate", StaticSkin.getSkin());
        generateButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                generateWorld();
                minimap.setWorld(world);
            }
        });
        menuTable.add(generateButton).colspan(2);
        menuTable.row();

        TextButton saveButton = new TextButton("Save", StaticSkin.getSkin());
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveMap();
            }
        });
        menuTable.add(saveButton).colspan(2);
        menuTable.row();

        TextButton backButton = new TextButton("Back", StaticSkin.getSkin());
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
        minimap.setWorld(world);
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

    private void updateWorldInfoToLabel() {
        if (world != null) {
            int x = minimap.getFocus().getX();
            int y = minimap.getFocus().getY();
            worldInfoLabel.setText(worldCellInfo.getCellInfo(x, y, Math.round(world.getWorldMap().getElevation(x, y)),
                    world.getWorldMap().getSummerTemperature(x, y),
                    world.getWorldMap().getRainfall(x, y)));
        }
    }

    private void generateWorld() { //from ui button
        WorldGenConfig config = new WorldGenConfig(seed, worldSize, worldSize);
        worldGeneratorContainer = new WorldGeneratorContainer();
        worldGeneratorContainer.init(config);
        worldGeneratorContainer.runContainer();
        world = worldGeneratorContainer.getWorld();
    }

    private void randomizeSeed() {
        seed = random.nextLong();
    }

    private void saveMap() {
        new WorldSaver().saveWorld(world);
        game.switchMainMenu();
    }

    public World getWorld() {
        return world;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    public int getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(int worldSize) {
        this.worldSize = worldSize;
    }

    public void setGame(TearFall game) {
        this.game = game;
    }

    @Override
    public void show() {
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
}