package stonering.menu.main;

import stonering.TearFall;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.io.File;

/**
 * Created by Alexander on 02.04.2017.
 *
 * Model for main menu
 */
public class MainMenuModel {
	private TearFall game;
	private MainMenuView view;
	private Stage stage;
	Table menuTable;

	public MainMenuModel(TearFall game) {
		this.game = game;

		init();
	}

	public void init() {
		stage = new Stage();
		stage.setDebugAll(true);
		stage.addActor(createTable());
	}

	public void reset() {
		stage.dispose();
		init();
	}

	private Table createTable() {
		menuTable = new Table();
		menuTable.defaults().height(30).width(300).pad(10,0,0,0);
		menuTable.pad(10);
		menuTable.left().bottom();
		menuTable.setFillParent(true);

		TextButton newWorldButton = new TextButton("Create world", game.getSkin());
		newWorldButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				game.switchWorldGenMenu();
			}
		});
		menuTable.add(newWorldButton);
		menuTable.row();

		if(worldExist()) {
			TextButton startGameButton = new TextButton("Start game", game.getSkin());
			startGameButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					game.switchWorldsSelectMenu();
				}
			});
			menuTable.add(startGameButton);
			menuTable.row();

			menuTable.add(new TextButton("Load game", game.getSkin()));
			menuTable.row();
		}

		menuTable.add(new TextButton("About", game.getSkin()));
		menuTable.row();

		TextButton quitButton = new TextButton("Quit", game.getSkin());
		quitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});
		menuTable.add(quitButton);

		return menuTable;
	}

	private boolean worldExist() {
		File file = new File("saves");
		return file.exists() && file.listFiles() != null;
	}

	public void setView(MainMenuView view) {
		this.view = view;
	}

	public Stage getStage() {
		return stage;
	}
}