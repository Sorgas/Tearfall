package com.model.generator.world;

import com.model.generator.world.voronoi.datastructure.OpenList;
import com.model.generator.world.voronoi.diagram.PowerDiagram;
import com.model.generator.world.voronoi.j2d.PolygonSimple;
import com.model.generator.world.voronoi.j2d.Site;
import com.model.generator.world.world_objects.Edge;
import com.model.generator.world.world_objects.Plate;
import com.model.utils.Position;
import com.model.utils.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexander on 28.02.2017.
 */
public class PlateGenerator {
	private int width;
	private int height;
	private Random rand;
	private PowerDiagram diagram;
	private OpenList sites;
	private List<Edge> edges;
	private List<Plate> plates;
	private boolean rejected;
	private int plateNum;

	public PlateGenerator(int width, int height, Random random) {
		this.width = width;
		this.height = height;
		this.rand = random;
		edges = new ArrayList<>();
		plates = new ArrayList<>();
	}

	public void generatePlates(int plateNum) {
		rejected = false;
		this.plateNum = plateNum;
		createPlates();
		createPlateSpeed();
		mergeEdges();
		if (!rejected) applyVectorsToEdges();
	}

	public void setRand(Random rand) {
		this.rand = rand;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public List<Plate> getPlates() {
		return plates;
	}

	public boolean isRejected() {
		return rejected;
	}

	private void createPlates() {
		diagram = new PowerDiagram();
		// normal list based on an array
		sites = new OpenList();

		// create a root polygon which limits the voronoi diagram.
		//  here it is just a rectangle.
		PolygonSimple rootPolygon = new PolygonSimple();
		rootPolygon.add(0, 0);
		rootPolygon.add(width, 0);
		rootPolygon.add(width, height);
		rootPolygon.add(0, height);
		// create 100 points (sites) and set random positions in the rectangle defined above.
		int widthMargin = width / 10;
		int heightMargin = height / 10;
		for (int i = 0; i < plateNum; i++) {
			Site site = new Site(widthMargin + rand.nextInt(width - 2 * widthMargin), heightMargin + rand.nextInt(width - 2 * heightMargin));
			// we could also set a different weighting to some sites
			// site.setWeight(30)
			sites.add(site);
		}
		// set the list of points (sites), necessary for the power diagram
		diagram.setSites(sites);
		// set the clipping polygon, which limits the power voronoi diagram
		diagram.setClipPoly(rootPolygon);
		// do the computation
		diagram.computeDiagram();
		// for each site we can no get the resulting polygon of its cell.
		// note that the cell can also be empty, in this case there is no polygon for the corresponding site.

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
			Vector speed = new Vector(center.getX(), center.getY(), (float) rand.nextInt(360), (float) rand.nextInt(7));
			plate.setSpeedVector(speed);
		}
	}

	private void mergeEdges() {
		edges.clear();
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
