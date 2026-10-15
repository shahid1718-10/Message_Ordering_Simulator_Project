# Message Ordering Simulator

A beginner-friendly Java console project for Distributed Computing.

## What it demonstrates
- Three logical processes: P1, P2, and P3.
- Sending messages with IDs, payloads, and Lamport send timestamps.
- Manual message delivery and a chronological event log.
- Optional FIFO enforcement per sender-to-receiver channel.
- Lamport logical-clock receive rule: `C_receiver = max(C_receiver, T_message) + 1`.
- A reproducible sample scenario and a short comparison of ordering concepts.

## Important scope statement
This is a **local simulation**. P1, P2, and P3 are Java objects, not separate operating-system processes or machines. No network sockets are used.

## Requirements
- Java JDK 17 or newer (the code uses switch-arrow syntax).
- No third-party libraries.

## Compile and run
From the project folder:

```bash
javac -d out src/*.java
java -cp out Main
```

On Windows PowerShell, the same commands work from the project folder if `javac` and `java` are on `PATH`.

## Menu
1. Send a message
2. Show pending messages
3. Deliver a message
4. View event log
5. Run sample scenario
6. Compare ordering rules
7. Toggle FIFO rule
8. Reset simulation
0. Exit

## Ordering behavior
FIFO applies only to messages on the same sender-to-receiver channel. For example, if P1 sends M1 and then M2 to P2, M1 must be delivered before M2 when FIFO is on. FIFO does not define one global order across messages from different senders.

When FIFO is off, the user can deliver any pending message first. Lamport timestamps are updated on sends and receives, but the simulator does not implement a total-order broadcast protocol or causal-order delivery.

## Event model
- A send event increments the sender's Lamport clock and attaches that value to the message.
- A receive event updates the receiver's clock using `max(receiver clock, message send timestamp) + 1`.
- The event log records send and receive events in the order the simulator processes them.

## Tests
The source was compiled and exercised during generation with Java if a JDK was available in the build environment. See the delivery notes provided alongside the ZIP for the exact verification status. For college submission, run the compile and menu commands on your own machine and capture your actual output.

## Suggested demo
1. Send P1 -> P2 payload `A`.
2. Send P1 -> P2 payload `B`.
3. Keep FIFO on and try to deliver the second message first; the simulator should reject it.
4. Deliver the first message, then the second.
5. View the event log and explain the Lamport clock values.
6. Toggle FIFO off and try delivering messages in a different order.
