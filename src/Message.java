import java.util.Objects;

/** A message sent between simulated logical processes. */
public class Message {
    private final String id;
    private final String sender;
    private final String receiver;
    private final String payload;
    private final int sendClock;
    private final long sendSequence;

    public Message(String id, String sender, String receiver, String payload,
                   int sendClock, long sendSequence) {
        this.id = Objects.requireNonNull(id);
        this.sender = Objects.requireNonNull(sender);
        this.receiver = Objects.requireNonNull(receiver);
        this.payload = Objects.requireNonNull(payload);
        this.sendClock = sendClock;
        this.sendSequence = sendSequence;
    }

    public String getId() { return id; }
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getPayload() { return payload; }
    public int getSendClock() { return sendClock; }
    public long getSendSequence() { return sendSequence; }

    @Override
    public String toString() {
        return id + " " + sender + " -> " + receiver
                + " payload=\"" + payload + "\" sendClock=" + sendClock;
    }
}
