package com.mvc.worldgen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mvc.GameView;
import com.mvc.worldgen.generators.world.WorldGenContainer;
import com.mvc.worldgen.generators.world.WorldMap;
import com.mvc.worldgen.generators.world.world_objects.Edge;
import com.mvc.worldgen.generators.world.world_objects.Mountain;
import com.mvc.worldgen.generators.world.world_objects.Plate;
import com.mvc.worldgen.generators.world.world_objects.WorldCell;
import com.utils.Position;
import com.utils.Vector;

import java.util.Iterator;
import java.util.List;

public class WorldGenView implements GameView, Screen {
	private WorldGenModel model;
	private float tileSize = 2f;
	private int tileOffsetX = 400;
	private int tileOffsetY = 20;
	private ShapeRenderer shapeRenderer;

	@Override
	public void show() {
		shapeRenderer = new ShapeRenderer();
		Gdx.input.setInputProcessor(model.getStage());
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
		model.getStage().draw();
		if (model.getMap() != null) {
			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			drawElevation();
			drawOceans();
			drawRivers();
			shapeRenderer.end();
		}
	}

	@Override
	public void resize(int width, int height) {
		shapeRenderer.dispose();
		shapeRenderer = new ShapeRenderer();
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
		shapeRenderer.dispose();
	}

	@Override
	public void dispose() {

	}

	private void drawPlates() {
		List<Plate> plates = model.getWorldGenContainer().getPlates();
		for (Iterator<Plate> iterator = plates.iterator(); iterator.hasNext(); ) {
			Plate plate = iterator.next();
			Position center = plate.getCenter();
			drawPoint(center.getX(), center.getY());
			for (Iterator<Edge> edgeIterator = plate.getEdges().iterator(); edgeIterator.hasNext(); ) {
				Edge edge = edgeIterator.next();
				drawLine(edge.getPoint1().getX(), edge.getPoint1().getY(), edge.getPoint2().getX(), edge.getPoint2().getY());
			}
			Vector vector = plate.getSpeedVector();
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
		int elevationMod = 16;
		WorldMap map = model.getMap();
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				float el = map.getCell(x, y).getElevation();
				if (el < 7) {
					shapeRenderer.setColor(0.00f * (el + elevationMod), 0.013f * (el + elevationMod), 0.00f * (el + elevationMod), 1f);
				} else {
					el -= 12;
					float grey = 0.021f * (el + elevationMod);
					shapeRenderer.setColor(grey, grey, grey, 1f);
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
					float el = cell.getElevation() / 5;
					shapeRenderer.setColor(0, 0, 0.03f * (15 + el), 1f);
					drawPoint(x, y);
				}
			}
		}
	}

	private void drawSlopes() {
		WorldGenContainer container = model.getWorldGenContainer();
		WorldMap map = model.getMap();
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				if (map.getCell(x, y).isOcean()) {
					shapeRenderer.setColor(0, 0, 0.005f * Math.abs((container.getSlopeAngles(x, y) % 360) - 180), 1);
				} else {
					shapeRenderer.setColor(0, 0.005f * Math.abs((container.getSlopeAngles(x, y) % 360) - 180), 0, 1);
				}
				drawPoint(x, y);
			}
		}
	}

	private void drawRivers() {
		WorldMap map = model.getMap();
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				if (map.getCell(x, y).isRiver()) {
					shapeRenderer.setColor(0, 0, 1, 1);
					drawPoint(x, y);
				}
			}
		}
	}

	private void drawLakes() {
		for (Iterator<Position> iterator = model.getWorldGenContainer().getLakes().iterator(); iterator.hasNext(); ) {
			Position pos = iterator.next();
			shapeRenderer.setColor(1, 0, 0, 1);
			drawPoint(pos.getX(), pos.getY());
		}
	}

	private void drawTemperature() {
		WorldGenContainer container = model.getWorldGenContainer();
		for (int x = 0; x < container.getConfig().getWidth(); x++) {
			for (int y = 0; y < container.getConfig().getHeight(); y++) {
				if (container.getTemperature(x, y) < -12) {
					shapeRenderer.setColor(1, 1, 1, 1); //
				} else if (container.getTemperature(x, y) < -0) {
					shapeRenderer.setColor(0.3f, 0.7f, 1, 1); //
				} else if (container.getTemperature(x, y) < 8) {
					shapeRenderer.setColor(0.3f, 1, 0.6f, 1); //
				} else if (container.getTemperature(x, y) < 22) {
					shapeRenderer.setColor(0.8f, 1, 0.2f, 1);
				} else if (container.getTemperature(x, y) < 27) {
					shapeRenderer.setColor(1, 0.8f, 0, 1);
				} else {
					shapeRenderer.setColor(1, 0.4f, 0, 1);
				}
//				shapeRenderer.setColor(0.01f * (container.getTemperature(x, y) + 40), 0, 0, 0.5f);
				drawPoint(x + 500, y);
			}
		}
	}

	private void drawDebug() {
		WorldGenContainer container = model.getWorldGenContainer();
		for (int x = 0; x < container.getMap().getWidth(); x++) {
			for (int y = 0; y < container.getMap().getHeight(); y++) {
				shapeRenderer.setColor(0, 0.5f * (container.getTemperature(x, y) + 0.1f), 0, 1);
				drawPoint(x, y);
			}
		}
	}

	private void drawLine(int x1, int y1, int x2, int y2) {
		shapeRenderer.line(x1 * tileSize + tileSize / 2 + tileOffsetX, y1 * tileSize + tileSize / 2 + tileOffsetY,
				x2 * tileSize + tileSize / 2 + tileOffsetX, y2 * tileSize + tileSize / 2 + tileOffsetY);
	}

	private void drawPoint(int x, int y) {
		shapeRenderer.rect(x * tileSize + tileOffsetX, y * tileSize + tileOffsetY,
				tileSize, tileSize);
	}

	public void setModel(WorldGenModel model) {
		this.model = model;
	}

}