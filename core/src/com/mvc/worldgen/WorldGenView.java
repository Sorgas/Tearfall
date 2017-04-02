package com.mvc.worldgen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mvc.worldgen.generators.world.WorldGenContainer;
import com.mvc.worldgen.generators.world.map_objects.WorldMap;
import com.mvc.worldgen.generators.world.world_objects.Edge;
import com.mvc.worldgen.generators.world.world_objects.Mountain;
import com.mvc.worldgen.generators.world.world_objects.Plate;
import com.mvc.worldgen.generators.world.world_objects.WorldCell;
import com.utils.Position;
import com.utils.Vector;
import com.mvc.GameView;

import java.util.Iterator;
import java.util.List;

public class WorldGenView implements GameView {
	private WorldGenModel model;
	private int windowWidth;
	private int windowHeight;
	private int tileWidth = 2;
	private int tileHeight = 2;
	private SpriteBatch batch;
	private Texture img;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;

	public WorldGenView() {

	}

	@Override
	public void init() {

	}

	@Override
	public void showFrame() {
		Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
		Gdx.graphics.getGL20().glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		drawDebug();
		drawElevation();
		drawOceans();
//		drawSlopes();
		drawRivers();
//		drawLakes();
		drawTemperature();
//		drawPlates();
//		drawDebug();
		shapeRenderer.end();
		Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);
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
				if (el < 5) {
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
					shapeRenderer.setColor(1,1,1,1); //
				} else if(container.getTemperature(x, y) < -0){
					shapeRenderer.setColor(0.3f,0.7f,1,1); //
				} else if(container.getTemperature(x, y) < 8){
					shapeRenderer.setColor(0.3f,1,0.6f,1); //
				} else if(container.getTemperature(x, y) < 22){
					shapeRenderer.setColor(0.8f,1,0.2f,1);
				} else if(container.getTemperature(x, y) < 27){
					shapeRenderer.setColor(1,0.8f,0,1);
				} else {
					shapeRenderer.setColor(1,0.4f,0,1);
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