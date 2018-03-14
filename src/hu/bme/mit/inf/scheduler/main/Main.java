package hu.bme.mit.inf.scheduler.main;

import org.slf4j.impl.SimpleLoggerFactory;

import hu.bme.mit.inf.modes3.messaging.communication.factory.MessagingServiceFactory;
import hu.bme.mit.inf.modes3.messaging.communication.factory.TopicFactory;
import hu.bme.mit.inf.modes3.messaging.mms.TopicBasedMessagingService;
import hu.bme.mit.inf.modes3.utils.common.jopt.ArgumentDescriptorWithParameter;
import hu.bme.mit.inf.modes3.utils.common.jopt.ArgumentRegistry;
import hu.bme.mit.inf.scheduler.config.Config;
import hu.bme.mit.inf.scheduler.controller.MainController;
import hu.bme.mit.inf.scheduler.gui.MainWindow;
import hu.bme.mit.inf.scheduler.model.Segment;
import hu.bme.mit.inf.scheduler.model.Train;

public class Main implements Config {

	private static Scheduler scheduler;
	private static MainWindow mw;

	private static void test(String... args) {
		/**
		 * 
		 */
		ArgumentRegistry registry = new ArgumentRegistry(new SimpleLoggerFactory());
		registry.registerArgumentWithOptions(new ArgumentDescriptorWithParameter<String>("address",
				"The address of the transport server", String.class));
		registry.registerArgumentWithOptions(new ArgumentDescriptorWithParameter<Integer>("port",
				"The port used by the transport server", Integer.class));

		registry.parseArguments(args);

		TopicBasedMessagingService msgService = MessagingServiceFactory.createStackForTopics(registry,
				new SimpleLoggerFactory(), TopicFactory.createEveryTopic());

		MainController controller = new MainController(msgService);
	}

	public static void main(String[] args) {
		// Just for testing
		test(args);
		initScheduler();
		initWindow();

		// GUI TEST
		// ArrayList<Segment> stations = DatabaseQueries.getStations();
		//
		// scheduler.addSchedule(null, stations.get(0), stations.get(2));
		//
		// mw.drawRoute(scheduler.getSchedules().getEntry(0));
	}

	public static boolean windowClosed() {
		return true;
		// TODO: finish process
	}

	private static void initWindow() {
		// mw = new MainWindow();
		try {
			MainWindow.init(null);
			mw = MainWindow.getWindow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addScheduleEntry(Train t, int fromID, int toID) {
		Segment fromStation = null, toStation = null;
		for (Segment s : scheduler.getStations()) {
			if (s.getId() == fromID)
				fromStation = s;
			if (s.getId() == toID)
				toStation = s;
		}
		if (fromStation == null || toStation == null)
			return;
		scheduler.addSchedule(t, fromStation, toStation);
		mw.drawRoute(scheduler.getSchedules().getEntry(0));
	}

	private static void initScheduler() {
		scheduler = new Scheduler();
		scheduler.loadData();
	}
}
