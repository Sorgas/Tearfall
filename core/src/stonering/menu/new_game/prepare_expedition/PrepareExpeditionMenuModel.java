package stonering.menu.new_game.prepare_expedition;

import stonering.TearFall;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import stonering.generators.worldgen.WorldMap;
import stonering.menu.ui_components.WorldListItem;
import stonering.global.utils.Position;

import java.io.File;

/**
 * Created by Alexander on 14.04.2017.
 */
public class PrepareExpeditionMenuModel {
	private TearFall game;
	private PrepareExpeditionMenuView view;
	private Stage stage;
	private Table table;


	private WorldMap world;
	private Position location;

	public PrepareExpeditionMenuModel(TearFall game) {
		this.game = game;
		init();
	}

	public void init() {
	}

	public Array<WorldListItem> getWorldListItems() {
		File root = new File("saves");
		Array<WorldListItem> list = new Array<>();
		for (File file : root.listFiles()) {
			list.add(new WorldListItem(file.getName(), file));
		}
		return list;
	}

	public void checkInput() {

	}

	public void setView(PrepareExpeditionMenuView view) {
		this.view = view;
	}

	public Stage getStage() {
		return stage;
	}

	public void setWorld(WorldMap world) {
		this.world = world;
	}

	public WorldMap getWorld() {
		return world;
	}

	public void setLocation(Position location) {
		this.location = location;
	}

	public Position getLocation() {
		return location;
	}
}