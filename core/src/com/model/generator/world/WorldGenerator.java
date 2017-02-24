package com.model.generator.world;


import com.model.generator.world.voronoi.datastructure.OpenList;
import com.model.generator.world.voronoi.diagram.PowerDiagram;
import com.model.generator.world.voronoi.j2d.PolygonSimple;
import com.model.generator.world.voronoi.j2d.Site;
import com.model.utils.Position;
import com.model.utils.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class WorldGenerator {
    private int width;
    private int height;
    private WorldMap map;
    private PowerDiagram diagram;
    private OpenList sites;
    private Random rand;
    private List<Plate> plates;
	private List<Edge> edges;

	public WorldGenerator() {
        rand = new Random();
        plates = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void createMap(int width, int height) {
        this.width = width;
        this.height = height;
        map = new WorldMap(width, height);
        createPlates(50);
        createPlateSpeed();
    }

    private void createFlat() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map.getCell(x, y);
            }
        }
    }

    private void createPlates(int plateNum) {
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
        for (int i = 0; i < plateNum; i++) {
            Site site = new Site(10 + rand.nextInt(width - 20), 10 + rand.nextInt(width - 20));
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
        for (Iterator<Plate> iterator = plates.iterator(); iterator.hasNext();) {
	        Plate plate = iterator.next();
        	Position center = plate.getCenter();
        	Vector speed = new Vector(center.getX(), center.getY(), rand.nextInt(360), rand.nextInt(10));
        	plate.setSpeedVector(speed);
        }
    }

    public WorldMap getMap() {
        return map;
    }

    public List<Plate> getPlates() {
        return plates;
    }

    public List<Position> getFocuses() {
        List<Position> list = new ArrayList<>();
        for (Iterator<Site> iterator = sites.iterator(); iterator.hasNext(); ) {
            Site site = iterator.next();
            list.add(new Position((int) site.getX(), (int) site.getY(), 0));
        }
        return list;
    }
}