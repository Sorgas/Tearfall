/*******************************************************************************
 * Copyright (c) 2013 Arlind Nocaj, University of Konstanz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 *
 * For distributors of proprietary software, other licensing is possible on request: arlind.nocaj@gmail.com
 *
 * This work is based on the publication below, please cite on usage, e.g.,  when publishing an article.
 * Arlind Nocaj, Ulrik Brandes, "Computing Voronoi Treemaps: Faster, Simpler, and Resolution-independent", Computer Graphics Forum, vol. 31, no. 3, June 2012, pp. 855-864
 ******************************************************************************/
package stonering.generators.worldgen.voronoi.diagram;


import stonering.generators.worldgen.voronoi.convexHull.HEdge;
import stonering.generators.worldgen.voronoi.convexHull.JConvexHull;
import stonering.generators.worldgen.voronoi.convexHull.JFace;
import stonering.generators.worldgen.voronoi.convexHull.JVertex;
import stonering.generators.worldgen.voronoi.j2d.Point2D;
import stonering.generators.worldgen.voronoi.j2d.PolygonSimple;
import stonering.generators.worldgen.voronoi.j2d.Site;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Computes the PowerDiagram by using the convex hull of the transformed half
 * planes of the sites.
 *
 * @author Arlind Nocaj
 */
public class PowerDiagram {

	private static final double numericError = 1E-10;
	public static Graphics2D graphics;

	protected JConvexHull hull = null;
	protected List<Site> sites;
	protected PolygonSimple clipPoly;
	private Rectangle2D bb;
	protected List<JFace> facets = null;

	// set of sites which forms a rectangle that is big enough to bound a
	// diagram with creating a bisector in the clipping polygon
	Site s1;
	Site s2;
	Site s3;
	Site s4;

	public PowerDiagram() {
		sites = null;
		clipPoly = null;
	}

	public PowerDiagram(List sites, PolygonSimple clipPoly) {
		setSites(sites);
		setClipPoly(clipPoly);
	}

	public void setSites(List sites) {
		this.sites = sites;
		hull = null;
	}

	public void setClipPoly(PolygonSimple polygon) {
		clipPoly = polygon;
		bb = polygon.getBounds2D();
		// create sites on a rectangle which is big enough to not create
		// bisectors which intersect the clippingPolygon
		double minX = bb.getMinX();
		double minY = bb.getMinY();

		double width = bb.getWidth();
		double height = bb.getHeight();

		s1 = new Site(minX - width, minY - height);
		s2 = new Site(minX + 2 * width, minY - height);
		s3 = new Site(minX + 2 * width, minY + 2 * height);
		s4 = new Site(minX - width, minY + 2 * height);

		s1.setAsDummy();
		s2.setAsDummy();
		s3.setAsDummy();
		s4.setAsDummy();
	}

	public void computeDiagram() {

		if (sites.size() > 0) {
			Collections.shuffle(sites);
			hull = new JConvexHull();
			List<Site> array = new ArrayList<>();
			array.addAll(sites);
			int size = sites.size();
			for (int z = 0; z < size; z++) {
				Site s = array.get(z);
				if (Double.isNaN(s.getWeight())) {
//					s.setWeight(0.001);
					throw new RuntimeException(
							"Weight of a Site may not be NaN.");
				}
				hull.addPoint(s);
			}

			// reset the border sites, otherwise they could have old data
			// cached.
			s1.clear();
			s2.clear();
			s3.clear();
			s4.clear();

			hull.addPoint(s1);
			hull.addPoint(s2);
			hull.addPoint(s3);
			hull.addPoint(s4);

			// long start = System.currentTimeMillis();
			facets = hull.compute();

			// long end = System.currentTimeMillis();
			// double seconds = end - start;
			// seconds = seconds/1000.0;
			// System.out.println("Hull needed seconds: " + seconds);

			computeData();
		}
	}

	/**
	 * For each site the corresponding polygon and the corresponding neighbours
	 * are computed and stored in the site.
	 */
	private void computeData() {
		// make all vertices visible. When we finished working on one we make
		// invisible to not do it several times
		int vertexCount = hull.getVertexCount();
		boolean[] verticesVisited = new boolean[vertexCount];

		int facetCount = facets.size();
		for (int i = 0; i < facetCount; i++) {
			JFace facet = facets.get(i);
			if (facet.isVisibleFromBelow()) {
				for (int e = 0; e < 3; e++) {
					// got through the edges and start to build the polygon by
					// going through the double connected edge list
					HEdge edge = facet.getEdge(e);
					JVertex destVertex = edge.getDest();
					Site site = (Site) destVertex.originalObject;

					if (!verticesVisited[destVertex.getIndex()]) {
						verticesVisited[destVertex.getIndex()] = true;
						if (site.isDummy) {
							continue;
						}

						// faces around the vertices which correspond to the
						// polygon corner points
						ArrayList<JFace> faces = getFacesOfDestVertex(edge);
						PolygonSimple poly = new PolygonSimple();
						double lastX = Double.NaN;
						double lastY = Double.NaN;
						double dx = 1;
						double dy = 1;
						for (JFace face : faces) {
							Point2D point = face
									.getDualPoint();
							double x1 = point.getX();
							double y1 = point.getY();
							if (!Double.isNaN(lastX)) {

								dx = lastX - x1;
								dy = lastY - y1;
								if (dx < 0) {
									dx = -dx;
								}
								if (dy < 0) {
									dy = -dy;
								}
							}
							if (dx > numericError || dy > numericError) {
								poly.add(x1, y1);
								lastX = x1;
								lastY = y1;
							}
						}
						site.nonClippedPolyon = poly;

						if (!site.isDummy) {
							site.setPolygon(clipPoly.convexClip(poly));
						}
					}
				}
			}
		}
	}

	/**
	 * Return the faces which are visible from below
	 *
	 * @param edge
	 * @return
	 */
	private ArrayList<JFace> getFacesOfDestVertex(HEdge edge) {
		ArrayList<JFace> faces = new ArrayList<JFace>();
		HEdge previous = edge;
		JVertex first = edge.getDest();

		Site site = (Site) first.originalObject;
		ArrayList<Site> neighbours = new ArrayList<Site>();
		do {
			previous = previous.getTwin().getPrev();

			// add neighbour to the neighbourlist
			Site siteOrigin = (Site) previous.getOrigin().originalObject;
			if (!siteOrigin.isDummy) {
				neighbours.add(siteOrigin);
			}
			JFace iFace = previous.getiFace();
			if (iFace.isVisibleFromBelow()) {
				faces.add(iFace);
			}
		} while (previous != edge);
		site.setNeighbours(neighbours);
		return faces;
	}
}
