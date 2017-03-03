package com.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.model.GameModel;
import com.model.generator.world.world_objects.Edge;
import com.model.generator.world.world_objects.Mountain;
import com.model.generator.world.world_objects.Plate;
import com.model.utils.Position;
import com.model.utils.Vector;

import java.util.Iterator;
import java.util.List;

public class WorldView implements GameView {
	private GameModel model;
	private int windowWidth;
	private int windowHeight;
	private int tileWidth = 4;
	private int tileHeight = 4;
	private SpriteBatch batch;
	private Texture img;
	private ShapeRenderer shapeRenderer;
	private BitmapFont font;

	public WorldView() {
	}

	@Override
	public void showSnapshot() {
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(1, 1, 1, 1);
		drawDots();
		shapeRenderer.setColor(0, 1, 0, 1);
		drawEdges();
		drawSpeedVectors();
		shapeRenderer.setColor(1, 0, 0, 1);
		drawElevationTops();
		shapeRenderer.end();
	}

	private void drawDots() {
		List<Position> list = model.getWorldGenerator().getFocuses();
		for (int i = 0; i < list.size(); i++) {
			Position pos = list.get(i);
			drawPoint(pos.getX(), pos.getY());
		}
	}

	private void drawEdges() {
		List<Plate> plates = model.getWorldGenerator().getPlates();
		for (Iterator<Plate> iterator = plates.iterator(); iterator.hasNext(); ) {
			Plate plate = iterator.next();
			for (Iterator<Edge> edgeIterator = plate.getEdges().iterator(); edgeIterator.hasNext(); ) {
				Edge edge = edgeIterator.next();
				drawLine(edge.getPoint1().getX(), edge.getPoint1().getY(), edge.getPoint2().getX(), edge.getPoint2().getY());
			}
		}
	}

	private void drawSpeedVectors() {
		List<Plate> plates = model.getWorldGenerator().getPlates();
		for (Iterator<Plate> iterator = plates.iterator(); iterator.hasNext(); ) {
			Vector vector = iterator.next().getSpeedVector();
			Position endPoint = vector.getEndPoint();
			drawLine(vector.getX(), vector.getY(), endPoint.getX(), endPoint.getY());
		}
	}

	private void drawElevationTops() {
		List<Edge> edges = model.getWorldGenerator().getEdges();
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

	private Position center(int x, int y) {
		int newX = windowWidth / 2 + x * tileWidth;
		int newY = windowHeight / 2 + y * tileHeight;
		return new Position(newX, newY, 0);
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
	public void setModel(GameModel model) {
		this.model = model;
	}

	@Override
	public void setWidth(int width) {
		this.windowWidth = width;
	}

	@Override
	public void setHeight(int height) {
		this.windowHeight = height;
	}

	@Override
	public void freeResources() {
		img.dispose();
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
}