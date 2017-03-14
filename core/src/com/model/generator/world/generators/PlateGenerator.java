package com.model.generator.world.generators;

import com.model.generator.world.map_objects.WorldGenContainer;
import com.model.generator.world.voronoi.datastructure.OpenList;
import com.model.generator.world.voronoi.diagram.PowerDiagram;
import com.model.generator.world.voronoi.j2d.PolygonSimple;
import com.model.generator.world.voronoi.j2d.Site;
import com.model.generator.world.world_objects.Edge;
import com.model.generator.world.world_objects.Plate;
import com.model.utils.Position;
import com.model.utils.Vector;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexander on 28.02.2017.
 */
public class PlateGenerator {
	private WorldGenContainer container;
	private Random random;
	private List<Edge> edges;
	private List<Plate> plates;
	private int plateNum;
	private int minPlateSpeed;
	private int maxPlateSpeed;
	private float centerMargin;

	public PlateGenerator(WorldGenContainer container) {
		this.container = container;
	}

	public boolean execute() {
		boolean rejected = false;
		extractContainer();
		createPlates();
		createPlateSpeed();
		if(mergeEdges()) return true;
		applyVectorsToEdges();
		return rejected;
	}

	private void extractContainer() {
		random = container.getConfig().getRandom();
		plateNum = container.getConfig().getPlateDensity();
		minPlateSpeed = container.getConfig().getMinPlateSpeed();
		maxPlateSpeed = container.getConfig().getMaxPlateSpeed();
		edges = container.getEdges();
		plates = container.getPlates();
		centerMargin = container.getConfig().getCenterMargin();
	}

	private void createPlates() {
		int width = container.getConfig().getWidth();
		int height = container.getConfig().getHeight();

		PowerDiagram diagram = new PowerDiagram();
		OpenList sites = new OpenList();

		PolygonSimple rootPolygon = new PolygonSimple();
		rootPolygon.add(0, 0);
		rootPolygon.add(width, 0);
		rootPolygon.add(width, height);
		rootPolygon.add(0, height);

		int widthMargin = (int) (width * centerMargin);
		int heightMargin = (int) (height * centerMargin);
		for (int i = 0; i < plateNum; i++) {
			Site site = new Site(widthMargin + random.nextInt(width - 2 * widthMargin), heightMargin + random.nextInt(width - 2 * heightMargin));
			sites.add(site);
		}
		diagram.setSites(sites);
		diagram.setClipPoly(rootPolygon);
		diagram.computeDiagram();
		transformSitesToPlates(sites);
	}

	private void transformSitesToPlates(OpenList sites) {
		for (int i = 0; i < sites.size; i++) {
			Site site = sites.get(i);
			PolygonSimple polygon = site.getPolygon();
			Plate plate = new Plate(new Position((int) site.getX(), (int) site.getY(), 0));
			if (polygon != null) {
				double[] xPoints = polygon.getXPoints();
				double[] yPoints = polygon.getYPoints();
				Position pos1 = new Position((int) xPoints[polygon.length - 1], (int) yPoints[polygon.length - 1], 0);
				for (int j = 0; j < polygon.length; j++) {
					Position pos2 = new Position((int) xPoints[j], (int) yPoints[j], 0);
					Edge edge = new Edge(pos1, pos2);
					edges.add(edge);
					plate.addEdge(edge);
					pos1 = pos2;
				}
			}
			plates.add(plate);
		}
	}

	private void createPlateSpeed() {
		for (Iterator<Plate> iterator = plates.iterator(); iterator.hasNext(); ) {
			Plate plate = iterator.next();
			Position center = plate.getCenter();
			float length = random.nextInt(maxPlateSpeed - minPlateSpeed) + minPlateSpeed;
			Vector speed = new Vector(center.getX(), center.getY(), (float) random.nextInt(360), length);
			plate.setSpeedVector(speed);
		}
	}

	private boolean mergeEdges() {
		edges.clear();
		boolean rejected = false;
		for (int i = 0; i < plates.size(); i++) {
			Plate plate = plates.get(i);
			for (int j = 0; j < plate.getEdges().size(); j++) {
				for (int k = i + 1; k < plates.size(); k++) {
					Plate plate2 = plates.get(k);
					for (int l = 0; l < plate2.getEdges().size(); l++) {
						if (plate.getEdges().get(j).equals(plate2.getEdges().get(l))) {
							plate2.setEdge(l, plate.getEdges().get(j));
						}
					}
				}
				if (!edges.contains(plate.getEdges().get(j))) {
					edges.add(plate.getEdges().get(j));
				}
			}
		}
		if (edges.size() != 1 + 3 * plateNum) {
			rejected = true;
		}
		return rejected;
	}

	private void applyVectorsToEdges() {
		for (Iterator<Plate> iterator = plates.iterator(); iterator.hasNext(); ) {
			Plate plate = iterator.next();
			for (Iterator<Edge> edgeIterator = plate.getEdges().iterator(); edgeIterator.hasNext(); ) {
				edgeIterator.next().addVector(plate.getSpeedVector());
			}
		}
	}
}