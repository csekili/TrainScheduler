package hu.bme.mit.inf.scheduler.controller;

import java.util.ArrayList;
import java.util.List;

import hu.bme.mit.inf.modes3.messaging.communication.command.trackelement.interfaces.ITrackElementCommander;
import hu.bme.mit.inf.modes3.messaging.communication.command.train.interfaces.ITrainCommander;
import hu.bme.mit.inf.modes3.messaging.communication.state.trackelement.interfaces.ISegmentOccupancyChangeListener;
import hu.bme.mit.inf.modes3.messaging.communication.state.trackelement.interfaces.ITrackElementStateRegistry;
import hu.bme.mit.inf.modes3.messaging.communication.state.trackelement.interfaces.ITurnoutStateChangeListener;
import hu.bme.mit.inf.modes3.messaging.messages.enums.SegmentOccupancy;
import hu.bme.mit.inf.modes3.messaging.messages.enums.TrainDirection;
import hu.bme.mit.inf.modes3.messaging.messages.enums.TurnoutState;
import hu.bme.mit.inf.modes3.utils.conf.LocomotivesConfiguration;
import hu.bme.mit.inf.scheduler.main.Main;
import hu.bme.mit.inf.scheduler.model.RailRoadElement;
import hu.bme.mit.inf.scheduler.model.ScheduleEntry;
import hu.bme.mit.inf.scheduler.model.Tuple;
import hu.bme.mit.inf.scheduler.model.TurnOut;

public class ScheduleEntryController implements Runnable {
	private ITrainCommander trainCommander;
	private ITrackElementCommander elementCommander;
	private ITrackElementStateRegistry segmentCommander;

	private boolean running = false;
	private Thread thread;

	private ScheduleEntry currentEntry;

	private ArrayList<Integer> occupiedSegments;

	public ScheduleEntryController(ITrainCommander trainCommander, ITrackElementStateRegistry segmentCommander,
			ITrackElementCommander elementCommander) {
		this.trainCommander = trainCommander;
		this.segmentCommander = segmentCommander;
		this.elementCommander = elementCommander;
		init();
	}

	///

