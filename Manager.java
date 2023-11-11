import java.io.*;
import java.net.*;
import java.util.Optional;
import java.util.Scanner;

public class Manager {
    private Optional<Double> answer;

    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.start();
    }

    private void start() {
        try {
            while (true) {
                Socket serverF = new Socket("localhost", 12345); // Connect to Server F
                Socket serverG = new Socket("localhost", 54321); // Connect to Server G

                Scanner scanner = new Scanner(System.in);

                // Get user input for x
                System.out.print("Enter x (or 'exit' to quit): ");
                String input = scanner.nextLine();

                if ("exit".equalsIgnoreCase(input)) {
                    break; // Exit the loop if the user inputs "exit"
                }

                try {
                    Double x = Double.parseDouble(input);

                    // Send x to Server F
                    ObjectOutputStream outF = new ObjectOutputStream(serverF.getOutputStream());
                    outF.writeObject(x);

                    // Send x to Server G
                    ObjectOutputStream outG = new ObjectOutputStream(serverG.getOutputStream());
                    outG.writeObject(x);

                    // Receive results from servers
                    ObjectInputStream inF = new ObjectInputStream(serverF.getInputStream());
                    ObjectInputStream inG = new ObjectInputStream(serverG.getInputStream());

                    Optional<Double> resultF = (Optional<Double>) inF.readObject(); // Result of f(x)
                    Optional<Double> resultG = (Optional<Double>) inG.readObject(); // Result of g(x)

                    // Calculate gcd(f(x), g(x))
                    this.answer = calculateGCD(resultF.orElse(null), resultG.orElse(null));

                    System.out.println("gcd(f(x), g(x)) = " + this.answer.orElse(null));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid x format. Please enter a double.");
                } catch (SocketException se) {
                    // Handle broken pipe (client disconnected)
                    System.out.println("Connection to the server was lost. Reconnecting...");
                    continue; // Reconnect to the server and continue with the next input
                }

                serverF.close();
                serverG.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to calculate GCD
    private Optional<Double> calculateGCD(Double a, Double b) {
        if (a != null && b != null) {
            // GCD calculation logic (modify as needed)
            return Optional.of(Math.abs(a - b));
        }
        return Optional.empty();
    }
}
