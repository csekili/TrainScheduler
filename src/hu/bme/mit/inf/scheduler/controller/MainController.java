package hu.bme.mit.inf.scheduler.controller;

import java.util.ArrayList;

import org.slf4j.impl.SimpleLoggerFactory;

import hu.bme.mit.inf.modes3.messaging.communication.command.trackelement.interfaces.ITrackElementCommander;
import hu.bme.mit.inf.modes3.messaging.communication.command.train.interfaces.ITrainCommander;
import hu.bme.mit.inf.modes3.messaging.communication.common.AbstractCommunicationComponent;
import hu.bme.mit.inf.modes3.messaging.communication.state.trackelement.interfaces.ITrackElementStateRegistry;
import hu.bme.mit.inf.modes3.messaging.mms.MessagingService;
import hu.bme.mit.inf.scheduler.model.ScheduleEntry;

public class MainController extends AbstractCommunicationComponent {
	private ScheduleEntryController schController;

	private Thread thread;
	private boolean running;

	private ArrayList<ScheduleEntry> scheduleEntries;
	private ITrainCommander trainCommander;
	private ITrackElementCommander elementCommander;
	private ITrackElementStateRegistry segmentCommander;

	public MainController(MessagingService messagingService) {
		super(messagingService, new SimpleLoggerFactory());
		initController();
		initScheduleEntryController();
	}

	private void initController() {
		scheduleEntries = new ArrayList<>();
		thread = new Thread() {
			@Override
			public void run() {
				runController();
			}
		};

		trainCommander = super.locator.getTrainCommander();
		elementCommander = super.locator.getTrackElementCommander();
		segmentCommander = super.locator.getTrackElementStateRegistry();
	}

	private void initScheduleEntryController() {
		schController = new ScheduleEntryController(trainCommander, segmentCommander, elementCommander);
		schController.startController();
	}

	/**
	 * Dont touch...
	 */
	@Override
	public void run() {
	}

	private void runController() {
		while (running) {
			if (scheduleEntries.size() == 0 || schController.isWorking()) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				schController.addScheduleEntry(scheduleEntries.get(0));
				scheduleEntries.remove(0);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void stopController() {
		schController.stopController();
		running = false;
		try {
			thread.join(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void startController() {
		if (running)
			return;
		running = true;
		if (thread != null)
			thread.start();
	}

	public void addScheduleEntry(ScheduleEntry entry) {
		scheduleEntries.add(entry);
	}
}
