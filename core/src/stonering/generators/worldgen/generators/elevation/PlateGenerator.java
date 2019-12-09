package stonering.generators.worldgen.generators.elevation;

import com.badlogic.gdx.math.Vector2;
import stonering.entity.world.TectonicPlate;
import stonering.generators.worldgen.WorldGenConfig;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.AbstractGenerator;
import stonering.generators.worldgen.voronoi.diagram.PowerDiagram;
import stonering.generators.worldgen.voronoi.j2d.PolygonSimple;
import stonering.generators.worldgen.voronoi.j2d.Site;
import stonering.entity.world.Edge;
import stonering.util.geometry.Position;
import stonering.util.geometry.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @author Alexander Kuzyakov on 28.02.2017.
 */
public class PlateGenerator extends AbstractGenerator {
    private WorldGenConfig config;
    private Random random;
    private List<Edge> edges;
    private List<TectonicPlate> tectonicPlates;
    private int plateNum;
    private int minPlateSpeed;
    private int maxPlateSpeed;
    private float centerMargin;
    private List<Position> centers;

    public PlateGenerator(WorldGenContainer container) {
        super(container);
    }

    public boolean execute() {
        System.out.println("generating tectonicPlates");
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
        tectonicPlates = container.getTectonicPlates();
        centers = new ArrayList<>();
        centerMargin = config.getCenterMargin();
    }

    private void createPlates() {
        int width = container.getConfig().getWidth();
        int height = container.getConfig().getHeight();

        PowerDiagram diagram = new PowerDiagram();

        PolygonSimple rootPolygon = new PolygonSimple();
        rootPolygon.add(0, 0);
        rootPolygon.add(width, 0);
        rootPolygon.add(width, height);
        rootPolygon.add(0, height);

        ArrayList<Site> sites = generatePlateCenters();
        diagram.setSites(sites);
        diagram.setClipPoly(rootPolygon);
        diagram.computeDiagram();
        transformSitesToPlates(sites);
    }

    private ArrayList<Site> generatePlateCenters() {
        int width = container.getConfig().getWidth();
        int height = container.getConfig().getHeight();
        int widthMargin = (int) (width * centerMargin);
        int heightMargin = (int) (height * centerMargin);
        ArrayList<Site> sites = new ArrayList<Site>();
        for (int i = 0; i < plateNum; i++) {
            Site site = new Site(widthMargin + random.nextInt(width - 2 * widthMargin), heightMargin + random.nextInt(width - 2 * heightMargin));
            sites.add(site);
        }
        return sites;
    }

    private void transformSitesToPlates(List<Site> sites) {
        for (int i = 0; i < sites.size(); i++) {
            Site site = sites.get(i);
            PolygonSimple polygon = site.getPolygon();
            TectonicPlate plate = new TectonicPlate(new Position((int) site.x, (int) site.y, 0));
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
            tectonicPlates.add(plate);
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
                if (new Vector(center.x, center.y, center2.x, center2.y).getLength() < 90) {
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
        for (Iterator<TectonicPlate> iterator = tectonicPlates.iterator(); iterator.hasNext(); ) {
            TectonicPlate plate = iterator.next();
            Position center = plate.getCenter();
            double minDistance = config.getHeight();
            Position nearestCenter = centers.get(0);
            for (Position acenter : centers) {
                double distance = Math.sqrt(Math.pow(center.x - acenter.x, 2) + Math.pow(center.y - acenter.y, 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestCenter = acenter;
                }
            }
            Vector2 speed = new Vector2(nearestCenter.x - center.x, nearestCenter.y - center.y);
            speed.setLength(random.nextInt(maxPlateSpeed - minPlateSpeed) + minPlateSpeed);
            plate.setSpeedVector(speed);
        }
    }

    private boolean mergeEdges() {
        edges.clear();
        boolean rejected = false;
        for (int i = 0; i < tectonicPlates.size(); i++) {
            TectonicPlate plate = tectonicPlates.get(i);
            for (int j = 0; j < plate.getEdges().size(); j++) {
                for (int k = i + 1; k < tectonicPlates.size(); k++) {
                    TectonicPlate plate2 = tectonicPlates.get(k);
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
        for (Iterator<TectonicPlate> iterator = tectonicPlates.iterator(); iterator.hasNext(); ) {
            TectonicPlate plate = iterator.next();
            for (Iterator<Edge> edgeIterator = plate.getEdges().iterator(); edgeIterator.hasNext(); ) {
                edgeIterator.next().addVector(plate.getSpeedVector());
            }
        }
    }
}