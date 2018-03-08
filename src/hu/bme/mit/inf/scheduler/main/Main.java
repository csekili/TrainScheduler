package hu.bme.mit.inf.scheduler.main;

import java.util.ArrayList;

import hu.bme.mit.inf.scheduler.config.Config;
import hu.bme.mit.inf.scheduler.database.DatabaseQueries;
import hu.bme.mit.inf.scheduler.model.Path;
import hu.bme.mit.inf.scheduler.model.Route;

public class Main implements Config {
	public static void main(String[] args) {
		// ArrayList<Segment> stations = DatabaseQueries.getStations();
		//
		// Scheduler scheduler = new Scheduler();
		// scheduler.loadData();
		//
		// scheduler.addSchedule(null, stations.get(0), stations.get(2));
		//
		// System.out.println("alma");

		ArrayList<Route> routes = DatabaseQueries.getRoutes();
		System.out.println("Routes size: " + routes.size());

		for (int i = 0; i < routes.size(); i++) {
			Route r = routes.get(i);
			System.out.println(i);
			System.out.println("From: " + r.getFrom().getId());
			System.out.println("To  : " + r.getTo().getId() + "\n");
		}

		// ArrayList<RouteLink> routes = DatabaseQueries.getRouteLinks();
		//
		// int i = 1;
		// for (RouteLink l : routes) {
		// System.out.println(i + ", " + l.s);
		// System.out.println("From:" + l.getFromRoute().getFrom().getId());
		// System.out.println("Via :" + l.getFromRoute().getTo().getId());
		// System.out.println("Via :" + l.getToRoute().getFrom().getId());
		// System.out.println("To :" + l.getToRoute().getTo().getId());
		// System.out.println("");
		// i++;
		// }

		// ArrayList<Path> paths = DatabaseQueries.getPaths();
		//
		// for (Path p : paths) {
		// if (p.getVia().getId() == 22 || p.getFrom().getId() == 22 ||
		// p.getTo().getId() == 22) {
		// System.out.println("to: " + p.getTo().getId());
		// System.out.println("via: " + p.getVia().getId());
		// System.out.println("from: " + p.getFrom().getId());
		// System.out.println();
		// }
		// }
		//
		// System.out.println(paths.size());
	}
}
