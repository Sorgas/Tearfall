package stonering.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
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
import stonering.entity.World;
import stonering.screen.ui_components.MiniMap;
import stonering.screen.util.WorldCellInfo;
import stonering.util.global.StaticSkin;

/**
 * Screen for selecting location for settlement on game start.
 * //TODO fix too fast input.
 *
 * @author Alexander Kuzyakov on 14.04.2017.
 */
public class SelectLocationMenu extends SimpleScreen {

    private TearFall game;
    private Stage stage;
    private MiniMap minimap;
    private World world;
    private Label worldInfoLabel;
    private WorldCellInfo worldCellInfo;

    private TextButton proceedButton;
    private TextButton backButton;

    public SelectLocationMenu(TearFall game) {
        this.game = game;
        worldCellInfo = new WorldCellInfo();
    }

    private void init() {
        if (stage != null) stage.dispose();
        stage = new Stage();
        stage.setDebugAll(true);
        stage.addActor(createRootTable());
        stage.addListener(createKeyListener());
    }

    private Table createRootTable() {
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
            public boolean keyDown(InputEvent event, int keycode) {
                return handleKey(keycode);
            }

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                return handleKey(charToKeycode(character));
            }

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

            /**
             * Minimap scrolling.
             */
            private boolean handleKey(int keycode) {
                switch (keycode) {
                    case Input.Keys.W: {
                        minimap.moveFocus(0, 1);
                        return true;
                    }
                    case Input.Keys.A: {
                        minimap.moveFocus(-1, 0);
                        return true;
                    }
                    case Input.Keys.S: {
                        minimap.moveFocus(0, -1);
                        return true;
                    }
                    case Input.Keys.D: {
                        minimap.moveFocus(1, 0);
                        return true;
                    }
                }
                return false;
            }

            /**
             * Translates typed character to corresponding keycode.
             * //TODO test letters, numbers, symbols.
             *
             * @param character
             * @return
             */
            private int charToKeycode(char character) {
                return Input.Keys.valueOf(Character.valueOf(character).toString().toUpperCase());
            }
        };
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        updateWorldInfoLabel();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        init();
        Gdx.input.setInputProcessor(stage);
    }

    private Table createMenuTable() {
        Table menuTable = new Table();
        menuTable.defaults().prefHeight(30).prefWidth(300).padBottom(10).minWidth(300);
        menuTable.align(Align.bottomLeft);
        menuTable.add(new Label("Select location", StaticSkin.getSkin())).row();
        worldInfoLabel = new Label("", StaticSkin.getSkin());
        menuTable.add(worldInfoLabel).row();
        menuTable.add().expandY().row();
        proceedButton = new TextButton("E: Proceed", StaticSkin.getSkin());
        proceedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchPrepareExpeditionMenu(world, minimap.getFocus());
            }
        });
        menuTable.add(proceedButton).row();
        backButton = new TextButton("Q: Back", StaticSkin.getSkin());
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchWorldsSelectMenu();
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

    private void updateWorldInfoLabel() {
        if (getWorld() != null) {
            int x = minimap.getFocus().x;
            int y = minimap.getFocus().y;
            worldInfoLabel.setText(worldCellInfo.getCellInfo(x, y,
                    Math.round(world.getWorldMap().getElevation(x, y)),
                    world.getWorldMap().getSummerTemperature(x, y),
                    world.getWorldMap().getRainfall(x, y)));
        }
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