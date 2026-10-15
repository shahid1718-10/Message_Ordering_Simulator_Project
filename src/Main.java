import java.util.Scanner;

public class Main {
    private static final Scanner INPUT = new Scanner(System.in);

    public static void main(String[] args) {
        MessageOrderingSimulator simulator = new MessageOrderingSimulator();
        boolean fifoEnabled = true;

        System.out.println("==================================");
        System.out.println("     MESSAGE ORDERING SIMULATOR");
        System.out.println("==================================");
        System.out.println("Logical processes available: P1, P2, P3");
        System.out.println("Note: these are simulated processes, not separate OS processes.");
        while (true) {
            System.out.println("\\nFIFO rule: " + (fifoEnabled ? "ON" : "OFF"));
            System.out.println("1. Send a message");
            System.out.println("2. Show pending messages");
            System.out.println("3. Deliver a message");
            System.out.println("4. View event log");
            System.out.println("5. Run sample scenario");
            System.out.println("6. Compare ordering rules");
            System.out.println("7. Toggle FIFO rule");
            System.out.println("8. Reset simulation");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = INPUT.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> sendMessage(simulator);
                    case "2" -> simulator.printPending();
                    case "3" -> deliverMessage(simulator, fifoEnabled);
                    case "4" -> simulator.printEventLog();
                    case "5" -> simulator.runSampleScenario();
                    case "6" -> simulator.compareOrdering();
                    case "7" -> {
                        fifoEnabled = !fifoEnabled;
                        System.out.println("FIFO is now " + (fifoEnabled ? "ON" : "OFF") + ".");
                    }
                    case "8" -> {
                        simulator.reset();
                        System.out.println("Simulation reset.");
                    }
                    case "0" -> {
                        System.out.println("Goodbye.");
                        return;
                    }
                    default -> System.out.println("Invalid menu option. Enter a number from 0 to 8.");
                }
            } catch (IllegalArgumentException | IllegalStateException ex) {
                System.out.println("Cannot complete action: " + ex.getMessage());
            }
        }
    }

    private static void sendMessage(MessageOrderingSimulator simulator) {
        System.out.print("Sender (P1/P2/P3): ");
        String sender = INPUT.nextLine();
        System.out.print("Receiver (P1/P2/P3): ");
        String receiver = INPUT.nextLine();
        System.out.print("Message text: ");
        String payload = INPUT.nextLine();
        Message m = simulator.send(sender, receiver, payload);
        System.out.println("Created message: " + m);
    }

    private static void deliverMessage(MessageOrderingSimulator simulator, boolean fifoEnabled) {
        simulator.printPending();
        System.out.print("Message ID to deliver: ");
        String id = INPUT.nextLine();
        simulator.deliver(id, fifoEnabled);
        System.out.println("Message delivered.");
    }
}
