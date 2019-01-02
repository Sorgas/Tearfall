package stonering.screen.ui_components;

import java.io.File;

/**
 * @author Alexander Kuzyakov on 03.05.2017.
 */
public class WorldListItem {
	private String title;
	private File world;

	public WorldListItem() {
	}

	public WorldListItem(File file) {
		world = file;
		title = file.getName();
	}

	public WorldListItem(String title, File world) {
		this.title = title;
		this.world = world;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public File getWorld() {
		return world;
	}

	public void setWorld(File world) {
		this.world = world;
	}

	@Override
	public String toString() {
		return title;
	}
}
