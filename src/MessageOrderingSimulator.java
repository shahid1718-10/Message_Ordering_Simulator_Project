import java.util.*;

/**
 * Local simulation of message passing between logical processes.
 * FIFO is enforced per sender-receiver channel, not globally.
 */
public class MessageOrderingSimulator {
    private final Map<String, SimProcess> processes = new LinkedHashMap<>();
    private final List<Message> pending = new ArrayList<>();
    private final List<EventRecord> eventLog = new ArrayList<>();
    private long messageCounter = 0;
    private int eventCounter = 0;
    private int deliveryCounter = 0;

    public MessageOrderingSimulator() {
        processes.put("P1", new SimProcess("P1"));
        processes.put("P2", new SimProcess("P2"));
        processes.put("P3", new SimProcess("P3"));
    }

    public Set<String> getProcessIds() { return Collections.unmodifiableSet(processes.keySet()); }
    public List<Message> getPendingMessages() { return Collections.unmodifiableList(pending); }
    public List<EventRecord> getEventLog() { return Collections.unmodifiableList(eventLog); }

    public boolean hasProcess(String id) { return processes.containsKey(normalize(id)); }
    private String normalize(String id) { return id == null ? "" : id.trim().toUpperCase(Locale.ROOT); }

    public Message send(String senderId, String receiverId, String payload) {
        String sender = normalize(senderId), receiver = normalize(receiverId);
        if (!processes.containsKey(sender)) throw new IllegalArgumentException("Unknown sender: " + senderId);
        if (!processes.containsKey(receiver)) throw new IllegalArgumentException("Unknown receiver: " + receiverId);
        if (sender.equals(receiver)) throw new IllegalArgumentException("Sender and receiver must be different.");
        if (payload == null || payload.trim().isEmpty()) throw new IllegalArgumentException("Message text cannot be empty.");

        SimProcess from = processes.get(sender);
        int timestamp = from.tick();
        String id = "M" + (++messageCounter);
        Message message = new Message(id, sender, receiver, payload.trim(), timestamp, messageCounter);
        pending.add(message);
        addEvent(sender, "SEND", id, sender + " -> " + receiver + " | " + payload.trim(), timestamp, 0);
        return message;
    }

    /**
     * Delivers the selected pending message. When FIFO is enabled, a message
     * cannot overtake an earlier pending message on the same sender->receiver channel.
     */
    public void deliver(String messageId, boolean fifoEnabled) {
        Message chosen = findPending(messageId);
        if (chosen == null) throw new IllegalArgumentException("No pending message with ID " + messageId);
        if (fifoEnabled) {
            for (Message other : pending) {
                if (other.getSender().equals(chosen.getSender())
                        && other.getReceiver().equals(chosen.getReceiver())
                        && other.getSendSequence() < chosen.getSendSequence()) {
                    throw new IllegalStateException("FIFO violation: deliver " + other.getId()
                            + " before " + chosen.getId() + " on channel "
                            + chosen.getSender() + " -> " + chosen.getReceiver() + ".");
                }
            }
        }
        pending.remove(chosen);
        SimProcess receiver = processes.get(chosen.getReceiver());
        receiver.getInbox().add(chosen);
        int receiveClock = receiver.receiveTick(chosen.getSendClock());
        int sequence = ++deliveryCounter;
        addEvent(receiver.getId(), "RECEIVE", chosen.getId(),
                chosen.getSender() + " -> " + chosen.getReceiver()
                        + " | payload=\"" + chosen.getPayload() + "\"",
                receiveClock, sequence);
    }

    private Message findPending(String id) {
        if (id == null) return null;
        for (Message message : pending) if (message.getId().equalsIgnoreCase(id.trim())) return message;
        return null;
    }

    private void addEvent(String processId, String type, String messageId,
                          String details, int clock, int deliverySequence) {
        eventLog.add(new EventRecord(++eventCounter, processId, type, messageId,
                details, clock, deliverySequence));
    }

    public void runSampleScenario() {
        reset();
        send("P1", "P2", "First from P1");
        send("P1", "P2", "Second from P1");
        send("P3", "P2", "Independent message from P3");
        System.out.println("\\nSample scenario created three messages.");
        System.out.println("FIFO demonstration: deliver M2 before M1 and observe the rejection.");
        try {
            deliver("M2", true);
        } catch (IllegalStateException ex) {
            System.out.println("Expected FIFO check: " + ex.getMessage());
        }
        System.out.println("Now delivering in a valid FIFO order: M1, M3, M2 is still rejected if M2 overtakes M1; use M1, M2, M3.");
        deliver("M1", true);
        deliver("M2", true);
        deliver("M3", true);
        printEventLog();
    }

    public void compareOrdering() {
        System.out.println("\\nOrdering comparison");
        System.out.println("FIFO ON: same sender -> same receiver messages must keep send order.");
        System.out.println("FIFO does not require messages from different senders to have one global order.");
        System.out.println("Without FIFO, the simulator permits any pending message to be delivered first.");
        System.out.println("Lamport clocks record logical event order; a timestamp alone does not prove causality.");
    }

    public void printPending() {
        if (pending.isEmpty()) {
            System.out.println("No pending messages.");
            return;
        }
        System.out.println("\\nPending messages:");
        for (Message m : pending) System.out.println("  " + m);
    }

    public void printEventLog() {
        if (eventLog.isEmpty()) {
            System.out.println("Event log is empty.");
            return;
        }
        System.out.println("\\nEVENT LOG");
        System.out.printf("%-5s %-8s %-9s %-8s %-8s %s%n",
                "Event", "Process", "Type", "Message", "Clock", "Details");
        for (EventRecord e : eventLog) {
            System.out.printf("%-5d %-8s %-9s %-8s %-8d %s",
                    e.getEventNumber(), e.getProcessId(), e.getEventType(),
                    e.getMessageId(), e.getLogicalClock(), e.getDetails());
            if (e.getDeliverySequence() > 0) System.out.print(" [delivery #" + e.getDeliverySequence() + "]");
            System.out.println();
        }
    }

    public void reset() {
        for (SimProcess process : processes.values()) process.reset();
        pending.clear();
        eventLog.clear();
        messageCounter = 0;
        eventCounter = 0;
        deliveryCounter = 0;
    }
}
