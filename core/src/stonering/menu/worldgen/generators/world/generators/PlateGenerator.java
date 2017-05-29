package stonering.menu.worldgen.generators.world.generators;

import stonering.menu.worldgen.generators.world.WorldGenConfig;
import stonering.menu.worldgen.generators.world.WorldGenContainer;
import stonering.menu.worldgen.generators.world.voronoi.diagram.PowerDiagram;
import stonering.menu.worldgen.generators.world.voronoi.j2d.PolygonSimple;
import stonering.menu.worldgen.generators.world.voronoi.j2d.Site;
import stonering.menu.worldgen.generators.world.world_objects.Edge;
import stonering.menu.worldgen.generators.world.world_objects.Plate;
import stonering.utils.Position;
import stonering.utils.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexander on 28.02.2017.
 */
public class PlateGenerator extends AbstractGenerator {
	private WorldGenConfig config;
	private Random random;
	private List<Edge> edges;
	private List<Plate> plates;
	private int plateNum;
	private int minPlateSpeed;
	private int maxPlateSpeed;
	private float centerMargin;
	private List<Position> centers;

	public PlateGenerator(WorldGenContainer container) {
		super(container);
	}

	public boolean execute() {
		System.out.println("generating plates");
		boolean rejected = false;
		extractContainer();
		createPlates();
		createAggregationCenters();
		createPlateSpeed();
		if (mergeEdges()) return true;
		applyVectorsToEdges();
		return rejected;
	}

	private void extractContainer() {
		config = container.getConfig();
		random = config.getRandom();
		plateNum = config.getPlateDensity();
		minPlateSpeed = config.getMinPlateSpeed();
		maxPlateSpeed = config.getMaxPlateSpeed();
		edges = container.getEdges();
		plates = container.getPlates();
		centers = new ArrayList<>();
		centerMargin = config.getCenterMargin();
	}

	private void createPlates() {
		int width = container.getConfig().getWidth();
		int height = container.getConfig().getHeight();

		PowerDiagram diagram = new PowerDiagram();
		List sites = new ArrayList<Site>();

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

	private void transformSitesToPlates(List<Site> sites) {
		for (int i = 0; i < sites.size(); i++) {
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

	private void createAggregationCenters() {
		int centerNum = plateNum / 3;
		int width = container.getConfig().getWidth();
		int height = container.getConfig().getHeight();
		int widthMargin = (int) (width * centerMargin);
		int heightMargin = (int) (height * centerMargin);
		for (int i = 0; centers.size() < centerNum && i < plateNum * 2; i++) {
			Position center = new Position(widthMargin + random.nextInt(width - 4 * widthMargin), heightMargin + random.nextInt(width - 4 * heightMargin), 0);
			boolean tooClose = false;
			for (Position center2 : centers) {
				if (new Vector(center.getX(), center.getY(), center2.getX(), center2.getY()).getLength() < 90) {
					tooClose = true;
					break;
				}
			}
			if (!tooClose) {
				centers.add(center);
				System.out.println("center added");
			}
		}
	}

	private void createPlateSpeed() {
		for (Iterator<Plate> iterator = plates.iterator(); iterator.hasNext(); ) {
			Plate plate = iterator.next();
			Position center = plate.getCenter();
			double minDistance = config.getHeight();
			Position nearestCenter = centers.get(0);
			for (Position acenter : centers) {
				double distance = Math.sqrt(Math.pow(center.getX() - acenter.getX(), 2) + Math.pow(center.getY() - acenter.getY(), 2));
				if (distance < minDistance) {
					minDistance = distance;
					nearestCenter = acenter;
				}
			}
			Vector speed = new Vector(center.getX(), center.getY(), nearestCenter.getX(), nearestCenter.getY());
//			if(speed.getLength() > 100) {
//				speed.setLength(0);
//			} else {
				speed.setLength(random.nextInt(maxPlateSpeed - minPlateSpeed) + minPlateSpeed);
//			}
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