	private void setTurnOut(TurnOut turnout, RailRoadElement r1, RailRoadElement r2) {
		try {
			System.out.println(
					"Current state: " + turnout.getId() + "-" + segmentCommander.getTurnoutState(turnout.getId()));
			Thread.sleep(500);

			if (turnout.getTop().equals(r1)) {
				if (turnout.getDivergent().equals(r2)) {
					elementCommander.sendTurnoutCommand(turnout.getId(), TurnoutState.DIVERGENT);
					System.out.println("Change Turnout state: " + turnout.getId() + " to DIVERGENT");
				} else if (turnout.getStraight().equals(r2)) {
					elementCommander.sendTurnoutCommand(turnout.getId(), TurnoutState.STRAIGHT);
					System.out.println("Change Turnout state: " + turnout.getId() + " to STRAIGHT");
				} else {
					System.err.println("turnout status not ok - 1");
				}
			} else if (turnout.getTop().equals(r2)) {
				if (turnout.getDivergent().equals(r1)) {
					elementCommander.sendTurnoutCommand(turnout.getId(), TurnoutState.DIVERGENT);
					System.out.println("Change Turnout state: " + turnout.getId() + " to DIVERGENT");
				} else if (turnout.getStraight().equals(r1)) {
					elementCommander.sendTurnoutCommand(turnout.getId(), TurnoutState.STRAIGHT);
					System.out.println("Change Turnout state: " + turnout.getId() + " to STRAIGHT");
				} else {
					System.err.println("turnout status not ok - 2");
				}
			}
			Thread.sleep(100);
			System.out.println(
					"Current state: " + turnout.getId() + "-" + segmentCommander.getTurnoutState(turnout.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean waitForTrain(int segID) {
		if (currentEntry == null)
			return false;

		while (running) {
			if (occupiedSegmentsContains(segID))
				return true;
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
		return false;
	}

	private void setTrainSpeed(int speed) {
		if (speed < 0 || speed > 128)
			return;
		// trainCommander.setTrainReferenceSpeedAndDirection(ID, 0-128, irány: ENUM);
		// nevek - ID
		// "BR294": 8,
		// "Taurus": 9,
		// "SNCF": 10
		trainCommander.setTrainReferenceSpeedAndDirection(
				LocomotivesConfiguration.INSTANCE.getLocomotiveIdByName("BR294"), speed, TrainDirection.FORWARD);
	}

	///

	private void init() {
		segmentCommander.registerTurnoutStateChangeListener(new ITurnoutStateChangeListener() {
			@Override
			public void onTurnoutStateChange(int id, TurnoutState oldValue, TurnoutState newValue) {
				try {
					String newS = newValue.compareTo(TurnoutState.DIVERGENT) == 0 ? "Divergent" : "Straight";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		segmentCommander.registerSegmentOccupancyChangeListener(new ISegmentOccupancyChangeListener() {
			@Override
			public void onSegmentOccupancyChange(int segID, SegmentOccupancy oldValue, SegmentOccupancy newValue) {
				if (newValue.compareTo(SegmentOccupancy.OCCUPIED) == 0)
					addOccupiedSegment(segID);
				else if (newValue.compareTo(SegmentOccupancy.FREE) == 0)
					removeOccupiedSegment(segID);
			}
		});

		occupiedSegments = new ArrayList<>();
		thread = new Thread(this, "ScheduleEntryController");
	}

	public boolean isWorking() {
		return currentEntry != null;
	}

	public void addScheduleEntry(ScheduleEntry scheduleEntry) {
		currentEntry = scheduleEntry;
	}

	@Override
	public void run() {
		loadOccupancyStates();
		while (running) {
			if (currentEntry == null) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				startScheduleEntry();
			}
		}
	}

	private void startScheduleEntry() {
		System.out.println("Starting schedule entry from " + currentEntry.getFrom_station().getId() + " to "
				+ currentEntry.getTo_station().getId() + "...");
		System.out.println("Waiting for train...");

		Tuple currentTuple = currentEntry.getTuples().get(0);
		Tuple nextTuple = null;
		if (currentEntry.getTuples().size() > 1)
			nextTuple = currentEntry.getTuples().get(1);
		int tupleIndex = 0;
		boolean turnoutAdjusted = false;

		waitForTrain(currentTuple.getFrom().getId());
		Main.setTrainPos(currentEntry.getRailRoadElements_RouteBorders().get(0));
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		setTrainSpeed((int) (128 * 0.65));
		boolean arrived = false;

		if (currentTuple.getTo() instanceof TurnOut) {
			setTurnOut((TurnOut) currentTuple.getTo(), currentTuple.getVia(), nextTuple.getTo());
			turnoutAdjusted = true;
		}

		if (currentTuple.getVia() instanceof TurnOut) {
			setTurnOut((TurnOut) currentTuple.getTo(), currentTuple.getVia(), nextTuple.getTo());
			turnoutAdjusted = true;
		}

		while (!arrived) {

			waitForTrain(currentTuple.getVia().getId());
			//

			System.out.println("Train on: " + currentTuple.getTo().getId() + " segment...");
			Main.setTrainPos(currentTuple.getFrom());
			turnoutAdjusted = false;
			tupleIndex++;
			currentTuple = currentEntry.getTuples().get(tupleIndex);
			if (!currentTuple.isLast_1()) // if(tupleIndex + 1 < currentEntry.getTuples().size())
				nextTuple = currentEntry.getTuples().get(tupleIndex + 1);
			else
				nextTuple = null;

			if (currentTuple.isLast_1()) {
				arrived = true;
			}

			//

			if (!turnoutAdjusted && nextTuple != null && nextTuple.getTo() != null && currentTuple.getTo() instanceof TurnOut) {
				setTurnOut((TurnOut) currentTuple.getTo(), currentTuple.getVia(), nextTuple.getTo());
				turnoutAdjusted = true;
			}

		}

		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
		}
		setTrainSpeed(0);
		
		try {
			Main.setTrainPos(new RailRoadElement(21));
			Thread.sleep(400);
			Main.setTrainPos(new RailRoadElement(22));
		} catch (InterruptedException e) {
		}

	}

	private boolean occupiedSegmentsContains(int id) {
		for (int segID : occupiedSegments) {
			if (segID == id)
				return true;
		}
		return false;
	}

	public void startController() {
		if (running)
			return;
		running = true;
		thread.start();
	}

	public void stopController() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void loadOccupancyStates() {
		List<Integer> segIDs = segmentCommander.getSegments();
		for (int id : segIDs) {
			SegmentOccupancy a = segmentCommander.getSegmentOccupancy(id);
			if (a.compareTo(SegmentOccupancy.OCCUPIED) == 0)
				addOccupiedSegment(id);
		}
	}

	private void addOccupiedSegment(int segID) {
		for (int i : occupiedSegments) {
			if (segID == i)
				return;
		}
		occupiedSegments.add(segID);
	}

	private void removeOccupiedSegment(int segID) {
		int index = -1;
		for (int j = 0; j < occupiedSegments.size(); j++) {
			int i = occupiedSegments.get(j);
			if (segID == i)
				index = j;
		}
		if (index != -1)
			occupiedSegments.remove(index);
	}

}
