package stonering.menu.new_game.local_generation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.TearFall;
import stonering.menu.ui_components.LabeledProgressBar;

/**
 * Created by Alexander on 31.05.2017.
 */
public class LocalGenerationView implements Screen{
    private TearFall game;
    private LocalGenerationModel model;

    private Stage stage;
    private LabeledProgressBar progressBar;

    public LocalGenerationView(TearFall game) {
        this.game = game;
    }

    @Override
    public void show() {
        model.generateLocal();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if(stage != null) stage.dispose();
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
        stage = new Stage();
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
        progressBar = new LabeledProgressBar("Generation", game.getSkin());
        menuTable.add(progressBar);
        menuTable.row();
        TextButton proceedButton = new TextButton("Proceed", game.getSkin());
        proceedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchToGame(model.getLocalMap());
            }
        });
        menuTable.add(proceedButton).pad(0);
        stage.addActor(menuTable);
        return menuTable;
    }

    public void setModel(LocalGenerationModel model) {
        this.model = model;
    }
}