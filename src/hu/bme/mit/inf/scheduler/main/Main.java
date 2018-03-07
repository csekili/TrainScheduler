package hu.bme.mit.inf.scheduler.main;

import java.util.ArrayList;

import hu.bme.mit.inf.scheduler.config.Config;
import hu.bme.mit.inf.scheduler.database.DatabaseQueries;
import hu.bme.mit.inf.scheduler.model.RouteLink;

public class Main implements Config {
	public static void main(String[] args) {
		System.out.println("ALMA");
		ArrayList<RouteLink> routes = DatabaseQueries.getRouteLinks();

		int i = 1;
		for (RouteLink l : routes) {
			System.out.println(i + ", " + l.s);
			System.out.println("From:" + l.getFromRoute().getFrom().getId());
			System.out.println("Via :" + l.getFromRoute().getTo().getId());
			System.out.println("Via :" + l.getToRoute().getFrom().getId());
			System.out.println("To  :" + l.getToRoute().getTo().getId());
			System.out.println("");
			i++;
		}

		// ArrayList<Path> paths = DatabaseQueries.getPaths();
		//
		// for (Path p : paths) {
		// if (p.getVia().getId() == 21) {
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
