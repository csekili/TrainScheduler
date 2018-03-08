package hu.bme.mit.inf.scheduler.database;

import java.util.ArrayList;

import hu.bme.mit.inf.scheduler.model.Path;
import hu.bme.mit.inf.scheduler.model.RailRoadElement;
import hu.bme.mit.inf.scheduler.model.Route;
import hu.bme.mit.inf.scheduler.model.RouteLink;
import hu.bme.mit.inf.scheduler.model.Segment;
import hu.bme.mit.inf.scheduler.model.TurnOut;

public class Calculations {
	public static ArrayList<RouteLink> getRouteLinks(ArrayList<Route> routes) {
		ArrayList<RouteLink> data = new ArrayList<>();
		for (Route r : routes) {
			ArrayList<Route> prevRoutes = new ArrayList<>();
			ArrayList<Route> nextRoutes = new ArrayList<>();
			for (Route r2 : routes) {
				if (r == r2)
					continue;
				if (r.getTo().getId() == r2.getFrom().getId()) {
					nextRoutes.add(r2);
				} else if (r.getFrom().getId() == r2.getTo().getId()) {
					prevRoutes.add(r2);
				}
			}
			for (Route prev : prevRoutes) {
				RailRoadElement via = r.getFrom();
				if (via instanceof TurnOut) {
					TurnOut turnout = (TurnOut) via;
					if (routeContainsElement(prev, turnout.getDivergent())
							&& routeContainsElement(r, turnout.getStraight())) {
						continue;
					} else if (routeContainsElement(prev, turnout.getStraight())
							&& routeContainsElement(r, turnout.getDivergent())) {
						continue;
					}
				}
				if (r.getFrom().getId() != r.getTo().getId()) {
					if (!containsRouteLink(via, prev, r, data))
						data.add(new RouteLink(via, prev, r, "to"));
				}
			}
			for (Route next : nextRoutes) {
				RailRoadElement via = r.getTo();
				if (via instanceof TurnOut) {
					TurnOut turnout = (TurnOut) via;
					if (routeContainsElement(r, turnout.getDivergent())
							&& routeContainsElement(next, turnout.getStraight())) {
						continue;
					} else if (routeContainsElement(r, turnout.getStraight())
							&& routeContainsElement(next, turnout.getDivergent())) {
						continue;
					}
				}
				if (r.getFrom().getId() != next.getTo().getId()) {
					if (!containsRouteLink(via, r, next, data))
						data.add(new RouteLink(via, r, next, "from"));
				}
			}
		}
		return data;
	}

	private static boolean containsRouteLink(RailRoadElement via, Route from, Route to, ArrayList<RouteLink> data) {
		int fromID = from.getFrom().getId();
		int viaID = via.getId();
		int toID = to.getTo().getId();
		for (RouteLink rl : data) {
			if (rl.getFromRoute().getFrom().getId() == fromID && rl.getViaNode().getId() == viaID
					&& rl.getToRoute().getTo().getId() == toID)
				return true;
		}
		return false;
	}

