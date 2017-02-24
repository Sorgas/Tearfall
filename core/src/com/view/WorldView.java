package com.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.model.GameModel;
import com.model.generator.world.Edge;
import com.model.generator.world.Plate;
import com.model.utils.Position;
import com.model.utils.Vector;

import java.util.Iterator;
import java.util.List;

public class WorldView implements GameView {
	private GameModel model;
	private int windowWidth;
	private int windowHeight;
	private int tileWidth = 8;
	private int tileHeight = 8;
	private int width = 100;
	private int heigth = 100;
	private SpriteBatch batch;
	private Texture img;
	private ShapeRenderer shapeRenderer;

	public WorldView() {
	}

	@Override
	public void showSnapshot() {
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(1, 1, 1, 1);
		drawDots();
		shapeRenderer.setColor(0, 1, 0, 1);
		drawEdges();
		drawSpeedVectors();
		shapeRenderer.end();
	}

	private void drawDots() {
		List<Position> list = model.getWorldGenerator().getFocuses();
		for (int i = 0; i < list.size(); i++) {
			Position pos = list.get(i);
			shapeRenderer.rect(pos.getX() * tileWidth, pos.getY() * tileHeight, 2, 2);
		}
	}

	private void drawEdges() {
		List<Plate> plates = model.getWorldGenerator().getPlates();
		for (Iterator<Plate> iterator = plates.iterator(); iterator.hasNext(); ) {
			Plate plate = iterator.next();
			for (Iterator<Edge> edgeIterator = plate.getEdges().iterator(); edgeIterator.hasNext(); ) {
				Edge edge = edgeIterator.next();
				shapeRenderer.line(edge.getPoint1().getX() * tileWidth, edge.getPoint1().getY() * tileHeight, edge.getPoint2().getX() * tileWidth, edge.getPoint2().getY() * tileHeight);
			}
		}
	}

	private void drawSpeedVectors() {
		List<Plate> plates = model.getWorldGenerator().getPlates();
		for (Iterator<Plate> iterator = plates.iterator(); iterator.hasNext(); ) {
			Vector vector = iterator.next().getSpeedVector();
			Position endPoint = vector.getEndPoint();
			shapeRenderer.line(vector.getX() * tileWidth, vector.getY() * tileHeight, endPoint.getX() * tileWidth, endPoint.getY() * tileHeight);
		}
	}

	private void drawBorder() {
		shapeRenderer.line(0, heigth * tileHeight + 3, width * tileWidth + 3, heigth * tileHeight + 3);
		shapeRenderer.line(width * tileWidth + 3, heigth * tileHeight + 3, width * tileWidth + 3, 0);
	}

	private void drawTile(int x, int y, int id) {
		batch.draw(new TextureRegion(img, 10, 10, tileWidth, tileHeight), x, y);
	}

	private Position center(int x, int y) {
		int newX = windowWidth / 2 + x * tileWidth;
		int newY = windowHeight / 2 + y * tileHeight;
		return new Position(newX, newY, 0);
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
}