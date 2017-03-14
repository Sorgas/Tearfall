package com.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.model.WorldGenModel;
import com.model.generator.world.map_objects.WorldMap;
import com.model.generator.world.world_objects.Edge;
import com.model.generator.world.world_objects.Mountain;
import com.model.generator.world.world_objects.Plate;
import com.model.generator.world.world_objects.WorldCell;
import com.model.utils.Position;
import com.model.utils.Vector;

import java.util.Iterator;
import java.util.List;

public class WorldView implements GameView {
	private WorldGenModel model;
	private int windowWidth;
	private int windowHeight;
	private int tileWidth = 5;
	private int tileHeight = 5;
	private SpriteBatch batch;
	private Texture img;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;

	public WorldView() {
	}

	@Override
	public void init() {

	}

	@Override
	public void showFrame() {
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(1, 1, 1, 1);
		drawElevation();
		drawOceans();
		shapeRenderer.end();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(0, 1, 0, 1);
//		drawDots();
//		drawEdges();
//		drawSpeedVectors();
		shapeRenderer.setColor(1, 0, 0, 1);
//		drawMountains();
		shapeRenderer.setColor(0, 0, 1, 1);
//		drawValleys();
		shapeRenderer.end();
	}

	private void drawDots() {
		List<Plate> list = model.getWorldGenContainer().getPlates();
		for (int i = 0; i < list.size(); i++) {
			Position pos = list.get(i).getCenter();
			drawPoint(pos.getX(), pos.getY());
		}
	}

	private void drawEdges() {
		List<Plate> plates = model.getWorldGenContainer().getPlates();
		for (Iterator<Plate> iterator = plates.iterator(); iterator.hasNext(); ) {
			Plate plate = iterator.next();
			for (Iterator<Edge> edgeIterator = plate.getEdges().iterator(); edgeIterator.hasNext(); ) {
				Edge edge = edgeIterator.next();
				drawLine(edge.getPoint1().getX(), edge.getPoint1().getY(), edge.getPoint2().getX(), edge.getPoint2().getY());
			}
		}
	}

	private void drawSpeedVectors() {
		List<Plate> plates = model.getWorldGenContainer().getPlates();
		for (Iterator<Plate> iterator = plates.iterator(); iterator.hasNext(); ) {
			Vector vector = iterator.next().getSpeedVector();
			Position endPoint = vector.getEndPoint();
			drawLine(vector.getX(), vector.getY(), endPoint.getX(), endPoint.getY());
		}
	}

	private void drawMountains() {
		List<Edge> edges = model.getWorldGenContainer().getEdges();
		for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext(); ) {
			Edge edge = iterator.next();
			List<Mountain> mountains = edge.getMountains();
			for (Iterator<Mountain> mountainIterator = mountains.iterator(); mountainIterator.hasNext(); ) {
				Mountain mountain = mountainIterator.next();
				Position top = mountain.getTop();
				drawPoint(top.getX(), top.getY());
				for (Iterator<Position> cornerIterator = mountain.getCorners().iterator(); cornerIterator.hasNext(); ) {
					Position corner = cornerIterator.next();
					drawLine(top.getX(), top.getY(), corner.getX(), corner.getY());
				}
			}
		}
	}

	private void drawValleys() {
		List<Edge> edges = model.getWorldGenContainer().getEdges();
		for (Iterator<Edge> iterator = edges.iterator(); iterator.hasNext(); ) {
			Edge edge = iterator.next();
			List<Mountain> valleys = edge.getValleys();
			for (Iterator<Mountain> valleyIterator = valleys.iterator(); valleyIterator.hasNext(); ) {
				Mountain valley = valleyIterator.next();
				Position top = valley.getTop();
				drawPoint(top.getX(), top.getY());
				for (Iterator<Position> cornerIterator = valley.getCorners().iterator(); cornerIterator.hasNext(); ) {
					Position corner = cornerIterator.next();
					drawLine(top.getX(), top.getY(), corner.getX(), corner.getY());
				}
			}
		}
	}

	private void drawElevation() {
		int elevationMod = 25;
		WorldMap map = model.getMap();
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				int el = map.getCell(x, y).getElevation();
				if (el < 4) {
					shapeRenderer.setColor(0.00f * (el + elevationMod), 0.009f * (el + elevationMod), 0.00f * (el + elevationMod), 1);
				} else {
					el -= 12;
					float grey = 0.01f * (el + elevationMod);
					shapeRenderer.setColor(grey, grey, grey, 1);
				}
				drawPoint(x, y);
			}
		}
	}

	private void drawOceans() {
		WorldMap map = model.getMap();
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				WorldCell cell = map.getCell(x, y);
				if (cell.isOcean()) {
					int el = cell.getElevation() + 20;
					shapeRenderer.setColor(0, 0, 0.012f * el, 1);
					drawPoint(x, y);
				}
			}
		}
	}

	private void drawLine(int x1, int y1, int x2, int y2) {
		shapeRenderer.line(x1 * tileWidth + tileWidth / 2, y1 * tileHeight + tileHeight / 2, x2 * tileWidth + tileWidth / 2, y2 * tileHeight + tileHeight / 2);
	}

	private void drawPoint(int x, int y) {
		shapeRenderer.rect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
	}

	@Override
	public void setTileset(Texture img) {
		this.img = img;
	}

	@Override
	public void setSpriteBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public void setShapeRenderer(ShapeRenderer shapeRenderer) {
		if (this.shapeRenderer != null) {
			this.shapeRenderer.dispose();
		}
		this.shapeRenderer = shapeRenderer;
	}

	public void setFont(BitmapFont font) {
		this.font = font;
	}

	@Override
	public void setWindowSize(int width, int height) {
		windowWidth = width;
		windowHeight = height;
	}

	public void setModel(WorldGenModel model) {
		this.model = model;
	}
}