package stonering.menu.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import stonering.menu.mvc_interfaces.GameView;

/**
 * Created by Alexander on 02.04.2017.
 */
public class MainMenuView implements GameView, Screen {
	private MainMenuModel model;
	private MainMenuController controller;

	public void setModel(MainMenuModel model) {
		this.model = model;
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(model.getStage());
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
		model.getStage().draw();
	}

	@Override
	public void resize(int width, int height) {
		model.reset();
		Gdx.input.setInputProcessor(model.getStage());
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
