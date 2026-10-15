import java.util.ArrayDeque;
import java.util.Queue;

/** A logical process; this is not an operating-system process. */
public class SimProcess {
    private final String id;
    private int logicalClock;
    private final Queue<Message> inbox = new ArrayDeque<>();

    public SimProcess(String id) { this.id = id; }
    public String getId() { return id; }
    public int getLogicalClock() { return logicalClock; }
    public Queue<Message> getInbox() { return inbox; }

    public int tick() { return ++logicalClock; }

    /** Lamport receive rule: C := max(C, message timestamp) + 1. */
    public int receiveTick(int messageTimestamp) {
        logicalClock = Math.max(logicalClock, messageTimestamp) + 1;
        return logicalClock;
    }

    public void reset() {
        logicalClock = 0;
        inbox.clear();
    }
}
