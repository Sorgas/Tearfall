package com.menu.ui_components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.menu.worldgen.generators.world.WorldMap;
import com.menu.worldgen.generators.world.world_objects.WorldCell;

/**
 * Created by Alexander on 23.04.2017.
 */
public class TileChooser {
	private WorldMap map;
	private Texture tiles;
	private int tileSize;
	TextureRegion sea;
	TextureRegion ocean;
	TextureRegion mountain;
	TextureRegion pike;
	TextureRegion forest;
	TextureRegion hill;
	TextureRegion river;
	TextureRegion cross;

	public TileChooser(Texture tiles) {
		this.tiles = tiles;
		initRegions();
	}

	public TextureRegion getTile(int x, int y) {
		WorldCell cell = map.getCell(x, y);
		if (cell.isOcean()) {
			if (cell.getElevation() < -8) {
				return ocean;
			} else {
				return sea;
			}
		} else {
			if (cell.isRiver()) {
				return river;
			} else if (cell.getElevation() < 10) {
				return hill;
			} else if (cell.getElevation() < 20) {
				return forest;
			} else if (cell.getElevation() < 40) {
				return mountain;
			} else {
				return pike;
			}
		}
	}

	public void setMap(WorldMap map) {
		this.map = map;
	}

	private void initRegions() {
		sea = new TextureRegion(tiles, 0, 0, 8, 8);
		ocean = new TextureRegion(tiles, 0, 8, 8, 8);

		pike = new TextureRegion(tiles, 8, 0, 8, 8);
		mountain = new TextureRegion(tiles, 8, 8, 8, 8);

		forest = new TextureRegion(tiles, 16, 0, 8, 8);
		hill = new TextureRegion(tiles, 16, 8, 8, 8);

		river = new TextureRegion(tiles, 0, 0, 8, 8);

		cross = new TextureRegion(tiles, 0, 16, 8, 8);
	}

	public TextureRegion getCross() {
		return cross;
	}
}
