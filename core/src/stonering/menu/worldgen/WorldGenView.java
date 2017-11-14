package stonering.menu.worldgen;

import stonering.TearFall;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.menu.mvc_interfaces.GameView;
import stonering.menu.ui_components.MiniMap;
import stonering.menu.utils.WorldCellInfo;

public class WorldGenView implements GameView, Screen {
    private WorldGenModel model;
    private WorldGenController controller;

    private Stage stage;
    private MiniMap minimap;
    private WorldCellInfo worldCellInfo;
    private TearFall game;

    private TextField seedField;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch spriteBatch;
    private Label worldInfoLabel;

    public WorldGenView(TearFall game) {
        this.game = game;
        worldCellInfo = new WorldCellInfo();
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        stage = new Stage();
        init();
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
        shapeRenderer.dispose();
        shapeRenderer = new ShapeRenderer();
        spriteBatch.dispose();
        spriteBatch = new SpriteBatch();
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
        shapeRenderer.dispose();
        spriteBatch.dispose();
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

    private Table createMinimap() {
        minimap = new MiniMap(new Texture("sprites/map_tiles.png"));
        minimap.setMap(model.getMap());
        return minimap;
    }

    private Table createMenuTable() {
        Table menuTable = new Table();
        menuTable.defaults().prefHeight(30).padBottom(10);
        menuTable.columnDefaults(0).prefWidth(260).fillX();
        menuTable.columnDefaults(1).prefWidth(30).padLeft(10);
        menuTable.align(Align.bottomLeft);

        menuTable.add(new Label("Seed: ", game.getSkin()));
        menuTable.row();

        seedField = new TextField(new Long(model.getSeed()).toString(), game.getSkin());
        menuTable.add(seedField);

        TextButton randButton = new TextButton("R", game.getSkin());
        randButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.randomizeSeed();
                seedField.setText(new Long(model.getSeed()).toString());
            }
        });
        menuTable.add(randButton);
        menuTable.row();

        menuTable.add(new Label("World size: ", game.getSkin()));
        menuTable.row();

        Slider worldSizeSlider = new Slider(100, 500, 100, false, game.getSkin());
        worldSizeSlider.setValue(model.getWorldSize());
        Label worldSizeLabel = new Label(new Integer(model.getWorldSize()).toString(), game.getSkin());

        worldSizeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int size = Math.round(((Slider) actor).getValue());
                model.setWorldSize(size);
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
                controller.generateWorld();
                minimap.setMap(model.getMap());
            }
        });
        menuTable.add(generateButton).colspan(2);
        menuTable.row();

        TextButton saveButton = new TextButton("Save", game.getSkin());
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.saveMap();
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
        if (model.getMap() != null) {
            int x = minimap.getFocus().getX();
            int y = minimap.getFocus().getY();
            worldInfoLabel.setText(worldCellInfo.getCellInfo(x, y, model.getMap().getElevation(x, y), model.getMap().getTemperature(x, y), model.getMap().getRainfall(x,y)));
        }
    }

    public void setModel(WorldGenModel model) {
        this.model = model;
    }

    public void setController(WorldGenController controller) {
        this.controller = controller;
    }

    public void setGame(TearFall game) {
        this.game = game;
    }
}