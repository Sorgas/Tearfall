package stonering.menu.new_game.prepare_expedition;

import stonering.TearFall;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Alexander on 14.04.2017.
 */
public class PrepareExpeditionMenuView implements Screen {
	private TearFall game;
	private PrepareExpeditionMenuController controller;
	private PrepareExpeditionMenuModel model;
	private Stage stage;


	public PrepareExpeditionMenuView(TearFall game) {
		this.game = game;
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

	private void init() {
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

		menuTable.add(new Label("Prepare to advance", game.getSkin())).row();

		menuTable.add().expandY();
		menuTable.row();

		TextButton proceedButton = new TextButton("Start", game.getSkin());
		proceedButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
			game.switchToLocalGen(model.getWorld(), model.getLocation());
			}
		});
		menuTable.add(proceedButton);
		menuTable.row();

		TextButton backButton = new TextButton("Back", game.getSkin());
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.switchLocationSelectMenu(model.getWorld());
			}
		});
		menuTable.add(backButton).colspan(2).pad(0);

		return menuTable;
	}

	public void setController(PrepareExpeditionMenuController controller) {
		this.controller = controller;
	}

	public void setModel(PrepareExpeditionMenuModel model) {
		this.model = model;
	}
}
