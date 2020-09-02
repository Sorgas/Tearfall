package stonering.screen;

import com.badlogic.gdx.Gdx;
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
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGeneratorContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.screen.util.SingleStageScreen;
import stonering.util.geometry.Position;
import stonering.screen.ui_components.LabeledProgressBar;
import stonering.util.lang.StaticSkin;

/**
 * Local generation screen to be shown during local generation.
 * Generation starts on show(). After generation, there is an instance of {@link GameMvc} with inited model.
 * //TODO progress bar and some visualization.
 *
 * @author Alexander Kuzyakov on 01.06.2017.
 */
public class LocalGenerationScreen extends SingleStageScreen {
    private LocalGeneratorContainer localGeneratorContainer;
    private World world;
    private Position location;
    private TearFall game;

    private LabeledProgressBar progressBar;
    private TextButton proceedButton;

    public LocalGenerationScreen(TearFall game, World world, Position location) {
        super(null);
        this.game = game;
        this.world = world;
        this.location = location;
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
        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                proceedButton.toggle();
                return true;
            }
        });
    }

    private Table createMenuTable() {
        Table table = new Table();
        table.defaults().prefHeight(30).prefWidth(300).padBottom(10).minWidth(300);
        table.align(Align.bottomLeft);
        table.add(progressBar = new LabeledProgressBar("Generation", StaticSkin.getSkin())).row();
        table.add(proceedButton = new TextButton("Proceed", StaticSkin.getSkin())).pad(0);
        proceedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMvc gameMvc = GameMvc.instance();
                gameMvc.createViewAndController();
                gameMvc.init(); // for initing V & C
                GameMvc.model().get(EntitySelectorSystem.class).placeSelectorAtMapCenter();
                GameMvc.model().get(LocalMap.class).initAreas(); // to avoid recalculations on map generation
                game.switchToGame(); // show game screen
                GameMvc.model().gameTime.setPaused(false);
            }
        });
//        stage.addActor(table);
        return table;
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) stage.dispose();
        init();
        Gdx.input.setInputProcessor(stage);
    }

    private void generateLocalMap() {
        LocalGenConfig config = new LocalGenConfig();
        config.setLocation(location);
        localGeneratorContainer = new LocalGeneratorContainer(config, world);
        localGeneratorContainer.execute();
    }

    @Override
    public void show() {
        generateLocalMap();
    }
}