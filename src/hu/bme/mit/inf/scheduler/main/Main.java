package hu.bme.mit.inf.scheduler.main;

import java.util.ArrayList;

import hu.bme.mit.inf.scheduler.config.Config;
import hu.bme.mit.inf.scheduler.database.DatabaseQueries;
import hu.bme.mit.inf.scheduler.gui.MainWindow;
import hu.bme.mit.inf.scheduler.model.ScheduleEntry;
import hu.bme.mit.inf.scheduler.model.Segment;
import hu.bme.mit.inf.scheduler.model.Train;

public class Main implements Config {

	private static Scheduler scheduler;
	private static MainWindow mw;

	private static void test() {
		// Open a new graphical window
		// MainWindow w = new MainWindow();
		// w.init(args);

		// --------------------------------------------

		// Test RouteLinks
		// ArrayList<RouteLink> routeLinks = DatabaseQueries.getRouteLinks();

		// System.out.println(routeLinks.size() + "\n");
		// for (int i = 0; i < routeLinks.size(); i++) {
		// RouteLink rl = routeLinks.get(i);
		// System.out.println(i);
		// System.out.println("From:" + rl.getFromRoute().getFrom().getId());
		// System.out.println("Via :" + rl.getFromRoute().getTo().getId());
		// System.out.println("Via :" + rl.getToRoute().getFrom().getId());
		// System.out.println("To :" + rl.getToRoute().getTo().getId());
		// System.out.println("");
		// }

		// --------------------------------------------

		// Test Scheduler class, adding a new ScheduleEntry
		// ArrayList<Segment> stations = DatabaseQueries.getStations();
		//
		// Scheduler scheduler = new Scheduler();
		// scheduler.loadData();
		//
		// scheduler.addSchedule(null, stations.get(1), stations.get(2));
		//
		// System.out.println("");
		// --------------------------------------------

		// Test routes
		// ArrayList<Route> routes = DatabaseQueries.getRoutes();
		// System.out.println("Routes size: " + routes.size());
		//
		// for (int i = 0; i < routes.size(); i++) {
		// Route r = routes.get(i);
		// System.out.println(i);
		// System.out.println("From: " + r.getFrom().getId());
		// System.out.println("To : " + r.getTo().getId() + "\n");
		// }

		// --------------------------------------------

		// Test paths' generation
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
		// --------------------------------------------
	}

	public static void main(String[] args) {
		// Just for testing
		test();
		initScheduler();
		initWindow();

		// GUI TEST
		ArrayList<Segment> stations = DatabaseQueries.getStations();

		scheduler.addSchedule(null, stations.get(0), stations.get(2));
		
		mw.drawRoute(scheduler.getSchedules().getEntry(0));
	}

	public static boolean windowClosed() {
		return true;
		//TODO: finish process
	}

	private static void initWindow() {
		//mw = new MainWindow();
		try {
			MainWindow.init(null);
			mw=MainWindow.getWindow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addScheduleEntry(Train t, int fromID, int toID){
		//scheduler.addSchedule(...);
	}

	private static void initScheduler() {
		scheduler = new Scheduler();
		scheduler.loadData();
	}
}
