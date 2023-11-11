import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class SideF {
    private static Map<Double, Double> memoizationCache = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server F is running...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected to Server F.");

                // Use CompletableFuture for asynchronous request processing
                CompletableFuture.runAsync(() -> {
                    try {
                        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                        Double x = (Double) in.readObject();

                        Optional<Double> resultF = calculateF(x);

                        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                        out.writeObject(resultF.orElse(null));

                        clientSocket.close();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Example implementation of the function f(x)
    private static Optional<Double> calculateF(Double x) {
        if (memoizationCache.containsKey(x)) {
            return Optional.of(memoizationCache.get(x));
        } else {
            // Calculation of f(x) and storing the result in the cache
            Optional<Double> resultF = tryF(x);
            resultF.ifPresent(value -> memoizationCache.put(x, value));
            return resultF;
        }
    }

    // Example implementation of the function f(x) (your implementation may vary)
    private static Optional<Double> tryF(Double x) {
        try {
            return Optional.of(x + 2);
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
