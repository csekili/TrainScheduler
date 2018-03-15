package hu.bme.mit.inf.scheduler.main;

import java.util.ArrayList;

import org.slf4j.impl.SimpleLoggerFactory;

import hu.bme.mit.inf.modes3.messaging.communication.factory.MessagingServiceFactory;
import hu.bme.mit.inf.modes3.messaging.communication.factory.TopicFactory;
import hu.bme.mit.inf.modes3.messaging.mms.TopicBasedMessagingService;
import hu.bme.mit.inf.modes3.utils.common.jopt.ArgumentDescriptorWithParameter;
import hu.bme.mit.inf.modes3.utils.common.jopt.ArgumentRegistry;
import hu.bme.mit.inf.scheduler.config.Config;
import hu.bme.mit.inf.scheduler.controller.MainController;
import hu.bme.mit.inf.scheduler.database.DatabaseQueries;
import hu.bme.mit.inf.scheduler.gui.MainWindow;
import hu.bme.mit.inf.scheduler.model.RailRoadElement;
import hu.bme.mit.inf.scheduler.model.ScheduleEntry;
import hu.bme.mit.inf.scheduler.model.Segment;
import hu.bme.mit.inf.scheduler.model.Train;

public class Main implements Config {
	private static Scheduler scheduler;
	private static MainWindow window;
	private static MainController controller;

	private static Thread thread_controller;

	public static boolean windowClosed() {
		controller.stopController();
		return true;
	}

	private static void initController(String... args) {
		thread_controller = new Thread() {
			public void run() {
				ArgumentRegistry registry = new ArgumentRegistry(new SimpleLoggerFactory());
				registry.registerArgumentWithOptions(new ArgumentDescriptorWithParameter<String>("address",
						"The address of the transport server", String.class));
				registry.registerArgumentWithOptions(new ArgumentDescriptorWithParameter<Integer>("port",
						"The port used by the transport server", Integer.class));

				registry.parseArguments(args);

				TopicBasedMessagingService msgService = MessagingServiceFactory.createStackForTopics(registry,
						new SimpleLoggerFactory(), TopicFactory.createEveryTopic());

				controller = new MainController(msgService);
				controller.startController();
			}
		};
		thread_controller.start();
	}

	public static void main(String[] args) {
		initController(new String[] { "-address", "root.modes3.intra", "-port", "1883" });
		initScheduler();
		initWindow();
		initTCPSocket();
	}

	private static void initTCPSocket() {

	}

	private static void initWindow() {
		// mw = new MainWindow();
		try {
			MainWindow.init(null);
			window = MainWindow.getWindow();
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
		window.drawRoute(scheduler.getSchedules().getEntry(0));

		controller.addScheduleEntry(scheduler.getSchedules().getEntry(0));
	}

	private static void initScheduler() {
		scheduler = new Scheduler();
		scheduler.loadData();
	}

	//

	public static void setTrainPos(RailRoadElement pos) {
		ScheduleEntry entry = scheduler.getSchedules().getEntry(0);
		for (RailRoadElement r : entry.getRailRoadElements_RouteBorders()) {
			if (r.equals(pos)) {
				window.setTrain(pos);
				return;
			}
		}
	}
}
