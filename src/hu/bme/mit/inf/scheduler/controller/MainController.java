package hu.bme.mit.inf.scheduler.controller;

import org.slf4j.impl.SimpleLoggerFactory;

import hu.bme.mit.inf.modes3.messaging.communication.command.train.interfaces.ITrainCommander;
import hu.bme.mit.inf.modes3.messaging.communication.common.AbstractCommunicationComponent;
import hu.bme.mit.inf.modes3.messaging.communication.state.trackelement.interfaces.ISegmentOccupancyChangeListener;
import hu.bme.mit.inf.modes3.messaging.communication.state.trackelement.interfaces.ITrackElementStateRegistry;
import hu.bme.mit.inf.modes3.messaging.communication.state.trackelement.interfaces.ITurnoutStateChangeListener;
import hu.bme.mit.inf.modes3.messaging.messages.enums.SegmentOccupancy;
import hu.bme.mit.inf.modes3.messaging.messages.enums.TrainDirection;
import hu.bme.mit.inf.modes3.messaging.messages.enums.TurnoutState;
import hu.bme.mit.inf.modes3.messaging.mms.MessagingService;
import hu.bme.mit.inf.modes3.utils.conf.LocomotivesConfiguration;

public class MainController extends AbstractCommunicationComponent {

	public MainController(MessagingService messagingService) {
		super(messagingService, new SimpleLoggerFactory());
		// ITrainCommander trainCommander = super.locator.getTrainCommander();
		// trainCommander.setTrainReferenceSpeedAndDirection(ID, 0-128, irány: ENUM);
		// nevek - ID
		// "BR294": 8,
		// "Taurus": 9,
		// "SNCF": 10
		// trainCommander.setTrainReferenceSpeedAndDirection(LocomotivesConfiguration.INSTANCE.getLocomotiveIdByName(""),
		// 80, TrainDirection.FORWARD);

		// --------------------------

		// seg ID
		ITrackElementStateRegistry segmentCommander = super.locator.getTrackElementStateRegistry();
		SegmentOccupancy a = segmentCommander.getSegmentOccupancy(8);

		segmentCommander.registerTurnoutStateChangeListener(new ITurnoutStateChangeListener() {

			@Override
			public void onTurnoutStateChange(int id, TurnoutState oldValue, TurnoutState newValue) {
				try {
					// String old = oldValue.compareTo(TurnoutState.DIVERGENT) == 0 ? "Divergent" :
					// "Straight";
					String newS = newValue.compareTo(TurnoutState.DIVERGENT) == 0 ? "Divergent" : "Straight";
					System.out.println("Turnout changed: " + id + "\t " + "old" + " -> " + newS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		super.locator.getTrackElementStateRegistry()
				.registerSegmentOccupancyChangeListener(new ISegmentOccupancyChangeListener() {
					@Override
					public void onSegmentOccupancyChange(int segID, SegmentOccupancy oldValue,
							SegmentOccupancy newValue) {
						if (newValue.compareTo(SegmentOccupancy.OCCUPIED) == 0)
							System.out.println("Segment occupied:" + segID);
						// else if (newValue.compareTo(SegmentOccupancy.FREE) == 0)
						// System.out.println("Segment freed:" + segID);
					}
				});
	}

	@Override
	public void run() {

	}
}
