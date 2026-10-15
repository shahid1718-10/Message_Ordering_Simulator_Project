# Worked Lamport clock example

Assume all clocks start at 0. A send event increments the sender's clock and attaches the resulting timestamp. A receive event applies `max(local clock, message timestamp) + 1`.

| Step | Process | Event | Message timestamp | Clock after event |
|---:|---|---|---:|---:|
| 1 | P1 | Send M1 to P2 | 1 | P1 = 1 |
| 2 | P2 | Receive M1 | 1 | P2 = max(0, 1) + 1 = 2 |
| 3 | P2 | Send M2 to P3 | 3 | P2 = 3 |
| 4 | P3 | Receive M2 | 3 | P3 = max(0, 3) + 1 = 4 |

The receive event at P2 is at clock 2, so its next send increments the clock to 3. This example demonstrates a causal chain: P1 sends M1, P2 receives M1, and then P2 sends M2. The timestamp of M2 is therefore greater than M1's timestamp.
