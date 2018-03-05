package hu.bme.mit.inf.scheduler.main;

import java.util.ArrayList;

import hu.bme.mit.inf.scheduler.config.Config;
import hu.bme.mit.inf.scheduler.database.DatabaseQueries;
import hu.bme.mit.inf.scheduler.model.Segment;

public class Main implements Config {
	public static void main(String[] args) {
		ArrayList<Segment> stations = DatabaseQueries.getStations();

		Scheduler s = new Scheduler();
		s.loadData();
		s.addSchedule(null, stations.get(0), stations.get(1));

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
