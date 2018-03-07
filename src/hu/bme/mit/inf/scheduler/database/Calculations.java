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
			ArrayList<Route> to = new ArrayList<>();
			ArrayList<Route> from = new ArrayList<>();
			for (Route r2 : routes) {
				if (r == r2)
					continue;
				if (r.getTo() == r2.getFrom()) {
					from.add(r2);
				} else if (r.getFrom() == r2.getTo()) {
					to.add(r2);
				}
			}
			for (Route t : to) {
				RailRoadElement via = r.getFrom();
				if (via instanceof TurnOut) {
					TurnOut turnout = (TurnOut) via;
					if (routeContainsElement(t, turnout.getDivergent())
							&& routeContainsElement(r, turnout.getStraight())) {
						continue;
					} else if (routeContainsElement(t, turnout.getStraight())
							&& routeContainsElement(r, turnout.getDivergent())) {
						continue;
					}
					data.add(new RouteLink(via, t, r, "to"));
				} else {
					data.add(new RouteLink(via, t, r, "to"));
				}
			}
			for (Route f : from) {
				RailRoadElement via = r.getTo();
				if (via instanceof TurnOut) {
					TurnOut turnout = (TurnOut) via;
					if (routeContainsElement(r, turnout.getDivergent())
							&& routeContainsElement(f, turnout.getStraight())) {
						continue;
					} else if (routeContainsElement(r, turnout.getStraight())
							&& routeContainsElement(f, turnout.getDivergent())) {
						continue;
					}
					data.add(new RouteLink(via, r, f, "from"));
				} else {
					data.add(new RouteLink(via, r, f, "from"));
				}
			}
		}
		return data;
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

		for (RailRoadElement r : stationOrTurnOut) {
			ArrayList<Path> beginnerPaths = new ArrayList<>();
			for (Path p : paths) {
				if (p.getFrom().getId() == r.getId()) {
					beginnerPaths.add(p);
				}
			}

			if (beginnerPaths.isEmpty())
				continue;

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
					routes.get(routes.size() - 1).setTo(routePaths.get(0).getVia());
				}
			}
			//
		}
		return routes;
	}

	/**
	 * Megnézi, hogy egy path-nak a "to" RailRoadElement-je egy station/turnout-e.
	 * 
	 * @param actual
	 *            - Path
	 * @param stationOrTurnOut
	 *            - Lista a station/turnoutokról
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
