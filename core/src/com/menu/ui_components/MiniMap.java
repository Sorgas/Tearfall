package com.menu.ui_components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.menu.worldgen.generators.world.WorldMap;
import com.utils.Position;

/**
 * Created by Alexander on 19.04.2017.
 */
public class MiniMap extends Table {
	private WorldMap map;
	private TileChooser tileChooser;
	private float tileScale = 1f;
	private int tileSize = 8;
	private Position focus = new Position(0, 0, 0);
	private Position size = new Position(0, 0, 0);

	public MiniMap(Texture tiles) {
		super();
		tileChooser = new TileChooser(tiles);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (map != null) {
			updateSize();
			int xStart = Math.max(focus.getX() - (size.getX() / 2), 0);
			xStart = Math.min(xStart, map.getWidth() - size.getX());

			int yStart = Math.max(focus.getY() - (size.getY() / 2), 0);
			yStart = Math.min(yStart, map.getHeight() - size.getY());
			for (int x = 0; x < size.getX(); x++) {
				for (int y = 0; y < size.getY(); y++) {
					drawTile(batch, tileChooser.getTile(xStart + x, yStart + y), x, y);
				}
			}
			drawTile(batch, tileChooser.getCross(), focus.getX() - xStart, focus.getY() - yStart);
		}
	}

	public void updateSize() {
		size.setX((int) (getWidth() / tileSize / tileScale));
		if (map != null && size.getX() > map.getWidth()) {
			size.setX(map.getWidth());
		}
		size.setY((int) (getHeight() / tileSize / tileScale));
		if (map != null && size.getY() > map.getHeight()) {
			size.setY(map.getHeight());
		}
//		System.out.println(getWidth() + " " + getHeight() + " " + size);
	}

	public void setFocus(int x, int y) {
		focus.setX(x);
		focus.setY(y);
	}

	public void moveFocus(int dx, int dy) {
		if (map != null) {
			focus.setX(focus.getX() + dx);
			focus.setY(focus.getY() + dy);
			if (focus.getX() < 0)
				focus.setX(0);
			if (focus.getX() >= map.getWidth())
				focus.setX(map.getWidth() - 1);
			if (focus.getY() < 0)
				focus.setY(0);
			if (focus.getY() >= map.getHeight())
				focus.setY(map.getHeight() - 1);
		}
	}

	public void setMap(WorldMap map) {
		if (map != null) {
			this.map = map;
			tileChooser.setMap(map);
			focus.setX(map.getWidth() / 2);
			focus.setY(map.getHeight() / 2);
			updateSize();
		}
	}

	@Override
	public float getPrefWidth() {
		if (map != null) {
			return map.getWidth() * tileScale * tileSize;
		} else {
			return super.getPrefWidth();
		}
	}

	@Override
	public float getPrefHeight() {
		if (map != null) {
			return map.getHeight() * tileScale * tileSize;
		} else {
			return super.getPrefHeight();
		}
	}

	private void drawTile(Batch batch, TextureRegion tile, int x, int y) {
		batch.draw(tile, getX() + x * tileSize * tileScale, getY() + y * tileSize * tileScale);
	}

	public Position getFocus() {
		return focus.clone();
	}
}
