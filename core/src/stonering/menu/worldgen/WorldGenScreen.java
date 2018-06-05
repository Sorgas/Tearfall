package stonering.menu.worldgen;

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
import stonering.generators.worldgen.WorldGenConfig;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.WorldGeneratorContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.menu.ui_components.MiniMap;
import stonering.menu.utils.WorldCellInfo;
import stonering.menu.utils.WorldSaver;

import java.util.Random;

/**
 * Created by Alexander on 06.03.2017.
 */
public class WorldGenScreen implements Screen {
    private WorldGenContainer worldGenContainer;
    private WorldGeneratorContainer worldGeneratorContainer;
    private WorldMap map;
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
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        checkInput();
        writeWorldInfoToLabel();
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

        menuTable.add(new Label("Seed: ", game.getSkin()));
        menuTable.row();

        seedField = new TextField(new Long(seed).toString(), game.getSkin());
        menuTable.add(seedField);

        TextButton randButton = new TextButton("R", game.getSkin());
        randButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                randomizeSeed();
                seedField.setText(new Long(seed).toString());
            }
        });
        menuTable.add(randButton);
        menuTable.row();

        menuTable.add(new Label("World size: ", game.getSkin()));
        menuTable.row();

        Slider worldSizeSlider = new Slider(100, 500, 100, false, game.getSkin());
        worldSizeSlider.setValue(worldSize);
        Label worldSizeLabel = new Label(new Integer(worldSize).toString(), game.getSkin());

        worldSizeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int size = Math.round(((Slider) actor).getValue());
                setWorldSize(size);
                worldSizeLabel.setText(new Integer(size).toString());
            }
        });

        menuTable.add(worldSizeSlider);
        menuTable.add(worldSizeLabel);
        menuTable.row();

        worldInfoLabel = new Label("", game.getSkin());
        menuTable.add(worldInfoLabel);
        menuTable.row();

        menuTable.add().expandY();
        menuTable.row();

        TextButton generateButton = new TextButton("Generate", game.getSkin());
        generateButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                generateWorld();
                minimap.setMap(map);
            }
        });
        menuTable.add(generateButton).colspan(2);
        menuTable.row();

        TextButton saveButton = new TextButton("Save", game.getSkin());
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveMap();
            }
        });
        menuTable.add(saveButton).colspan(2);
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
        minimap.setMap(map);
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
        if (map != null) {
            int x = minimap.getFocus().getX();
            int y = minimap.getFocus().getY();
            worldInfoLabel.setText(worldCellInfo.getCellInfo(x, y, Math.round(map.getElevation(x, y)),
                    map.getSummerTemperature(x, y),
                    map.getRainfall(x, y)));
        }
    }

    public void setGame(TearFall game) {
        this.game = game;
    }

    public void generateWorld() { //from ui button
        WorldGenConfig config = new WorldGenConfig(seed, worldSize, worldSize);
        worldGeneratorContainer = new WorldGeneratorContainer();
        worldGeneratorContainer.init(config);
        worldGeneratorContainer.runContainer();
        map = worldGeneratorContainer.getWorldMap();
    }

    public void randomizeSeed() {
        seed = random.nextLong();
    }

    public void saveMap() {
        new WorldSaver().saveWorld(map);
        game.switchMainMenu();
    }

    public WorldMap getMap() {
        return map;
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
}