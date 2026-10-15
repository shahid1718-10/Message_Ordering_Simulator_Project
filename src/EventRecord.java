/** One line in the simulator's chronological event log. */
public class EventRecord {
    private final int eventNumber;
    private final String processId;
    private final String eventType;
    private final String messageId;
    private final String details;
    private final int logicalClock;
    private final int deliverySequence;

    public EventRecord(int eventNumber, String processId, String eventType,
                       String messageId, String details, int logicalClock,
                       int deliverySequence) {
        this.eventNumber = eventNumber;
        this.processId = processId;
        this.eventType = eventType;
        this.messageId = messageId;
        this.details = details;
        this.logicalClock = logicalClock;
        this.deliverySequence = deliverySequence;
    }

    public int getEventNumber() { return eventNumber; }
    public String getProcessId() { return processId; }
    public String getEventType() { return eventType; }
    public String getMessageId() { return messageId; }
    public String getDetails() { return details; }
    public int getLogicalClock() { return logicalClock; }
    public int getDeliverySequence() { return deliverySequence; }
}