	public static boolean routeContainsElement(Route r, RailRoadElement element) {
		ArrayList<Path> s = r.getPaths();
		for (Path p : s) {
			if (p.getFrom() == element || p.getVia() == element || p.getTo() == element) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Segment> getStations(ArrayList<RailRoadElement> sections) {
		ArrayList<Segment> data = new ArrayList<>();

		for (RailRoadElement r : sections) {
			if (r instanceof Segment && ((Segment) r).isStation())
				data.add((Segment) r);
		}
		return data;
	}

	public static ArrayList<RailRoadElement> getRouteBorders(ArrayList<RailRoadElement> sections) {
		ArrayList<RailRoadElement> stationOrTurnOut = new ArrayList<>();

		for (RailRoadElement p : sections) {
			if (p instanceof Segment && ((Segment) p).isStation() || p instanceof TurnOut) {
				stationOrTurnOut.add(p);
			}
		}
		return stationOrTurnOut;
	}

	public static ArrayList<Route> getRoutes(ArrayList<Path> paths, ArrayList<RailRoadElement> sections) {
		ArrayList<Route> routes = new ArrayList<>();
		ArrayList<RailRoadElement> stationOrTurnOut = getRouteBorders(sections);

		// ----

		// Minden meg�ll�ra vagy v�lt�ra keres�nk onnan kiindul� route-okat, amik
		// path-okb�l �p�lnek fel.
		for (RailRoadElement r : stationOrTurnOut) {
			ArrayList<Path> beginnerPaths = new ArrayList<>(); // Ezekkel a path-okkal tudunk elindulni.
			ArrayList<Path> viaPaths = new ArrayList<>(); // zs�kutc�k miatt kell
			for (Path p : paths) {
				if (p.getFrom().getId() == r.getId()) {
					beginnerPaths.add(p);
				}
				if (p.getVia().getId() == r.getId()) {
					viaPaths.add(p);
				}
			}

			// Minden kiindul� path-ra keres�nk v�gigj�rjuk a (m�g nem l�tez�) route-ot,
			// am�g egy meg�ll�ba/v�lt�ba �tk�z�nk
			// Ez �gy pont egy route lesz.
			for (Path begin : beginnerPaths) {
				ArrayList<Path> routePaths = new ArrayList<>();
				Path actual = begin;
				routePaths.add(actual);
				while (!isLastStepInRoute(actual, stationOrTurnOut)) {
					A: for (Path p : paths) {
						if (p.getVia().getId() == actual.getTo().getId()
								&& p.getFrom().getId() == actual.getVia().getId()) {
							actual = p;
							break A;
						}
					}
					routePaths.add(actual);
				}
				routes.add(new Route(routePaths));
				if (routePaths.size() == 1) {
					boolean toIsStation = false;
					for (RailRoadElement st : stationOrTurnOut) {
						if (routePaths.get(0).getTo().getId() == st.getId()) {
							toIsStation = true;
						}
					}
					if (!toIsStation)
						routes.get(routes.size() - 1).setTo(routePaths.get(0).getVia());
				}
			}

			// Ugyanez zs�kutc�kn�l...
			for (Path via : viaPaths) {
				boolean last = false;
				for (RailRoadElement s : stationOrTurnOut) {
					if (s.getId() == via.getTo().getId()) {
						last = true;
						break;
					}
				}
				if (last) {
					ArrayList<Path> rp = new ArrayList<>();
					rp.add(via);
					routes.add(new Route(rp));
					routes.get(routes.size() - 1).setFrom(via.getVia());
				}
			}
			//
		}

		// Van n�h�ny duplik�l�s, azokat itt kiszedj�k (a zs�kutc�k miatt, ha k�t
		// v�lt�/meg�ll� k�zvetlen egym�s mellett van)
		// Az�rt n�z ki ilyen macer�san, mert Iter�torral v�gigmegy�nk, akkor nem lehet
		// �tk�zben kiszedni elemet a list�b�l :D
		ArrayList<Route> deletion = new ArrayList<>();
		ArrayList<Route> cantDelete = new ArrayList<>();

		for (Route r1 : routes) {
			for (Route r2 : routes) {
				if (r1 == r2)
					continue;
				if (r1.getFrom().getId() == r2.getFrom().getId() && r1.getTo().getId() == r2.getTo().getId()) {
					if (!cantDelete.contains(r2)) {
						deletion.add(r2);
						if (!cantDelete.contains(r1))
							cantDelete.add(r1);
					}
				}
			}
		}
		routes.removeAll(deletion);

		return routes;
	}

	/**
	 * Megn�zi, hogy egy path-nak a "to" RailRoadElement-je egy station/turnout-e.
	 * 
	 * @param actual
	 *            - Path
	 * @param stationOrTurnOut
	 *            - Lista a station/turnoutokr�l
	 * @return
	 */
	private static boolean isLastStepInRoute(Path actual, ArrayList<RailRoadElement> stationOrTurnOut) {
		for (RailRoadElement r : stationOrTurnOut) {
			if (r.getId() == actual.getTo().getId()) {
				return true;
			} else if (r.getId() == actual.getVia().getId()) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<Path> getPaths(ArrayList<RailRoadElement> sections) {
		ArrayList<Path> paths = new ArrayList<>();

		for (RailRoadElement e : sections) {
			if (e instanceof Segment) {
				if (((Segment) e).getConnectedTo1().getId() == -1 || ((Segment) e).getConnectedTo2().getId() == -1)
					continue;
				paths.add(new Path(((Segment) e).getConnectedTo1(), ((Segment) e).getConnectedTo2(), (Segment) e));
				paths.add(new Path(((Segment) e).getConnectedTo2(), ((Segment) e).getConnectedTo1(), (Segment) e));
			} else if (e instanceof TurnOut) {
				paths.add(new Path(((TurnOut) e).getStraight(), ((TurnOut) e).getTop(), (TurnOut) e));
				paths.add(new Path(((TurnOut) e).getTop(), ((TurnOut) e).getStraight(), (TurnOut) e));

				paths.add(new Path(((TurnOut) e).getDivergent(), ((TurnOut) e).getTop(), (TurnOut) e));
				paths.add(new Path(((TurnOut) e).getTop(), ((TurnOut) e).getDivergent(), (TurnOut) e));
			}
		}

		return paths;
	}

	public static ArrayList<RailRoadElement> getSections(ArrayList<TurnOut> turnouts, ArrayList<Segment> segments) {
		ArrayList<RailRoadElement> elements = new ArrayList<>();

		for (TurnOut t : turnouts) {
			int div = t.getDivergent().getId();
			int top = t.getTop().getId();
			int str = t.getStraight().getId();
			for (Segment s : segments) {
				int id = s.getId();
				if (id == div)
					t.setDivergent(s);
				else if (id == top)
					t.setTop(s);
				else if (id == str)
					t.setStraight(s);
			}
		}
		for (Segment s : segments) {
			int id1 = s.getConnectedTo1().getId();
			int id2 = s.getConnectedTo2().getId();
			for (TurnOut t : turnouts) {
				int id = t.getId();
				if (id == id1)
					s.setConnectedTo1(t);
				else if (id == id2)
					s.setConnectedTo2(t);
			}
		}
		elements.addAll(turnouts);
		elements.addAll(segments);
		return elements;
	}
}
