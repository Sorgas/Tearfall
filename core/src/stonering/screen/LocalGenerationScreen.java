package stonering.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.TearFall;
import stonering.entity.world.World;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGeneratorContainer;
import stonering.game.core.model.LocalMap;
import stonering.global.utils.Position;
import stonering.screen.ui_components.LabeledProgressBar;

/**
 * Local generation screen to be shown during local generation.
 * //TODO progress bar and some visualization.
 *
 * @author Alexander Kuzyakov on 01.06.2017.
 */
public class LocalGenerationScreen extends SimpleScreen {
    private LocalGeneratorContainer localGeneratorContainer;
    private World world;
    private Position location;
    private LocalMap localMap;
    private Stage stage;
    private TearFall game;

    private LabeledProgressBar progressBar;
    private TextButton proceedButton;

    public LocalGenerationScreen(TearFall game) {
        this.game = game;
    }

    private void init() {
        stage = new Stage();
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
        progressBar = new LabeledProgressBar("Generation", game.getSkin());
        menuTable.add(progressBar).row();
        proceedButton = new TextButton("Proceed", game.getSkin());
        proceedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchToGame(getLocalGeneratorContainer().getLocalGenContainer());
            }
        });
        menuTable.add(proceedButton).pad(0);
        stage.addActor(menuTable);
        return menuTable;
    }

    private InputListener createKeyListener() {
        return new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                proceedButton.toggle();
                return true;
            }
        };
    }

    @Override
    public void show() {
        generateLocal();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) stage.dispose();
        init();
        Gdx.input.setInputProcessor(stage);
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setLocation(Position location) {
        this.location = location;
    }

    public void generateLocal() {
        LocalGenConfig config = new LocalGenConfig();
        config.setLocation(location);
        localGeneratorContainer = new LocalGeneratorContainer(config, world);
        localGeneratorContainer.execute();
        localMap = localGeneratorContainer.getLocalMap();
    }

    public LocalMap getLocalMap() {
        return localMap;
    }

    public LocalGeneratorContainer getLocalGeneratorContainer() {
        return localGeneratorContainer;
    }
}